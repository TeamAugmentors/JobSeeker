package com.example.jobseeker.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.jobseeker.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;

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

    public static void addButtonsToLayout(ArrayList<ParseFile> parseFiles, LinearLayout linearLayout, Context context) {
        int numberOfButtons = parseFiles.size();
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_regular);

        int dimensions[] = new int[4];
        dimensions[0] = (int) context.getResources().getDimension(R.dimen._5sdp);
        dimensions[1] = (int) context.getResources().getDimension(R.dimen._10sdp);
        dimensions[2] = (int) context.getResources().getDimension(R.dimen._13sdp);

        for (int i = 0; i < numberOfButtons; i++) {
            Button button = new Button(context);
            button.setText("File"+(i+1));

            button.setBackground(context.getDrawable(R.drawable.button_background_filled));
            button.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.white));
            button.setCompoundDrawablePadding(dimensions[0]);
            button.setTypeface(typeface);
            button.setMinimumWidth(0);
            button.setMinimumHeight(0);
            button.setPadding(dimensions[1],dimensions[0],dimensions[1],dimensions[0]);

            button.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_download,0);
            button.setTextColor(context.getColor(R.color.white));
            button.setTextSize(15);
            linearLayout.addView(button);

            LinearLayout.LayoutParams btnParams = ((LinearLayout.LayoutParams) button.getLayoutParams());
            btnParams.setMarginEnd(dimensions[1]);
            button.setLayoutParams(btnParams);
        }
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
}
