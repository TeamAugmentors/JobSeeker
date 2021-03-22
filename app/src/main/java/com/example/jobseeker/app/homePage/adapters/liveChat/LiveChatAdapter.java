package com.example.jobseeker.app.homePage.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jobseeker.R;
import com.example.jobseeker.databinding.ItemJobBoardBinding;
import com.example.jobseeker.databinding.ItemLiveChatBinding;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class LiveChatAdapter extends RecyclerView.Adapter<LiveChatAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private ArrayList<ParseObject> parseObjects;
    TextView seenText;

    public LiveChatAdapter(ArrayList<ParseObject> object) {
        parseObjects = object;
    }

    public LiveChatAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new LiveChatAdapter.ViewHolder(ItemLiveChatBinding.inflate((inflater), parent, false), parent.getContext());
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        Context context;
        ItemLiveChatBinding binding;

        public ViewHolder(ItemLiveChatBinding b, Context context) {

            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.

            super(b.getRoot());
            binding = b;
            this.context = context;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {

        seenText = holder.binding.txtRSeen;
        if (parseObjects.get(pos).getString("createdBy").equals(ParseUser.getCurrentUser().getUsername())){
            //user made this message
            holder.binding.senderMessage.setVisibility(View.VISIBLE);
            holder.binding.clientMessage.setVisibility(View.GONE);
            holder.binding.senderMessage.setText(parseObjects.get(pos).getString("message"));
        }else {
            //client made this message
            holder.binding.senderMessage.setVisibility(View.GONE);
            holder.binding.clientMessage.setVisibility(View.VISIBLE);
            holder.binding.clientMessage.setText(parseObjects.get(pos).getString("message"));
        }
    }

    public void setSeen(String string){
        if(seenText!=null) {
            seenText.setVisibility(View.VISIBLE);
            seenText.setText(string);
        }
    }
    private void setSalary(){

    }
    @Override
    public int getItemCount() {
        return parseObjects.size();
    }

    public void filter(ArrayList<ParseObject> filteredList){
        if (!parseObjects.equals(filteredList)){
            parseObjects = filteredList;
            notifyDataSetChanged();
        }
    }
}