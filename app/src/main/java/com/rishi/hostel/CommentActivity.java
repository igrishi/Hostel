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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentActivity extends AppCompatActivity {
    private String Postuserid, Postdesc, Postid;
    private TextView name, desciption;
    private CircleImageView image, myimage;
    private String myuserimageurl;
    private String TAG = "CommentsActivity";
    private RecyclerView commentlist;
    private EditText comment;
    private ImageView send;
    private List<CommentData> data;
    private CommentsAdapter commentsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        myuserimageurl = User.getImage_url();
        data = new ArrayList<>();
        Log.d(TAG, "onCreate: " + myuserimageurl);
        Intent intent = getIntent();
        Postuserid = intent.getStringExtra("userid");
        Postdesc = intent.getStringExtra("description");
        Postid = intent.getStringExtra("postid");
        image = findViewById(R.id.comments_my_user);
        name = findViewById(R.id.c_username);
        comment = findViewById(R.id.comment_edit_text);
        desciption = findViewById(R.id.c_description);
        myimage = findViewById(R.id.myimage);
        commentlist = findViewById(R.id.comments_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        commentsAdapter = new CommentsAdapter(data);
        commentlist.setAdapter(commentsAdapter);
        send = findViewById(R.id.send);
        commentlist.setLayoutManager(layoutManager);
        Glide.with(CommentActivity.this).load(myuserimageurl).into(myimage);
        getdata(Postuserid);
        desciption.setText(Postdesc);
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
    }

    private void uploadcomment(String comment_string) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String usertoken = auth.getCurrentUser().getUid();
        HashMap<String, Object> map = new HashMap<>();
        map.put("time", FieldValue.serverTimestamp());
        map.put("comment", comment_string);
        map.put("username", User.getName());
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("posts").document(Postid)
                .collection("comments").add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(CommentActivity.this, "comment uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(CommentActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
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
                assert queryDocumentSnapshots != null;
                Log.d(TAG, "onEvent: " +"triggered");
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
        });
    }

    private void getdata(String postuserid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Users").document(Postuserid).get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String username = task.getResult().getString("name");
                            String profileimage = task.getResult().getString("image url");
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
