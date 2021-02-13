package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.app.startScreen.WelcomeScreen;
import com.example.jobseeker.databinding.ActivityHomepageBinding;
import com.google.android.material.navigation.NavigationView;
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
        if (ParseUser.getCurrentUser().get("name") != null){
            ((TextView) findViewById(R.id.user)).setText("Welcome, " + ParseUser.getCurrentUser().get("name").toString() + "!");
        } else
            ((TextView) findViewById(R.id.user)).setText("Welcome, user!");
    }

    private void init() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,binding.drawerLayout,binding.toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        binding.navView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setQueryHint("Search");
        return super.onCreateOptionsMenu(menu);
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

        return true;
    }
}