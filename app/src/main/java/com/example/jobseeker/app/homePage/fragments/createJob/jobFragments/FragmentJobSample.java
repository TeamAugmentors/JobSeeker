package com.example.jobseeker.app.homePage.fragments.createJob.jobFragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.jobseeker.databinding.FragmentCreateJobSampleBinding;
import com.example.jobseeker.utils.ProgressBarStatus;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ProgressCallback;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
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
        String fileTypes[] = {"image/*", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        binding.addFile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                myFileIntent.putExtra(Intent.EXTRA_MIME_TYPES, fileTypes);
                startActivityForResult(myFileIntent, 1);

            }
        });

        binding.addFile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                myFileIntent.putExtra(Intent.EXTRA_MIME_TYPES, fileTypes);
                startActivityForResult(myFileIntent, 2);
            }
        });
        binding.addFile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
                myFileIntent.setType("*/*");
                myFileIntent.putExtra(Intent.EXTRA_MIME_TYPES, fileTypes);
                startActivityForResult(myFileIntent, 3);
            }
        });

    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String uriString = uri.toString();

                    InputStream iStream;

                    byte[] inputData = null;
                    try {
                        iStream = getActivity().getContentResolver().openInputStream(uri);
                        inputData = getBytes(iStream);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    File myFile = new File(uriString);

                    StringBuilder extension = new StringBuilder();

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

                    for (int i = displayName.length() - 1; i >= 0; i--) {
                        if (displayName.charAt(i) == '.') {
                            break;
                        }
                        extension.insert(0, displayName.charAt(i));
                    }

                    Log.d("gawgaw", "SampleFile1." + extension);

                    parseFiles[0] = new ParseFile("SampleFile1." + extension, inputData);

                    parseFiles[0].saveInBackground(e -> {
                        if (e!=null){
                            Toast.makeText(getActivity(), "Error uploading! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            ProgressBarStatus.errorFlash(binding.progressOne);

                            new Handler().postDelayed(() -> {
                                binding.progressOne.setVisibility(GONE);
                                binding.progressOne.setProgress(0, true);
                            }, 1000);
                        }
                    }, percentDone -> {
                        if (percentDone != 100) {
                            if (binding.progressOne.getVisibility() == GONE) {
                                binding.progressOne.setVisibility(View.VISIBLE);
                            }
                            binding.progressOne.setProgress(percentDone, true);
                        } else {
                            ProgressBarStatus.successFlash(binding.progressOne);

                            new Handler().postDelayed(() -> {
                                binding.progressOne.setVisibility(GONE);
                                binding.progressOne.setProgress(0, true);

                                binding.cross1.setVisibility(View.VISIBLE);
                            }, 1000);
                        }

                    });

                    binding.file1.setText(displayName);
                }
                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String uriString = uri.toString();

                    InputStream iStream = null;

                    byte[] inputData = null;
                    try {
                        iStream = getActivity().getContentResolver().openInputStream(uri);
                        inputData = getBytes(iStream);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    File myFile = new File(uriString);

                    StringBuilder extension = new StringBuilder();

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

                    for (int i = displayName.length() - 1; i >= 0; i--) {
                        if (displayName.charAt(i) == '.') {
                            break;
                        }
                        extension.insert(0, displayName.charAt(i));
                    }

                    Log.d("gawgaw", "SampleFile1." + extension);

                    parseFiles[1] = new ParseFile("SampleFile2." + extension, inputData);

                    parseFiles[1].saveInBackground(e -> {
                        if (e!=null){
                            Toast.makeText(getActivity(), "Error uploading! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            ProgressBarStatus.errorFlash(binding.progressTwo);

                            new Handler().postDelayed(() -> {
                                binding.progressTwo.setVisibility(GONE);
                                binding.progressTwo.setProgress(0, true);
                            }, 1000);
                        }
                    }, percentDone -> {
                        if (percentDone != 100) {
                            if (binding.progressTwo.getVisibility() == GONE) {
                                binding.progressTwo.setVisibility(View.VISIBLE);
                            }
                            binding.progressTwo.setProgress(percentDone, true);
                        } else {
                            ProgressBarStatus.successFlash(binding.progressTwo);

                            new Handler().postDelayed(() -> {
                                binding.progressTwo.setVisibility(GONE);
                                binding.progressTwo.setProgress(0, true);

                                binding.cross2.setVisibility(View.VISIBLE);
                            }, 1000);
                        }

                    });

                    binding.file2.setText(displayName);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String uriString = uri.toString();

                    InputStream iStream = null;

                    byte[] inputData = null;
                    try {
                        iStream = getActivity().getContentResolver().openInputStream(uri);
                        inputData = getBytes(iStream);
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    File myFile = new File(uriString);

                    StringBuilder extension = new StringBuilder();

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

                    for (int i = displayName.length() - 1; i >= 0; i--) {
                        if (displayName.charAt(i) == '.') {
                            break;
                        }
                        extension.insert(0, displayName.charAt(i));
                    }

                    Log.d("gawgaw", "SampleFile1." + extension);

                    parseFiles[2] = new ParseFile("SampleFile1." + extension, inputData);

                    parseFiles[2].saveInBackground(e -> {
                        if (e!=null){
                            Toast.makeText(getActivity(), "Error uploading! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                            ProgressBarStatus.errorFlash(binding.progressThree);

                            new Handler().postDelayed(() -> {
                                binding.progressThree.setVisibility(GONE);
                                binding.progressThree.setProgress(0, true);
                            }, 1000);
                        }
                    }, percentDone -> {
                        if (percentDone != 100) {
                            if (binding.progressThree.getVisibility() == GONE) {
                                binding.progressThree.setVisibility(View.VISIBLE);
                            }
                            binding.progressThree.setProgress(percentDone, true);
                        } else {
                            ProgressBarStatus.successFlash(binding.progressThree);

                            new Handler().postDelayed(() -> {
                                binding.progressThree.setVisibility(GONE);
                                binding.progressThree.setProgress(0, true);

                                binding.cross3.setVisibility(View.VISIBLE);
                            }, 1000);
                        }

                    });

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
}
