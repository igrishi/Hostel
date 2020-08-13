package com.rishi.hostel.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rishi.hostel.ModelClasses.NoticeModel;
import com.rishi.hostel.R;

import java.util.List;

public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.ViewHolder> {

    Context context;
    List<NoticeModel> notices;
    public NoticeAdapter(List<NoticeModel> notices){
        this.notices = notices;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_layout,parent,false);
        context=parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String head = notices.get(position).getHeading();
        final String desc= notices.get(position).getDesc();
        boolean college = notices.get(position).getCollege();
        holder.head.setText(head);
        if(college){
            holder.label.setVisibility(View.VISIBLE);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog =new Dialog(context);
                dialog.setContentView(R.layout.notice_dialog);
                TextView d_head = dialog.findViewById(R.id.dn_head);
                TextView d_desc = dialog.findViewById(R.id.dn_desc);
                d_head.setText(head);
                d_desc.setText(desc);
                dialog.setCancelable(true);
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return notices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView head,label;
        View view;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            head = itemView.findViewById(R.id.notice_head);
            label = itemView.findViewById(R.id.sender);
            this.view=itemView;
        }
    }
}
