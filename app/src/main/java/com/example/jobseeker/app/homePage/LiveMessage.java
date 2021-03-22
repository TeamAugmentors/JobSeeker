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
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;


public class LiveMessage extends AppCompatActivity {
    ActivityLiveMessageBinding binding;
    ParseUser clientUser;
    LiveChatAdapter adapter;
    ArrayList<ParseObject> parseObjects = new ArrayList<>();
    private NotificationManagerCompat notificationManager;
    String currentDate,seenDate,seenClock,outputTime="",showTime,seenTime,currentTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityLiveMessageBinding.inflate(getLayoutInflater())).getRoot());

        init();
        fetchData();
    }

    private void fetchData() {
        getMainQuery().findInBackground((objects, e) -> {
            if (e == null) {
                parseObjects = (ArrayList<ParseObject>) objects;

                adapter = new LiveChatAdapter(parseObjects);
                binding.recyclerView.setAdapter(adapter);

                if (parseObjects.size() != 0) {
                    ParseObject lastObject = parseObjects.get(parseObjects.size() - 1);
                    if (!lastObject.getBoolean("seenByFor")) {
                        lastObject.put("seenByFor", true);
                        lastObject.saveInBackground();
                    }
                    if (lastObject.getString("createdBy").equals(ParseUser.getCurrentUser().getUsername())) {
                        seenTime = lastObject.getUpdatedAt().toString();
                        currentTime = Calendar.getInstance().getTime().toString();
                        showTime = seenCheckTime(seenTime,currentTime);
                        //Log.d("key", showTime+seenDate+" at " +outputTime);
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

    private String seenCheckTime(String seenTime, String currentTime){
        currentDate = currentTime.substring(7,10)+" "+ currentTime.substring(4,7) +" "+ currentTime.substring(30,34);
        seenDate = seenTime.substring(7,10) +" "+ seenTime.substring(4,7) +" "+ seenTime.substring(30,34);

        seenClock = seenTime.substring(11,16);
        StringBuilder temp = new StringBuilder(seenClock);
        outputTime = convertTo12(temp);

        if(currentDate.equals(seenDate)) {
            return "Seen "+ outputTime;
        }else{
            return "Seen "+ seenDate +" at " +outputTime;
        }
    }

    private String convertTo12(StringBuilder outputTime){
        if(outputTime.charAt(1)>'2' && outputTime.charAt(0)>'0') {
            int temp2 = ((outputTime.charAt(0) - '0') * 10 + (outputTime.charAt(1) - '0')) - 12;
            if (temp2 < 10) {
                outputTime.setCharAt(0, '0');
                outputTime.setCharAt(1, (char) (temp2 + '0'));
            } else {
                outputTime.setCharAt(1, (char) (temp2 % 10 + '0'));
                temp2 /= 10;
                outputTime.setCharAt(0, (char) (temp2 % 10 + '0'));
            }
            return outputTime.toString() + " pm";
        }
        return outputTime.toString()+" am";
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

        notificationManager = NotificationManagerCompat.from(this);

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
                        showNotification(clientUser.getString("firstName"));
                        if(!object.getBoolean("seenByFor")) {
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
                            seenTime = parseObjects.get(parseObjects.size()-1).getUpdatedAt().toString();
                            currentTime = Calendar.getInstance().getTime().toString();
                            showTime = seenCheckTime(seenTime,currentTime);

                            binding.txtRSeen.setText(showTime);
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

    private void showNotification(String name) {
        Notification notification = new NotificationCompat.Builder(this, Connect.CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_one)
                .setContentTitle(name)
                .setContentText("Sent you a message")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();
        notificationManager.notify(1, notification);
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
        String message = binding.msgEditText.getText().toString();
        if (!message.trim().equals("")) {
            binding.msgEditText.getText().clear();

            ParseObject messageObject = new ParseObject("LiveMessage");
            messageObject.put("message", message.trim());
            messageObject.put("createdBy", ParseUser.getCurrentUser().getUsername());
            messageObject.put("createdFor", clientUser.getUsername());

            messageObject.saveInBackground(e -> {
                if (e == null) {
                    binding.txtRSeen.setVisibility(View.VISIBLE);
                    binding.txtRSeen.setText("Delivered");
                } else {
                    Toast.makeText(this, "error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
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