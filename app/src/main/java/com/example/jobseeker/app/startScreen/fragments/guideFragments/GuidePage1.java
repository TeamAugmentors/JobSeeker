package com.example.jobseeker.app.startScreen.fragments.guideFragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.FragmentCreateJobBudgetBinding;
import com.example.jobseeker.databinding.GuidePage1Binding;

public class GuidePage1 extends Fragment {
    GuidePage1Binding binding;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = GuidePage1Binding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.header.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.bounce));
        binding.description.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spin_in));

    }
}
