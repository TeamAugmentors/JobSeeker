package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCreatedPostBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.ncorti.slidetoact.SlideToActView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class AppliedPosts extends AppCompatActivity implements CreatedPostsAdapter.OnCreatedPostsListener {

    ActivityCreatedPostBinding binding;
    ArrayList<ParseObject> parseObjects;
    CreatedPostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatedPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        fetchData();
    }

    private void fetchData() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("appliedPosts");

        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                List<ParseObject> objects = (List<ParseObject>) object.get("appliedPosts");
                parseObjects = (ArrayList<ParseObject>) objects;

                if (parseObjects != null) {

                    adapter = new CreatedPostsAdapter(parseObjects, this);

                    binding.recyclerView.setAdapter(adapter);
                    binding.chip.setText("Applied to " + parseObjects.size() + " Jobs");
                } else {
                    binding.chip.setText("Applied to 0 Jobs");
                }
            } else {
                Toast.makeText(AppliedPosts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.chip.setText("Unable to fetch");
            }

        });

    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, binding.collapsingToolbar, this, "Applied Jobs");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);


        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            verticalOffset = verticalOffset * -1;
            Log.d("AWOFJAWOF", verticalOffset + "");
            float x = (float) (binding.appBar.getTotalScrollRange() / 2.0);
            if (verticalOffset <= x) {
                binding.chip.setAlpha((float) (1.0 - (verticalOffset / x)));
            }
        });

    }

    @Override
    public void onJobBoardClick(int position, List<ParseObject> parseObjects) {

        Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        View dialogView = dialog.getWindow().getDecorView();


        dialogView.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
        });

        ((TextView) dialogView.findViewById(R.id.title)).setText(parseObjects.get(position).getString("title"));
        ((TextView) dialogView.findViewById(R.id.description)).setText(parseObjects.get(position).getString("description"));
        ((TextView) dialogView.findViewById(R.id.budget)).setText(parseObjects.get(position).getInt("budget") + "");
        ((TextView) dialogView.findViewById(R.id.duration)).setText(parseObjects.get(position).getString("duration"));
        ((TextView) dialogView.findViewById(R.id.revisions)).setText(parseObjects.get(position).getInt("revisions") + "");
        (dialogView.findViewById(R.id.seeFreelancerButton)).setVisibility(View.GONE);
        (dialogView.findViewById(R.id.deleteButton)).setVisibility(View.GONE);
        (dialogView.findViewById(R.id.horizontalLine)).setVisibility(View.GONE);

        if (parseObjects.get(position).getBoolean("negotiable"))
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("Yes");
        else
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("No");
        ScrollView scrollView = dialogView.findViewById(R.id.scrollView);
        String text = ((TextView) dialogView.findViewById(R.id.description)).getText().toString();
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
        scrollView.setLayoutParams(params);

        ((SlideToActView) dialogView.findViewById(R.id.applySlider)).setVisibility(View.GONE);

        dialog.findViewById(R.id.message).setVisibility(View.VISIBLE);

        dialog.findViewById(R.id.message).setOnClickListener(v -> {
            startActivity(new Intent(this, Inbox.class));
        });

    }

}