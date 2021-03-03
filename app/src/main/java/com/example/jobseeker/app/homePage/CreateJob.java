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
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.CreateJobTitlePagerAdapter;
import com.example.jobseeker.app.homePage.adapters.CreateJobViewPagerAdapter;
import com.example.jobseeker.app.homePage.adapters.CreateProfileInfoViewPagerAdapter;
import com.example.jobseeker.app.homePage.adapters.CreateProfileViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityCreateJobBinding;
import com.example.jobseeker.utils.ChipHelper;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static android.view.View.GONE;

public class CreateJob extends AppCompatActivity {

    ActivityCreateJobBinding binding;
    CreateJobViewPagerAdapter adapter;

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

        setPageViewPager();
        setTitleViewPager();

        setViewPagerOnChangeCallBacks();

    }

    private void setViewPagerOnChangeCallBacks() {
        //Edge negative margin
        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        int offsetPx = getResources().getDimensionPixelOffset(R.dimen.offset);

        binding.titleViewPager2.setPageTransformer((page, position) -> {
            ViewPager2 viewpager = (ViewPager2) page.getParent().getParent();
            float offset = position * -(2 * offsetPx + pageMarginPx);

            if (viewpager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewpager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.setTranslationX(-offset);
                } else {
                    page.setTranslationX(offset);
                }
            } else {
                page.setTranslationX(offset);
            }

            if (position <= -1.0F || position >= 1.0F) {
                page.setAlpha(0.4F);
            } else if (position == 0.0F) {
                page.setAlpha(1.0F);
            } else {

                page.setAlpha((float) (1.0F - (0.6 * Math.abs(position))));
            }
        });

        //Callback
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.titleViewPager2.setCurrentItem(position);

                if (binding.viewPager2.getCurrentItem() == binding.viewPager2.getAdapter().getItemCount() - 1) {
                    //if it is at last page
                    binding.next.setVisibility(View.INVISIBLE);
                } else
                    binding.next.setVisibility(View.VISIBLE);

                if (binding.viewPager2.getCurrentItem() == 0) {
                    //if it is at first page
                    binding.back.setVisibility(View.INVISIBLE);
                } else
                    binding.back.setVisibility(View.VISIBLE);
            }
        });

        binding.titleViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.viewPager2.setCurrentItem(position);
            }
        });
    }

    private void setTitleViewPager() {
        binding.titleViewPager2.setAdapter(new CreateJobTitlePagerAdapter(this));
        binding.titleViewPager2.setOffscreenPageLimit(3);

        binding.titleViewPager2.setHorizontalFadingEdgeEnabled(true);
    }

    private void setPageViewPager() {
        binding.viewPager2.setAdapter(adapter);
        binding.viewPager2.setOffscreenPageLimit(5);
        binding.dotsIndicator.setViewPager2(binding.viewPager2);

    }
    public void next(View view) {
        goToNextSlide();
    }

    private void goToNextSlide() {
        if (binding.viewPager2.getCurrentItem() != binding.viewPager2.getAdapter().getItemCount() - 1) {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
            if (binding.viewPager2.getCurrentItem() == binding.viewPager2.getAdapter().getItemCount() - 1) {
                //lastSlide

            } else {

            }
        }

    }

    public void back(View view) {
        if (binding.viewPager2.getCurrentItem() != 0) {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() - 1);
            if (binding.viewPager2.getCurrentItem() == 0)
                binding.back.setVisibility(View.INVISIBLE);
            else {
                binding.back.setVisibility(View.VISIBLE);
            }
        }
    }

    public ViewPager2 getViewPager() {
        return binding.viewPager2;
    }

    public void createJob(View v) {
        //Check for errors
        String checkBudget = adapter.getFragmentJobBudget().getBudget();
        if (adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().getText().toString().length() < 50 || adapter.getFragmentJobTitle().getBinding().jobTitleLayout.getEditText().getText().toString().length() == 0) {
            binding.viewPager2.setCurrentItem(0);

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
        } else if (checkBudget.length() < 3 || adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().length() == 0 || adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().compareTo("Please select a date") == 0) {
            binding.viewPager2.setCurrentItem(1);

            if (checkBudget.compareTo("500") < 0 && checkBudget.toString().length() < 4) {
                adapter.getFragmentJobBudget().getBinding().budgetWarning.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobBudget().getBinding().budgetWarning.setText("Minimum budget is 500 BDT");
            }
            if (adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().length() == 0 || adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString().compareTo("Please select a date") == 0) {
                adapter.getFragmentJobBudget().getBinding().dateTextView.setTextColor(ContextCompat.getColor(this, R.color.job_seeker_red));
                adapter.getFragmentJobBudget().getBinding().dateTextView.setText("Please select a date");
                adapter.getFragmentJobBudget().getBinding().dateTextView.setTextSize(17);
            }
        } else if(ParseUser.getCurrentUser().get("firstName") == null) {
            //No errors found! Lets post this to the jobboard
            Toast.makeText(this, "Please create a profile first!", Toast.LENGTH_SHORT).show();
        } else{
             ParseObject entity = new ParseObject("JobBoard");
            entity.put("title", adapter.getFragmentJobTitle().getBinding().jobTitleLayout.getEditText().getText().toString());
            entity.put("description", adapter.getFragmentJobTitle().getBinding().jobDescriptionLayout.getEditText().getText().toString());
            entity.put("budget", Integer.parseInt(adapter.getFragmentJobBudget().getBinding().budgetLayout.getEditText().getText().toString()));
            entity.put("duration", adapter.getFragmentJobBudget().getBinding().dateTextView.getText().toString());
            entity.put("revisions", Integer.parseInt(ChipHelper.getTextFromSelectedChip(adapter.getFragmentJobBudget().getBinding().revisionChipGroup)));

            if (ChipHelper.getTextFromSelectedChip(adapter.getFragmentJobBudget().getBinding().negotiableChipGroup).equals("Yes")) {
                entity.put("negotiable", true);
            } else
                entity.put("negotiable", false);

            entity.put("createdBy", ParseUser.getCurrentUser());

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

    public void goToNextPage(View view) {
        if (binding.viewPager2.getCurrentItem() != binding.viewPager2.getAdapter().getItemCount() - 1) {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
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
