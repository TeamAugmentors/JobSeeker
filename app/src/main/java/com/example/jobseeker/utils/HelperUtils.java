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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.jobseeker.R;
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

    private static final int PERMISSION_CODE = 1000;
    public static final String JOBSEEKER_DIR = Environment.DIRECTORY_DOWNLOADS + "/JobSeeker";

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

    public static void addButtonsToLayout(ArrayList<ParseFile> parseFiles, LinearLayout linearLayout, Activity context, String jobId) {
        Typeface typeface = ResourcesCompat.getFont(context, R.font.roboto_regular);

        int dimen10sdp = (int) context.getResources().getDimension(R.dimen._10sdp);

        for (int i = 0; i < parseFiles.size(); i++) {

            int buttonStyle = R.style.Widget_AppCompat_Button_Borderless;
            Button button = new Button(new ContextThemeWrapper(context, buttonStyle), null, buttonStyle);

            button.setText("File " + (i + 1));

            button.setBackground(context.getDrawable(R.drawable.button_background_filled));
            button.setCompoundDrawableTintList(ContextCompat.getColorStateList(context, R.color.white));
            button.setCompoundDrawablePadding(dimen10sdp);
            button.setTypeface(typeface);
            button.setMinimumWidth(0);
            button.setMinimumHeight(0);
            button.setMinWidth(0);
            button.setMinHeight(0);
            button.setPadding(25, 15, 25, 15);

            button.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_download, 0);
            button.setTextColor(context.getColor(R.color.white));
            button.setTextSize(16);
            linearLayout.addView(button);

            LinearLayout.LayoutParams btnParams = ((LinearLayout.LayoutParams) button.getLayoutParams());
            btnParams.setMarginEnd(dimen10sdp);
            button.setLayoutParams(btnParams);

            ParseFile parseFile = parseFiles.get(i);

            button.setOnClickListener(v -> parseFile.getDataInBackground((data, e) -> {
                if (e == null) {
                    try {
                        if (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                            //ask for permission
                            requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
                        } else {
                            saveFile(data, parseFile, context, jobId);
                        }
                    } catch (Exception exception) {
                        Log.d("error!", exception.getMessage());
                    }

                } else {
                    Toast.makeText(context, "error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }


    private static void saveFile(byte[] data, ParseFile parseFile, Activity context, String jobId) throws Exception {
        OutputStream outputStream;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!query(context, parseFile.getName(), jobId)) {
                ContentResolver resolver = context.getContentResolver();
                ContentValues contentValues = new ContentValues();
                //Automatically creates a directory if there is no directory. Might need a permission check in the future
                contentValues.put(MediaStore.Downloads.DISPLAY_NAME, parseFile.getName());
                contentValues.put(MediaStore.Downloads.MIME_TYPE, "image/jpg");
                contentValues.put(MediaStore.Downloads.RELATIVE_PATH, JOBSEEKER_DIR + "/JobId_" + jobId);

                Uri uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues);

                outputStream = resolver.openOutputStream(uri);
            } else {
                Toast.makeText(context, "Duplicate file found!", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {
            File imageDir = Environment.getExternalStoragePublicDirectory(JOBSEEKER_DIR + "/JobId_"+ jobId);
            imageDir.mkdir();
            File image = new File(imageDir, parseFile.getName());
            outputStream = new FileOutputStream(image);
        }

        outputStream.write(data);
        outputStream.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private static boolean query(Context context, String fileName, String jobId) {

        final Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        final String id = MediaStore.Downloads._ID;
        final String name = MediaStore.Downloads.DISPLAY_NAME;

        final String[] columns = {id, name};

        String folder = "/storage/emulated/0/Download/JobSeeker/";
        //folder = folder.concat(jobId+"");
        //Log.d("Item1",folder);
        String selection = MediaStore.Downloads.DATA + " LIKE ? AND " + MediaStore.Downloads.DATA + " NOT LIKE ? ";
        String[] selectionArgs = new String[]{
                "%" + folder + "%",
                "%" + folder + "/%/%"
        };

        Cursor cursor = resolver.query(uri, columns, selection, selectionArgs, null);
        Log.d("Item", cursor.getCount()+"");
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                String retrievedFileName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Downloads.DISPLAY_NAME));
                if (retrievedFileName.equals(fileName)) {
                    cursor.close();
                    return true;
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return false;
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
}
