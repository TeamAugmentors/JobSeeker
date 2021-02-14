package com.example.jobseeker.app.startScreen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ActivityCreateProfileBinding;

import java.io.IOException;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CreateProfile extends AppCompatActivity {

    ActivityCreateProfileBinding binding;
    private static final int PICK_IMAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }


    public void update(View view) {
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
                    .transform(new MultiTransformation<Bitmap>(new CircleCrop(),new RoundedCornersTransformation(30,10)))
                    .into(binding.profileImage);
        }
    }
}
