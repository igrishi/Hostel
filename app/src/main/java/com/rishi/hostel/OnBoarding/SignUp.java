package com.rishi.hostel.OnBoarding;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rishi.hostel.HomePage;
import com.rishi.hostel.R;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth auth;
    private TextView email, password;
    private String TAG = "SignUp";
    private ProgressBar signupprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        auth = FirebaseAuth.getInstance();
        email = findViewById(R.id.su_email);
        password = findViewById(R.id.su_pass);
        signupprogress=findViewById(R.id.signup_progress);
        Button button = findViewById(R.id.su_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_email(email.getText().toString(),password.getText().toString());

            }
        });

    }

    private void check_email(String email_, String password_) {
        if (!(email_.length() > 10) || !(email_.contains("@gmail.com"))) {
            email.setError("Invalid email");
        } else if (password_.length() < 6) {
            password.setError("Length of password should be 6 at least");
        } else {
            signupprogress.setVisibility(View.VISIBLE);
            authuser(email_, password_);
        }
    }

    private void authuser(final String email_, String password_) {
        auth.createUserWithEmailAndPassword(email_, password_).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "User Authenticated", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, UserDetail.class);
                    intent.putExtra("token", auth.getUid());
                    startActivity(intent);
                    signupprogress.setVisibility(View.INVISIBLE);
                    finish();
                } else {
                    Log.d(TAG, "onComplete: " + task.getException());
                    if(task.getException().toString().contains("The email address is already in use by another account")){
                        Toast.makeText(SignUp.this,"email already in use",Toast.LENGTH_SHORT).show();
                    }else{Toast.makeText(SignUp.this, "something went wrong", Toast.LENGTH_SHORT).show();}
                    signupprogress.setVisibility(View.INVISIBLE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: ", e.getCause());
                signupprogress.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            //will update UI over here for current user
            //if user had already logged in
            Intent intent=new Intent(SignUp.this,HomePage.class);
            intent.putExtra("user_uid",auth.getUid());
            startActivity(intent);
            finish();
        }
    }
}
