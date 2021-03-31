package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCompleteThisJobBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseObject;

public class CompleteThisJob extends AppCompatActivity {
    ActivityCompleteThisJobBinding binding;
    ParseObject jobObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCompleteThisJobBinding.inflate(getLayoutInflater())).getRoot());
        init();
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Complete This Job");
        jobObject = getIntent().getParcelableExtra("jobObject");
    }


    public void uploadFiles(View view) {
        //upload files to drive here

        //after job completed

        completeJob();
    }

    private void completeJob() {
        jobObject.put("completed", true);
        jobObject.saveInBackground();
    }
}