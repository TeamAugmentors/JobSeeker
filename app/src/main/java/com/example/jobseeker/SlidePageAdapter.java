package com.example.jobseeker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.fragments.PageFragment1;
import com.example.jobseeker.fragments.PageFragment2;

import java.util.List;

public class SlidePageAdapter extends FragmentStateAdapter {


    public SlidePageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new PageFragment1();
            case 1:
                return new PageFragment2();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
