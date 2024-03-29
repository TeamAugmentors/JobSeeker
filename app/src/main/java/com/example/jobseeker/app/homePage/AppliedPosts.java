package com.example.jobseeker.app.homePage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCreatedPostBinding;
import com.example.jobseeker.databinding.DialogLayoutBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.ncorti.slidetoact.SlideToActView;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppliedPosts extends AppCompatActivity implements CreatedPostsAdapter.OnCreatedPostsListener {

    ActivityCreatedPostBinding binding;
    ArrayList<ParseObject> parseObjects;
    CreatedPostsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatedPostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        fetchData();
    }

    private void fetchData() {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.include("appliedPosts");

        query.getInBackground(ParseUser.getCurrentUser().getObjectId(), (object, e) -> {
            if (e == null) {
                List<ParseObject> objects = (List<ParseObject>) object.get("appliedPosts");
                parseObjects = new ArrayList<>();

                if (objects != null) {
                    for (int i = 0; i < objects.size(); i++) {
                        if (!objects.get(i).getBoolean("locked")){
                            parseObjects.add(objects.get(i));
                        }
                    }

                    adapter = new CreatedPostsAdapter(parseObjects, this);

                    binding.recyclerView.setAdapter(adapter);
                    binding.chip.setText("Applied to " + parseObjects.size() + " Jobs");
                } else {
                    binding.chip.setText("Applied to 0 Jobs");
                }
            } else {
                Toast.makeText(AppliedPosts.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                binding.chip.setText("Unable to fetch");
            }

        });

    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, binding.collapsingToolbar, this, "Applied Jobs");

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setItemViewCacheSize(1);


        binding.appBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            verticalOffset = verticalOffset * -1;
            Log.d("AWOFJAWOF", verticalOffset + "");
            float x = (float) (binding.appBar.getTotalScrollRange() / 2.0);
            if (verticalOffset <= x) {
                binding.chip.setAlpha((float) (1.0 - (verticalOffset / x)));
            }
        });

    }

    DialogLayoutBinding bindingDialog;

    @Override
    public void onJobBoardClick(int position, List<ParseObject> parseObjects) {

        Dialog dialog = new Dialog(this, R.style.Dialog);
        bindingDialog = DialogLayoutBinding.inflate(getLayoutInflater());

        dialog.setContentView(bindingDialog.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        View dialogView = dialog.getWindow().getDecorView();


        dialogView.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
        });

        ((TextView) dialogView.findViewById(R.id.title)).setText(parseObjects.get(position).getString("title"));
        ((TextView) dialogView.findViewById(R.id.description)).setText(parseObjects.get(position).getString("description"));
        ((TextView) dialogView.findViewById(R.id.budget)).setText(parseObjects.get(position).getInt("budget") + "");
        ((TextView) dialogView.findViewById(R.id.duration)).setText(parseObjects.get(position).getString("duration"));
        ((TextView) dialogView.findViewById(R.id.revisions)).setText(parseObjects.get(position).getInt("revisions") + "");
        (dialogView.findViewById(R.id.seeFreelancerButton)).setVisibility(View.GONE);
        (dialogView.findViewById(R.id.deleteButton)).setVisibility(View.GONE);
        (dialogView.findViewById(R.id.horizontalLine)).setVisibility(View.GONE);

        if (parseObjects.get(position).getBoolean("negotiable"))
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("Yes");
        else
            ((TextView) dialogView.findViewById(R.id.negotiable)).setText("No");
        ScrollView scrollView = dialogView.findViewById(R.id.scrollView);
        String text = ((TextView) dialogView.findViewById(R.id.description)).getText().toString();
        int charCount = text.length();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        LinearLayout.LayoutParams params;
        if (charCount <= 200) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
        }
        scrollView.setLayoutParams(params);

        ((SlideToActView) dialogView.findViewById(R.id.applySlider)).setVisibility(View.GONE);

        dialog.findViewById(R.id.message).setVisibility(View.GONE);

        ParseObject currentObject = parseObjects.get(position);

        dialog.findViewById(R.id.message).setOnClickListener(v -> {
            startActivity(new Intent(this, Inbox.class));
        });

        /////========================> files
        ArrayList<ParseFile> parseFiles = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            if (currentObject.getParseFile("file" + (i + 1)) != null)
                parseFiles.add(currentObject.getParseFile("file" + (i + 1)));
        }
        if (parseFiles.size() != 0)
            addButtonsToLayout(parseFiles, bindingDialog.fileLinearLayout, this, currentObject.getObjectId());
        else {
            bindingDialog.sampleFileTextView.setText("No sample files provided");
        }

    }


    private final int PERMISSION_CODE = 1000;
    public final String JOBSEEKER_DIR = Environment.DIRECTORY_DOWNLOADS + "/JobSeeker";

    public void addButtonsToLayout(ArrayList<ParseFile> parseFiles, LinearLayout linearLayout, Activity context, String jobId) {
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

            button.setOnClickListener(v -> {
                parseFile.getDataInBackground((data, e) -> {

                    Log.d("afijawif", Arrays.toString(data));

                    if (e == null) {

                        try {
                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                                //ask for permission
                                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
                            } else {
                                saveFile(data, parseFile, context, jobId);
                            }
                        } catch (Exception exception) {
                            Log.d("error!", exception.getMessage());
                        }

                    } else {
                        Toast.makeText(context, "error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, percentDone -> {

                    Log.d("afsfasf", "new: " + percentDone);

                    if (percentDone == 100) {
                        new Handler().postDelayed(() -> {
                        }, 1400);
                    }
                });
            });
        }
    }


    private void saveFile(byte[] data, ParseFile parseFile, Activity context, String jobId) throws Exception {
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
                Toast.makeText(context, "successfully downloaded!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Duplicate file found!", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {

            File imageDir = Environment.getExternalStoragePublicDirectory(JOBSEEKER_DIR + "/JobId_" + jobId);
            imageDir.mkdir();
            File image = new File(imageDir, parseFile.getName());
            outputStream = new FileOutputStream(image);
        }

        outputStream.write(data);
        outputStream.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private boolean query(Context context, String fileName, String jobId) {

        final Uri uri = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
        ContentResolver resolver = context.getContentResolver();

        final String id = MediaStore.Downloads._ID;
        final String name = MediaStore.Downloads.DISPLAY_NAME;

        final String[] columns = {id, name};

        String folder = "/storage/emulated/0/Download/JobSeeker/JobId_";
        folder = folder.concat(jobId + "/");
        //Log.d("Item1",folder);
        String selection = MediaStore.Downloads.DATA + " LIKE ? AND " + MediaStore.Downloads.DATA + " NOT LIKE ? ";
        String[] selectionArgs = new String[]{
                "%" + folder + "%",
                "%" + folder + "/%/%",
        };

        Cursor cursor = resolver.query(uri, columns, selection, selectionArgs, null);
        Log.d("Item", cursor.getCount() + "");
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                String retrievedFileName = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                if (retrievedFileName.equals(fileName)) {
                    cursor.close();
                    return true;
                }

            } while (cursor.moveToNext());
        }

        cursor.close();
        return false;
    }


}