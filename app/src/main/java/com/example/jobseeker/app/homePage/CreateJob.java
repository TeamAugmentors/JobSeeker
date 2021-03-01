package com.example.jobseeker.app.homePage;

import android.app.DatePickerDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.CreateJobViewPagerAdapter;
import com.example.jobseeker.databinding.ActivityCreateJobBinding;
import com.example.jobseeker.utils.ChipHelper;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static android.view.View.GONE;

public class CreateJob extends AppCompatActivity {

    ActivityCreateJobBinding binding;
    CreateJobViewPagerAdapter adapter;
    DatePickerDialog picker;
    ArrayList<ParseFile> parseFiles ;

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

    public ViewPager2 getViewPager() {
        return binding.viewPagerJob;
    }

    public void createJob(View v) {
        //Check for errors
        if (adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().getText().toString().length() < 50 || adapter.getFragmentJobTitle().getBinding().jobTitleLayout.getEditText().getText().toString().length() == 0) {
            binding.viewPagerJob.setCurrentItem(0);

            if (adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().getText().toString().length() == 0) {
                adapter.getFragmentJobTitle().getBinding().description.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobTitle().getBinding().description.setText("This field is required");
            } else if (adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().getText().toString().length() < 50) {
                adapter.getFragmentJobTitle().getBinding().description.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobTitle().getBinding().description.setText("Enter minimum 50 characters");
            }
            if (adapter.getFragmentJobTitle().getBinding().jobTitleLayout.getEditText().getText().toString().length() == 0) {
                adapter.getFragmentJobTitle().getBinding().title.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobTitle().getBinding().title.setText("This field is required");
            }
        } else if (adapter.getFragmentJobBudget().getBinding().budgetLayout.getEditText().getText().toString().length() < 3 || adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().length() == 0 || adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().compareTo("Please select a date") == 0) {
            binding.viewPagerJob.setCurrentItem(1);

            if (adapter.getFragmentJobBudget().getBinding().budgetLayout.getEditText().getText().toString().compareTo("500") < 0 && adapter.getFragmentJobBudget().getBinding().budgetLayout.getEditText().getText().toString().length() < 4) {
                adapter.getFragmentJobBudget().getBinding().budgetWarning.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobBudget().getBinding().budgetWarning.setText("Minimum budget is 500 BDT");
            }
            if (adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().length() == 0 || adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().compareTo("Please select a date") == 0) {
                adapter.getFragmentJobBudget().getBinding().dateTextView.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobBudget().getBinding().dateTextView.setText("Please select a date");
                adapter.getFragmentJobBudget().getBinding().dateTextView.setTextSize(17);
            }
        } else {
            //No errors found! Lets post this to the jobboard

            ParseObject entity = new ParseObject("JobBoard");
            entity.put("title", adapter.getFragmentJobTitle().getBinding().jobTitleLayout.getEditText().toString());
            entity.put("description", adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().toString());
            entity.put("budget", Integer.parseInt(adapter.getFragmentJobBudget().getBinding().budgetLayout.getEditText().getText().toString()));
            entity.put("duration", adapter.getFragmentJobBudget().getBinding().dateTextView.toString());
            entity.put("revisions", ChipHelper.getTextFromSelectedChip(adapter.getFragmentJobBudget().getBinding().revisionChipGroup));

            if (ChipHelper.getTextFromSelectedChip(adapter.getFragmentJobBudget().getBinding().negotiableChipGroup).equals("Yes")) {
                entity.put("negotiable", true);
            } else
                entity.put("negotiable", false);

//            parseFiles = new ArrayList<>();

//
//            for (int i = 0; i < adapter.getFragmentJobSample().getParseFiles().length; i++) {
//                if (adapter.getFragmentJobSample().getParseFiles()[i] != null){
//                    parseFiles.add(adapter.getFragmentJobSample().getParseFiles()[i]);
//                }
//            }
//
//
//            entity.put("fileOne", parseFiles.get(0));
//            entity.put("fileTwo", parseFiles.get(1));
//            entity.put("fileThree", parseFiles.get(2));

//            for (int i = 0; i < parseFiles.size(); i++) {
//                try {
//                    parseFiles.get(i).save();
//                } catch (ParseException e) {
//                    Toast.makeText(this, "e" + e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }

            // Saves the new object.
            // Notice that the SaveCallback is totally optional!
            entity.saveInBackground(e -> {
                if (e == null) {
                    //Save was done
                    Toast.makeText(this, "success!", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    //Something went wrong
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void fileRemove1(View view) {
        view.setVisibility(GONE);
        adapter.getFragmentJobSample().getBinding().file1.setText(".pdf/.doc/.png/.jpeg");
        (adapter.getFragmentJobSample().getParseFiles())[0] = null;
    }

    public void fileRemove2(View view) {
        view.setVisibility(GONE);
        adapter.getFragmentJobSample().getBinding().file2.setText(".pdf/.doc/.png/.jpeg");
        (adapter.getFragmentJobSample().getParseFiles())[1] = null;

    }

    public void fileRemove3(View view) {
        view.setVisibility(GONE);
        adapter.getFragmentJobSample().getBinding().file3.setText(".pdf/.doc/.png/.jpeg");
        (adapter.getFragmentJobSample().getParseFiles())[2] = null;
    }
}
