package com.example.jobseeker.app.startScreen.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.startScreen.fragments.createJobFragment.CreateJob1;
import com.example.jobseeker.app.startScreen.fragments.createJobFragment.CreateJob2;
import com.example.jobseeker.app.startScreen.fragments.createJobFragment.CreateJob3;
import com.example.jobseeker.app.startScreen.fragments.createJobFragment.CreateJob4;

public class JobViewPager2Adapter extends FragmentStateAdapter {

    public JobViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CreateJob1();
            case 1:
                return new CreateJob2();
            case 2:
                return new CreateJob3();
            case 3:
                return new CreateJob4();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}
