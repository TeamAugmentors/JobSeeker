package com.example.jobseeker.app.homePage.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ItemAppliedBinding;
import com.example.jobseeker.utils.HelperUtils;
import com.google.android.material.chip.ChipGroup;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.ArrayList;

import static android.view.View.GONE;

public class AppliedFreelancerAdapter extends RecyclerView.Adapter<AppliedFreelancerAdapter.ViewHolder> {

    private ArrayList<ParseUser> parseObjects = new ArrayList<>();
    private onItemClickListener monItemClickListener;
    private ParseObject jobObject;

    public AppliedFreelancerAdapter(ArrayList<ParseUser> objects, onItemClickListener onItemClickListener) {
        parseObjects = objects;
        monItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new AppliedFreelancerAdapter.ViewHolder(ItemAppliedBinding.inflate((inflater), parent, false), parent.getContext(), monItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.context, R.anim.fade_scale_in));
        ParseUser currentObject = (ParseUser) parseObjects.get(pos);

        holder.binding.firstName.setText(currentObject.getString("firstName"));
        holder.binding.jobComplete.setText(currentObject.getInt("jobsCompleted") + " Jobs Completed");
        if (currentObject.getString("skillSet") != null)
            HelperUtils.addChipIntoChipGroup(holder.binding.skillChipGroup, holder.context, false, false, currentObject.getString("skillSet").split(","));
        else {
            holder.binding.skillChipGroup.setVisibility(GONE);
            holder.binding.scrollView.setVisibility(GONE);
            holder.binding.skillSetTextView.setVisibility(GONE);
        }

        ScrollView scrollView = holder.binding.getRoot().findViewById(R.id.scroll_view);
        ChipGroup chipGroup = holder.binding.getRoot().findViewById(R.id.skill_chip_group);
        int chipCount = chipGroup.getChildCount();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) (holder.context)).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        //if skillset is more than 10 then show the chips in  scrollview
        if (chipCount <= 10) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scrollView.setLayoutParams(params);
        } else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height / 4);
            scrollView.setLayoutParams(params);
        }

        currentObject.getParseFile("proPic").getDataInBackground((data, e) -> {
            if (e == null) {
                Glide.with(holder.context).load(data).into(holder.binding.viewProfileImage);
            }
        });

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
            b.hireButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition(), parseObjects);
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position, ArrayList<ParseUser> users);
    }


}
