package com.example.jobseeker.app.startScreen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.R;
import com.mukesh.OtpView;

public class EnterOTPSlide extends Fragment {

    OtpView otpView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_enter_otp_slide,container,false);
    }

    TextView otpHeader;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        otpHeader = getView().findViewById(R.id.otp_header);
        otpView = getView().findViewById(R.id.otp_view);
        otpView.setAnimationEnable(true);

    }

    public OtpView getOtpView() {
        return otpView;
    }

    public TextView getOtpHeader() {
        return otpHeader;
    }

    public void setOtpHeaderText(String text) {
        otpHeader.setText(text);
    }

    public void onResume() {
        super.onResume();
        otpView.requestFocus();
    }
}
