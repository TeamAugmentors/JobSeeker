package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.JobBoardAdapter;
import com.example.jobseeker.databinding.ActivityJobBoardBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.ncorti.slidetoact.SlideToActView;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.lang.reflect.Field;
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
            Log.d("AWOFJAWOF", verticalOffset + "");
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

        onQueryTextListener = new SearchView.OnQueryTextListener() {
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
                return true;
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

    @Override
    public void onJobBoardClick(int position, List<ParseObject> parseObjects) {

        Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        View dialogView = dialog.getWindow().getDecorView();


        dialogView.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
        });

        ((TextView) dialogView.findViewById(R.id.title)).setText(parseObjects.get(position).getString("title"));
        ((TextView) dialogView.findViewById(R.id.description)).setText(parseObjects.get(position).getString("description"));
        ((TextView) dialogView.findViewById(R.id.budget)).setText(parseObjects.get(position).getInt("budget") + "");
        ((TextView) dialogView.findViewById(R.id.duration)).setText(parseObjects.get(position).getString("duration"));
        ((TextView) dialogView.findViewById(R.id.revisions)).setText(parseObjects.get(position).getInt("revisions") + "");
        dialogView.findViewById(R.id.seeFreelancerButton).setVisibility(View.GONE);
        dialogView.findViewById(R.id.deleteButton).setVisibility(View.GONE);

        if (parseObjects.get(position).getBoolean("negotiable"))
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("Yes");
        else
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("No");

        //Dynamic scroll view height

        ScrollView scrollView = dialogView.findViewById(R.id.scrollView);

        String text = ((TextView) dialogView.findViewById(R.id.description)).getText().toString();

        int charCount = text.length();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        if (charCount <= 200) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scrollView.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
            scrollView.setLayoutParams(params);
        }


        //---------------------->

        ((SlideToActView) dialogView.findViewById(R.id.applySlider)).setOnSlideCompleteListener(slideToActView -> {

            //apply
            if (ParseUser.getCurrentUser().getString("firstName") != null) {
                parseObjects.get(position).add("applied", ParseUser.getCurrentUser());

                parseObjects.get(position).saveInBackground(e -> {
                    if (e == null) {
                        Toast.makeText(this, "Successfully applied!", Toast.LENGTH_SHORT).show();

                        ParseUser.getCurrentUser().add("appliedPosts", parseObjects.get(position));
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setQueryHint("Search Job");
        searchView.setBackground(getDrawable(R.drawable.search_bar_background1));

        EditText txtSearch = ((EditText)searchView.findViewById(androidx.appcompat.R.id.search_src_text));
        ImageView closeBtn = ((ImageView)searchView.findViewById(androidx.appcompat.R.id.search_close_btn));
        closeBtn.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        txtSearch.setHint(getResources().getString(R.string.app_name));
        txtSearch.setHintTextColor(Color.LTGRAY);
        txtSearch.setTextColor(Color.WHITE);

        return super.onCreateOptionsMenu(menu);
    }

}