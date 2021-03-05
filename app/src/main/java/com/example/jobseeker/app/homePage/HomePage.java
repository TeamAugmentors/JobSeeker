package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.jobseeker.R;
import com.example.jobseeker.app.startScreen.WelcomeScreen;
import com.example.jobseeker.databinding.ActivityHomepageBinding;
import com.example.jobseeker.utils.ChipHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHomepageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
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
                dialog.setContentView(R.layout.dialogue_view_profile);
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

                View dialogView = dialog.getWindow().getDecorView();

                dialogView.findViewById(R.id.close).setOnClickListener(v -> {
                    dialog.dismiss();
                });

            }
            else{

            }
        }
        return true;
    }

    public void jobBoard(View view) {
        startActivity (new Intent(this, JobBoard.class));
    }

    public void createJob(View view){
        startActivity(new Intent(this, CreateJob.class));
    }
}