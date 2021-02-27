package com.example.jobseeker.app.startScreen.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.startScreen.fragments.createProfileFragments.CreateProfile1;
import com.example.jobseeker.app.startScreen.fragments.createProfileFragments.CreateProfile2;
import com.example.jobseeker.app.startScreen.fragments.createProfileFragments.CreateProfile3;

public class CreateProfileViewPager2Adapter extends FragmentStateAdapter {

    public CreateProfileViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new CreateProfile1();
            case 1:
                return new CreateProfile2();
            case 2:
                return new CreateProfile3();
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
