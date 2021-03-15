package com.example.jobseeker.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.jobseeker.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.parse.ParseFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class HelperUtils {


    public static String getAllChipText(ChipGroup chipGroup) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            result.append(((Chip) chipGroup.getChildAt(i)).getText());
            if (i != chipGroup.getChildCount() - 1) {
                result.append(",");
            }
        }

        return result.toString();
    }


    public static void addChipIntoChipGroup(ChipGroup chipGroup, Context context, boolean isCloseIcon, boolean isDarkModeOn, String... texts) {
        for (String text : texts) {
            Chip chip = new Chip(context);
            ChipDrawable chipDrawable = ChipDrawable.createFromResource(context, R.xml.chip);

            chip.setChipDrawable(chipDrawable);
            chip.setCheckable(false);
            chip.setClickable(true);

            if (!isCloseIcon) {
                chip.setCloseIcon(null);
            } else {
                chip.setOnCloseIconClickListener(v -> {
                    chipGroup.removeView(chip);
                });
            }
            if (isDarkModeOn) {
                chip.setTextColor(Color.BLACK);
                chip.setCloseIconTint(ColorStateList.valueOf(Color.BLACK));
            } else {
                chip.setTextColor(Color.WHITE);
                chip.setCloseIconTint(ColorStateList.valueOf(Color.WHITE));
            }
            chip.setText(text.toUpperCase());
            chipGroup.addView(chip);
        }
    }

    public static String getTextFromSelectedChip(ChipGroup chipGroup) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            if (((Chip) chipGroup.getChildAt(i)).isChecked())
                return ((Chip) chipGroup.getChildAt(i)).getText().toString();
        }

        return null;
    }

    public static boolean findMatch(ChipGroup chipGroup, String s) {
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            if (((Chip) chipGroup.getChildAt(i)).getText().toString().toLowerCase().equals(s)) {
                return true;
            }
        }
        return false;
    }

    public static String getFileExtention(String fileName) {
        StringBuilder extension = new StringBuilder();
        for (int i = fileName.length() - 1; i >= 0; i--) {
            if (fileName.charAt(i) == '.') {
                break;
            }
            extension.insert(0, fileName.charAt(i));
        }

        return extension.toString();
    }

    public static BottomSheetBehavior defaultSheet(ViewGroup bottomSheetRoot, Button dismissButton) {

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetRoot);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setPeekHeight(bottomSheetRoot.getHeight());
        bottomSheetBehavior.setHideable(true);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        dismissButton.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        return bottomSheetBehavior;
    }
}
