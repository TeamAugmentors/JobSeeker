package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.jobseeker.app.homePage.adapters.InboxAdapter;
import com.example.jobseeker.databinding.ActivityInboxBinding;
import com.example.jobseeker.utils.ToolbarHelper;

public class Inbox extends AppCompatActivity {
    ActivityInboxBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInboxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Inbox");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);

    }
}