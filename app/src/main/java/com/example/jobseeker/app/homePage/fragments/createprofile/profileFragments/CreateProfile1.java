package com.example.jobseeker.app.homePage.fragments.createprofile.profileFragments;

import android.os.Bundle;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.CreateProfile;
import com.example.jobseeker.databinding.FragmentCreateJobBudgetBinding;
import com.example.jobseeker.databinding.FragmentCreateProfile1Binding;
import com.google.android.material.chip.ChipDrawable;

public class CreateProfile1 extends Fragment {

    FragmentCreateProfile1Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentCreateProfile1Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public FragmentCreateProfile1Binding getBinding() {
        return binding;
    }
}
