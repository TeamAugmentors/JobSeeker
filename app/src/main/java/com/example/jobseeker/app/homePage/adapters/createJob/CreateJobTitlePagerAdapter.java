package com.example.jobseeker.app.homePage.adapters.createJob;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.homePage.fragments.createJob.titleFragments.FragmentBudget;
import com.example.jobseeker.app.homePage.fragments.createJob.titleFragments.FragmentSampleFiles;
import com.example.jobseeker.app.homePage.fragments.createJob.titleFragments.FragmentJobInfo;


public class CreateJobTitlePagerAdapter extends FragmentStateAdapter {

    public CreateJobTitlePagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentJobInfo();
            case 1:
                return new FragmentBudget();
            case 2:
                return new FragmentSampleFiles();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
