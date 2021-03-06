package com.example.jobseeker.app.homePage.adapters.inbox;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jobseeker.app.homePage.fragments.inbox.AppliedInbox;
import com.example.jobseeker.app.homePage.fragments.inbox.CreatedJobInbox;

public class InboxViewPager2Adapter extends FragmentStateAdapter {

    AppliedInbox appliedInbox = new AppliedInbox();
    CreatedJobInbox createdJobInbox = new CreatedJobInbox();

    public InboxViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return appliedInbox;
            case 1:
                return createdJobInbox;
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public AppliedInbox getAppliedInbox() {
        return appliedInbox;
    }

    public CreatedJobInbox getCreatedJobInbox() {
        return createdJobInbox;
    }
}
