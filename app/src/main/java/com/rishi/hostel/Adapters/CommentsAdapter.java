package com.rishi.hostel.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rishi.hostel.ModalClasses.CommentData;
import com.rishi.hostel.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder>{
    private List<CommentData> data;
    private String TAG="CommentAdapter";
    private Context context;
    public CommentsAdapter(List<CommentData> data) {
        //here data is a reference variable pointing towards the location
        //pointed by data in argument
        this.data=data;

        //in order to create a new list with same elements of data we use
        //List<COmmentData> list=new ArrayList<>(data);
        //this create a new list and copy the elements of data in new list
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.commentlayout,parent,false);
        context=parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=data.get(position).getUsername();
        String comment_s=data.get(position).getComment();
        holder.username.setText(name);
        holder.comment.setText(comment_s);
        String url=data.get(position).getImageurl();
        Glide.with(context).load(url).into(holder.commentuserimage);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+data.size());
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        TextView comment;
        CircleImageView commentuserimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username=itemView.findViewById(R.id.c_r_username);
            comment=itemView.findViewById(R.id.c_r_comment);
            commentuserimage=itemView.findViewById(R.id.comment_user_image);
        }
    }
}
