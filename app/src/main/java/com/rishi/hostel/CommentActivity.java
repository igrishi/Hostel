package com.rishi.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rishi.hostel.Adapters.CommentsAdapter;
import com.rishi.hostel.ModalClasses.CommentData;
import com.rishi.hostel.ModalClasses.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private String Postuserid;
    private String Postid;
    private TextView name;
    private CircleImageView image;
    private String TAG = "CommentsActivity";
    private EditText comment;
    private List<CommentData> data;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        String myuserimageurl = User.getImage_url();
        data = new ArrayList<>();
        final Intent intent = getIntent();
        Postuserid = intent.getStringExtra("userid");
        String postdesc = intent.getStringExtra("description");
        Postid = intent.getStringExtra("postid");
        image = findViewById(R.id.comments_my_user);
        name = findViewById(R.id.c_username);
        comment = findViewById(R.id.comment_edit_text);
        TextView desciption = findViewById(R.id.c_description);
        Toolbar toolbar = findViewById(R.id.toolbar_comment);
        CircleImageView myimage = findViewById(R.id.myimage);
        RecyclerView commentlist = findViewById(R.id.comments_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commentsAdapter = new CommentsAdapter(data);
        commentlist.setAdapter(commentsAdapter);
        ImageView send = findViewById(R.id.send);
        commentlist.setLayoutManager(layoutManager);
        Glide.with(CommentActivity.this).load(myuserimageurl).into(myimage);
        getdata(Postuserid);
        desciption.setText(postdesc);
        updateAdapter();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommentActivity.this, "clicked", Toast.LENGTH_SHORT).show();
                String comment_string = comment.getText().toString();
                if (comment_string.length() != 0) {
                    uploadcomment(comment_string);
                } else {
                    comment.setError("enter some text");
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth auth=FirebaseAuth.getInstance();
                String current_user= Objects.requireNonNull(auth.getCurrentUser()).getUid();
                Intent intent=new Intent(CommentActivity.this,HomePage.class);
                intent.putExtra("user_uid",current_user);
                startActivity(intent);
                finish();
            }
        });
    }

    private void uploadcomment(String comment_string) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String usertoken = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", FieldValue.serverTimestamp());
        map.put("comment", comment_string);
        map.put("username", User.getName());
        map.put("imageurl",User.getImage_url());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("posts").document(Postid)
                .collection("comments").add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CommentActivity.this, "comment uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CommentActivity.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommentActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateAdapter() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query first = firestore
                .collection("posts")
                .document(Postid).collection("comments")
                .orderBy("time", Query.Direction.ASCENDING);
        first.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                Log.d(TAG, "onEvent: " +"triggered");
                if (queryDocumentSnapshots != null) {
                    for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        Log.d(TAG, "onEvent: "+"document change");
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            CommentData comment=doc.getDocument().toObject(CommentData.class);
                            Log.d(TAG, "onEvent: "+"document added");
                            data.add(comment);
                            commentsAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
    }

    private void getdata(String postuserid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(Postuserid).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String username = Objects.requireNonNull(task.getResult()).getString("name");
                            String profileimage = task.getResult().getString("image_url");
                            Glide.with(CommentActivity.this).load(profileimage).into(image);
                            name.setText(username);
                        } else {
                            Toast.makeText(CommentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CommentActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
