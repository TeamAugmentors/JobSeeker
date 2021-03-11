package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.JobBoardAdapter;
import com.example.jobseeker.databinding.ActivityJobBoardBinding;
import com.example.jobseeker.databinding.DialogLayoutBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.ncorti.slidetoact.SlideToActView;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class JobBoard extends AppCompatActivity implements JobBoardAdapter.OnJobBoardListener {
    JobBoardAdapter adapter;
    ActivityJobBoardBinding binding;
    ArrayList<ParseObject> parseObjects;

    MenuItem searchItem;
    SearchView searchView;

    TextWatcher textWatcher;
    SearchView.OnQueryTextListener onQueryTextListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        fetchData();
    }

    private void fetchData() {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.include("applied");
        query.whereNotEqualTo("applied", ParseUser.getCurrentUser());
        query.whereNotEqualTo("createdBy", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");

        query.findInBackground((objects, e) -> {
            if (e == null) {
                parseObjects = (ArrayList<ParseObject>) objects;
                adapter = new JobBoardAdapter(parseObjects, this);
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                binding.recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(JobBoard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @SuppressLint("RestrictedApi")
    private void init() {

        ToolbarHelper.create(binding.toolbar, binding.collapsingToolbar, this, "Job Board");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);

        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            verticalOffset = verticalOffset * -1;
            float x = (float) (binding.appBar.getTotalScrollRange() / 2.0);
            if (verticalOffset <= x) {
                binding.search.setAlpha((float) (1.0 - (verticalOffset / x)));
            }

            if (searchItem != null) {
                if (verticalOffset / x < 1) {
                    searchItem.setVisible(false);
                    searchView.setVisibility(View.GONE);
                    getSupportActionBar().collapseActionView();
                } else {
                    searchItem.setVisible(true);
                    searchView.setVisibility(View.VISIBLE);
                    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            binding.search.removeTextChangedListener(textWatcher);
                            binding.search.setText(newText);
                            binding.search.addTextChangedListener(textWatcher);
                            filter(newText);
                            return false;
                        }
                    });
                }
            }
        });

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchView.setOnQueryTextListener(null);
                searchView.setQuery(s.toString(), false);
                searchView.setOnQueryTextListener(onQueryTextListener);
                filter(s.toString());
            }
        };

        binding.search.addTextChangedListener(textWatcher);

    }

    private void filter(String text) {
        ArrayList<ParseObject> filteredList = new ArrayList<>();

        for (ParseObject item : parseObjects) {
            if (item.getString("title").toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filter(filteredList);
    }
    DialogLayoutBinding bindingDialog;
    @Override
    public void onJobBoardClick(int position, List<ParseObject> parseObjects) {

        Dialog dialog = new Dialog(this, R.style.Dialog);
        bindingDialog = DialogLayoutBinding.inflate(getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        bindingDialog.close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        ParseObject currentObject = parseObjects.get(position);

        bindingDialog.title.setText(currentObject.getString("title"));
        bindingDialog.description.setText(currentObject.getString("description"));
        bindingDialog.budget.setText(currentObject.getInt("budget") + "");
        bindingDialog.duration.setText(currentObject.getString("duration"));
        bindingDialog.revisions.setText(currentObject.getInt("revisions") + "");
        bindingDialog.seeFreelancerButton.setVisibility(View.GONE);
        bindingDialog.deleteButton.setVisibility(View.GONE);

        if (currentObject.getBoolean("negotiable"))
            bindingDialog.negotiable.setText("Yes");
        else
            bindingDialog.negotiable.setText("No");

        //Dynamic scroll view height

        String text = bindingDialog.description.getText().toString();

        int charCount = text.length();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        LinearLayout.LayoutParams params;

        if (charCount <= 200) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
        }

        bindingDialog.scrollView.setLayoutParams(params);


        //---------------------->

        bindingDialog.applySlider.setOnSlideCompleteListener(slideToActView -> {
            //apply
            if (ParseUser.getCurrentUser().getString("firstName") != null) {
                currentObject.add("applied", ParseUser.getCurrentUser());

                currentObject.saveInBackground(e -> {
                    if (e == null) {
                        Toast.makeText(this, "Successfully applied!", Toast.LENGTH_SHORT).show();

                        ParseUser.getCurrentUser().add("appliedPosts", currentObject);
                        ParseUser.getCurrentUser().saveEventually();

                        removeJob(parseObjects, position);

                        dialog.dismiss();
                    } else {
                        Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        slideToActView.resetSlider();
                    }
                });

            } else {
                Toast.makeText(this, "Please create an account first", Toast.LENGTH_SHORT).show();
                slideToActView.resetSlider();
            }
        });

    }

    private void removeJob(List<ParseObject> parseObjects, int pos) {
        parseObjects.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, parseObjects.size());
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search Job");
        searchView.setAlpha((float) 0.9);
        EditText txtSearch = (searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        ImageView closeBtn = (searchView.findViewById(androidx.appcompat.R.id.search_close_btn));
        txtSearch.setHintTextColor(getResources().getColor(R.color.hintColor));
        closeBtn.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        txtSearch.setTextColor(Color.WHITE);
        txtSearch.setTextCursorDrawable(R.drawable.cursor);

        return super.onCreateOptionsMenu(menu);
    }

}