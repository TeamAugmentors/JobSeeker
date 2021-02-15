package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.Adapters.JobBoardAdapter;
import com.example.jobseeker.app.startScreen.adapters.RecyclerViewAdapter;
import com.example.jobseeker.databinding.ActivityJobBoardBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class JobBoard extends AppCompatActivity {
    JobBoardAdapter adapter;
    ActivityJobBoardBinding binding;
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
        query.findInBackground((objects, e) -> {
            if (e==null) {
                adapter = new JobBoardAdapter(objects);
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
                binding.recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(JobBoard.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Job Board");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);
    }
}