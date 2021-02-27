package com.example.jobseeker.app.homePage;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.jobseeker.R;
import com.example.jobseeker.app.startScreen.Guide;
import com.example.jobseeker.app.startScreen.adapters.CreateProfileViewPager2Adapter;
import com.example.jobseeker.app.startScreen.adapters.WelcomeScreenViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityCreateProfileBinding;
import com.example.jobseeker.utils.HideKeyboard;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.mukesh.OtpView;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

import at.markushi.ui.CircleButton;

public class CreateProfile extends AppCompatActivity {

    ActivityCreateProfileBinding binding;
    private static final int PICK_IMAGE = 1;
    private boolean isImageSelected = false;
    CreateProfileViewPager2Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
        ((TextView)findViewById(R.id.yourInfo)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
        new Handler().postDelayed(() -> {
            fetchData();
            errorTextControl();
        }, 1000);
        binding.viewPagerCreateProfile.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
               manageText(binding.viewPagerCreateProfile.getCurrentItem());
            }
        });
    }


    private void errorTextControl() {
        ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().getText().toString().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setError("*This Field is Required");
                } else {
                    ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().getText().toString().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setError("*This Field is Required");
                } else {
                    ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setError("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).getEditText().getText().toString().isEmpty()) {
                    ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).setError("*This Field is Required");
                } else {
                    ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).setError("");
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
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().setText(ParseUser.getCurrentUser().getString("firstName"));
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().setText(ParseUser.getCurrentUser().getString("lastName"));
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).getEditText().setText(ParseUser.getCurrentUser().getString("bkashNo"));

            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setEndIconVisible(false);
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setEndIconVisible(false);
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).setEndIconVisible(false);

            ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if ((actionId & EditorInfo.IME_MASK_ACTION) != 0) {
                        getCurrentFocus().clearFocus();
                        HideKeyboard.hide((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE), binding.getRoot());
                        return true;
                    } else
                        return false;
                }
            });

            ParseFile imageFile = (ParseFile) ParseUser.getCurrentUser().get("proPic");
            imageFile.getDataInBackground((data, e) -> {
                if (e == null) {

                    Glide.with(this)
                            .asBitmap()
                            .load(data)
                            .override(500, 500)
                            .transform(new CircleCrop())
                            .into((ImageView) findViewById(R.id.profile_Image));
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
        adapter = new CreateProfileViewPager2Adapter(this);
        binding.viewPagerCreateProfile.setAdapter(adapter);
        binding.viewPagerCreateProfile.setOffscreenPageLimit(5);
        binding.dotsIndicator.setViewPager2(binding.viewPagerCreateProfile);

    }


    public boolean check() {
        boolean isFieldEmpty = false;
        if (((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().getText().toString().isEmpty()) {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setError("*This Field is Required");
            isFieldEmpty = true;
        } else {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setError("");
        }
        if (((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().getText().toString().isEmpty()) {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setError("*This Field is Required");
            isFieldEmpty = true;
        } else {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setError("");
        }
        if (((TextInputLayout) (findViewById(R.id.outlinedTextFieldBkashNo))).getEditText().getText().toString().isEmpty()) {
            ((TextInputLayout) (findViewById(R.id.outlinedTextFieldBkashNo))).setError("*This Field is Required");
            isFieldEmpty = true;
        } else {
            ((TextInputLayout) (findViewById(R.id.outlinedTextFieldBkashNo))).setError("");
        }
        if (!isImageSelected) {
            ((TextView) findViewById(R.id.imageUpload)).setText("Please! Upload a professional" + "\n" + "Profile Picture");
            ((TextView) findViewById(R.id.imageUpload)).setTextColor(getColor(R.color.red));
            isFieldEmpty = true;
        }
        Log.d("checkPage1: ", String.valueOf(isFieldEmpty));
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
            ((TextView) findViewById(R.id.imageUpload)).setText(null);
            Glide.with(this)
                    .asBitmap()
                    .load(data.getData())
                    .override(500, 500)
                    .transform(new CircleCrop())
                    .into((ImageView) findViewById(R.id.profile_Image));
        }
    }

    public void submit(View view) {
        if (!check()) {
            ParseUser.getCurrentUser().put("firstName", ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().getText().toString());
            ParseUser.getCurrentUser().put("lastName", ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().getText().toString());

            ParseUser.getCurrentUser().put("bkashNo", ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).getEditText().getText().toString());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ((BitmapDrawable) ((ImageView) findViewById(R.id.profile_Image)).getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
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

    public void nextPage(View view) {
        if (binding.viewPagerCreateProfile.getCurrentItem() != binding.viewPagerCreateProfile.getAdapter().getItemCount() - 1) {
                binding.viewPagerCreateProfile.setCurrentItem(binding.viewPagerCreateProfile.getCurrentItem() + 1);
                manageText(binding.viewPagerCreateProfile.getCurrentItem());
        }
    }

    public void pageOne(View view) {
        binding.viewPagerCreateProfile.setCurrentItem(0);
        manageText(0);

    }
    public void pageTwo(View view) {
        binding.viewPagerCreateProfile.setCurrentItem(1);
        manageText(1);

    }
    public void pageThree(View view) {
        binding.viewPagerCreateProfile.setCurrentItem(2);
        manageText(2);
    }

    public void manageText(int currPage){
        if(currPage == 0){
            ((TextView)findViewById(R.id.yourInfo)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            ((TextView)findViewById(R.id.skillSets)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.hint_black));
            ((TextView)findViewById(R.id.payment)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.hint_black));
            ((TextView)findViewById(R.id.yourInfo)).setTextSize(20);
            ((TextView)findViewById(R.id.skillSets)).setTextSize(18);
            ((TextView)findViewById(R.id.payment)).setTextSize(18);
            ((TextView)findViewById(R.id.payment)).setBackground(null);
            ((TextView)findViewById(R.id.skillSets)).setBackground(null);
            ((TextView)findViewById(R.id.yourInfo)).setBackgroundResource(R.drawable.text_background);
        }
        else if(currPage == 1){
            ((TextView)findViewById(R.id.yourInfo)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.hint_black));
            ((TextView)findViewById(R.id.skillSets)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            ((TextView)findViewById(R.id.payment)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.hint_black));
            ((TextView)findViewById(R.id.yourInfo)).setTextSize(18);
            ((TextView)findViewById(R.id.skillSets)).setTextSize(20);
            ((TextView)findViewById(R.id.payment)).setTextSize(18);
            ((TextView)findViewById(R.id.yourInfo)).setBackground(null);
            ((TextView)findViewById(R.id.payment)).setBackground(null);
            ((TextView)findViewById(R.id.skillSets)).setBackgroundResource(R.drawable.text_background);
        }
        else if(currPage == 2){
            ((TextView)findViewById(R.id.yourInfo)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.hint_black));
            ((TextView)findViewById(R.id.skillSets)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.hint_black));
            ((TextView)findViewById(R.id.payment)).setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black));
            ((TextView)findViewById(R.id.yourInfo)).setTextSize(18);
            ((TextView)findViewById(R.id.skillSets)).setTextSize(18);
            ((TextView)findViewById(R.id.payment)).setTextSize(20);
            ((TextView)findViewById(R.id.yourInfo)).setBackground(null);
            ((TextView)findViewById(R.id.skillSets)).setBackground(null);
            ((TextView)findViewById(R.id.payment)).setBackgroundResource(R.drawable.text_background);
        }
    }

}
