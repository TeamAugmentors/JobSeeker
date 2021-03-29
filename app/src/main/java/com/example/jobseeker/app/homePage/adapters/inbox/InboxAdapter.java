package com.example.jobseeker.app.homePage.adapters.inbox;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ItemInboxBinding;
import com.example.jobseeker.utils.HelperUtils;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ProgressCallback;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private ArrayList<ParseObject> parseObjects = new ArrayList<>();

    private OnInboxListener mOnInboxListener;

    public InboxAdapter(HashMap<String, ParseObject> objectHashMap, OnInboxListener onInboxListener) {

        objectHashMap.forEach((s, parseObject) -> {
            parseObjects.add(parseObject);
        });

        mOnInboxListener = onInboxListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new InboxAdapter.ViewHolder(ItemInboxBinding.inflate((inflater), parent, false), parent.getContext(), mOnInboxListener);
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        Context context;
        ItemInboxBinding binding;
        OnInboxListener onInboxListener;

        public ViewHolder(ItemInboxBinding b, Context context, OnInboxListener onInboxListener) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            super(b.getRoot());
            binding = b;
            this.context = context;
            this.onInboxListener = onInboxListener;
            b.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onInboxListener.onInboxClick(getAdapterPosition(), parseObjects);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.context, R.anim.fade_scale_in));

        holder.binding.firstName.setText(parseObjects.get(pos).getString("firstName"));

        parseObjects.get(pos).getParseFile("proPic").getDataInBackground((data, e) -> {

            if (e == null) {
                Glide.with(holder.context)
                        .asBitmap()
                        .load(data)
                        .transform(new CircleCrop())
                        .override(250, 250)
                        .into(holder.binding.proPic);

            } else {
                Log.d("fiasdjfiaws", "Error on e not equal to null! " + e.getMessage());
            }
        });

        ParseQuery<ParseObject> parseQuery1 = ParseQuery.getQuery("LiveMessage");

        parseQuery1.whereEqualTo("createdBy", ParseUser.getCurrentUser().getUsername());
        parseQuery1.whereEqualTo("createdFor", parseObjects.get(pos).get("username"));

        ParseQuery<ParseObject> parseQuery2 = ParseQuery.getQuery("LiveMessage");

        parseQuery2.whereEqualTo("createdFor", ParseUser.getCurrentUser().getUsername());
        parseQuery2.whereEqualTo("createdBy", parseObjects.get(pos).get("username"));

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();
        queries.add(parseQuery1);
        queries.add(parseQuery2);

        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.orderByAscending("createdAt");

        mainQuery.findInBackground((objects, e) -> {
            if (e == null) {
                if (objects.size() != 0) {
                    ParseObject parseObject = objects.get(objects.size() - 1);
                    String seenTime,currentTime,showTime;
                    seenTime = parseObject.getUpdatedAt().toString();
                    currentTime = Calendar.getInstance().getTime().toString();

                    showTime = HelperUtils.getTime(seenTime, currentTime,false);

                    if (parseObject.getString("createdBy").equals(ParseUser.getCurrentUser().getUsername())) {
                        //message made by me
                        holder.binding.recentMessage.setText("You: " + parseObject.getString("message")+" ~ "+showTime);
                        holder.binding.firstName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                    } else {
                        holder.binding.recentMessage.setText(parseObject.getString("message")+" ~ "+showTime);

                        if (parseObject.getBoolean("seenByFor"))
                            holder.binding.firstName.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public interface OnInboxListener {
        void onInboxClick(int position, ArrayList<ParseObject> users);
    }

    public void filter(ArrayList<ParseObject> filteredList) {
        if (!parseObjects.equals(filteredList)) {
            parseObjects = filteredList;
            notifyDataSetChanged();
        }
    }
}