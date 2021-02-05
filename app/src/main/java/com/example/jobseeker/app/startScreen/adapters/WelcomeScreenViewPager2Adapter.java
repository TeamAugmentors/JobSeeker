package com.example.jobseeker.app.startScreen.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.startScreen.fragments.EnterPhoneSlide;
import com.example.jobseeker.app.startScreen.fragments.EnterOTPSlide;

public class WelcomeScreenViewPager2Adapter extends FragmentStateAdapter {
    EnterOTPSlide enterOTPSlide = new EnterOTPSlide();

    public WelcomeScreenViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new EnterPhoneSlide();
            case 1:
                return enterOTPSlide;
        }
        return null;
    }

    public EnterOTPSlide getEnterOTPSlide(){
        return enterOTPSlide;
    }
    @Override
    public int getItemCount() {
        return 2;
    }
}
