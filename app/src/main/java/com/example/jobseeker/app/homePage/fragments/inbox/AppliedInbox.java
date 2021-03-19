package com.example.jobseeker.app.homePage.fragments.inbox;

import android.content.Intent;
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

import com.example.jobseeker.app.homePage.LiveMessage;
import com.example.jobseeker.app.homePage.adapters.inbox.InboxAdapter;
import com.example.jobseeker.databinding.FragemntAppliedInboxBinding;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
                        public void onInboxClick(int position, ArrayList<ParseObject> users, ArrayList<byte[]> picBytesList) {
                            ///check length of our file in bytes. Pic wont exceed 500kb so we can pass bytes through intent ez if > 500kb,  your app will crash on intent with no error logs

                            startActivity(new Intent(getActivity(), LiveMessage.class).putExtra("picBytes", picBytesList.get(position))
                                    .putExtra("clientUser", users.get(position))
                                    .putExtra("type", "Hirer"));
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
