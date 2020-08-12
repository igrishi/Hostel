package com.rishi.hostel.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rishi.hostel.Adapters.NoticeAdapter;
import com.rishi.hostel.ModelClasses.NoticeModel;
import com.rishi.hostel.R;

import java.util.ArrayList;
import java.util.List;

public class NoticeFragment extends Fragment {

    List<NoticeModel> list;
    private String TAG = "NoticeFragment";
    NoticeAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_notice_fragment,container,false);
        RecyclerView recyclerview = view.findViewById(R.id.notice_recycler);
        list=new ArrayList<>();
        adapter = new NoticeAdapter(list);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setAdapter(adapter);
        getNotice();
        return view;
    }

    private void getNotice() {
        FirebaseFirestore data = FirebaseFirestore.getInstance();
        Query firebasequery=data.collection("notice").orderBy("time", Query.Direction.DESCENDING).limit(20);
        firebasequery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: "+ "notice obtained");
                   QuerySnapshot snapshots =task.getResult();
                   if(snapshots!=null){
                       for(DocumentSnapshot doc:snapshots.getDocuments()){
                           String head=doc.getString("heading");
                           String desc = doc.getString("desc");
                           boolean label = doc.getBoolean("college");
                           Log.d(TAG, "heading: "+ head );
                           NoticeModel model = new NoticeModel(head,desc,label);
                           list.add(model);
                           adapter.notifyDataSetChanged();
                       }
                   }
                   else{
                       Log.d(TAG, "snapshot null");
                   }
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
}
