package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SecondPage extends AppCompatActivity {

    private TextView numberTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);

        numberTextView = (TextView) findViewById(R.id.numberTextView);

        String phoneNum = getIntent().getStringExtra("phoneNo");
        numberTextView.setText(phoneNum);
    }
}