package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.inbox.InboxAdapter;
import com.example.jobseeker.app.homePage.adapters.inbox.InboxViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityInboxBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Inbox extends AppCompatActivity {
    ActivityInboxBinding binding;
    InboxViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInboxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }


    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Inbox");

        binding.viewPager2.setAdapter(adapter = new InboxViewPager2Adapter(this));

        binding.viewPager2.setOffscreenPageLimit(5);

        new TabLayoutMediator(binding.tabLayout, binding.viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Applied Jobs");
                        break;
                    case 1:
                        tab.setText("Created Jobs");
                        break;
                }
            }
        }).attach();

        binding.swipeRefresh.setColorSchemeResources(R.color.job_seeker_logo_green);

        binding.swipeRefresh.setOnRefreshListener(() -> {

            adapter.getAppliedInbox().fetchData(binding.swipeRefresh);
            adapter.getCreatedJobInbox().fetchData(binding.swipeRefresh);

        });


    }

    public ActivityInboxBinding getBinding() {
        return binding;
    }
}