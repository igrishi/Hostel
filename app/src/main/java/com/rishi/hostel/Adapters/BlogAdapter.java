package com.rishi.hostel.Adapters;


import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rishi.hostel.BlogPost;
import com.rishi.hostel.CommentActivity;
import com.rishi.hostel.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    private List<BlogPost> blog_list;
    private String TAG="BlogAdapter";
    private FirebaseAuth auth;
    private FragmentActivity activity;

    //<BlogAdapter.ViewHolder> means that View holder is a inner class within the class BlogAdapter
    //because its written in the arrow braces therefore first create the inner class view holder and extend
    //RecyclerView.ViewHolder and then implement the methods

    public BlogAdapter(List<BlogPost> blog_list, FragmentActivity activity){
        this.blog_list=blog_list;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.blog_layout,parent,false);
        //Returns the context the view is running in, through which it can access the current theme, resources, etc
        auth=FirebaseAuth.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String user_token= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        final String blogpostid=blog_list.get(position).postid;
        final String description=blog_list.get(position).getDescription();
        String imageurl=blog_list.get(position).getImageurl();
        userdata(position,holder);
        holder.description.setText(description);
        Glide.with(activity).load(imageurl).into(holder.image);
        long milliseconds=blog_list.get(position).getTime().getTime();
        String datestring= (String) DateFormat.format("dd MMM yyy",milliseconds);
        Log.d(TAG, "onBindViewHolder: "+datestring);
        holder.time.setText(datestring);

        //like feature
        initialdata(blogpostid,user_token,holder);
        holder.likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likebutton(blogpostid,user_token,holder);
            }
        });

        //comment feature
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //here we can not directly launch an activity using startactivity because
                //the class does not extend to AppcompactActivity or any Activity class
                Intent intent=new Intent(activity, CommentActivity.class);
                intent.putExtra("postid",blogpostid);
                intent.putExtra("userid",blog_list.get(position).getUser_id());
                intent.putExtra("description",description);
                activity.startActivity(intent);
            }
        });
    }

    private void initialdata(String blogpostid, String user_token, final ViewHolder holder) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference collectionReference=rootRef
                .collection("posts")
                .document(blogpostid)
                .collection("Likes");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                assert queryDocumentSnapshots != null;
                long number=queryDocumentSnapshots.size();
                holder.numberlikes.setText(number+" Likes");
            }
        });

        DocumentReference docIdRef = collectionReference.document(user_token);
        //checking real time likes
        docIdRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: "+"like event triggered");
                if(documentSnapshot.exists()){
                    holder.likes.setImageResource(R.drawable.liked);
                }

            }
        });
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
                            Glide.with(activity).load(profileimage).into(holder.userimage);
                            holder.username.setText(username);
                        }else{
                            Toast.makeText(activity, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(activity, e.toString(), Toast.LENGTH_SHORT).show();
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
        private ImageView likes;
        private TextView numberlikes;
        private ImageView comments;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            description=itemView.findViewById(R.id.post_des);
            image=itemView.findViewById(R.id.post_image);
            userimage=itemView.findViewById(R.id.post_user_image);
            username=itemView.findViewById(R.id.post_username);
            time=itemView.findViewById(R.id.post_date);
            likes=itemView.findViewById(R.id.like);
            numberlikes=itemView.findViewById(R.id.numberlikes);
            comments=itemView.findViewById(R.id.comment);
        }
    }

    private void likebutton(String blogpostid, String user_token, final ViewHolder holder){
        final HashMap<String,Object> likes=new HashMap<>();
        likes.put("time", FieldValue.serverTimestamp());

         FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
         final CollectionReference collectionReference=rootRef
                .collection("posts")
                .document(blogpostid)
                .collection("Likes");


        final DocumentReference docIdRef = collectionReference.document(user_token);

        //my early mistake is that here we can't have a real time checking because are
        //deleting document over here so in case if we delete a document again the
        //listener gets triggered and create that document and again deletes it as it exist also
        //hence it becomes an infinite loop
        //therefore we user get() method when deleting a document

        docIdRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot doc=task.getResult();
                    if(doc.exists()){
                        Log.d(TAG, "Document exists!");
                        docIdRef.delete();
                        holder.likes.setImageResource(R.drawable.unliked);

                        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                assert queryDocumentSnapshots != null;
                                long number=queryDocumentSnapshots.size();
                                holder.numberlikes.setText(number+" Likes");
                            }
                        });
                    }
                    else{
                        Log.d(TAG, "Document does not exist!");
                        docIdRef.set(likes);
                        holder.likes.setImageResource(R.drawable.liked);

                        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                                assert queryDocumentSnapshots != null;
                                long number=queryDocumentSnapshots.size();
                                holder.numberlikes.setText(number+" Likes");
                            }
                        });
                    }
                }
            }
        });
    }
}
