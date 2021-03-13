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
import com.example.jobseeker.utils.HelperUtils;
import com.example.jobseeker.utils.ProgressBarStatus;
import com.parse.ParseFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
        setFileIntent();

        binding.addFile1.setOnClickListener(v -> startActivityForResult(myFileIntent, 1));
        binding.addFile2.setOnClickListener(v -> startActivityForResult(myFileIntent, 2));
        binding.addFile3.setOnClickListener(v -> startActivityForResult(myFileIntent, 3));

    }

    private void setFileIntent() {
        String fileTypes[] = {"image/jpg","image/jpeg","image/png", "application/pdf", "application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

        myFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        myFileIntent.setType("*/*");
        myFileIntent.putExtra(Intent.EXTRA_MIME_TYPES, fileTypes);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            fileHandler(data, requestCode);
        }
    }

    public void fileHandler(@Nullable Intent data, int requestCode) {
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

        String extension = HelperUtils.getFileExtention(displayName);

        parseFiles[requestCode - 1] = new ParseFile("SampleFile1." + extension, inputData);

        parseFiles[requestCode - 1].saveInBackground(e -> {
            if (e != null) {
                Toast.makeText(getActivity(), "Error uploading! " + e.getMessage(), Toast.LENGTH_SHORT).show();

                if (requestCode == 1)
                    ProgressBarStatus.errorFlash(binding.progressOne, getContext());
                else if (requestCode == 2)
                    ProgressBarStatus.errorFlash(binding.progressTwo, getContext());
                else if (requestCode == 3)
                    ProgressBarStatus.errorFlash(binding.progressThree, getContext());

                new Handler().postDelayed(() -> {
                    if (requestCode == 1) {
                        binding.progressOne.setVisibility(GONE);
                        binding.progressOne.setProgress(0, true);
                    } else if (requestCode == 2) {
                        binding.progressTwo.setVisibility(GONE);
                        binding.progressTwo.setProgress(0, true);
                    } else if (requestCode == 3) {
                        binding.progressThree.setVisibility(GONE);
                        binding.progressThree.setProgress(0, true);
                    }
                }, 1000);
            }
        }, percentDone -> {
            if (percentDone != 100) {
                if (requestCode == 1) {
                    if (binding.progressOne.getVisibility() == GONE) {
                        binding.progressOne.setVisibility(View.VISIBLE);
                    }
                    binding.progressOne.setProgress(percentDone, true);
                } else if (requestCode == 2) {
                    if (binding.progressTwo.getVisibility() == GONE) {
                        binding.progressTwo.setVisibility(View.VISIBLE);
                    }
                    binding.progressTwo.setProgress(percentDone, true);
                } else if (requestCode == 3) {
                    if (binding.progressThree.getVisibility() == GONE) {
                        binding.progressThree.setVisibility(View.VISIBLE);
                    }
                    binding.progressThree.setProgress(percentDone, true);
                }
            } else {
                if (requestCode == 1) {
                    ProgressBarStatus.successFlash(binding.progressOne, getContext());
                } else if (requestCode == 2) {
                    ProgressBarStatus.successFlash(binding.progressTwo, getContext());
                } else if (requestCode == 3) {
                    ProgressBarStatus.successFlash(binding.progressThree, getContext());
                }

                new Handler().postDelayed(() -> {
                    if (requestCode == 1) {
                        binding.progressOne.setVisibility(GONE);
                        binding.progressOne.setProgress(0, true);

                        binding.cross1.setVisibility(View.VISIBLE);
                    } else if (requestCode == 2) {
                        binding.progressTwo.setVisibility(GONE);
                        binding.progressTwo.setProgress(0, true);

                        binding.cross2.setVisibility(View.VISIBLE);
                    } else if (requestCode == 3) {
                        binding.progressThree.setVisibility(GONE);
                        binding.progressThree.setProgress(0, true);

                        binding.cross3.setVisibility(View.VISIBLE);
                    }
                }, 1000);
            }
        });
        if (requestCode == 1)
            binding.file1.setText(displayName);
        else if (requestCode == 2)
            binding.file2.setText(displayName);
        else if (requestCode == 3)
            binding.file3.setText(displayName);
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

    public FragmentCreateJobSampleBinding getBinding() {
        return binding;
    }

    public ParseFile[] getParseFiles() {
        return parseFiles;
    }
}
