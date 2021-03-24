package com.example.jobseeker.app.homePage.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ItemAppliedBinding;
import com.example.jobseeker.databinding.ItemInboxBinding;
import com.example.jobseeker.databinding.ItemJobBoardBinding;
import com.example.jobseeker.parseSdk.Connect;
import com.google.android.material.chip.ChipGroup;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class WhoAppliedAdapter extends RecyclerView.Adapter<WhoAppliedAdapter.ViewHolder> {

    private ArrayList<ParseObject> parseObjects = new ArrayList<>();
    private onItemClickListener monItemClickListener;

    public WhoAppliedAdapter(ArrayList<ParseObject> objects, onItemClickListener onItemClickListener) {
        parseObjects = objects;
        monItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new WhoAppliedAdapter.ViewHolder(ItemAppliedBinding.inflate((inflater),parent,false),parent.getContext(),monItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.context, R.anim.fade_scale_in));

        ScrollView scrollView = holder.binding.getRoot().findViewById(R.id.scroll_view);
        ChipGroup chipGroup = holder.binding.getRoot().findViewById(R.id.skill_chip_group);
        int chipCount = chipGroup.getChildCount();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)(holder.context)).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        //if skillset is more than 10 then show the chips in  scrollview
        if (chipCount <= 10) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scrollView.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
            scrollView.setLayoutParams(params);
        }

    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Context context;
        ItemAppliedBinding binding;
        onItemClickListener onItemClickListener;

        public ViewHolder(ItemAppliedBinding b, Context context, onItemClickListener onItemClickListener) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(b.getRoot());
            binding = b;
            this.context = context;
            this.onItemClickListener = onItemClickListener;
            b.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition(), parseObjects);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position, ArrayList<ParseObject> users);
    }


}
