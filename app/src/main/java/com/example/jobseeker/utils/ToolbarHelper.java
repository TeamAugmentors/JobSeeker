package com.example.jobseeker.utils;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToolbarHelper {

    ToolbarHelper(Toolbar toolbar, AppCompatActivity activity, String title){
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setTitle(title);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public static ToolbarHelper create(Toolbar toolbar, AppCompatActivity activity, String title){
        return new ToolbarHelper(toolbar, activity, title);
    }
}
