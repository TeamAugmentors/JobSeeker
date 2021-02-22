package com.example.jobseeker.app.startScreen.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.startScreen.fragments.guideFragments.GuidePage1;
import com.example.jobseeker.app.startScreen.fragments.guideFragments.GuidePage2;

public class GuideViewPager2Adapter extends FragmentStateAdapter {

    public GuideViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new GuidePage1();
            case 1:
                return new GuidePage2();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
