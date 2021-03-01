package com.example.jobseeker.app.homePage.fragments.createprofile.titleFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.FragementCreateProfileYourInfoBinding;
import com.example.jobseeker.databinding.FragmentCreateProfileSkillsetBinding;

public class FragmentSkillSet extends Fragment {

    FragementCreateProfileYourInfoBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragementCreateProfileYourInfoBinding.inflate(inflater, container, false)).getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((TextView) getView().findViewById(R.id.titleInfo)).setText("Skill Set");
    }

}
