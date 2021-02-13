package com.example.jobseeker.app.jobBoard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.example.jobseeker.R;
import com.example.jobseeker.app.startScreen.adapters.RecyclerViewAdapter;
import com.example.jobseeker.databinding.ActivityJobBoardBinding;

public class JobBoard extends AppCompatActivity {

    String jobs[],descriptions[];
    int images[];
    ActivityJobBoardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityJobBoardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        jobs = getResources().getStringArray(R.array.job_list);
        descriptions = getResources().getStringArray(R.array.description);

        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, jobs, descriptions);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}