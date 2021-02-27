package com.example.jobseeker.app.homePage.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.CreateJob;
import com.example.jobseeker.databinding.FragmentCreateJobRequirementBinding;

public class FragmentJobRequirement extends Fragment {
    FragmentCreateJobRequirementBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateJobRequirementBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.datePickerLayout.setOnKeyListener(null);

        binding.datePickerLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus){
                ((CreateJob)getActivity()).showCalender(null);
            }
        });
    }
}
