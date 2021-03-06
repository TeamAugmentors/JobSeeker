package com.example.jobseeker.app.homePage.adapters.createJob;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.homePage.fragments.createJob.jobFragments.FragmentJobBudget;
import com.example.jobseeker.app.homePage.fragments.createJob.jobFragments.FragmentJobSample;
import com.example.jobseeker.app.homePage.fragments.createJob.jobFragments.FragmentJobTitle;

public class CreateJobViewPagerAdapter  extends FragmentStateAdapter {
    FragmentJobTitle fragmentJobTitle = new FragmentJobTitle();
    FragmentJobBudget fragmentJobBudget = new FragmentJobBudget();
    FragmentJobSample fragmentJobSample = new FragmentJobSample();

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
                return fragmentJobSample;
        }

        return new FragmentJobTitle();
    }

    public FragmentJobTitle getFragmentJobTitle() {
        return fragmentJobTitle;
    }

    public FragmentJobBudget getFragmentJobBudget() {
        return fragmentJobBudget;
    }

    public FragmentJobSample getFragmentJobSample() {
        return fragmentJobSample;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
