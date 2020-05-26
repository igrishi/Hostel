package com.rishi.hostel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rishi.hostel.OnBoarding.UserDetail;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import io.grpc.Context;

public class PostBlog extends AppCompatActivity {
private ImageView post_image;
private EditText text;
private Uri imageuri;
private String TAG="PostBlog";
private String image_url;
private ProgressBar loading;
private String user_id;
private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=FirebaseAuth.getInstance();
        user_id= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        Log.d(TAG, "upload_data: user_id:--"+user_id);
        setContentView(R.layout.activity_post_blog);
        post_image=findViewById(R.id.post_blog_image);
        text=findViewById(R.id.post_description);
        Button button=findViewById(R.id.upload_blog);
        loading=findViewById(R.id.progress_bar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(imageuri,text.getText().toString());
            }
        });
        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        /*        CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .setAspectRatio(1,1)
                        .start(PostBlog.this);*/

                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMinCropResultSize(512,512)
                        .start(PostBlog.this);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE&&data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                imageuri= result.getUri();
                post_image.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
                Log.d(TAG, "onActivityResult: "+error);
            }
        }
    }

    private void validate(Uri imageuri, String description) {
        if(description.length()==0){
            text.setError("Write something");
        }
        else{
            loading.setVisibility(View.VISIBLE);
            if(imageuri!=null){
            postblog();}
            else{
                noimageblog();
            }
        }
    }

    private void noimageblog() {
        //when you set on a document reference them we are actually mean that
        // whose document id we already know or already has a unique id
        //by using set we mean to replace the original document of modify data in it
        //and by using add() on a collection reference we are unconditionally creating a new document
        //which will be assigned a unique id by the firebase firestore
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        HashMap<String,Object> post=new HashMap<>();
        post.put("imageurl",null);
        post.put("description",text.getText().toString());
        post.put("user_id",user_id);
        post.put("time", FieldValue.serverTimestamp());
        firestore.collection("posts").add(post).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostBlog.this,"Post uploaded",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: post uploaded");
                    Intent intent=new Intent(PostBlog.this,HomePage.class);
                    intent.putExtra("user_uid",user_id);
                    startActivity(intent);
                    finish();
                }else{
                    Log.d(TAG, "onComplete: "+task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: "+e.toString());
            }
        });
    }

    private void postblog() {
        String blogdetail= random();
        final StorageReference reference= FirebaseStorage.getInstance().getReference().child("blogimage")
                .child(blogdetail);
        final UploadTask upload=reference.putFile(imageuri);
        upload.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(PostBlog.this,"Image uploaded",Toast.LENGTH_SHORT).show();
                //getting uploaded image url
                upload.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw Objects.requireNonNull(task.getException());
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(PostBlog.this,"URL obtained",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: "+task.getResult());
                            image_url= String.valueOf(task.getResult());
                            upload_data();}
                        else{
                            loading.setVisibility(View.INVISIBLE);
                            Toast.makeText(PostBlog.this,"Url not obtained",Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: "+task.getException());
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onFailure: "+e);
                Toast.makeText(PostBlog.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void upload_data() {
        FirebaseFirestore data=FirebaseFirestore.getInstance();
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("imageurl",image_url);
        hashMap.put("description",text.getText().toString());
        hashMap.put("user_id",user_id);
        hashMap.put("time", FieldValue.serverTimestamp());
        Log.d(TAG, "upload_data: "+FieldValue.serverTimestamp().toString());
        //here we don't need to add document name in firestore it will automatically generate a random string for it
        //it is added by the random document ID
        data.collection("posts").add(hashMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if(task.isSuccessful()){
                    Toast.makeText(PostBlog.this,"Post uploaded",Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: post uploaded");
                    Intent intent=new Intent(PostBlog.this,HomePage.class);
                    intent.putExtra("user_uid",user_id);
                    startActivity(intent);
                    finish();
                }
                else{
                    loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(PostBlog.this,"something went wrong"+task.getException(),Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(PostBlog.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int MAX_LENGTH = 30;
        int randomLength = generator.nextInt(MAX_LENGTH);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
