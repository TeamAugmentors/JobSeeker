package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityAppliedPostBinding;
import com.example.jobseeker.databinding.ActivityCreatedPostBinding;
import com.example.jobseeker.utils.ToolbarHelper;

public class CreatedPost extends AppCompatActivity {

    ActivityCreatedPostBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityCreatedPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Created Posts");
    }
}