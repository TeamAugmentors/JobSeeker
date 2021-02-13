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
import com.mukesh.OtpView;
import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.HashMap;

public class WelcomeScreen extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean slideChange = false;
    WelcomeScreenViewPager2Adapter adapter;
    String phoneNo;

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
        phoneNo = ((EditText) findViewById(R.id.editTextPhone)).getText().toString();
        if (phoneNo.length() == 0) {
            Toast.makeText(WelcomeScreen.this, "Please enter your phone number", Toast.LENGTH_SHORT).show();
        } else if (phoneNo.length() != 11) {
            Toast.makeText(WelcomeScreen.this, "Please enter a correct phone number!", Toast.LENGTH_SHORT).show();
        } else {

            HashMap<String, String> map = new HashMap<>();
            map.put("username" , phoneNo);
            String otp = generateOtp(5);
            Toast.makeText(this, otp, Toast.LENGTH_SHORT).show();
            map.put("otp", otp);

            ParseCloud.callFunctionInBackground("getOtp", map, (FunctionCallback<Boolean>) (isLogin, e) -> {
                if (e == null){
                    slideChange = true;
                    binding.viewPager2.setCurrentItem(1);
                    adapter.getEnterOTPSlide().setOtpHeaderText("We have sent a code to +88" + phoneNo);
                } else
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }


    private String generateOtp(int length) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            str.append((int) (Math.random() * 10));
        }
        return str.toString();
    }

    public void submitOtp(View view) {
        ParseUser.logInInBackground(phoneNo, ((OtpView) findViewById(R.id.otp_view)).getText().toString(), (user1, e) -> {
            if (e == null) {
                Toast.makeText(this, "Login was a success!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomePage.class));
                finish();
            } else
                Toast.makeText(this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public void logout(View view) {
        ParseUser.logOut();
    }
}