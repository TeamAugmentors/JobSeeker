package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.JobBoardAdapter;
import com.example.jobseeker.databinding.ActivityAppliedPostBinding;
import com.example.jobseeker.databinding.ActivityCreatedPostBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.ncorti.slidetoact.SlideToActView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CreatedPosts extends AppCompatActivity implements CreatedPostsAdapter.OnCreatedPostsListener {

    ActivityCreatedPostBinding binding;
    ArrayList<ParseObject> parseObjects;
    CreatedPostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatedPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        fetchData();
    }

    private void fetchData() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.whereEqualTo("createdBy", ParseUser.getCurrentUser());

        query.findInBackground((objects, e) -> {
            if (e == null) {
                parseObjects = (ArrayList<ParseObject>) objects;
                adapter = new CreatedPostsAdapter(parseObjects, this);

                binding.recyclerView.setAdapter(adapter);

                binding.chip.setText("Created " + parseObjects.size() + " Jobs");

            } else {
                Toast.makeText(CreatedPosts.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                binding.chip.setText("Unable to fetch");
            }
        });
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Created Jobs");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);


        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            verticalOffset = verticalOffset * -1;
            Log.d("AWOFJAWOF", verticalOffset + "");
            float x = (float) (binding.appBar.getTotalScrollRange() / 2.0);
            if (verticalOffset <= x) {
                binding.chip.setAlpha((float) (1.0 - (verticalOffset / x)));
            }
//            if (searchItem != null){
//                if (verticalOffset/x < 1){
//                    searchItem.setVisible(false);
//                    searchView.setVisibility(View.GONE);
//                    getSupportActionBar().collapseActionView();
//                } else{
//                    searchItem.setVisible(true);
//                    searchView.setVisibility(View.VISIBLE);
//
//                }
//            }
        });

    }

    @Override
    public void onJobBoardClick(int position, List<ParseObject> parseObjects) {

        Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_layout);
        //delete confirmation dialog
        Dialog confirmationDialog = new Dialog(this, R.style.Dialog);
        confirmationDialog.setContentView(R.layout.dialog_delete_confirmation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        View dialogView = dialog.getWindow().getDecorView();
        View confirmationDialogView = confirmationDialog.getWindow().getDecorView();


        dialogView.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialogView.findViewById(R.id.deleteButton).setOnClickListener(v1 -> {
            dialog.dismiss();

            confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            confirmationDialog.show();

        });

        //delete confirmation dialog
        confirmationDialogView.findViewById(R.id.close).setOnClickListener(v1 -> {
            confirmationDialog.dismiss();
        });

        confirmationDialogView.findViewById(R.id.yesButton).setOnClickListener(v1 -> {
            confirmationDialog.dismiss();
        });

        confirmationDialogView.findViewById(R.id.noButton).setOnClickListener(v1 -> {
            confirmationDialog.dismiss();
        });


        ((TextView) dialogView.findViewById(R.id.title)).setText(parseObjects.get(position).getString("title"));
        ((TextView) dialogView.findViewById(R.id.description)).setText(parseObjects.get(position).getString("description"));
        ((TextView) dialogView.findViewById(R.id.budget)).setText(parseObjects.get(position).getInt("budget") + "");
        ((TextView) dialogView.findViewById(R.id.duration)).setText(parseObjects.get(position).getString("duration"));
        ((TextView) dialogView.findViewById(R.id.revisions)).setText(parseObjects.get(position).getInt("revisions") + "");

        if (parseObjects.get(position).getBoolean("negotiable"))
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("Yes");
        else
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("No");
        ScrollView scrollView = dialogView.findViewById(R.id.scrollView);
        String text = ((TextView) dialogView.findViewById(R.id.description)).getText().toString();
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
        scrollView.setLayoutParams(params);

        ((SlideToActView) dialogView.findViewById(R.id.applySlider)).setVisibility(View.GONE);
    }

}