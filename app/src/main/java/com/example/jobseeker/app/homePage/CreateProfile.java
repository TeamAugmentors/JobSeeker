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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        fetchData();

    }

    private void fetchData() {

        if (ParseUser.getCurrentUser().get("firstName") != null){
            Toast.makeText(this, "Fetching", Toast.LENGTH_SHORT).show();
            //Profile was created before
            binding.outlinedTextFieldFirstName.getEditText().setText(ParseUser.getCurrentUser().getString("firstName"));
            binding.outlinedTextFieldLastName.getEditText().setText(ParseUser.getCurrentUser().getString("lastName"));
            binding.outlinedTextFieldBkashNo.getEditText().setText(ParseUser.getCurrentUser().getString("bkashNo"));

            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("proPic");
            imageFile.getDataInBackground((data, e) -> {
                        if (e == null) {
                            // Decode the Byte[] into
                            // Bitmap

                            Bitmap bmp = BitmapFactory
                                    .decodeByteArray(
                                            data, 0,
                                            data.length);

                            binding.profileImage.setImageBitmap(bmp);
                        } else {
                            Toast.makeText(this, "Error ! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }else
            Toast.makeText(this, "lol gg", Toast.LENGTH_SHORT).show();

    }

    private void init() {
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setTitle("Create Profile");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    public void uploadImage(View view) {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"),PICK_IMAGE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_IMAGE && resultCode == RESULT_OK){
            Glide.with(this)
                    .asBitmap()
                    .load(data.getData())
                    .override(500,500)
                    .transform(new CircleCrop())
                    .into(binding.profileImage);
        }
    }

    public void submit(View view) {
        ParseUser.getCurrentUser().put("fullName" ,  binding.outlinedTextFieldFirstName.getEditText().getText().toString() + " " +
                binding.outlinedTextFieldLastName.getEditText().getText().toString());

        ParseUser.getCurrentUser().put("bkashNo" , binding.outlinedTextFieldBkashNo.getEditText().getText().toString());

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ((BitmapDrawable)binding.profileImage.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();
        ParseFile file = new ParseFile("proPic.jpeg", data);

        ParseUser.getCurrentUser().put("proPic", file);
        ParseUser.getCurrentUser().saveInBackground(e -> {
            if (e == null){
                Toast.makeText(this, "SUCCESS!", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }
}
