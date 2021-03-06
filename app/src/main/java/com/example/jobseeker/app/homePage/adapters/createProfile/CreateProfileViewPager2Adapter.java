package com.example.jobseeker.app.homePage.adapters.createProfile;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.example.jobseeker.app.homePage.fragments.createprofile.profileFragments.CreateProfile1;
import com.example.jobseeker.app.homePage.fragments.createprofile.profileFragments.CreateProfile2;
import com.example.jobseeker.app.homePage.fragments.createprofile.profileFragments.CreateProfile3;

public class CreateProfileViewPager2Adapter extends FragmentStateAdapter {

    CreateProfile1 createProfile1 = new CreateProfile1();
    CreateProfile2 createProfile2 = new CreateProfile2();
    CreateProfile3 createProfile3 = new CreateProfile3();

    public CreateProfileViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return createProfile1;
            case 1:
                return createProfile2;
            case 2:
                return createProfile3;
        }
        return null;
    }

    public CreateProfile1 getFragmentCreateProfile1() {
        return createProfile1;
    }

    public CreateProfile2 getFragmentCreateProfile2() {
        return createProfile2;
    }

    public CreateProfile3 getFragmentCreateProfile3() {
        return createProfile3;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
