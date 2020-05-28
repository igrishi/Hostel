package com.rishi.hostel.OnBoarding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rishi.hostel.HomePage;
import com.rishi.hostel.R;

import java.util.Objects;

public class Login extends AppCompatActivity {
    private EditText l_email,l_password;
    private ProgressBar loading;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TextView createnewac = findViewById(R.id.create_new_account);
        l_email=findViewById(R.id.li_email);
        loading=findViewById(R.id.login_progress);
        l_password=findViewById(R.id.li_pass);
        Button button=findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkdetail(l_email.getText().toString(),l_password.getText().toString());
            }
        });

        createnewac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Login.this,SignUp.class);
                startActivity(intent);
            }
        });
    }

    private void checkdetail(String email, String password) {
        if(!(email.length() > 10) || !(email.contains("@gmail.com"))){
            l_email.setError("enter a valid email id");
        }else if(password.length()==0){
            l_password.setError("enter password");
        }else{
            loading.setVisibility(View.VISIBLE);
            login(email,password);
        }
    }

    private void login(String email, String password) {
        final FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String usertoken= Objects.requireNonNull(auth.getCurrentUser()).getUid();
                    Toast.makeText(Login.this, "Logged in", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(Login.this, HomePage.class);
                    intent.putExtra("user_uid", usertoken);
                    startActivity(intent);
                    finish();
                }else{
                    loading.setVisibility(View.INVISIBLE);
                    Toast.makeText(Login.this, Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loading.setVisibility(View.INVISIBLE);
                Toast.makeText(Login.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
