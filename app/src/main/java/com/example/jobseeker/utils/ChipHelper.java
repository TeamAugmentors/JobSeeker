package com.example.jobseeker.utils;

import android.content.Context;

import com.example.jobseeker.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;

public class ChipHelper {

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

    public static void addChipIntoChipGroup(ChipGroup chipGroup, Context context, boolean isCloseIcon, String... texts) {
        for (String text : texts) {
            Chip chip = new Chip(context);
            ChipDrawable chipDrawable = ChipDrawable.createFromResource(context, R.xml.chip);

            chip.setChipDrawable(chipDrawable);
            chip.setCheckable(false);
            chip.setClickable(true);

            if(!isCloseIcon) {
                chip.setCloseIcon(null);
            }else{
                chip.setOnCloseIconClickListener(v -> {
                    chipGroup.removeView(chip);
                });
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

    public static boolean findMatch(ChipGroup chipGroup, String s){
        for (int i = 0; i < chipGroup.getChildCount(); i++) {
            if (((Chip) chipGroup.getChildAt(i)).getText().toString().toLowerCase().equals(s)) {
                return true;
            }
        }
        return false;
    }
}
