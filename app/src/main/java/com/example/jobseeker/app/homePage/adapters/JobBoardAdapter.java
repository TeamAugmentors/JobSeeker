package com.example.jobseeker.app.homePage.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ItemJobBoardBinding;
import com.parse.ParseObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class JobBoardAdapter extends RecyclerView.Adapter<JobBoardAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private ArrayList<ParseObject> parseObjects;
    private OnJobBoardListener mOnJobBoardListener;

    public JobBoardAdapter(ArrayList<ParseObject> object, OnJobBoardListener onJobBoardListener) {
        parseObjects = object;
        mOnJobBoardListener = onJobBoardListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new JobBoardAdapter.ViewHolder(ItemJobBoardBinding.inflate((inflater), parent, false), parent.getContext(), mOnJobBoardListener);
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        Context context;
        ItemJobBoardBinding binding;
        OnJobBoardListener onJobBoardListener;

        public ViewHolder(ItemJobBoardBinding b, Context context, OnJobBoardListener onJobBoardListener) {

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
//        holder.binding.skills.setText(parseObjects.get(pos).getString("requiredSkills"));

        /*a function that displays salary in K if its greater than 1000, and displays it normally if it is under 1000, and set it to holder.biding.salary*/
        double temp=parseObjects.get(pos).getInt("budget");
        if(temp>1000)
        {
            temp/=1000;
            int flag = (int)((temp*10)%10);
            int temp2 = (int)(temp);

            if(flag==0)
                holder.binding.salary.setText(temp2+"K");
            else{
                holder.binding.salary.setText(String.format("%.1f",temp)+"K");
            }
        }
        else
            holder.binding.salary.setText(parseObjects.get(pos).getInt("budget") + "");

        /*a function that displays text upto 25 characters from description and puts "..." at the end if its greater than 25 characters, set it to holder.biding.description*/
        String test = holder.binding.description.getText().toString();
        if(test.length()>74)
        {
            holder.binding.description.setText(test.substring(0,73)+"...");
        }
        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.context, R.anim.fade_scale_in));
    }

    private void setSalary(){

    }
    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public interface OnJobBoardListener {
        void onJobBoardClick(int position,List<ParseObject> parseObjects);
    }

    public void filter(ArrayList<ParseObject> filteredList){
        if (!parseObjects.equals(filteredList)){
            parseObjects = filteredList;
            notifyDataSetChanged();
        }
    }
}