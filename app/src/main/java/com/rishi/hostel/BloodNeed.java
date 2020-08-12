package com.rishi.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

public class BloodNeed extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_need);
        RelativeLayout layout=findViewById(R.id.bloodbanks);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BloodBankSheet bloodBankSheet=new BloodBankSheet();
                bloodBankSheet.show(getSupportFragmentManager(),"bottom sheet");
            }
        });

    }

    public void A_positive(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","A+");
        startActivity(intent);
    }

    public void A_negative(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","A-");
        startActivity(intent);
    }

    public void B_positive(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","B+");
        startActivity(intent);
    }

    public void B_negative(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","B-");
        startActivity(intent);
    }

    public void O_positive(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","O+");
        startActivity(intent);
    }

    public void O_negative(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","O-");
        startActivity(intent);
    }

    public void AB_positive(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","AB+");
        startActivity(intent);
    }

    public void AB_negative(View view) {
        Intent intent=new Intent(BloodNeed.this,BloodDonors.class);
        intent.putExtra("blood_group","AB-");
        startActivity(intent);
    }
}
