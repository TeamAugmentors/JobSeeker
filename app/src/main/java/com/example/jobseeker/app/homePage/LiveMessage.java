package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jobseeker.app.homePage.adapters.LiveChatAdapter;
import com.example.jobseeker.databinding.ActivityLiveMessageBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.parse.livequery.ParseLiveQueryClient;
import com.parse.livequery.SubscriptionHandling;


public class LiveMessage extends AppCompatActivity {
    ActivityLiveMessageBinding binding;
    ParseUser clientUser;
    LiveChatAdapter adapter;
    ArrayList<ParseObject> parseObjects = new ArrayList<>();


    byte[] proPicBytes;

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

                Log.d("agag", parseObjects.toString());
            } else {
                Toast.makeText(this, "error " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private ParseQuery<ParseObject> getMainQuery() {

        ParseQuery<ParseObject> parseQuery1 = ParseQuery.getQuery("LiveMessage");

        parseQuery1.whereEqualTo("createdBy" , ParseUser.getCurrentUser().getUsername());
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

        proPicBytes = getIntent().getByteArrayExtra("picBytes");
        clientUser = getIntent().getParcelableExtra("clientUser");

        Glide.with(this).load(proPicBytes).into(binding.clientPic);

        binding.fullname.setText(clientUser.getString("firstName"));
        binding.type.setText(getIntent().getStringExtra("type"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        binding.recyclerView.setLayoutManager(layoutManager);

        binding.recyclerView.setItemViewCacheSize(50);

        liveQueryInit();
    }

    ParseLiveQueryClient parseLiveQueryClient;

    private void liveQueryInit() {
        // Init Live Query Client
        try {
            parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient(new URI("wss://jobseeker.b4a.io/"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        if (parseLiveQueryClient != null) {

            ParseQuery<ParseObject> mainQuery = getMainQuery();

            SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(mainQuery);

            subscriptionHandling.handleEvent(SubscriptionHandling.Event.CREATE, (query, object) -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(() -> {
                    addMessage(object);

                    binding.recyclerView.scrollToPosition(binding.recyclerView.getAdapter().getItemCount() - 1);
                });
            });
        }

    }


    public void sendMessage(View view) {
        String message = binding.msgEditText.getText().toString();
        if (!message.trim().equals("")){
            binding.msgEditText.getText().clear();

            ParseObject messageObject = new ParseObject("LiveMessage");
            messageObject.put("message", message.trim());
            messageObject.put("createdBy", ParseUser.getCurrentUser().getUsername());
            messageObject.put("createdFor", clientUser.getUsername());
            messageObject.addAll("pairUsernames", Arrays.asList(ParseUser.getCurrentUser().getUsername(), clientUser.getUsername()));

            messageObject.saveInBackground(e -> {
                if (e == null) {
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