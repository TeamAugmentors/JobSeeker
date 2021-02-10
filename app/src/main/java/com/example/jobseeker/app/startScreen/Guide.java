package com.example.jobseeker.app.startScreen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.jobseeker.app.homePage.HomePage;
import com.example.jobseeker.app.startScreen.adapters.GuideViewPager2Adapter;
import com.example.jobseeker.databinding.ActivityGuideBinding;
import com.example.jobseeker.databinding.ActivityMainBinding;

public class Guide extends AppCompatActivity {

    ActivityGuideBinding binding;
    GuideViewPager2Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        slide();
    }

    public void slide()
    {
        adapter = new GuideViewPager2Adapter(this);
        binding.viewPagerGuide.setAdapter(adapter);
        binding.viewPagerGuide.setOffscreenPageLimit(4);
        binding.dotsIndicator.setViewPager2(binding.viewPagerGuide);
        binding.viewPagerGuide.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                if(position==binding.viewPagerGuide.getAdapter().getItemCount()-1) {
                    if(binding.viewPagerGuide.getCurrentItem()==binding.viewPagerGuide.getAdapter().getItemCount()-1)
                        binding.next.setText("DONE");
                }
                else
                {
                        binding.next.setText("NEXT");
                }
            }
        });
    }
    public void next(View view)
    {
        if(binding.viewPagerGuide.getCurrentItem()!=binding.viewPagerGuide.getAdapter().getItemCount()-1)
        {
            binding.viewPagerGuide.setCurrentItem(binding.viewPagerGuide.getCurrentItem()+1);
            if(binding.viewPagerGuide.getCurrentItem()==binding.viewPagerGuide.getAdapter().getItemCount()-1)
                binding.next.setText("DONE");
            else
                binding.next.setText("NEXT");
        }
        else
        {
            Intent intent = new Intent(Guide.this, HomePage.class);
            startActivity(intent);
            finish();
        }
    }
    public void skip(View view)
    {
        Intent intent = new Intent(Guide.this, HomePage.class);
        startActivity(intent);
        finish();
    }
}