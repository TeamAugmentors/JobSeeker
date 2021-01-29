package com.example.jobseeker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jobseeker.fragments.PageFragment1;
import com.example.jobseeker.fragments.PageFragment2;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<Fragment> list = new ArrayList<>();
        list.add(new PageFragment1());
        list.add(new PageFragment2());

        pager = findViewById(R.id.pager);
        pagerAdapter = new SlidePageAdapter(getSupportFragmentManager(),list);
        pager.setAdapter(pagerAdapter);
    }

}