package com.example.jobseeker.app.homePage.fragments.createprofile.profileFragments;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.CreateProfile;
import com.example.jobseeker.databinding.FragmentCreateJobBudgetBinding;
import com.example.jobseeker.databinding.FragmentCreateProfile2Binding;
import com.example.jobseeker.utils.ChipHelper;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.textfield.TextInputLayout;

import static androidx.core.content.ContextCompat.getColor;

public class CreateProfile2 extends Fragment {

    FragmentCreateProfile2Binding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentCreateProfile2Binding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.SkillSetLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String input = binding.SkillSetLayout.getEditText().getText().toString();
                if (input.length() > 0) {
                    binding.skillWarning.setText("Please enter any skills you posses! This will show up on your profile");
                    binding.skillWarning.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    binding.addSkill.setVisibility(View.GONE);
                    binding.skillWarning.setText("Please enter any skills you posses! This will show up on your profile");
                    binding.skillWarning.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
                } else if (s.toString().contains(" ")) {
                    binding.addSkill.setVisibility(View.GONE);
                    binding.skillWarning.setText("No space is allowed");
                    binding.skillWarning.setTextColor(getContext().getColor(R.color.job_seeker_red));
                } else {
                    binding.addSkill.setVisibility(View.VISIBLE);
                    binding.skillWarning.setText("Please enter any skills you posses! This will show up on your profile");
                    binding.skillWarning.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
                }
            }
        });

        binding.SkillSetLayout.getEditText().setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER) && !binding.SkillSetLayout.getEditText().getText().toString().contains(" ") &&
                        binding.SkillSetLayout.getEditText().getText().toString().length()!=0) {
                        ((CreateProfile) getActivity()).addSkill(v);

                    return true;
                }
                return false;
            }
        });
    }

    public FragmentCreateProfile2Binding getBinding() {
        return binding;
    }
}
