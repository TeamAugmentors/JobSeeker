package com.example.jobseeker.app.homePage.fragments.createprofile.profileFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.FragmentCreateJobBudgetBinding;
import com.example.jobseeker.databinding.FragmentCreateProfile2Binding;
import com.example.jobseeker.databinding.FragmentCreateProfile3Binding;

public class CreateProfile3 extends Fragment {

    FragmentCreateProfile3Binding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_profile3,container,false);
    }
    public FragmentCreateProfile3Binding getBinding() {
        return binding;
    }
}
