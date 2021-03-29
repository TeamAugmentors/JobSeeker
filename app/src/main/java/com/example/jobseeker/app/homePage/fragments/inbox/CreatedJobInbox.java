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

//                for(int i=0;i<parseObjects.size();i++) {
//                    for(int j=i+1;j<parseObjects.size();j++) {
//                        if(parseObjects.get(i).getUpdatedAt().toString().compareTo(parseObjects.get(j).getUpdatedAt().toString())>0){
//                            parseObjects.get(i).setObjectId(parseObjects.get(j).getObjectId());
//                            parseObjects.get(j).setObjectId(parseObjects.get(i).getObjectId());
//                        }
//                    }
//                }
                
                adapter = new InboxAdapter(parseObjects, new InboxAdapter.OnInboxListener() {

                    public void onInboxClick(int position, ArrayList<ParseObject> users) {
                        ///check length of our file in bytes. Pic wont exceed 500kb so we can pass bytes through intent ez if > 500kb,  your app will crash on intent with no error logs

                        startActivity(new Intent(getActivity(), LiveMessage.class)
                                .putExtra("clientUser", users.get(position))
                                .putExtra("type", "Free Lancer"));
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
