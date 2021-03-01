package com.example.jobseeker.app.homePage.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.homePage.fragments.createprofile.titleFragments.FragmentPaymentInfo;
import com.example.jobseeker.app.homePage.fragments.createprofile.titleFragments.FragmentSkillSet;
import com.example.jobseeker.app.homePage.fragments.createprofile.titleFragments.FragmentYourInfo;

public class CreateProfileInfoViewPagerAdapter extends FragmentStateAdapter {

    public CreateProfileInfoViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new FragmentYourInfo();
            case 1:
                return new FragmentSkillSet();
            case 2:
                return new FragmentPaymentInfo();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
