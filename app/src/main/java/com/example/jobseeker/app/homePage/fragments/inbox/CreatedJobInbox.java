package com.example.jobseeker.app.homePage.fragments.inbox;

import android.os.Bundle;
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

import com.example.jobseeker.app.homePage.adapters.inbox.InboxAdapter;
import com.example.jobseeker.databinding.FragemntAppliedInboxBinding;
import com.example.jobseeker.databinding.FragemntCreatedJobsInboxBinding;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CreatedJobInbox extends Fragment {

    FragemntCreatedJobsInboxBinding binding;
    HashMap<String, ParseObject> parseObjects = new HashMap<>();
    InboxAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragemntCreatedJobsInboxBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerview.setItemViewCacheSize(1);

        fetchData(null);
    }

    public void fetchData(SwipeRefreshLayout swipeRefreshLayout) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.include("applied");
        query.whereEqualTo("createdBy", ParseUser.getCurrentUser());

        query.findInBackground((objects, e) -> {
            if (e == null) {
                for (int i = 0; i < objects.size(); i++) {
                    if (objects.get(i).getList("applied") != null) {
                        for (int j = 0; j < objects.get(i).getList("applied").size(); j++) {
                            parseObjects.put(((ParseObject) objects.get(i).getList("applied").get(j)).getObjectId(), (ParseObject) objects.get(i).getList("applied").get(j));
                        }
                    }
                }
                adapter = new InboxAdapter(parseObjects, new InboxAdapter.OnInboxListener() {
                    @Override
                    public void onInboxClick(int position, List<ParseObject> parseObjects) {

                    }
                });
                if (adapter.getItemCount() != 0)
                    binding.recyclerview.setAdapter(adapter);
            }
        });
    }

    public FragemntCreatedJobsInboxBinding getBinding() {
        return binding;
    }


}
