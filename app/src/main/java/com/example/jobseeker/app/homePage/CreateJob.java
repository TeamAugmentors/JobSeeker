package com.example.jobseeker.app.homePage;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
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

}
