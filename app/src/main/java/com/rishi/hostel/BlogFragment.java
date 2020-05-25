package com.rishi.hostel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class BlogFragment extends Fragment {
    private List<BlogPost> list;
    private BlogAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_blog_fragment, container, false);
        FloatingActionButton button = v.findViewById(R.id.fab);
        RecyclerView recyclerview = v.findViewById(R.id.recycler_view);
        //implementing the list with a ArrayList
        list = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostBlog.class);
                startActivity(intent);
            }
        });
        adapter=new BlogAdapter(list);
        RecyclerView.LayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(manager);
        recyclerview.setAdapter(adapter);
        //getting data from firebaseFireStore
        FirebaseFirestore data=FirebaseFirestore.getInstance();
        //addSnapshotlistener help in retrieving data in real time(ie: whenever data changes)
        Query firstquery=data.collection("posts")
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(20);
        firstquery.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

               /* document change:-- Returns the list of documents that changed since the last snapshot.
                If it's the first snapshot all documents will be in the list as added changes.
                Documents with changes only to their metadata will not be included*/
                assert queryDocumentSnapshots != null;
                for(DocumentChange doc:queryDocumentSnapshots.getDocumentChanges()){
                    //now over here we are only looking for new document added changes
                    //we are not looking for modified and deleted changes
                    if(doc.getType()== DocumentChange.Type.ADDED){
                        String blogpostid=doc.getDocument().getId();
                        BlogPost blogPost=doc.getDocument().toObject(BlogPost.class);
                        blogPost.postid=blogpostid;
                        list.add(blogPost);
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        });
        //inflating rhe view
        return v;
    }
}
