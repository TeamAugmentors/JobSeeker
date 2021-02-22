package com.example.jobseeker.app.homePage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jobseeker.R;
import com.example.jobseeker.app.startScreen.Guide;
import com.example.jobseeker.app.startScreen.adapters.GuideViewPager2Adapter;
import com.example.jobseeker.app.startScreen.adapters.JobViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityCreateJobBinding;
import com.example.jobseeker.utils.ToolbarHelper;

public class CreateJob extends AppCompatActivity {

    ActivityCreateJobBinding binding;
    JobViewPager2Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJobBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        init();
    }
    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Create Job");
        adapter = new JobViewPager2Adapter(this);
        binding.viewPagerJob.setAdapter(adapter);
        binding.viewPagerJob.setOffscreenPageLimit(5);
        binding.dotsIndicator.setViewPager2(binding.viewPagerJob);
        binding.viewPagerJob.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(position==binding.viewPagerJob.getAdapter().getItemCount()-1) {
                    if(binding.viewPagerJob.getCurrentItem()==binding.viewPagerJob.getAdapter().getItemCount()-1)
                        binding.next.setText("SUBMIT");
                }
                else
                {
                    binding.next.setText("NEXT");
                }
                if(position==0){
                    binding.back.setVisibility(View.GONE);
                }
                else
                {
                    binding.back.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void next(View view) {
        if(binding.viewPagerJob.getCurrentItem()!=binding.viewPagerJob.getAdapter().getItemCount()-1)
        {
            binding.viewPagerJob.setCurrentItem(binding.viewPagerJob.getCurrentItem()+1);
            if(binding.viewPagerJob.getCurrentItem()==binding.viewPagerJob.getAdapter().getItemCount()-1)
                binding.next.setText("SUBMIT");
            else
                binding.next.setText("NEXT");
        }
        else
        {
            Toast.makeText(this, "SUBMIT", Toast.LENGTH_SHORT).show();
        }
    }

    public void back(View view) {
        if(binding.viewPagerJob.getCurrentItem()!=0) {
            binding.viewPagerJob.setCurrentItem(binding.viewPagerJob.getCurrentItem() - 1);
            if(binding.viewPagerJob.getCurrentItem()==0)
                binding.back.setVisibility(View.GONE);
            else{
                binding.back.setVisibility(View.VISIBLE);
            }
        }
    }
}