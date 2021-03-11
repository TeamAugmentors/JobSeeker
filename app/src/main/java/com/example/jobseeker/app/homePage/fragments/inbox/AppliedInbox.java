package com.example.jobseeker.app.homePage.fragments.inbox;

import android.os.Bundle;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.AppliedPosts;
import com.example.jobseeker.app.homePage.CreateProfile;
import com.example.jobseeker.app.homePage.CreatedPostsAdapter;
import com.example.jobseeker.app.homePage.JobBoard;
import com.example.jobseeker.app.homePage.adapters.JobBoardAdapter;
import com.example.jobseeker.app.homePage.adapters.inbox.InboxAdapter;
import com.example.jobseeker.databinding.FragemntAppliedInboxBinding;
import com.example.jobseeker.databinding.FragmentCreateJobBudgetBinding;
import com.example.jobseeker.databinding.FragmentCreateProfile1Binding;
import com.google.android.material.chip.ChipDrawable;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AppliedInbox extends Fragment {

    FragemntAppliedInboxBinding binding;
    List<ParseObject> parseObjects;
    InboxAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragemntAppliedInboxBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setItemViewCacheSize(1);

        fetchData(null);
    }

    public void fetchData(SwipeRefreshLayout swipeRefreshLayout) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("appliedPosts.createdBy");

        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                HashMap<String, ParseObject> createdBys = new HashMap<>();
                parseObjects = object.getList("appliedPosts");
                if (parseObjects != null) {
                    for (int i = 0; i < parseObjects.size(); i++) {
                        createdBys.put(parseObjects.get(i).getParseObject("createdBy").getObjectId(), parseObjects.get(i).getParseObject("createdBy"));
                    }

                    adapter = new InboxAdapter(createdBys, new InboxAdapter.OnInboxListener() {
                        @Override
                        public void onInboxClick(int position, List<ParseObject> parseObjects) {

                        }
                    });

                    if (adapter.getItemCount() != 0)
                        binding.recyclerview.setAdapter(adapter);
                }
                Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        });
    }

    public FragemntAppliedInboxBinding getBinding() {
        return binding;
    }
}
