package com.rishi.hostel;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    private List<BlogPost> blog_list;
    private Context context;
    private String TAG="BlogAdapter";

    //<BlogAdapter.ViewHolder> means that View holder is a inner class within the class BlogAdapter
    //because its written in the arrow braces therefore first create the inner class view holder and extend
    //RecyclerView.ViewHolder and then implement the methods

    public BlogAdapter(List<BlogPost> blog_list){
        this.blog_list=blog_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_layout,parent,false);
        //Returns the context the view is running in, through which it can access the current theme, resources, etc
        context=parent.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String description=blog_list.get(position).getDescription();
        String imageurl=blog_list.get(position).getImageurl();
        userdata(position,holder);
        holder.description.setText(description);
        Glide.with(context).load(imageurl).into(holder.image);
        long milliseconds=blog_list.get(position).getTime().getTime();
        String datestring= (String) DateFormat.format("dd MMM yyy",milliseconds);
        Log.d(TAG, "onBindViewHolder: "+datestring);
        holder.time.setText(datestring);
    }

    private void userdata(int position, final ViewHolder holder) {
        String token_id=blog_list.get(position).getUser_id();
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("Users").document(token_id).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            String username=task.getResult().getString("name");
                            String profileimage=task.getResult().getString("image url");
                            Glide.with(context).load(profileimage).into(holder.userimage);
                            holder.username.setText(username);
                        }else{
                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return blog_list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView description;
        private ImageView image;
        private CircleImageView userimage;
        private TextView username;
        private TextView time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description=itemView.findViewById(R.id.post_des);
            image=itemView.findViewById(R.id.post_image);
            userimage=itemView.findViewById(R.id.post_user_image);
            username=itemView.findViewById(R.id.post_username);
            time=itemView.findViewById(R.id.post_date);

        }
    }
}
