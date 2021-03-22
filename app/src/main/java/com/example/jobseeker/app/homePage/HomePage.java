package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.ForYouAdapter;
import com.example.jobseeker.app.homePage.adapters.JobBoardAdapter;
import com.example.jobseeker.app.startScreen.WelcomeScreen;
import com.example.jobseeker.databinding.ActivityHomepageBinding;
import com.example.jobseeker.databinding.DialogLayoutBinding;
import com.example.jobseeker.utils.HelperUtils;
import com.example.jobseeker.utils.HorizontalZoomCenterLayoutManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.pushy.sdk.Pushy;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHomepageBinding binding;
    SwitchMaterial switch_id;
    boolean isDarkModeOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomepageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        registerUserForPush();
        fetchData();

    }

    private void registerUserForPush() {
        if (!Pushy.isRegistered(this)) {
            new RegisterForPushNotificationsAsync(this).execute();
        }
    }


    private void fetchData() {
        if (ParseUser.getCurrentUser().get("firstName") != null) {
            binding.navView.getMenu().getItem(0).setTitle("Edit Profile");
            binding.navView.getMenu().getItem(0).setIcon(R.drawable.ic_edit_profile);
            ((TextView) binding.navView.getHeaderView(0).getRootView().findViewById(R.id.user)).setText("Welcome, " + ParseUser.getCurrentUser().getString("firstName") + "!");

            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("proPic");
            imageFile.getDataInBackground((data, e) -> {
                if (e == null) {

                    Glide.with(this)
                            .asBitmap()
                            .load(data)
                            .override(500, 500)
                            .transform(new CircleCrop())
                            .into((ImageView) binding.navView.getHeaderView(0).getRootView().findViewById(R.id.proPic));

                } else {
                    Toast.makeText(this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            if (ParseUser.getCurrentUser().get("skillSet") != null) {
                fetchJobs(null);
            } else {
                binding.textViewLinearLayout.setVisibility(View.VISIBLE);
                binding.refreshForYou.setVisibility(View.GONE);

                binding.forYouRecyclerView.setAdapter(null);

                binding.addSkill.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit_small,0,0,0);
                binding.addSkill.setText("Edit Profile");
                binding.textView.setText("Please add some skills\nto get recommendations!");
            }

        } else {
            ((TextView) binding.navView.getHeaderView(0).getRootView().findViewById(R.id.user)).setText("Welcome, user!");
            binding.navView.getMenu().getItem(0).setTitle("Create Profile");
            binding.navView.getMenu().getItem(0).setIcon(R.drawable.ic_create_profile);

            binding.addSkill.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_create_small,0,0,0);
            binding.addSkill.setText("Create Profile");
            binding.textView.setText("Please create a profile\nto get recommendations!");
        }

    }

    ArrayList<ParseObject> jobObjects = new ArrayList<>();
    ForYouAdapter adapter;
    DialogLayoutBinding bindingDialog;

    public void fetchJobs(View view) {
        binding.forYouSpinKit.setVisibility(View.VISIBLE);
        binding.textViewLinearLayout.setVisibility(View.GONE);
        binding.refreshForYou.setVisibility(View.GONE);

        String skillSet = ParseUser.getCurrentUser().getString("skillSet").toUpperCase();

        String tokens[] = skillSet.split(",");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.include("applied");
        query.whereNotEqualTo("applied", ParseUser.getCurrentUser());
        query.whereNotEqualTo("createdBy", ParseUser.getCurrentUser());
        query.orderByDescending("createdAt");

        binding.forYouSpinKit.setVisibility(View.VISIBLE);
        query.findInBackground((List<ParseObject> objects, ParseException e) -> {
            if (e == null) {
                jobObjects = new ArrayList<>();
                int len = objects.size();
                for (int i = 0; i < len; i++) {

                    for (int j = 0; j < tokens.length; j++) {

                        if (objects.get(i).getString("title").toUpperCase().contains(tokens[j])){
                            objects.get(i).put("chip", tokens[j]);
                            jobObjects.add(objects.get(i));
                            break;
                        }
                    }

                }

                if (jobObjects.size() != 0) {
                    adapter = new ForYouAdapter(jobObjects, new ForYouAdapter.OnJobBoardListener() {
                        @Override
                        public void onJobBoardClick(int position, List<ParseObject> parseObjects) {
                            Dialog dialog = new Dialog(HomePage.this, R.style.Dialog);
                            bindingDialog = DialogLayoutBinding.inflate(getLayoutInflater());

                            dialog.setContentView(bindingDialog.getRoot());

                            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            dialog.show();

                            bindingDialog.close.setOnClickListener(v -> {
                                dialog.dismiss();
                            });

                            ParseObject currentObject = parseObjects.get(position);

                            bindingDialog.title.setText(currentObject.getString("title"));
                            bindingDialog.description.setText(currentObject.getString("description"));
                            bindingDialog.budget.setText(currentObject.getInt("budget") + "");
                            bindingDialog.duration.setText(currentObject.getString("duration"));
                            bindingDialog.revisions.setText(currentObject.getInt("revisions") + "");
                            bindingDialog.seeFreelancerButton.setVisibility(View.GONE);
                            bindingDialog.deleteButton.setVisibility(View.GONE);

                            if (currentObject.getBoolean("negotiable"))
                                bindingDialog.negotiable.setText("Yes");
                            else
                                bindingDialog.negotiable.setText("No");

                            //Dynamic scroll view height

                            String text = bindingDialog.description.getText().toString();

                            int charCount = text.length();

                            DisplayMetrics displayMetrics = new DisplayMetrics();
                            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                            int height = displayMetrics.heightPixels;

                            LinearLayout.LayoutParams params;

                            if (charCount <= 200) {
                                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            } else {
                                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
                            }

                            bindingDialog.scrollView.setLayoutParams(params);

                            //---------------------->
                            ArrayList<ParseFile> parseFiles = new ArrayList<>();
                            for (int i = 0; i < 3; i++) {
                                if (currentObject.getParseFile("file" + (i + 1)) != null)
                                    parseFiles.add(currentObject.getParseFile("file" + (i + 1)));
                            }
                            JobBoard jobBoard = new JobBoard();
                            if (parseFiles.size() != 0)
                                jobBoard.addButtonsToLayout(parseFiles, bindingDialog.fileLinearLayout,HomePage.this, currentObject.getObjectId());
                            else {
                                bindingDialog.sampleFileTextView.setText("No sample files provided");
                            }
                            //---------------------->
                            bindingDialog.applySlider.setOnSlideCompleteListener(slideToActView -> {
                                //apply
                                if (ParseUser.getCurrentUser().getString("firstName") != null) {
                                    currentObject.add("applied", ParseUser.getCurrentUser());

                                    currentObject.saveInBackground(e -> {
                                        if (e == null) {
                                            Toast.makeText(HomePage.this, "Successfully applied!", Toast.LENGTH_SHORT).show();

                                            ParseUser.getCurrentUser().add("appliedPosts", currentObject);
                                            ParseUser.getCurrentUser().saveEventually();

                                            removeJob(parseObjects, position);

                                            dialog.dismiss();
                                        } else {
                                            Toast.makeText(HomePage.this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            slideToActView.resetSlider();
                                        }
                                    });

                                } else {
                                    Toast.makeText(HomePage.this, "Please create an account first", Toast.LENGTH_SHORT).show();
                                    slideToActView.resetSlider();
                                }
                            });
                        }
                    });

                    binding.forYouRecyclerView.setAdapter(adapter);
                    //Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();

                    binding.textViewLinearLayout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(this, "Sorry! no matching jobs found", Toast.LENGTH_SHORT).show();

                    binding.refreshForYou.setVisibility(View.VISIBLE);
                    binding.refreshForYou.setAnimation(AnimationUtils.loadAnimation(this, R.anim.spin_in));

                    binding.textViewLinearLayout.setVisibility(View.GONE);

                    binding.forYouRecyclerView.setAdapter(null);
                }

                binding.forYouSpinKit.setVisibility(View.GONE);
            } else {
                Toast.makeText(HomePage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void init() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        setSupportActionBar(binding.toolbar);

        binding.forYouRecyclerView.setLayoutManager(new HorizontalZoomCenterLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.forYouRecyclerView.setItemViewCacheSize(1);
        SnapHelper snap= new LinearSnapHelper();
        snap.attachToRecyclerView(binding.forYouRecyclerView);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        binding.drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            boolean bool = false;

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                if (!bool) {
                    switch_id = findViewById(R.id.switch_id);
                    resetSwitch();
                    bool = true;
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //check for dark mode and set toggle accordingly

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
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
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {
            ParseUser.logOutInBackground(e -> {
                if (e == null) {
                    Toast.makeText(this, "logged out successfully!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, WelcomeScreen.class));
                    finish();
                } else
                    Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else if (item.getItemId() == R.id.nav_profile) {
            createProfile(null);
        } else if (item.getItemId() == R.id.nav_created_jobs) {
            startActivity(new Intent(this, CreatedPosts.class));
        } else if (item.getItemId() == R.id.nav_applied_jobs) {
            startActivity(new Intent(this, AppliedPosts.class));
        } else if (item.getItemId() == R.id.nav_view_profile) {
            if (ParseUser.getCurrentUser().get("firstName") != null) {

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
                                .override(500, 500)
                                .transform(new CircleCrop())
                                .into((ImageView) dialog.findViewById(R.id.view_profile_Image));

                    } else {
                        Toast.makeText(this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                //fetching userinfo
                ((TextView) dialog.findViewById(R.id.view_profile_picture_text_view)).setText(ParseUser.getCurrentUser().get("firstName").toString());
                ((TextView) dialog.findViewById(R.id.phone_chip)).setText(ParseUser.getCurrentUser().get("username").toString());
                ((TextView) dialog.findViewById(R.id.bkash_chip)).setText(ParseUser.getCurrentUser().get("bkashNo").toString());

                double temp = ParseUser.getCurrentUser().getInt("totalEarned");
                if (temp > 1000) {
                    temp /= 1000;
                    int flag = (int) ((temp * 10) % 10);
                    int temp2 = (int) (temp);

                    if (flag == 0)
                        ((TextView) dialog.findViewById(R.id.earn)).setText(temp2 + "K earned");
                    else {
                        ((TextView) dialog.findViewById(R.id.earn)).setText(String.format("%.1f", temp) + "K earned");
                    }
                } else {
                    ((TextView) dialog.findViewById(R.id.earn)).setText(String.valueOf(temp) + " earned");
                }

                int jobs = ParseUser.getCurrentUser().getInt("jobsCompleted");
                if (jobs > 1000) {
                    jobs /= 1000;
                    ((TextView) dialog.findViewById(R.id.job_complete)).setText(String.format("%.1f", jobs) + " jobs completed");
                } else {
                    ((TextView) dialog.findViewById(R.id.job_complete)).setText(String.valueOf(jobs) + " jobs completed");
                }

                if (ParseUser.getCurrentUser().getString("skillSet") != null) {
                    HelperUtils.addChipIntoChipGroup(dialog.findViewById(R.id.skill_chip_group), this, false, isDarkModeOn, ParseUser.getCurrentUser().getString("skillSet").split(","));
                }

                ScrollView scrollView = dialog.findViewById(R.id.scroll_view);
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

            } else {
                Toast.makeText(this, "Please create a profile", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.nav_switch) {
            setDarkMode(true);
        }
        return true;
    }

    public void createProfile(View view) {
        startActivity(new Intent(this, CreateProfile.class));
    }

    private void setDarkMode(Boolean flag) {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        switch_id = findViewById(R.id.switch_id);

        //menu item-theme
        if (flag) {
            if (switch_id.isChecked()) {
                editor.putBoolean("isDarkModeOn", false);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                switch_id.setChecked(false);
            } else {
                editor.putBoolean("isDarkModeOn", true);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                switch_id.setChecked(true);
            }
        } else {
            if (!switch_id.isChecked()) {
                editor.putBoolean("isDarkModeOn", false);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else {
                editor.putBoolean("isDarkModeOn", true);
                editor.apply();
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            }
        }
    }

    public void resetSwitch() {
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        final boolean isButtonChecked = sharedPreferences.getBoolean("isDarkModeOn", false);
        switch_id.setChecked(isButtonChecked);
    }

    private void removeJob(List<ParseObject> parseObjects, int pos) {
        parseObjects.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, parseObjects.size());
    }

    public void jobBoard(View view) {
        startActivity(new Intent(this, JobBoard.class));
    }

    public void createJob(View view) {
        startActivity(new Intent(this, CreateJob.class));
    }

    public void liveChat(View view) {
        startActivity(new Intent(this, Inbox.class));
    }

    public void darkMode(View view) {
        setDarkMode(false);
    }
}