package com.example.jobseeker.utils;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.example.jobseeker.R;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class ToolbarHelper {

    ToolbarHelper(Toolbar toolbar, CollapsingToolbarLayout collapsingToolbarLayout, AppCompatActivity activity, String title) {
        toolbar.setTitleTextColor(activity.getColor(R.color.white));
        activity.setSupportActionBar(toolbar);

        activity.getSupportActionBar().setTitle(title);

        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity.getSupportActionBar().setDisplayShowHomeEnabled(true);

        final Drawable upArrow = ContextCompat.getDrawable(activity, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(activity, R.color.white), PorterDuff.Mode.SRC_ATOP);
        activity.getSupportActionBar().setHomeAsUpIndicator(upArrow);


        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setExpandedTitleColor(activity.getColor(R.color.white));
            collapsingToolbarLayout.setCollapsedTitleTextColor(activity.getColor(R.color.white));
        }

    }

    public static ToolbarHelper create(Toolbar toolbar, CollapsingToolbarLayout collapsingToolbarLayout, AppCompatActivity activity, String title) {
        return new ToolbarHelper(toolbar, collapsingToolbarLayout, activity, title);
    }

}
