package com.rishi.hostel.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rishi.hostel.ModelClasses.User;
import com.rishi.hostel.R;

import java.util.HashMap;

public class ComplainFragment extends Fragment {

    RadioButton hostel, mess, clean, others;
    EditText complain;
    String section, complain_text;
    String TAG = "ComplainFragment";
    FirebaseFirestore firestore;
    ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_complain_fragment, container, false);
        hostel = v.findViewById(R.id.hostel);
        mess = v.findViewById(R.id.mess);
        clean = v.findViewById(R.id.clean);
        others = v.findViewById(R.id.others);
        complain = v.findViewById(R.id.complain_box);
        progress = v.findViewById(R.id.complain_progress);
        firestore = FirebaseFirestore.getInstance();
        Button upload = v.findViewById(R.id.upload_complain);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkcomplain();
            }
        });
        return v;
    }

    private void checkcomplain() {
        if (hostel.isChecked()) {
            progress.setVisibility(View.VISIBLE);
            section = hostel.getText().toString();
            Log.d(TAG, "checkcomplain: " + section);
            complain_text = complain.getText().toString();
            if (complain_text.length() != 0) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("head", section);
                map.put("complain", complain_text);
                map.put("userid", User.getUserid());
                map.put("status","not resolved");
                firestore.collection("complains").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "complain uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
            } else {
                progress.setVisibility(View.INVISIBLE);
                complain.setError("this field cannot be empty");
            }
        } else if (mess.isChecked()) {
            progress.setVisibility(View.VISIBLE);
            section = mess.getText().toString();
            Log.d(TAG, "checkcomplain: " + section);
            complain_text = complain.getText().toString();
            if (complain_text.length() != 0) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("head", section);
                map.put("complain", complain_text);
                map.put("userid", User.getUserid());
                map.put("status","not resolved");
                firestore.collection("complains").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "complain uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
            } else {
                progress.setVisibility(View.INVISIBLE);
                complain.setError("this field cannot be empty");
            }
        } else if (clean.isChecked()) {
            progress.setVisibility(View.VISIBLE);
            section = clean.getText().toString();
            Log.d(TAG, "checkcomplain: " + section);
            complain_text = complain.getText().toString();
            if (complain_text.length() != 0) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("head", section);
                map.put("complain", complain_text);
                map.put("userid", User.getUserid());
                map.put("status","not resolved");
                firestore.collection("complains").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "complain uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
            } else {
                progress.setVisibility(View.INVISIBLE);
                complain.setError("this field cannot be empty");
            }
        } else if (others.isChecked()) {
            progress.setVisibility(View.VISIBLE);
            progress.setVisibility(View.VISIBLE);
            section = others.getText().toString();
            Log.d(TAG, "checkcomplain: " + section);
            complain_text = complain.getText().toString();
            if (complain_text.length() != 0) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("head", section);
                map.put("complain", complain_text);
                map.put("userid", User.getUserid());
                map.put("status","not resolved");
                firestore.collection("complains").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "complain uploaded", Toast.LENGTH_SHORT).show();
                        } else {
                            progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: " + task.getException());
                        }
                    }
                });
            } else {
                progress.setVisibility(View.INVISIBLE);
                complain.setError("this field cannot be empty");
            }
        } else {
            Toast.makeText(getActivity(), "Please select complain field!", Toast.LENGTH_SHORT).show();
        }
    }
}
