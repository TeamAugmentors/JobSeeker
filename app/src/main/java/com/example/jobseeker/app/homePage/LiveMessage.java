package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityInboxBinding;
import com.example.jobseeker.databinding.ActivityLiveMessageBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.parse.ParseUser;

public class LiveMessage extends AppCompatActivity {
    ActivityLiveMessageBinding binding;
    ParseUser parseUser;
    Drawable drawable;

    byte[] proPicBytes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityLiveMessageBinding.inflate(getLayoutInflater())).getRoot());
        init();
    }

    private void init() {

        ToolbarHelper.create(binding.toolbar, null, this, null);

        proPicBytes = getIntent().getByteArrayExtra("picBytes");
        parseUser = getIntent().getParcelableExtra("clientUser");

        Glide.with(this).load(proPicBytes).into(binding.clientPic);

        binding.fullname.setText(parseUser.getString("firstName"));
        binding.type.setText(getIntent().getStringExtra("type"));
    }


    public void sendMessage(View view) {

    }
}