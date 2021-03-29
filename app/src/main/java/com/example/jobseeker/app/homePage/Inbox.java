package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.inbox.InboxAdapter;
import com.example.jobseeker.databinding.ActivityInboxBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

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
        binding.swipeRefresh.setRefreshing(true);

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", ParseUser.getCurrentUser().getObjectId());
        Log.d("asfijasif", "aowjdfaw");

        ParseCloud.callFunctionInBackground("fetchInboxNew", params, (ArrayList<ArrayList<ParseObject>> object, ParseException e) -> {
            //Object.get(0) are your users, Object.get(1) are recent messages considering current parseUser
            if (e == null) {
                if (object.size() != 0) {
                    adapter = new InboxAdapter(object.get(0), object.get(1), (position, users, recentMessagePosition) -> {
                        startActivity(new Intent(this, LiveMessage.class)
                                .putExtra("clientUser", users.get(position))
                                .putExtra("type", "Hirer"));
                    });

                    binding.recyclerView.setAdapter(adapter);
                }

            } else {
                Toast.makeText(this, "error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            binding.swipeRefresh.setRefreshing(false);
        });
    }


    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Inbox");

        binding.swipeRefresh.setColorSchemeResources(R.color.job_seeker_logo_green);

        binding.swipeRefresh.setOnRefreshListener(this::fetchData);
    }
}