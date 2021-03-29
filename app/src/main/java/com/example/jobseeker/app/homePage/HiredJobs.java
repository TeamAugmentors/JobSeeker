package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCreatedPostBinding;
import com.example.jobseeker.databinding.ActivityHiredJobsBinding;
import com.example.jobseeker.databinding.DialogLayoutBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.ncorti.slidetoact.SlideToActView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HiredJobs extends AppCompatActivity implements CreatedPostsAdapter.OnCreatedPostsListener{

    ActivityHiredJobsBinding binding;
    ArrayList<ParseObject> parseObjects;
    CreatedPostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHiredJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        fetchData();

    }

    private void init() {

        ToolbarHelper.create(binding.toolbar, binding.collapsingToolbar, this, "Hired Jobs");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);


        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            verticalOffset = verticalOffset * -1;
            float x = (float) (binding.appBar.getTotalScrollRange() / 2.0);
            if (verticalOffset <= x) {
                binding.chip.setAlpha((float) (1.0 - (verticalOffset / x)));
            }
        });
    }

    private void fetchData() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("appliedPosts");

        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                List<ParseObject> objects = (List<ParseObject>) object.get("appliedPosts");
                parseObjects = (ArrayList<ParseObject>) objects;

                if (parseObjects != null) {

                    adapter = new CreatedPostsAdapter(parseObjects, this);

                    binding.recyclerView.setAdapter(adapter);
                    binding.chip.setText("Applied to " + parseObjects.size() + " Jobs");
                } else {
                    binding.chip.setText("Applied to 0 Jobs");
                }
            } else {
                Toast.makeText(HiredJobs.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.chip.setText("Unable to fetch");
            }

        });

    }

    DialogLayoutBinding bindingDialog;

    public void onJobBoardClick(int position, List<ParseObject> parseObjects) {

        Dialog dialog = new Dialog(this, R.style.Dialog);
        bindingDialog = DialogLayoutBinding.inflate(getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        bindingDialog.close.setOnClickListener(v -> {
            dialog.dismiss();
        });


        bindingDialog.title.setText(parseObjects.get(position).getString("title"));
        bindingDialog.description.setText(parseObjects.get(position).getString("description"));
        bindingDialog.budget.setText(parseObjects.get(position).getInt("budget") + "");
        bindingDialog.duration.setText(parseObjects.get(position).getString("duration"));
        bindingDialog.revisions.setText(parseObjects.get(position).getInt("revisions") + "");
        bindingDialog.seeFreelancerButton.setText("Complete this job");
        bindingDialog.deleteButton.setVisibility(View.GONE);
        bindingDialog.horizontalLine.setVisibility(View.GONE);
        bindingDialog.message.setVisibility(View.GONE);
        bindingDialog.applySlider.setVisibility(View.GONE);

        if (parseObjects.get(position).getBoolean("negotiable"))
            bindingDialog.negotiable.setText("Yes");
        else
            bindingDialog.negotiable.setText("No");
        ScrollView scrollView = bindingDialog.scrollView;
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
        scrollView.setLayoutParams(params);


    }

}