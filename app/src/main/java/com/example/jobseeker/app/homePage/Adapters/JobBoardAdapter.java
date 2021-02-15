package com.example.jobseeker.app.homePage.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ItemJobBoardBinding;
import com.parse.ParseObject;

import java.util.List;

public class JobBoardAdapter extends RecyclerView.Adapter<JobBoardAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<ParseObject> parseObjects;

    public JobBoardAdapter(List<ParseObject> object) {
        parseObjects = object;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new JobBoardAdapter.ViewHolder(ItemJobBoardBinding.inflate((inflater), parent, false), parent.getContext());
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        Context context;
        ItemJobBoardBinding binding;

        public ViewHolder(ItemJobBoardBinding b, Context context) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(b.getRoot());
            binding = b;
            this.context = context;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        holder.binding.title.setText(parseObjects.get(pos).getString("title"));
        holder.binding.description.setText(parseObjects.get(pos).getString("description"));
        holder.binding.skills.setText(parseObjects.get(pos).getString("requiredSkills"));
        holder.binding.salary.setText(parseObjects.get(pos).getString("salary") + "$");

        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.context, R.anim.fade_scale_in));
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }
}