package com.rishi.hostel.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rishi.hostel.ModelClasses.BloodDonorModel;
import com.rishi.hostel.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BloodDonorsAdapter extends RecyclerView.Adapter<BloodDonorsAdapter.ViewHolder> {
    private List<BloodDonorModel> list;
    private Context mContext;

    public BloodDonorsAdapter(List<BloodDonorModel> list, Context context) {
        this.list=list;
        this.mContext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.blooddonors_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(list.get(position).getName());
        holder.rollno.setText(list.get(position).getRollno());
        holder.roomno.setText(list.get(position).getRoomno());
        String imageurl=list.get(position).getImage_url();
        Glide.with(mContext).load(imageurl).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView image;
        TextView username;
        TextView roomno,rollno;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.bd_userimage);
            username=itemView.findViewById(R.id.bd_username);
            roomno=itemView.findViewById(R.id.bd_roomno);
            rollno=itemView.findViewById(R.id.bd_rollno);
        }
    }
}
