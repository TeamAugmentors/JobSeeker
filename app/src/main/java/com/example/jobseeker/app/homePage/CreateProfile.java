package com.example.jobseeker.app.homePage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.animation.LayoutTransition;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
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
import com.example.jobseeker.app.homePage.adapters.CreateProfileInfoViewPagerAdapter;
import com.example.jobseeker.app.homePage.adapters.CreateProfileViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityCreateProfileBinding;
import com.example.jobseeker.utils.ChipHelper;
import com.example.jobseeker.utils.HideKeyboard;
import com.example.jobseeker.utils.ToolbarHelper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;


public class CreateProfile extends AppCompatActivity {

    ActivityCreateProfileBinding binding;
    private static final int PICK_IMAGE = 1;
    private boolean isImageSelected = false;
    CreateProfileViewPager2Adapter adapter;
    ChipGroup skillChipGroup;

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
                ChipHelper.addChipIntoChipGroup(skillChipGroup, this, ParseUser.getCurrentUser().getString("skillSet").split(","));
            }

            ((Button) findViewById(R.id.createProfile)).setText("Update Profile");

        }

        skillChipGroup.setLayoutTransition(new LayoutTransition());

    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        ToolbarHelper.create(binding.toolbar, this, "Create Profile");

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
        if(where==-1)
            where=2;
        binding.viewPager2.setCurrentItem(where);
        return isFieldEmpty;
    }

    public void uploadProPic(View view) {
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

            Glide.with(this)
                    .asBitmap()
                    .load(data.getData())
                    .override(500, 500)
                    .transform(new CircleCrop())
                    .into((ImageView) findViewById(R.id.profile_Image));

            ((TextView) findViewById(R.id.profile_picture_text_view)).setText("Professional Profile Picture");
        }
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
            else{
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
            ChipHelper.addChipIntoChipGroup(skillChipGroup, this, skills);
            ((TextInputLayout) findViewById(R.id.SkillSetLayout)).getEditText().setText("");
        } else {
            Toast.makeText(this, "Skill already exists!", Toast.LENGTH_SHORT).show();
        }
    }
}
