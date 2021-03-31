package com.example.jobseeker.app.homePage;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCompleteThisJobBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseObject;

public class CompleteThisJob extends AppCompatActivity {
    ActivityCompleteThisJobBinding binding;
    ParseObject jobObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityCompleteThisJobBinding.inflate(getLayoutInflater())).getRoot());
        init();
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Complete This Job");
        jobObject = getIntent().getParcelableExtra("jobObject");

        errorTextControl();
    }

    public void errorTextControl() {
        binding.driveLink.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.driveLink.getEditText().getText().toString().isEmpty()) {
                    binding.driveLink.getEditText().setError("*This Field is Required");
                } else {
                    binding.driveLink.getEditText().setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.sampleLink.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.sampleLink.getEditText().getText().toString().isEmpty()) {
                    binding.sampleLink.getEditText().setError("*This Field is Required");
                } else {
                    binding.sampleLink.getEditText().setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void shareLink(View view) {
        //upload files to drive here
        String driveLink = binding.driveLink.getEditText().getText().toString();
        String sampleLink = binding.sampleLink.getEditText().getText().toString();

        if (sampleLink != null && driveLink != null) {
            jobObject.put("projectFileLink", driveLink);
            jobObject.put("sampleFileLink", sampleLink);
            jobObject.put("completed", true);
            jobObject.saveInBackground(e -> {
                if (e == null) {
                    finish();
                }
            });
        }
    }
}