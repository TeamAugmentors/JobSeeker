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
import com.example.jobseeker.databinding.DialogApplyBinding;
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

public class CreatedPosts extends AppCompatActivity implements CreatedPostsAdapter.OnCreatedPostsListener {

    private final int PERMISSION_CODE = 1000;
    public final String JOBSEEKER_DIR = Environment.DIRECTORY_DOWNLOADS + "/JobSeeker";
    public int currentPosition = 0;

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
        ParseQuery<ParseObject> query = ParseQuery.getQuery("JobBoard");
        query.include("applied");
        query.whereEqualTo("createdBy", ParseUser.getCurrentUser());

        query.whereEqualTo("locked", false);

        query.findInBackground((objects, e) -> {
            if (e == null) {
                parseObjects = (ArrayList<ParseObject>) objects;
                adapter = new CreatedPostsAdapter(parseObjects, this);

                binding.recyclerView.setAdapter(adapter);

                binding.chip.setText("Created " + parseObjects.size() + " Jobs");
            } else {
                Toast.makeText(CreatedPosts.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                binding.chip.setText("Unable to fetch");
            }
        });
    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, binding
                .collapsingToolbar, this, "Created Jobs");

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
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        LinearLayout.LayoutParams params;

        if (charCount <= 200) {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
        }

        bindingDialog.scrollView.setLayoutParams(params);

        //---------------------->
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
        //---------------------->

        bindingDialog.applySlider.setVisibility(View.GONE);
        bindingDialog.seeFreelancerButton.setVisibility(View.VISIBLE);
        bindingDialog.deleteButton.setVisibility(View.VISIBLE);

        //---------------------->

        Dialog confirmationDialog = new Dialog(this, R.style.Dialog);
        confirmationDialog.setContentView(R.layout.dialog_delete_confirmation);

        View confirmationDialogView = confirmationDialog.getWindow().getDecorView();

        bindingDialog.close.setOnClickListener(v -> {
            dialog.dismiss();
        });

        bindingDialog.deleteButton.setOnClickListener(v1 -> {
            dialog.dismiss();

            confirmationDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            confirmationDialog.show();
        });

        //see freelancers
        bindingDialog.seeFreelancerButton.setOnClickListener(v -> {
            dialog.dismiss();
            currentPosition = position;
            startActivity(new Intent(this,AppliedFreelancers.class).putExtra("jobObject", currentObject));
        });

        //delete confirmation dialog
        confirmationDialogView.findViewById(R.id.close).setOnClickListener(v1 -> {
            confirmationDialog.dismiss();

        });

        confirmationDialogView.findViewById(R.id.yesButton).setOnClickListener(v1 -> {

            parseObjects.get(position).deleteInBackground(e -> {
                if (e == null) {
                    Toast.makeText(this, "Successful!", Toast.LENGTH_SHORT).show();
                    removeJob(parseObjects, position);
                } else {
                    Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            confirmationDialog.dismiss();
        });

        confirmationDialogView.findViewById(R.id.noButton).setOnClickListener(v1 -> {
            confirmationDialog.dismiss();
        });


    }


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




    private void removeJob(List<ParseObject> parseObjects, int pos) {
        parseObjects.remove(pos);
        adapter.notifyItemRemoved(pos);
        adapter.notifyItemRangeChanged(pos, parseObjects.size());

        binding.chip.setText("Created " + parseObjects.size() + " Jobs");
    }

}