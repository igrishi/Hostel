package com.rishi.hostel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rishi.hostel.Adapters.BloodDonorsAdapter;
import com.rishi.hostel.ModalClasses.BloodDonorModal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class BloodDonors extends AppCompatActivity {

    private RecyclerView rows;
    private List<BloodDonorModal> list;
    private String bloodgrp;
    private String TAG="BloodDonor";
    private BloodDonorsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        bloodgrp=intent.getStringExtra("blood_group");
        setContentView(R.layout.activity_blood_donors);
        rows=findViewById(R.id.blood_donor_recycler);
        Context context = getApplicationContext();
        list=new ArrayList<>();
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rows.setLayoutManager(linearLayoutManager);
        adapter=new BloodDonorsAdapter(list, context);
        rows.setAdapter(adapter);
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("Users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots!=null) {
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        String blood=doc.getString("bloodgrp");
                        Log.d(TAG, "onEvent: "+blood);
                        Log.d(TAG, "onEvent: "+"getting documents");
                        if(blood!=null) {
                            if (blood.equals(bloodgrp)) {
                                Log.d(TAG, "onEvent: "+"blood present");
                                BloodDonorModal bloodDonorModal=doc.toObject(BloodDonorModal.class);
                                list.add(bloodDonorModal);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            }
        });
    }
}
