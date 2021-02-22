package com.example.jobseeker.app.homePage;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCreateProfileBinding;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

public class CreateProfile extends AppCompatActivity {

    ActivityCreateProfileBinding binding;
    private static final int PICK_IMAGE = 1;
    private boolean isImageSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        fetchData();
        errorTextControl();
    }

    private void errorTextControl() {
        binding.outlinedTextFieldFirstName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.outlinedTextFieldFirstName.getEditText().getText().toString().isEmpty()) {
                    binding.outlinedTextFieldFirstName.setError("*This Field is Required");
                } else {
                    binding.outlinedTextFieldFirstName.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.outlinedTextFieldLastName.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.outlinedTextFieldLastName.getEditText().getText().toString().isEmpty()) {
                    binding.outlinedTextFieldLastName.setError("*This Field is Required");
                } else {
                    binding.outlinedTextFieldLastName.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.outlinedTextFieldBkashNo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (binding.outlinedTextFieldBkashNo.getEditText().getText().toString().isEmpty()) {
                    Log.d("Here", "I am here");
                    binding.outlinedTextFieldBkashNo.setError("*This Field is Required");
                } else {
                    Log.d("Here1", "I am here");
                    binding.outlinedTextFieldBkashNo.setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void fetchData() {

        if (ParseUser.getCurrentUser().get("firstName") != null) {
            Toast.makeText(this, "Fetching", Toast.LENGTH_SHORT).show();
            //Profile was created before
            binding.outlinedTextFieldFirstName.getEditText().setText(ParseUser.getCurrentUser().getString("firstName"));
            binding.outlinedTextFieldLastName.getEditText().setText(ParseUser.getCurrentUser().getString("lastName"));
            binding.outlinedTextFieldBkashNo.getEditText().setText(ParseUser.getCurrentUser().getString("bkashNo"));

            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("proPic");
            imageFile.getDataInBackground((data, e) -> {
                if (e == null) {

                    Glide.with(this)
                            .asBitmap()
                            .load(data)
                            .override(500, 500)
                            .transform(new CircleCrop())
                            .into(binding.profileImage);
                    isImageSelected = true;

                } else {
                    Toast.makeText(this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "lol gg", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Create Profile");
    }


    public boolean check() {
        boolean isFieldEmpty = false;
        if (binding.outlinedTextFieldFirstName.getEditText().getText().toString().isEmpty()) {
            binding.outlinedTextFieldFirstName.setError("*This Field is Required");
            isFieldEmpty = true;
        } else {
            binding.outlinedTextFieldFirstName.setError("");
        }
        if (binding.outlinedTextFieldLastName.getEditText().getText().toString().isEmpty()) {
            binding.outlinedTextFieldLastName.setError("*This Field is Required");
            isFieldEmpty = true;
        } else {
            binding.outlinedTextFieldLastName.setError("");
        }
        if (binding.outlinedTextFieldBkashNo.getEditText().getText().toString().isEmpty()) {
            binding.outlinedTextFieldBkashNo.setError("*This Field is Required");
            isFieldEmpty = true;
        } else {
            binding.outlinedTextFieldBkashNo.setError("");
        }
        if (!isImageSelected) {
            binding.imageUpload.setText("Please! Upload a professional" + "\n" + "Profile Picture");
            binding.imageUpload.setTextColor(getColor(R.color.red));
            isFieldEmpty = true;
        }

        return isFieldEmpty;
    }

    public void uploadImage(View view) {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            isImageSelected = true;
            binding.imageUpload.setText(null);
            Glide.with(this)
                    .asBitmap()
                    .load(data.getData())
                    .override(500, 500)
                    .transform(new CircleCrop())
                    .into(binding.profileImage);
        }
    }

    public void submit(View view) {
        if (!check()) {
            ParseUser.getCurrentUser().put("firstName", binding.outlinedTextFieldFirstName.getEditText().getText().toString());
            ParseUser.getCurrentUser().put("lastName", binding.outlinedTextFieldLastName.getEditText().getText().toString());

            ParseUser.getCurrentUser().put("bkashNo", binding.outlinedTextFieldBkashNo.getEditText().getText().toString());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ((BitmapDrawable) binding.profileImage.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            ParseFile file = new ParseFile("proPic.jpeg", data);

            ParseUser.getCurrentUser().put("proPic", file);
            ParseUser.getCurrentUser().saveInBackground(e -> {
                if (e == null) {
                    Toast.makeText(this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
