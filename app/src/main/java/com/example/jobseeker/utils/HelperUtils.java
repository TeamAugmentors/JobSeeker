package com.example.jobseeker.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
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
import com.example.jobseeker.databinding.DialogApplyBinding;
import com.example.jobseeker.databinding.DialogLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static androidx.core.app.ActivityCompat.requestPermissions;
import static androidx.core.content.ContextCompat.checkSelfPermission;

public class HelperUtils {


    private static DialogLayoutBinding bindingDialog;
    private static DialogApplyBinding dialogApplyBinding;

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



    public static String getTime(String seenTime, String currentTime, boolean seenFlag) {
        String currentDate, seenDate, seenClock, outputTime = "";

        currentDate = currentTime.substring(7, 10) + " " + currentTime.substring(4, 7) + " " + currentTime.substring(30, 34);
        seenDate = seenTime.substring(7, 10) + " " + seenTime.substring(4, 7) + " " + seenTime.substring(30, 34);

        seenClock = seenTime.substring(11, 16);
        StringBuilder temp = new StringBuilder(seenClock);
        outputTime = convertTo12(temp);

        if(seenFlag) {
            if (currentDate.equals(seenDate)) {
                return "Seen " + outputTime;
            } else {
                return "Seen " + seenDate + " at " + outputTime;
            }
        }
        return outputTime;
    }

    private static String convertTo12(StringBuilder outputTime) {
        if ((outputTime.charAt(0)=='1' && outputTime.charAt(1) > '2') || outputTime.charAt(0) > '1') {
            int temp2 = ((outputTime.charAt(0) - '0') * 10 + (outputTime.charAt(1) - '0')) - 12;
            if (temp2 < 10) {
                outputTime.setCharAt(0, '0');
                outputTime.setCharAt(1, (char) (temp2 + '0'));
            } else {
                outputTime.setCharAt(1, (char) (temp2 % 10 + '0'));
                temp2 /= 10;
                outputTime.setCharAt(0, (char) (temp2 % 10 + '0'));
            }

            return outputTime.toString() + " pm";
        }
        return outputTime.toString() + " am";
    }
    public static Object[] createDialog(int position, List<ParseObject> parseObjects, Context context){
        Dialog dialog = new Dialog(context, R.style.Dialog);
        bindingDialog = DialogLayoutBinding.inflate(((Activity)context).getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        bindingDialog.close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        ParseObject currentObject = parseObjects.get(position);

        bindingDialog.title.setText(currentObject.getString("title"));
        bindingDialog.description.setText(currentObject.getString("description"));
        bindingDialog.budget.setText(currentObject.getInt("budget") + "");
        bindingDialog.duration.setText(currentObject.getString("duration"));
        bindingDialog.revisions.setText(currentObject.getInt("revisions") + "");
        bindingDialog.seeFreelancerButton.setVisibility(View.GONE);
        bindingDialog.deleteButton.setVisibility(View.GONE);

        if (currentObject.getBoolean("negotiable"))
            bindingDialog.negotiable.setText("Yes");
        else
            bindingDialog.negotiable.setText("No");

        //Dynamic scroll view height

        String text = bindingDialog.description.getText().toString();

        int charCount = text.length();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        LinearLayout.LayoutParams params;

        if (charCount <= 200) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
        }

        bindingDialog.scrollView.setLayoutParams(params);
        return new Object[]{dialog, bindingDialog};
    }
}
