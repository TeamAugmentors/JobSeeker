package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobseeker.databinding.ActivityMainBinding;
import com.example.jobseeker.databinding.Fragment1Binding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding Binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(Binding.getRoot());


    }

}