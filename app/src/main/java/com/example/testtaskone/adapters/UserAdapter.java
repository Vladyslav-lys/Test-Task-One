package com.example.testtaskone.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testtaskone.R;
import com.example.testtaskone.entities.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<User> users;
    private OnItemListener onItemListener;

    public UserAdapter(Context context, List<User> users, OnItemListener onItemListener) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
        this.onItemListener = onItemListener;
    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.user_list_view, parent, false);
        return new ViewHolder(view, onItemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.flagView.setImageBitmap(user.getPhoto());
        holder.nameText.setText(user.getLastName() + " " + user.getFirstName());
        holder.phoneText.setText(user.getPhone());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView flagView;
        TextView nameText, phoneText;
        OnItemListener onItemListener;
        ViewHolder(View view, OnItemListener onItemListener){
            super(view);
            flagView = (ImageView)view.findViewById(R.id.avatarView);
            nameText = (TextView) view.findViewById(R.id.nameText);
            phoneText = (TextView) view.findViewById(R.id.phoneText);
            this.onItemListener = onItemListener;

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
