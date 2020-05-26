package com.rishi.hostel.Adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rishi.hostel.CommentData;
import com.rishi.hostel.R;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    private List<CommentData> data;
    private String TAG="CommentAdapter";

    public CommentsAdapter(List<CommentData> data) {
        this.data=data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlayout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=data.get(position).getUsername();
        String comment_s=data.get(position).getComment();
        holder.username.setText(name);
        holder.comment.setText(comment_s);

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+data.size());
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView comment;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.c_r_username);
            comment=itemView.findViewById(R.id.c_r_comment);
        }
    }
}
