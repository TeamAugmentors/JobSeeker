package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.jobseeker.databinding.ActivityCreatedPostBinding;
import com.example.jobseeker.databinding.ActivityHiredJobsBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseObject;

import java.util.ArrayList;

public class HiredJobs extends AppCompatActivity {

    ActivityHiredJobsBinding binding;
    ArrayList<ParseObject> parseObjects;
    CreatedPostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHiredJobsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {

        ToolbarHelper.create(binding.toolbar, binding.collapsingToolbar, this, "Hired Jobs");

        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            verticalOffset = verticalOffset * -1;
            float x = (float) (binding.appBar.getTotalScrollRange() / 2.0);
            if (verticalOffset <= x) {
                binding.chip.setAlpha((float) (1.0 - (verticalOffset / x)));
            }
        });
    }
}