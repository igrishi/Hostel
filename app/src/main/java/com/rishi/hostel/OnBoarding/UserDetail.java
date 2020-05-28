package com.rishi.hostel.OnBoarding;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rishi.hostel.HomePage;
import com.rishi.hostel.R;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetail extends AppCompatActivity {
    private TextView username, userroll, userroom;
    private CircleImageView user_image;
    private String name, rollno, roomno, branch, bloodgrp;
    private String usertoken, TAG = "UserDetail";
    private final int REQUEST_CODE = 10;
    private Uri imageuri;
    private final int REQUEST_CODEP = 123;
    private ProgressBar loading;
    private String image_url;
    private Spinner spinner, spinner_blood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Intent intent = getIntent();
        usertoken = intent.getStringExtra("token");
        Log.d(TAG, "onCreate: user token is: " + usertoken);
        user_image = findViewById(R.id.user_icon);
        username = findViewById(R.id.user_name);
        userroll = findViewById(R.id.user_rollno);
        userroom = findViewById(R.id.user_roomno);
        Button button = findViewById(R.id.user_button);
        loading = findViewById(R.id.user_detail_progress);
        spinner_blood = findViewById(R.id.spinner2);
        spinner = findViewById(R.id.spinner);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = username.getText().toString();
                rollno = userroll.getText().toString();
                roomno = userroom.getText().toString();
                checkdetail();
            }
        });
        user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We are asking permission explicitly if the android version is marshmallow or above
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkpermission();
                } else {
                    openGallery();
                }
            }
        });


        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.branches, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        branchspinner();

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.bloodgrp, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner_blood.setAdapter(adapter1);
        bloodgrpspinner();
    }

    private void bloodgrpspinner() {
        spinner_blood.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodgrp = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: " + "no item selected");
            }
        });
    }

    private void branchspinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //getItemAtpostition returns the data associated with the selected view at that postion
                String b = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemSelected: " + b);
                branch = b;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d(TAG, "onNothingSelected: " + "no item selected");
            }
        });
    }

    private void checkpermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODEP);
        } else {
            openGallery();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODEP) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "onRequestPermissionsResult: permission granted");
                openGallery();
            } else {
                Log.d(TAG, "onRequestPermissionsResult: permission denied");
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE == requestCode && resultCode == RESULT_OK && data != null) {
            imageuri = data.getData();
            CropImage.activity(imageuri)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                imageuri = result.getUri();
                user_image.setImageURI(imageuri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                assert result != null;
                Exception error = result.getError();
                Log.d(TAG, "onActivityResult: " + error);
            }
        }
    }

    private void checkdetail() {
        if (imageuri == null) {
            Toast.makeText(this, "Select image please", Toast.LENGTH_SHORT).show();
        } else if (name.length() == 0) {
            username.setError("enter name");
        } else if (rollno.length() != 7) {
            userroll.setError("enter valid roll no");
        } else if (roomno.length() == 0) {
            userroom.setError("enter room no");
        } else {
            loading.setVisibility(View.VISIBLE);
            upload_image();
        }
    }

    private void upload_image() {
        final StorageReference reference = FirebaseStorage.getInstance().getReference("images").child("profile image").child(usertoken);
        final UploadTask uploadTask = reference.putFile(imageuri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(UserDetail.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                //getting URL of uploaded image

                //what continue_with_task does is that
                //Returns a new Task that will be completed with the result of applying the specified Continuation to this Task.

                //UploadTask class provides features to upload and  fires events for success, progress and failure. It also allows pause and resume to control the upload operation.

                uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw Objects.requireNonNull(task.getException());
                        }
                        return reference.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UserDetail.this, "URL obtained", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + task.getResult());
                            image_url = String.valueOf(task.getResult());
                            upload_data();
                        } else {
                            loading.setVisibility(View.INVISIBLE);
                            Toast.makeText(UserDetail.this, "Image not Uploaded", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.INVISIBLE);
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });
    }

    private void upload_data() {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("name", name);
        hashMap.put("rollno", rollno);
        hashMap.put("roomno", roomno);
        hashMap.put("branch", branch);
        hashMap.put("bloodgrp", bloodgrp);
        hashMap.put("image url", image_url);
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        data.collection("Users").document(usertoken).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(UserDetail.this, "Data Uploaded", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UserDetail.this, HomePage.class);
                    intent.putExtra("user_uid", usertoken);
                    startActivity(intent);
                    finish();
                } else {
                    loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(UserDetail.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: " + task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(UserDetail.this, "Data upload failed", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + e.toString());
            }
        });

    }
}
