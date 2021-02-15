package com.example.jobseeker.app.homePage;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCreateJobBinding;

public class CreateJob extends AppCompatActivity {

    ActivityCreateJobBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJobBinding.inflate(getLayoutInflater());

        setContentView(R.layout.activity_create_job);

    }
}