package com.example.jobseeker.app.homePage.fragments.createprofile.titleFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.FragmentCreateProfileSkillsetBinding;

public class FragmentSkillSet extends Fragment {

    FragmentCreateProfileSkillsetBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentCreateProfileSkillsetBinding.inflate(inflater, container, false)).getRoot();
    }

}
