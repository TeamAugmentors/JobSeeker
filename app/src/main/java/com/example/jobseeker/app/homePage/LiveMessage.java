package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.LiveChatAdapter;
import com.example.jobseeker.databinding.ActivityLiveMessageBinding;
import com.example.jobseeker.parseSdk.Connect;
import com.example.jobseeker.utils.HelperUtils;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;


public class LiveMessage extends AppCompatActivity {
    ActivityLiveMessageBinding binding;
    ParseUser clientUser;
    LiveChatAdapter adapter;
    ArrayList<ParseObject> parseObjects = new ArrayList<>();

    String currentDate, seenDate, seenClock, outputTime = "", showTime, seenTime, currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityLiveMessageBinding.inflate(getLayoutInflater())).getRoot());

        init();
        fetchData();
    }

    private void fetchData() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("clientUsername", clientUser.getUsername());

        ParseCloud.callFunctionInBackground("fetchInbox", hashMap, (objects, e) -> {
            if (e == null) {
                Log.d("asfokasf", objects.toString());
                parseObjects = (ArrayList<ParseObject>) objects;

                adapter = new LiveChatAdapter(parseObjects);
                binding.recyclerView.setAdapter(adapter);

                if (parseObjects.size() != 0) {
                    ParseObject lastObject = parseObjects.get(parseObjects.size() - 1);
                    if (!lastObject.getBoolean("seenByFor") && !lastObject.getString("createdBy").equals(ParseUser.getCurrentUser().getUsername())) {
                        lastObject.put("seenByFor", true);
                        lastObject.saveInBackground();
                    }

                    if (lastObject.getString("createdBy").equals(ParseUser.getCurrentUser().getUsername()) && lastObject.getBoolean("seenByFor")) {
                        seenTime = lastObject.getUpdatedAt().toString();
                        currentTime = Calendar.getInstance().getTime().toString();

                        showTime = HelperUtils.getTime(seenTime, currentTime);

                        binding.txtRSeen.setText(showTime);
                        binding.txtRSeen.setVisibility(View.VISIBLE);
                    } else {
                        binding.txtRSeen.setVisibility(View.GONE);
                    }

                }
            } else {
                Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onPause() {
        ParseCloud.callFunctionInBackground("exitActivity", new HashMap<>());
        super.onPause();
    }

    private ParseQuery<ParseObject> getMainQuery() {

        ParseQuery<ParseObject> parseQuery1 = ParseQuery.getQuery("LiveMessage");

        parseQuery1.whereEqualTo("createdBy", ParseUser.getCurrentUser().getUsername());
        parseQuery1.whereEqualTo("createdFor", clientUser.getUsername());

        ParseQuery<ParseObject> parseQuery2 = ParseQuery.getQuery("LiveMessage");

        parseQuery2.whereEqualTo("createdFor", ParseUser.getCurrentUser().getUsername());
        parseQuery2.whereEqualTo("createdBy", clientUser.getUsername());

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(parseQuery1);
        queries.add(parseQuery2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending("createdAt");

        return mainQuery;
    }

    private void init() {

        ToolbarHelper.create(binding.toolbar, null, this, null);

        clientUser = getIntent().getParcelableExtra("clientUser");

        clientUser.getParseFile("proPic").getDataInBackground((data, e) -> {
            Glide.with(this).load(data).into(binding.clientPic);
        });

        binding.fullname.setText(clientUser.getString("firstName"));
        binding.type.setText(getIntent().getStringExtra("type"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        binding.recyclerView.setLayoutManager(layoutManager);

        binding.recyclerView.setItemViewCacheSize(50);


        liveQueryInit();

    }


    ParseLiveQueryClient parseLiveQueryClient;
    ParseQuery<ParseObject> mainQuery;

    private void liveQueryInit() {
        // Init Live Query Client
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://jobseeker.b4a.io/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (parseLiveQueryClient != null) {

            mainQuery = getMainQuery();

            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(mainQuery);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    addMessage(object);

                    if (!object.getString("createdBy").equals(ParseUser.getCurrentUser().getUsername())) {

                        playSound();

                        if (!object.getBoolean("seenByFor")) {
                            object.put("seenByFor", true);
                            object.saveInBackground();
                        }
                        binding.txtRSeen.setVisibility(View.GONE);
                    }
                    binding.recyclerView.scrollToPosition(binding.recyclerView.getAdapter().getItemCount() - 1);
                });
            });

            SubscriptionHandling<ParseObject> updateHandler = parseLiveQueryClient.subscribe(mainQuery);

            updateHandler.handleEvent(SubscriptionHandling.Event.UPDATE, (query, object) -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    if (object.getString("createdBy").equals(ParseUser.getCurrentUser().getUsername())) {
                        if (object.getBoolean("seenByFor")) {
                            binding.txtRSeen.setText("Seen");
                            binding.txtRSeen.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.txtRSeen.setVisibility(View.GONE);
                    }

                    binding.recyclerView.scrollToPosition(binding.recyclerView.getAdapter().getItemCount() - 1);
                });
            });

        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        parseLiveQueryClient.unsubscribe(mainQuery);
    }

    private void playSound() {
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.pop);
        mp.start();
    }


    public void sendMessage(View view) {
        String message = binding.msgEditText.getText().toString().trim();
        if (!message.equals("")) {
            binding.msgEditText.getText().clear();
            HashMap<String, String> params = new HashMap<>();

            params.put("message", message);
            params.put("deviceToken", clientUser.getString("pushyDeviceToken"));
            params.put("senderName", ParseUser.getCurrentUser().getString("firstName"));
            params.put("createdBy", ParseUser.getCurrentUser().getUsername());
            params.put("createdFor", clientUser.getUsername());
            params.put("clientUserId", clientUser.getObjectId());

            binding.txtRSeen.setVisibility(View.VISIBLE);
            binding.txtRSeen.setText("Sending...");

            ParseCloud.callFunctionInBackground("saveMessage", params, (object, e) -> {
                if (e == null) {
                    Toast.makeText(this, "msg saved, notification delivered!", Toast.LENGTH_SHORT).show();

                    binding.txtRSeen.setVisibility(View.VISIBLE);
                    binding.txtRSeen.setText("Delivered");
                } else {
                    Toast.makeText(this, "error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void addMessage(ParseObject messageObject) {
        parseObjects.add(messageObject);
        adapter.notifyItemInserted(parseObjects.size() - 1);
        adapter.notifyItemRangeChanged(parseObjects.size() - 1, parseObjects.size());
    }

}