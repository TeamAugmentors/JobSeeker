package com.example.jobseeker.app.homePage.fragments.createJob.jobFragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.FragmentCreateJobBudgetBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Date;

public class FragmentJobBudget extends Fragment {
    FragmentCreateJobBudgetBinding binding;
    MaterialDatePicker materialDatePicker;
    String budget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateJobBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MaterialDatePicker.Builder materialDatePickerBuilder = MaterialDatePicker.Builder.datePicker();

        materialDatePickerBuilder.setTitleText("Select A Deadline");
        CalendarConstraints.DateValidator dateValidator = DateValidatorPointForward.now();
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        constraintsBuilder.setValidator(dateValidator);
        materialDatePickerBuilder.setCalendarConstraints(constraintsBuilder.build());

        materialDatePicker = materialDatePickerBuilder.build();

        binding.datePickButton.setOnClickListener(v -> materialDatePicker.show(getActivity().getSupportFragmentManager(), "MATERIAL_DATE_PICKER"));

        materialDatePicker.addOnPositiveButtonClickListener(selection -> setNumberOfDays());
        budget = "";

        //budget
        binding.budgetLayout.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                budget = binding.budgetLayout.getEditText().getText().toString();
                char[] budgetArray = budget.toCharArray();

                StringBuilder result = new StringBuilder();
                boolean isLeading = false;

                for (char letter : budgetArray) {
                    if (letter != '0')
                        isLeading = true;
                    if (isLeading) {
                        result.append(letter);
                    }
                }

                budget = result.toString();

                if (budget.length() >= 5) {
                    if (budget.compareTo("15000") > 0 || budget.length() > 5) {
                        binding.budgetWarning.setText("Please select a budget lower than 15,000 BDT");
                        binding.budgetLayout.setBoxStrokeColor(getContext().getColor(R.color.job_seeker_red));
                        binding.budgetLayout.getEditText().setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(R.color.job_seeker_red)));
                        binding.budgetLayout.setHintTextColor(getContext().getColorStateList(R.color.job_seeker_red));
                        binding.budgetWarning.setTextColor(getContext().getColor(R.color.job_seeker_red));
                    } else {
                        binding.budgetWarning.setText("This is the total budget for your project");
                        binding.budgetLayout.setBoxStrokeColor(getContext().getColor(R.color.job_seeker_logo_green));
                        binding.budgetLayout.getEditText().setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(R.color.job_seeker_logo_green)));
                        binding.budgetLayout.setHintTextColor(getContext().getColorStateList(R.color.job_seeker_logo_green));
                        binding.budgetWarning.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
                    }
                } else {
                    binding.budgetWarning.setText("This is the total budget for your project");
                    binding.budgetLayout.setBoxStrokeColor(getContext().getColor(R.color.job_seeker_logo_green));
                    binding.budgetLayout.getEditText().setCompoundDrawableTintList(ColorStateList.valueOf(getContext().getColor(R.color.job_seeker_logo_green)));
                    binding.budgetLayout.setHintTextColor(getContext().getColorStateList(R.color.job_seeker_logo_green));
                    binding.budgetWarning.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void setNumberOfDays() {
        double diff = printDifference(new Date().getTime(), materialDatePicker.getSelection().toString());
        binding.dateTextView.setTextSize(30);
        if (diff <= 0d) {
            //12 hours
            binding.dateTextView.setText("12 Hours");
            binding.dateTextView.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
            binding.dateWarning.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
        } else if ((int) Math.ceil(diff) > 120) {
            binding.dateTextView.setText((int) Math.ceil(diff) + " Days");
            binding.dateTextView.setTextColor(getContext().getColor(R.color.job_seeker_red));
            binding.dateWarning.setTextColor(getContext().getColor(R.color.job_seeker_red));
            binding.dateWarning.setText("Please pick a shorter duration");
        } else {
            binding.dateTextView.setText((int) Math.ceil(diff) + " Days");
            binding.dateTextView.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
            binding.dateWarning.setTextColor(getContext().getColor(R.color.job_seeker_logo_green));
        }
    }

    public static double printDifference(Long startDate, String endDate) {

        Long endDateLong = Long.parseLong(endDate);
        //milliseconds
        double different = endDateLong - startDate;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        double daysInMilli = hoursInMilli * 24;

        return different / daysInMilli;
    }

    @Override
    public void setTargetFragment(@Nullable Fragment fragment, int requestCode) {
        super.setTargetFragment(fragment, requestCode);
    }

    public FragmentCreateJobBudgetBinding getBinding() {
        return binding;
    }

    public String getBudget() {
        return budget;
    }
}
