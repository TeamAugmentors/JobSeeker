package com.example.jobseeker.app.homePage.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.FragmentCreateJobTitleBinding;

public class FragmentJobTitle extends Fragment {
    FragmentCreateJobTitleBinding binding;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentCreateJobTitleBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.jobTitleLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.jobTitleLayout.getEditText().getText().toString().length()!=0){
                    binding.title.setText("A clear, concrete description is welcomed");
                    binding.title.setTextColor(ContextCompat.getColor(getContext(), R.color.job_seeker_logo_green));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int charCount = s.toString().length();
                binding.titleTooltip.setText("" + charCount + "/30");

                if (charCount > 30){
                    binding.jobTitleLayout.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.job_seeker_red));
                    binding.jobTitleLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.job_seeker_red));
                    binding.titleTooltip.setTextColor(ContextCompat.getColor(getContext(), R.color.job_seeker_red));

                }else {
                    binding.jobTitleLayout.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.job_seeker_logo_green));
                    binding.jobTitleLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.job_seeker_logo_green));
                    binding.titleTooltip.setTextColor(ContextCompat.getColor(getContext(), R.color.job_seeker_logo_green));
                }
            }
        });

        binding.jobDescriptionLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(binding.jobDescriptionLayout.getEditText().getText().toString().length()!=0){
                    binding.description.setText("Always remember, the best job titles are\\nunder 30 letters!");
                    binding.description.setTextColor(ContextCompat.getColor(getContext(), R.color.job_seeker_logo_green));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                int charCount = s.toString().length();
                binding.descriptionToolTip.setText("" + charCount + "/600");

                if (charCount > 600){
                    binding.jobDescriptionLayout.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.job_seeker_red));
                    binding.jobDescriptionLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.job_seeker_red));
                    binding.descriptionToolTip.setTextColor(ContextCompat.getColor(getContext(), R.color.job_seeker_red));

                }else {
                    binding.jobDescriptionLayout.setBoxStrokeColor(ContextCompat.getColor(getContext(), R.color.job_seeker_logo_green));
                    binding.jobDescriptionLayout.setHintTextColor(ContextCompat.getColorStateList(getContext(), R.color.job_seeker_logo_green));
                    binding.descriptionToolTip.setTextColor(ContextCompat.getColor(getContext(), R.color.job_seeker_logo_green));
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public FragmentCreateJobTitleBinding getBinding() {
        return binding;
    }

}
