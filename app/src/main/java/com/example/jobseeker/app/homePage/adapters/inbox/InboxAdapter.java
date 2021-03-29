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
    private ArrayList<ParseObject> recentMessages = new ArrayList<>();

    private HashMap<Integer, Integer> recentMessageMap = new HashMap<>();

    private OnInboxListener mOnInboxListener;

    public InboxAdapter(ArrayList<ParseObject> parseObjects, ArrayList<ParseObject> recentMessages, OnInboxListener onInboxListener) {
        this.parseObjects = parseObjects;
        this.recentMessages = recentMessages;
        for (int i = 0; i < getItemCount(); i++) {
            recentMessageMap.put(i,0);
        }
        mOnInboxListener = onInboxListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new InboxAdapter.ViewHolder(ItemInboxBinding.inflate((inflater), parent, false), parent.getContext(), mOnInboxListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        ParseUser clientUser = (ParseUser) parseObjects.get(pos);

        holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.context, R.anim.fade_scale_in));

        holder.binding.firstName.setText(clientUser.getString("firstName"));

        clientUser.getParseFile("proPic").getDataInBackground((data, e) -> {

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

        //Check if recent messages are between me and client and vice versa
        for (int i = 0; i < recentMessages.size(); i++) {
            if (recentMessages.get(i).get("createdBy").equals(clientUser.getUsername()) || recentMessages.get(i).get("createdFor").equals(clientUser.getUsername())) {

                //recent message exists
                String seenTime,currentTime,showTime,message;
                seenTime = recentMessages.get(i).getUpdatedAt().toString();
                currentTime = Calendar.getInstance().getTime().toString();

                showTime = HelperUtils.getTime(seenTime, currentTime,false);

                message = recentMessages.get(i).getString("message");
                if(message.length()>19)
                    message = message.substring(0,18)+"...";
                if (recentMessages.get(i).get("createdBy").equals(ParseUser.getCurrentUser().getUsername())) {
                    // message was made by me
                    holder.binding.recentMessage.setText("You: " + message + " \u25c8 "  + showTime);
                } else {
                    holder.binding.recentMessage.setText(message + " \u25c8 "  + showTime);
                }
                holder.binding.firstName.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                recentMessageMap.put(pos, i);
                break;

                //---------------->
            } else {
                holder.binding.recentMessage.setText("Hello! I would like to contact you!");
            }
        }
    }

    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public interface OnInboxListener {
        void onInboxClick(int position, ArrayList<ParseObject> users, Integer recentMessagePosition);
    }

    public void filter(ArrayList<ParseObject> filteredList) {
        if (!parseObjects.equals(filteredList)) {
            parseObjects = filteredList;
            notifyDataSetChanged();
        }
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
            onInboxListener.onInboxClick(getAdapterPosition(), parseObjects, recentMessageMap.get(getAdapterPosition()));
        }
    }

}