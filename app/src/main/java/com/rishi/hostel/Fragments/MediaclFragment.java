package com.rishi.hostel.Fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rishi.hostel.Adapters.MedicalAdapter;
import com.rishi.hostel.BottomSheet;
import com.rishi.hostel.R;

import org.w3c.dom.Text;

import java.util.Objects;

public class MediaclFragment extends Fragment {


    public MediaclFragment() {
        // Required empty public constructor
    }

    private TextView call_doctor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_medical_fragment, container, false);
        RecyclerView recycler = view.findViewById(R.id.medical_recycler);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recycler.setLayoutManager(layoutManager);
        MedicalAdapter adapter=new MedicalAdapter();
        call_doctor=view.findViewById(R.id.call_doctor);
        call_doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheet bottomSheet=new BottomSheet();
                //what show does is that it Display the dialog, adding the fragment to the given FragmentManager
                bottomSheet.show(Objects.requireNonNull(getFragmentManager()),"bottom sheet");
            }
        });
        recycler.setAdapter(adapter);
        return view;
    }

}
