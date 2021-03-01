package com.example.jobseeker.app.homePage.fragments.createJob;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.jobseeker.databinding.FragmentCreateJobSampleBinding;
import com.parse.ParseFile;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

public class FragmentJobSample extends Fragment {
    FragmentCreateJobSampleBinding binding;
    Intent myFileIntent;
    ParseFile[] parseFiles = new ParseFile[3];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentCreateJobSampleBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parseFiles[0] = null;
        parseFiles[1] = null;
        parseFiles[2] = null;
        //Files
        String fileTypes[] = {"image/*","application/pdf","application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        binding.addFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                myFileIntent.putExtra(Intent.EXTRA_MIME_TYPES,fileTypes);
                startActivityForResult(myFileIntent, 1);

            }
        });

        binding.addFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                myFileIntent.putExtra(Intent.EXTRA_MIME_TYPES,fileTypes);
                startActivityForResult(myFileIntent, 2);
            }
        });
        binding.addFile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                myFileIntent.putExtra(Intent.EXTRA_MIME_TYPES,fileTypes);
                startActivityForResult(myFileIntent, 3);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    binding.cross1.setVisibility(View.VISIBLE);

                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    parseFiles[0] = (new ParseFile(myFile));

                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }

                    binding.file1.setText(displayName);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    binding.cross2.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    parseFiles[1] = (new ParseFile(myFile));

                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }

                    binding.file2.setText(displayName);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    binding.cross3.setVisibility(View.VISIBLE);
                    Uri uri = data.getData();
                    String uriString = uri.toString();
                    File myFile = new File(uriString);

                    parseFiles[2] = (new ParseFile(myFile));

                    String displayName = null;

                    if (uriString.startsWith("content://")) {
                        Cursor cursor = null;
                        try {
                            cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
                            if (cursor != null && cursor.moveToFirst()) {
                                displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                            }
                        } finally {
                            cursor.close();
                        }
                    } else if (uriString.startsWith("file://")) {
                        displayName = myFile.getName();
                    }

                    binding.file3.setText(displayName);
                }
                break;
        }
    }
    public FragmentCreateJobSampleBinding getBinding() {
        return binding;
    }

    public ParseFile[] getParseFiles() {
        return parseFiles;
    }

    public ArrayList<ParseFile> getParseFileList(){
        return new ArrayList<ParseFile>(Arrays.asList(parseFiles));
    }
}