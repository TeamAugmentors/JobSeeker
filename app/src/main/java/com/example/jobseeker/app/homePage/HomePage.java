package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.jobseeker.R;
import com.example.jobseeker.app.startScreen.WelcomeScreen;
import com.example.jobseeker.databinding.ActivityHomepageBinding;
import com.example.jobseeker.utils.ChipHelper;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHomepageBinding binding;
    SwitchMaterial switch_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        switch_id = findViewById(R.id.switch_id);
        init();
        fetchData();

    }

    private void fetchData() {
        if (ParseUser.getCurrentUser().get("firstName") != null){
            binding.navView.getMenu().getItem(0).setTitle("Edit Profile");
            binding.navView.getMenu().getItem(0).setIcon(R.drawable.ic_edit_profile);
            ((TextView)binding.navView.getHeaderView(0).getRootView().findViewById(R.id.user)).setText("Welcome, " + ParseUser.getCurrentUser().getString("firstName") + "!");

            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("proPic");
            imageFile.getDataInBackground((data, e) -> {
                if (e == null) {

                    Glide.with(this)
                            .asBitmap()
                            .load(data)
                            .override(500,500)
                            .transform(new CircleCrop())
                            .into((ImageView) binding.navView.getHeaderView(0).getRootView().findViewById(R.id.proPic));

                } else {
                    Toast.makeText(this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ((TextView)binding.navView.getHeaderView(0).getRootView().findViewById(R.id.user)).setText("Welcome, user!");
            binding.navView.getMenu().getItem(0).setTitle("Create Profile");
            binding.navView.getMenu().getItem(0).setIcon(R.drawable.ic_create_profile);
        }
    }

    private void init() {
        setSupportActionBar(binding.toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getColor(R.color.white));
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchData();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_logout){
            ParseUser.logOutInBackground(e -> {
                if (e==null) {
                    Toast.makeText(this, "logged out successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, WelcomeScreen.class));
                    finish();
                } else
                    Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
        else if(item.getItemId()==R.id.nav_profile){
                startActivity(new Intent(this, CreateProfile.class));
        }
        else if(item.getItemId()==R.id.nav_created_jobs){
            startActivity(new Intent(this, CreatedPosts.class));
        }
        else if(item.getItemId()==R.id.nav_applied_jobs){
            startActivity(new Intent(this, AppliedPosts.class));
        }
        else if(item.getItemId()==R.id.nav_view_profile){
            if (ParseUser.getCurrentUser().get("firstName") != null){

                Dialog dialog = new Dialog(this, R.style.Dialog);
                dialog.setContentView(R.layout.dialog_view_profile);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                //fetching user image
                ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("proPic");
                imageFile.getDataInBackground((data, e) -> {
                    if (e == null) {

                        Glide.with(this)
                                .asBitmap()
                                .load(data)
                                .override(500,500)
                                .transform(new CircleCrop())
                                .into((ImageView) dialog.findViewById(R.id.view_profile_Image));

                    } else {
                        Toast.makeText(this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //fetching userinfo
                ((TextView)dialog.findViewById(R.id.view_profile_picture_text_view)).setText(ParseUser.getCurrentUser().get("firstName").toString());
                ((TextView)dialog.findViewById(R.id.phone_chip)).setText(ParseUser.getCurrentUser().get("username").toString());
                ((TextView)dialog.findViewById(R.id.bkash_chip)).setText(ParseUser.getCurrentUser().get("bkashNo").toString());

                double temp=ParseUser.getCurrentUser().getInt("totalEarned");
                if(temp>1000)
                {
                    temp/=1000;
                    int flag = (int)((temp*10)%10);
                    int temp2 = (int)(temp);

                    if(flag==0)
                        ((TextView)dialog.findViewById(R.id.earn)).setText(temp2+"K earned");
                    else{
                        ((TextView)dialog.findViewById(R.id.earn)).setText(String.format("%.1f",temp)+"K earned");
                    }
                }
                else{
                    ((TextView)dialog.findViewById(R.id.earn)).setText(String.valueOf(temp)+" earned");
                }

                int jobs = ParseUser.getCurrentUser().getInt("jobsCompleted");
                if(jobs>1000)
                {
                    jobs/=1000;
                    ((TextView)dialog.findViewById(R.id.job_complete)).setText(String.format("%.1f",jobs)+" jobs completed");
                }
                else {
                    ((TextView) dialog.findViewById(R.id.job_complete)).setText(String.valueOf(jobs)+ " jobs completed");
                }

                if (ParseUser.getCurrentUser().getString("skillSet") != null) {
                    ChipHelper.addChipIntoChipGroup(dialog.findViewById(R.id.skill_chip_group), this, false, ParseUser.getCurrentUser().getString("skillSet").split(","));
                }

                ScrollView scrollView=dialog.findViewById(R.id.scroll_view);
                ChipGroup chipGroup = dialog.findViewById(R.id.skill_chip_group);
                int chipCount = chipGroup.getChildCount();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = displayMetrics.heightPixels;

                if (chipCount <= 10) {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    scrollView.setLayoutParams(params);
                } else {
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
                    scrollView.setLayoutParams(params);
                }

                dialog.show();

                dialog.findViewById(R.id.close).setOnClickListener(v -> {
                    dialog.dismiss();
                });

                dialog.findViewById(R.id.root).setOnFocusChangeListener((v, hasFocus) -> {
                    if (hasFocus)
                        dialog.dismiss();
                });

            }
            else{
                Toast.makeText(this, "Please create a profile", Toast.LENGTH_SHORT).show();
            }
        }
        else if(item.getItemId()==R.id.nav_switch){
           setDarkMode(true);
        }
        return true;
    }

    private void setDarkMode(Boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        switch_id = findViewById(R.id.switch_id);

        //menu item-theme
        if (flag){
            if(switch_id.isChecked()) {
                editor.putBoolean("isDarkModeOn", false);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                switch_id.setChecked(false);
            }
            else {
                editor.putBoolean("isDarkModeOn", true);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                switch_id.setChecked(true);
            }
        }

        else{
            if(!switch_id.isChecked()) {
                editor.putBoolean("isDarkModeOn", false);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            else {
                editor.putBoolean("isDarkModeOn", true);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
    }

//    public void resetSwitch(){
//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
//        final boolean isButtonChecked = sharedPreferences.getBoolean("isDarkModeOn",false);
//        switch_id.setChecked(isButtonChecked);
//    }

    public void jobBoard(View view) {
        startActivity (new Intent(this, JobBoard.class));
    }

    public void createJob(View view){
        startActivity(new Intent(this, CreateJob.class));
    }

    public void liveChat(View view) {
        startActivity(new Intent(this, Inbox.class));
    }

    public void darkMode(View view) {
        setDarkMode(false);
    }
}