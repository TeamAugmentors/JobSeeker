package com.example.jobseeker.app.startScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityMainBinding;
import com.example.jobseeker.app.homePage.HomePage;
import com.example.jobseeker.app.startScreen.adapters.WelcomeScreenViewPager2Adapter;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class WelcomeScreen extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean slideChange = false;
    WelcomeScreenViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
    }

    private void init() {
        adapter = new WelcomeScreenViewPager2Adapter(this);

        binding.viewPager2.setAdapter(adapter);
        binding.viewPager2.setOffscreenPageLimit(5);

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if (position == 1 && !slideChange) {
                    binding.viewPager2.setCurrentItem(0);
                    Toast.makeText(WelcomeScreen.this, "You must enter phone number", Toast.LENGTH_SHORT).show();
                } else {
                    slideChange = false;
                }
            }
        });

    }

    //onClick
    public void getOtp(View view) {
        String phoneNo = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
        if (phoneNo.length() == 0) {
            Toast.makeText(WelcomeScreen.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (phoneNo.length() != 11) {
            Toast.makeText(WelcomeScreen.this, "Please enter a correct phone number!", Toast.LENGTH_SHORT).show();
        } else {
            slideChange = true;
            binding.viewPager2.setCurrentItem(1);
            adapter.getEnterOTPSlide().setOtpHeaderText("We have sent a code to +88" + phoneNo);

            createUser(phoneNo);
        }
    }

    //Parse

    public void createUser(String phoneNo) {
        ParseUser user = new ParseUser();
        user.setUsername(phoneNo);
        user.setPassword("1234abc");

    }

    public void verify(View view) {
        Intent intent = new Intent(WelcomeScreen.this, HomePage.class);
        startActivity(intent);
    }
}