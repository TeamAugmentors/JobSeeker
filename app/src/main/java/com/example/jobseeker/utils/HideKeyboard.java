package com.example.jobseeker.utils;

import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class HideKeyboard {
    public static void hide(InputMethodManager imm, View view){
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
