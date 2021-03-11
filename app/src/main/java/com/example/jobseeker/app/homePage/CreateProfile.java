package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.adapters.createProfile.CreateProfileInfoViewPagerAdapter;
import com.example.jobseeker.app.homePage.adapters.createProfile.CreateProfileViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityCreateProfileBinding;
import com.example.jobseeker.utils.ChipHelper;
import com.example.jobseeker.utils.HideKeyboard;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;


public class CreateProfile extends AppCompatActivity {

    private static final int REQUEST_STORAGE = 111;
    private static final int REQUEST_FILE = 222;
    private static final int REQUEST_CAMERA = 333;
    private static final int CAMERA_REQUEST_CODE = 444;
    private static final int PERMISSION_CODE = 1000;
    private static final int CAPTURE_CODE = 1001;
    ActivityCreateProfileBinding binding;
    private Uri imageUri;
    private boolean isImageSelected = false;
    CreateProfileViewPager2Adapter adapter;
    ChipGroup skillChipGroup;
    boolean isDarkModeOn=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityCreateProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();

        new Handler().postDelayed(() -> {
            fetchData();
            errorTextControl();
        }, 1000);
        SharedPreferences sharedPreferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);
    }


    public void errorTextControl() {
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

    public void fetchData() {
        skillChipGroup = findViewById(R.id.skillChipGroup);
        if (ParseUser.getCurrentUser().get("firstName") != null) {

            Toast.makeText(this, "Fetching", Toast.LENGTH_SHORT).show();
            //Profile was created before
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().setText(ParseUser.getCurrentUser().getString("firstName"));
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().setText(ParseUser.getCurrentUser().getString("lastName"));
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).getEditText().setText(ParseUser.getCurrentUser().getString("bkashNo"));

            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setEndIconVisible(false);
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setEndIconVisible(false);
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).setEndIconVisible(false);

            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

            if (ParseUser.getCurrentUser().getString("skillSet") != null) {
                ChipHelper.addChipIntoChipGroup(skillChipGroup, this, true, isDarkModeOn,ParseUser.getCurrentUser().getString("skillSet").split(","));
            }

            ((Button) findViewById(R.id.createProfile)).setText("Update Profile");

        }

        skillChipGroup.setLayoutTransition(new LayoutTransition());

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        ToolbarHelper.create(binding.toolbar, null, this, "Create Profile");

        adapter = new CreateProfileViewPager2Adapter(this);

        setPageViewPager();
        setTitleViewPager();

        setViewPagerOnChangeCallBacks();

    }

    private void setViewPagerOnChangeCallBacks() {
        //Edge negative margin
        int pageMarginPx = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        int offsetPx = getResources().getDimensionPixelOffset(R.dimen.offset);

        binding.titleViewPager2.setPageTransformer((page, position) -> {
            ViewPager2 viewpager = (ViewPager2) page.getParent().getParent();
            float offset = position * -(2 * offsetPx + pageMarginPx);

            if (viewpager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewpager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.setTranslationX(-offset);
                } else {
                    page.setTranslationX(offset);
                }
            } else {
                page.setTranslationX(offset);
            }

            if (position <= -1.0F || position >= 1.0F) {
                page.setAlpha(0.4F);
            } else if (position == 0.0F) {
                page.setAlpha(1.0F);
            } else {

                page.setAlpha((float) (1.0F - (0.6 * Math.abs(position))));
            }
        });

        //Callback
        binding.viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.titleViewPager2.setCurrentItem(position);

                if (binding.viewPager2.getCurrentItem() == binding.viewPager2.getAdapter().getItemCount() - 1) {
                    //if it is at last page
                    binding.next.setVisibility(View.INVISIBLE);
                } else
                    binding.next.setVisibility(View.VISIBLE);

                if (binding.viewPager2.getCurrentItem() == 0) {
                    //if it is at first page
                    binding.back.setVisibility(View.INVISIBLE);
                } else
                    binding.back.setVisibility(View.VISIBLE);
            }
        });

        binding.titleViewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                binding.viewPager2.setCurrentItem(position);
            }
        });
    }

    private void setTitleViewPager() {
        binding.titleViewPager2.setAdapter(new CreateProfileInfoViewPagerAdapter(this));
        binding.titleViewPager2.setOffscreenPageLimit(3);

        binding.titleViewPager2.setHorizontalFadingEdgeEnabled(true);
    }

    private void setPageViewPager() {
        binding.viewPager2.setAdapter(adapter);
        binding.viewPager2.setOffscreenPageLimit(5);
        binding.dotsIndicator.setViewPager2(binding.viewPager2);

    }


    public boolean check() {
        int where = -1;
        boolean isFieldEmpty = false;
        if (((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().getText().toString().isEmpty()) {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setError("*This Field is Required");
            where = 0;
            isFieldEmpty = true;
        } else {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).setError("");
        }
        if (((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().getText().toString().isEmpty()) {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setError("*This Field is Required");
            where = 0;
            isFieldEmpty = true;
        } else {
            ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).setError("");
        }
        if (!isImageSelected) {
            ((TextView) findViewById(R.id.profile_picture_text_view)).setText("Please upload a Professional Profile Picture");
            ((TextView) findViewById(R.id.profile_picture_text_view)).setTextColor(getColor(R.color.job_seeker_red));
            where = 0;
            isFieldEmpty = true;
        }
        if (((TextInputLayout) (findViewById(R.id.outlinedTextFieldBkashNo))).getEditText().getText().toString().isEmpty()) {
            ((TextInputLayout) (findViewById(R.id.outlinedTextFieldBkashNo))).setError("*This Field is Required");
            if (where != 0) {
                where = 2;
            }
            isFieldEmpty = true;
        } else {
            ((TextInputLayout) (findViewById(R.id.outlinedTextFieldBkashNo))).setError("");
        }
        if (where == -1)
            where = 2;
        binding.viewPager2.setCurrentItem(where);
        return isFieldEmpty;
    }

    public void uploadProPic(View view) {


        Dialog dialog = new Dialog(this, R.style.Dialog);
        dialog.setContentView(R.layout.dialog_image_select);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        View dialogView = dialog.getWindow().getDecorView();

        dialogView.findViewById(R.id.close).setOnClickListener(v -> {
            dialog.dismiss();
        });

        //Button for open gallery
        dialogView.findViewById(R.id.galleryButton).setOnClickListener(v -> {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, REQUEST_STORAGE);
            } else {
                openGallery();
            }

            dialog.dismiss();
        });

        //Button for open camera
        dialogView.findViewById(R.id.cameraButton).setOnClickListener(v -> {

            if ((checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                openCamera();
            }

            dialog.dismiss();
        });

    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, REQUEST_FILE);
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pictures");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(camera, CAPTURE_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_FILE && resultCode == RESULT_OK && data != null) {
            startCrop(data.getData());

        } else if (requestCode == CAPTURE_CODE && resultCode == RESULT_OK) {
            startCrop(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            isImageSelected = true;
            Uri imageUriResultCrop = UCrop.getOutput(data);
            if (imageUriResultCrop != null) {
                Glide.with(this)
                        .asBitmap()
                        .load(imageUriResultCrop)
                        .override(500, 500)
                        .transform(new CircleCrop())
                        .into((ImageView) findViewById(R.id.profile_Image));

                ((TextView) findViewById(R.id.profile_picture_text_view)).setText("Professional Profile Picture");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else if (requestCode == REQUEST_STORAGE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }

    private void startCrop(@NonNull Uri uri) {

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(this.getCacheDir(), "U_Crop_Image_" + System.currentTimeMillis() + ".jpeg")));

        uCrop.withOptions(getCropOption()).start(CreateProfile.this);
    }

    private UCrop.Options getCropOption() {
        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(100);
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
        options.setStatusBarColor(getColor(R.color.job_seeker_logo_green));
        options.setToolbarColor(getColor(R.color.job_seeker_logo_green));
        options.setActiveControlsWidgetColor(getColor(R.color.job_seeker_logo_green));
        options.setCircleDimmedLayer(true);
        options.setShowCropGrid(false);
        options.withAspectRatio(1,1);

        return options;
    }

    public void createProfile(View view) {
        if (!check()) {
            ParseUser.getCurrentUser().put("firstName", ((TextInputLayout) findViewById(R.id.outlinedTextFieldFirstName)).getEditText().getText().toString());
            ParseUser.getCurrentUser().put("lastName", ((TextInputLayout) findViewById(R.id.outlinedTextFieldLastName)).getEditText().getText().toString());
            ParseUser.getCurrentUser().put("bkashNo", ((TextInputLayout) findViewById(R.id.outlinedTextFieldBkashNo)).getEditText().getText().toString());

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ((BitmapDrawable) ((ImageView) findViewById(R.id.profile_Image)).getDrawable()).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] data = byteArrayOutputStream.toByteArray();
            ParseFile file = new ParseFile("proPic.jpeg", data);

            ParseUser.getCurrentUser().put("proPic", file);


            if (skillChipGroup.getChildCount() == 0)
                ParseUser.getCurrentUser().remove("skillSet");
            else {
                ParseUser.getCurrentUser().put("skillSet", ChipHelper.getAllChipText(skillChipGroup));
            }

            ParseUser.getCurrentUser().saveInBackground(e -> {
                if (e == null) {
                    Toast.makeText(this, "SUCCESS!", Toast.LENGTH_SHORT).show();
                    finish();
                } else
                    Toast.makeText(this, "Error! " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });

        }

    }

    public void goToNextPage(View view) {
        if (binding.viewPager2.getCurrentItem() != binding.viewPager2.getAdapter().getItemCount() - 1) {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() + 1);
        }
    }

    public void goToPreviousPage(View view) {
        if (binding.viewPager2.getCurrentItem() != 0) {
            binding.viewPager2.setCurrentItem(binding.viewPager2.getCurrentItem() - 1);
        }
    }


    public void addSkill(View view) {

        String skills = ((TextInputLayout) findViewById(R.id.SkillSetLayout)).getEditText().getText().toString().toLowerCase();
        if (!ChipHelper.findMatch(skillChipGroup, skills)) {
            ChipHelper.addChipIntoChipGroup(skillChipGroup, this, true,isDarkModeOn,skills);
            ((TextInputLayout) findViewById(R.id.SkillSetLayout)).getEditText().setText("");
        } else {
            Toast.makeText(this, "Skill already exists!", Toast.LENGTH_SHORT).show();
        }
    }
}
