package com.example.jobseeker.app.homePage.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ItemForYouBinding;
import com.example.jobseeker.databinding.ItemJobBoardBinding;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

public class ForYouAdapter extends RecyclerView.Adapter<ForYouAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private ArrayList<ParseObject> parseObjects;
    private OnJobBoardListener mOnJobBoardListener;

    public ForYouAdapter(ArrayList<ParseObject> object, OnJobBoardListener onJobBoardListener) {
        parseObjects = object;
        mOnJobBoardListener = onJobBoardListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ForYouAdapter.ViewHolder(ItemForYouBinding.inflate((inflater), parent, false), parent.getContext(), mOnJobBoardListener);
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        Context context;
        ItemForYouBinding binding;
        OnJobBoardListener onJobBoardListener;

        public ViewHolder(ItemForYouBinding b, Context context, OnJobBoardListener onJobBoardListener) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            super(b.getRoot());
            binding = b;
            this.context = context;
            this.onJobBoardListener = onJobBoardListener;
            b.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onJobBoardListener.onJobBoardClick(getAdapterPosition(), parseObjects);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        holder.binding.title.setText(parseObjects.get(pos).getString("title"));
        holder.binding.description.setText(parseObjects.get(pos).getString("description"));


        /*a function that displays text upto 25 characters from description and puts "..." at the end if its greater than 25 characters, set it to holder.biding.description*/
        String test = holder.binding.description.getText().toString();
        if(test.length()>74)
        {
            holder.binding.description.setText(test.substring(0,73)+"...");
        }
        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.context, R.anim.fade_scale_in));
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public interface OnJobBoardListener {
        void onJobBoardClick(int position,List<ParseObject> parseObjects);
    }

}