package com.example.jobseeker.utils;

import android.content.Context;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.jobseeker.R;
import com.google.android.material.textfield.TextInputLayout;

import org.w3c.dom.Text;

public class EmptyFieldHandler {
    TextView textView;
    TextInputLayout[] textInputLayouts;
    public EmptyFieldHandler(TextView textView, TextInputLayout... textInputLayouts) {
           this.textView = textView;
           this.textInputLayouts  = textInputLayouts;
    }

    public static EmptyFieldHandler create(TextView textview, TextInputLayout... textInputLayouts){
        return new EmptyFieldHandler(textview, textInputLayouts);
    }

    public void setGreen(){

    }

    public void setRed(Context context){
        for(int i=0;i<textInputLayouts.length;i++){
            if(textInputLayouts[i].getEditText().getText().toString().length()==0){
                //textView.setTextColor();
            }
        }
    }
}
