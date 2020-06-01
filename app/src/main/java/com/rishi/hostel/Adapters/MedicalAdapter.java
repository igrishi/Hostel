package com.rishi.hostel.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rishi.hostel.R;

public class MedicalAdapter extends RecyclerView.Adapter<MedicalAdapter.ViewHolder> {

    private String[] m_text={"Fever & cold","Depression and stress","Headache & Body pain","Injuries & cuts","Bone break"};
    private int[] m_desciption={R.string.fever,R.string.mentalstress,R.string.headache,R.string.injuries,R.string.brokenbone};
    private int[] m_image={R.drawable.illness,R.drawable.mentalhealth,R.drawable.headache,R.drawable.injury,R.drawable.brokenbone};
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.health_layout,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.image.setImageResource(m_image[position]);
        holder.header.setText(m_text[position]);
        holder.description.setText(m_desciption[position]);
    }

    @Override
    public int getItemCount() {
        return m_image.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView header;
        TextView description;
        ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.health_image);
            header=itemView.findViewById(R.id.health_text);
            description=itemView.findViewById(R.id.health_symp);
        }
    }
}
