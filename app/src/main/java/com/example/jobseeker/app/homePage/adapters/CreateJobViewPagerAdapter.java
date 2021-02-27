package com.example.jobseeker.app.homePage.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.homePage.fragments.CreateJob2;
import com.example.jobseeker.app.homePage.fragments.CreateJob3;
import com.example.jobseeker.app.homePage.fragments.FragmentJobTitle;

public class CreateJobViewPagerAdapter  extends FragmentStateAdapter {

    public CreateJobViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentJobTitle();
            case 1:
                return new CreateJob2();
            case 2:
                return new CreateJob3();
        }

        return new FragmentJobTitle();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
