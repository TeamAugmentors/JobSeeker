package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.inbox.InboxAdapter;
import com.example.jobseeker.app.homePage.adapters.inbox.InboxViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityInboxBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Inbox extends AppCompatActivity {
    ActivityInboxBinding binding;
    ArrayList<ParseObject> usersThatIHired = new ArrayList<>();
    ArrayList<ParseObject> usersThatHiredMe = new ArrayList<>();
    HashMap<String, ParseObject> userMap = new HashMap<>();
    InboxAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityInboxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        fetchData();
    }

    private void fetchData() {
        HashMap<String, String > params = new HashMap<>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());
        Log.d("asfijasif", "aowjdfaw");
//        ParseCloud.callFunctionInBackground("fetchInboxNew", params, (ArrayList<ArrayList<ParseObject>> mixedList, ParseException e) -> {
//            if (e == null){
//
////                usersThatIHired = (ArrayList<ParseObject>) mixedList.get(0);
////                usersThatHiredMe = (ArrayList<ParseObject>) mixedList.get(1);
//
//                ArrayList<ParseObject> list1 =  mixedList.get(0);
//                ArrayList<ParseObject> list2 =  mixedList.get(1);
//
//                Log.d("asfijasif", list1.size() + " list 1");
//                Log.d("asfijasif", list2.size() + " list 2");
//
//                for (int i = 0; i < list1.size(); i++) {
//                    usersThatIHired.add(list1.get(i).getParseObject("hired"));
//                    userMap.put(usersThatIHired.get(i).getObjectId(), usersThatIHired.get(i));
//                }
//
//                for (int i = 0; i < list2.size(); i++) {
//                    usersThatHiredMe.add(list2.get(i).getParseObject("createdBy"));
//                    userMap.put(usersThatHiredMe.get(i).getObjectId(), usersThatHiredMe.get(i));
//                }
//
//                if(!userMap.isEmpty()){
//                    adapter = new InboxAdapter(userMap, (position, users) -> {
//                        startActivity(new Intent(this, LiveMessage.class)
//                                .putExtra("clientUser", users.get(position))
//                                .putExtra("type", "Hirer"));
//                    });
//
//                    binding.recyclerView.setAdapter(adapter);
//                }
//
//            } else {
//                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });

        ParseCloud.callFunctionInBackground("fetchInboxNew", params, (ArrayList<ArrayList<ParseObject>> object, ParseException e) -> {
            if (e==null){
                if (object.size() != 0){
                    adapter = new InboxAdapter(object.get(0), object.get(1), (position, users) -> {
                                 startActivity(new Intent(this, LiveMessage.class)
                                .putExtra("clientUser", users.get(position))
                                .putExtra("type", "Hirer"));
                    });

                    binding.recyclerView.setAdapter(adapter);
                }
            } else {
                Toast.makeText(this, "error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Inbox");

        binding.swipeRefresh.setColorSchemeResources(R.color.job_seeker_logo_green);

        binding.swipeRefresh.setOnRefreshListener(this::fetchData);
    }
}