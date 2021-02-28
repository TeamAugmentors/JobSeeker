package com.example.jobseeker.app.homePage.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.homePage.fragments.FragmentJobBudget;
import com.example.jobseeker.app.homePage.fragments.FragmentJobPayment;
import com.example.jobseeker.app.homePage.fragments.FragmentJobTitle;

public class CreateJobViewPagerAdapter  extends FragmentStateAdapter {
    FragmentJobTitle fragmentJobTitle = new FragmentJobTitle();
    FragmentJobBudget fragmentJobBudget = new FragmentJobBudget();
    FragmentJobPayment fragmentJobPayment = new FragmentJobPayment();

    public CreateJobViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return fragmentJobTitle;
            case 1:
                return  fragmentJobBudget;
            case 2:
                return  fragmentJobPayment;
        }

        return new FragmentJobTitle();
    }

    public FragmentJobTitle getFragmentJobTitle() {
        return fragmentJobTitle;
    }

    public FragmentJobBudget getFragmentJobBudget() {
        return fragmentJobBudget;
    }

    public FragmentJobPayment getFragmentJobPayment() {
        return fragmentJobPayment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
