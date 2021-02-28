package com.example.jobseeker.app.homePage;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.CreateJobViewPagerAdapter;
import com.example.jobseeker.databinding.ActivityCreateJobBinding;
import com.example.jobseeker.utils.EmptyFieldHandler;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Calendar;
import java.util.Date;

public class CreateJob extends AppCompatActivity {

    ActivityCreateJobBinding binding;
    CreateJobViewPagerAdapter adapter;
    DatePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJobBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        init();

    }


    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Create Job");
        final Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        adapter = new CreateJobViewPagerAdapter(this);
        binding.viewPagerJob.setAdapter(adapter);
        binding.viewPagerJob.setOffscreenPageLimit(5);
        binding.dotsIndicator.setViewPager2(binding.viewPagerJob);

        binding.viewPagerJob.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == binding.viewPagerJob.getAdapter().getItemCount() - 1) {
                    if (binding.viewPagerJob.getCurrentItem() == binding.viewPagerJob.getAdapter().getItemCount() - 1) {
                        //Last slide
                        binding.next.setVisibility(View.INVISIBLE);
                    }
                } else {
                    binding.next.setVisibility(View.VISIBLE);
                }
                if (position == 0) {
                    binding.back.setVisibility(View.INVISIBLE);
                } else {
                    binding.back.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void next(View view) {
        goToNextSlide();
    }

    private void goToNextSlide() {
        if (binding.viewPagerJob.getCurrentItem() != binding.viewPagerJob.getAdapter().getItemCount() - 1) {
            binding.viewPagerJob.setCurrentItem(binding.viewPagerJob.getCurrentItem() + 1);
            if (binding.viewPagerJob.getCurrentItem() == binding.viewPagerJob.getAdapter().getItemCount() - 1) {
                //lastSlide

            } else {

            }
        }

    }

    public void back(View view) {
        if (binding.viewPagerJob.getCurrentItem() != 0) {
            binding.viewPagerJob.setCurrentItem(binding.viewPagerJob.getCurrentItem() - 1);
            if (binding.viewPagerJob.getCurrentItem() == 0)
                binding.back.setVisibility(View.INVISIBLE);
            else {
                binding.back.setVisibility(View.VISIBLE);
            }
        }
    }

    public ViewPager2 getViewPager(){
        return binding.viewPagerJob;
    }

    public void createJob(View v){
        if(adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().getText().toString().length()==0 || adapter.getFragmentJobTitle().getBinding().jobTitleLayout.getEditText().getText().toString().length()==0){
            binding.viewPagerJob.setCurrentItem(0);

            if(adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().getText().toString().length()==0){
                adapter.getFragmentJobTitle().getBinding().description.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobTitle().getBinding().description.setText("This field is required");
            }
            if(adapter.getFragmentJobTitle().getBinding().jobTitleLayout.getEditText().getText().toString().length()==0){
                adapter.getFragmentJobTitle().getBinding().title.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobTitle().getBinding().title.setText("This field is required");
            }
        }
        else if(adapter.getFragmentJobBudget().getBinding().budgetLayout.getEditText().getText().toString().length()==0 || adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().length()==0){
            binding.viewPagerJob.setCurrentItem(1);

            if(adapter.getFragmentJobBudget().getBinding().budgetLayout.getEditText().getText().toString().length()==0){
                adapter.getFragmentJobBudget().getBinding().budgetWarning.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobBudget().getBinding().budgetWarning.setText("This field is required");
            }
            if(adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().length()==0){
                adapter.getFragmentJobBudget().getBinding().dateTextView.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobBudget().getBinding().dateTextView.setText("Please select a date");
                adapter.getFragmentJobBudget().getBinding().dateTextView.setTextSize(17);
            }
        }
        else
        {
            Toast.makeText(this, "WHY SO EZ", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,HomePage.class));
            finish();
        }
    }
}
