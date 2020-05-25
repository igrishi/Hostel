package com.rishi.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity {
    private DrawerLayout drawer;
    private NavigationView nav;
    private String usertoken;
    private String TAG="HomePage";
    private TextView username;
    private CircleImageView imageview;
    private BottomNavigationView bottomnav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Instances
        final BlogFragment blog=new BlogFragment();
        final MediaclFragment medical=new MediaclFragment();
        setContentView(R.layout.activity_home_page);
        drawer=findViewById(R.id.drawer);
        nav=findViewById(R.id.nav_view);
        bottomnav=findViewById(R.id.bottom_nav_bar);
        //cant directly use findviewbyid ... we can only link it using nav view
        //then getting the header using getHeaderview(0) and then getting the name using findviewbyid
        View header=nav.getHeaderView(0);
        username=header.findViewById(R.id.user_nav_name);
        imageview=header.findViewById(R.id.user_nav_image);
        Toolbar toolbar=findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.normal_frame,blog);
        fragmentTransaction.commit();
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                if(id==R.id.blog){
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.normal_frame,blog);
                    fragmentTransaction.commit();
                }else if(id==R.id.medical_help){
                    FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.normal_frame,medical);
                    fragmentTransaction.commit();
                }
                //returning true to show that the task is consumed and everything is ok
                //if false then fragment may change but the selected item is not highlighted as for it task is
                //not consumed
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        usertoken=intent.getStringExtra("user_uid");
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        // use get() method to retrieve data once not for real time
        firestore.collection("Users").document(usertoken).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    String name= Objects.requireNonNull(task.getResult()).getString("name");
                    String image_url=task.getResult().getString("image url");
                    Log.d(TAG, "onComplete: ");
                    username.setText(name);
                    Glide.with(HomePage.this).load(image_url).into(imageview);
                }else{
                    Toast.makeText(HomePage.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onComplete: "+task.getException());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomePage.this,"Something went wrong", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+e);
            }
        });
    }


}