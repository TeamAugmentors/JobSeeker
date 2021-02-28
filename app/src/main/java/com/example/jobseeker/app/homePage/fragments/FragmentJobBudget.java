package com.example.jobseeker.app.homePage.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.CreateJob;
import com.example.jobseeker.databinding.FragmentCreateJobBudgetBinding;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Date;

public class FragmentJobBudget extends Fragment {
    FragmentCreateJobBudgetBinding binding;
    MaterialDatePicker materialDatePicker;

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
    }

    public void setNumberOfDays(){
        double diff = printDifference(new Date().getTime(), materialDatePicker.getSelection().toString());
        if (diff <= 0d){
            //12 hours
            binding.dateTextView.setText("12 Hours");
        }else{
            binding.dateTextView.setText((int) Math.ceil(diff) + " Days");
        }
    }

    public static double printDifference(Long startDate, String endDate){

        Long endDateLong = Long.parseLong(endDate);
        //milliseconds
        double different = endDateLong - startDate;

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        double daysInMilli = hoursInMilli * 24;

        return different / daysInMilli;
    }
}
