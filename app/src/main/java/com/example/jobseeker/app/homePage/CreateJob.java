package com.example.jobseeker.app.homePage;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.CreateJobViewPagerAdapter;
import com.example.jobseeker.databinding.ActivityCreateJobBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.datepicker.MaterialDatePicker;

public class CreateJob extends AppCompatActivity {

    ActivityCreateJobBinding binding;
    CreateJobViewPagerAdapter adapter;
    MaterialDatePicker materialDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateJobBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        final Drawable upArrow =  ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_ATOP);

        init(upArrow);
    }

    private void init(Drawable upArrow) {
        ToolbarHelper.create(binding.toolbar, this, "Create Job");
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

        MaterialDatePicker.Builder materialDatePickerBuilder =  MaterialDatePicker.Builder.dateRangePicker();
        materialDatePicker = materialDatePickerBuilder.build();



    }

    public void next(View view) {
        goToNextSlide();
    }

    private void goToNextSlide() {
        if (binding.viewPagerJob.getCurrentItem() != binding.viewPagerJob.getAdapter().getItemCount() - 1) {
            binding.viewPagerJob.setCurrentItem(binding.viewPagerJob.getCurrentItem() + 1);
            if (binding.viewPagerJob.getCurrentItem() == binding.viewPagerJob.getAdapter().getItemCount() - 1){
                //lastSlide
                Toast.makeText(this, "last slide", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "last not", Toast.LENGTH_SHORT).show();
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

    public void calender(View view) {
        Toast.makeText(this, "AOPFGLAWOG", Toast.LENGTH_SHORT).show();
        materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");

    }
}