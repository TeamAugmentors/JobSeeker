package com.example.jobseeker.app.startScreen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.HomePage;
import com.example.jobseeker.databinding.ActivitySplashScreenBinding;
import com.parse.ParseUser;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        new Handler().postDelayed(() -> {
            if (ParseUser.getCurrentUser() != null)
                startActivity(new Intent(this, HomePage.class));
            else
                startActivity(new Intent(this, WelcomeScreen.class));
            finish();
        }, 1300);

    }

}