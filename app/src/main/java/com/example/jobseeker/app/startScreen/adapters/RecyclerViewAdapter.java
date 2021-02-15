package com.example.jobseeker.app.startScreen.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobseeker.R;
import com.example.jobseeker.app.homePage.JobBoard;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    String jobData[],descriptionData[];
    //int images[];
    Context context;

    public RecyclerViewAdapter(Context ct, String s1[],String s2[]){
        context=ct;
        jobData=s1;
        descriptionData=s2;
        //images=img;
    }


    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.job_row, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.title.setText(jobData[position]);
        holder.description.setText(descriptionData[position]);
        //holder.image.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return jobData.length;
    }
    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title, description;
        //ImageView image;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.jobs);
            description = itemView.findViewById(R.id.descriptions);
           // image = itemView.findViewById(R.id.jobImageView);
        }
    }
}
