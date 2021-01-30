package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jobseeker.databinding.ActivityMainBinding;
import com.example.jobseeker.fragments.PageFragment1;
import com.example.jobseeker.fragments.PageFragment2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    boolean slideChange = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        SlidePageAdapter slidePageAdapter = new SlidePageAdapter(this);

        binding.viewPager2.setAdapter(slidePageAdapter);

        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(position==1 && !slideChange){
                    binding.viewPager2.setCurrentItem(0);
                    Toast.makeText(MainActivity.this,"You must enter phone number",Toast.LENGTH_SHORT).show();
                }
                else{
                    slideChange=false;
                }
            }
        });


    }
    public void register(View view) {
        String phoneNo = ((EditText)findViewById(R.id.editTextPhone)).getText().toString();
        if(phoneNo.length()==0){
            Toast.makeText(MainActivity.this,"Enter phone number",Toast.LENGTH_SHORT).show();
        }
        else if(phoneNo.length()!=11){
            Toast.makeText(MainActivity.this,"Enter correct phone number",Toast.LENGTH_SHORT).show();
        }
        else{
            slideChange=true;
            binding.viewPager2.setCurrentItem(1);
        }
    }

    public void verify(View view) {
        Intent intent = new Intent(MainActivity.this,NextActivity.class);
        startActivity(intent);
    }
}