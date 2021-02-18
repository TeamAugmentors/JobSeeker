package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.Adapters.JobBoardAdapter;
import com.example.jobseeker.databinding.ActivityJobBoardBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class JobBoard extends AppCompatActivity implements JobBoardAdapter.OnJobBoardListener {
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
                adapter = new JobBoardAdapter(objects, this);
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

    @Override
    public void onJobBoardClick(int position) {

        Dialog dialog = new Dialog(this,R.style.Theme_JobSeeker);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.description_popup_window);

        //dialog.show();
    }

}