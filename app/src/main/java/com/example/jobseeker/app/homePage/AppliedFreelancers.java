package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jobseeker.app.homePage.adapters.AppliedFreelancerAdapter;
import com.example.jobseeker.databinding.ActivityAppliedFreelancersBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AppliedFreelancers extends AppCompatActivity {

    ActivityAppliedFreelancersBinding binding;
    AppliedFreelancerAdapter adapter;
    ParseObject currentJob;

    ArrayList<ParseUser> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityAppliedFreelancersBinding.inflate(getLayoutInflater())).getRoot());

        init();
        fetchData();
    }

    private void fetchData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("objectId" , currentJob.getObjectId());

        ParseCloud.callFunctionInBackground("fetchAppliedFreeLancers" , params , (object, e) -> {
            if (e==null){
                ParseObject parseObject = (ParseObject) object;

                users = (ArrayList) parseObject.getList("applied");

                if (users != null){
                    adapter = new AppliedFreelancerAdapter(users, new AppliedFreelancerAdapter.onItemClickListener() {
                        @Override
                        public void onItemClick(int position, ArrayList<ParseUser> users) {
                            currentJob.put("hired", users.get(position));
                            currentJob.put("locked", true);

                            currentJob.saveInBackground(e1 -> {
                                if (e1 == null){

                                    Toast.makeText(AppliedFreelancers.this, "Successfully hired! You can now chat with the free lancer in your inbox!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AppliedFreelancers.this, CreatedPosts.class).putExtra("jobObject", currentJob));
                                    finish();

                                }
                            });
                        }
                    });

                    binding.recyclerView.setAdapter(adapter);
                }else{
                    Toast.makeText(this, "no one has applied to your post yet :(", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(this, "error!"  + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Applied Freelancers");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);

        binding.recyclerView.setAdapter(adapter);

        currentJob = getIntent().getParcelableExtra("jobObject");
        Toast.makeText(this, ""+ currentJob.getString("title"), Toast.LENGTH_SHORT).show();
    }


}