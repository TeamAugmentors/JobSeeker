package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.widget.Toast;

import com.example.jobseeker.app.homePage.adapters.JobBoardAdapter;
import com.example.jobseeker.app.homePage.adapters.WhoAppliedAdapter;
import com.example.jobseeker.databinding.ActivityCreateJobBinding;
import com.example.jobseeker.databinding.ActivityWhoAppliedBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;

public class WhoApplied extends AppCompatActivity {

    ActivityWhoAppliedBinding binding;
    WhoAppliedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWhoAppliedBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Who Applied");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);

        binding.recyclerView.setAdapter(adapter);
    }

}