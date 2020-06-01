package com.rishi.hostel.Fragments;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rishi.hostel.Adapters.MedicalAdapter;
import com.rishi.hostel.BloodNeed;
import com.rishi.hostel.BottomSheet;
import com.rishi.hostel.HomePage;
import com.rishi.hostel.R;

import java.util.HashMap;
import java.util.Objects;

public class MediaclFragment extends Fragment implements MedicalAdapter.ClickListener {

    private String TAG = "MedicalFragment";
    private String[] p_header = {"Fever & cold", "Depression and stress", "Headache & Body pain", "Injuries & cuts", "Bone break"};
    private int[] p_image = {R.drawable.illness, R.drawable.mentalhealth, R.drawable.headache, R.drawable.injury, R.drawable.brokenbone};
    private int[] p_what_to_do = {R.string.what_to_do_fever, R.string.what_to_do_depression, R.string.what_to_do_headache, R.string.what_to_do_injury, R.string.what_to_do_brokenbone};
    private int[] medicine1 = {R.string.m1_fever, R.string.m1_depression, R.string.m1_headache, R.string.m1_injury, R.string.m1_brokenbone};
    private int[] medicine2 = {R.string.m2_fever, R.string.m2_depression, R.string.m2_headache, R.string.m2_injury, R.string.m2_brokenbone};
    private int[] medicine3 = {R.string.m3_fever, R.string.m3_depression, R.string.m3_headache, R.string.m3_injury, R.string.m3_brokenbone};
    private FloatingActionButton blood_b;
    private EditText health_issue;
    private Button upload;

    public MediaclFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_medical_fragment, container, false);
        RecyclerView recycler = view.findViewById(R.id.medical_recycler);
        blood_b=view.findViewById(R.id.fab);
        upload=view.findViewById(R.id.health_issue_button);
        health_issue=view.findViewById(R.id.health_issue_et);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recycler.setLayoutManager(layoutManager);
        //up casting occurs here ie: Listener listener=new MedicalFragment();
        //and in this way we create an reference variable
        MedicalAdapter adapter = new MedicalAdapter(this);
        TextView call_doctor = view.findViewById(R.id.call_doctor);
        call_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet = new BottomSheet();
                //what show does is that it Display the dialog, adding the fragment to the given FragmentManager
                bottomSheet.show(Objects.requireNonNull(getFragmentManager()), "bottom sheet");
            }
        });
        recycler.setAdapter(adapter);
        blood_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), BloodNeed.class);
                startActivity(intent);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(health_issue.getText().toString().length()!=0){
                    uploadissue(health_issue.getText().toString());
                }else{
                    health_issue.setError("This field can't be empty");
                }
            }
        });
        return view;
    }

    private void uploadissue(String issue) {
        HashMap<String,Object> map=new HashMap<>();
        map.put("timestamp", FieldValue.serverTimestamp());
        map.put("issue",issue);
        FirebaseAuth auth=FirebaseAuth.getInstance();
        String user_token= Objects.requireNonNull(auth.getCurrentUser()).getUid();
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        firestore.collection("health_issue").document(user_token).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "Issue Uploaded!!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onComplete: "+task.getException());
                        }
                    }
                });
    }

    @Override
    public void clickevent(int postion) {
        Log.d(TAG, "onClick: " + "clicked");
        final Dialog dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_popup);
        ImageView imageView = dialog.findViewById(R.id.cancel);
        TextView header = dialog.findViewById(R.id.popup_header);
        TextView what_to_do = dialog.findViewById(R.id.what_to_do_s);
        ImageView pop_up_image = dialog.findViewById(R.id.popup_image);
        TextView m1 = dialog.findViewById(R.id.medic_1);
        TextView m2 = dialog.findViewById(R.id.medic_2);
        TextView m3 = dialog.findViewById(R.id.medic_3);
        m1.setText(medicine1[postion]);
        m2.setText(medicine2[postion]);
        m3.setText(medicine3[postion]);
        header.setText(p_header[postion]);
        what_to_do.setText(p_what_to_do[postion]);
        pop_up_image.setImageResource(p_image[postion]);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
