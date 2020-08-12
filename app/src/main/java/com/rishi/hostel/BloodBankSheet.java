package com.rishi.hostel;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BloodBankSheet extends BottomSheetDialogFragment {

    private LinearLayout bloodbank1,bloodbank2,bloodbank3,bloodbank4,bloodbank5,bloodbank6;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.bloodbanksheet,container,false);
        bloodbank1=view.findViewById(R.id.bloodbank1);
        bloodbank2=view.findViewById(R.id.bloodbank2);
        bloodbank3=view.findViewById(R.id.bloodbank3);
        bloodbank4=view.findViewById(R.id.bloodbank4);
        bloodbank5=view.findViewById(R.id.bloodbank5);
        bloodbank6=view.findViewById(R.id.bloodbank6);

        bloodbank1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=loc:" + 26.0114151 + "," + 84.5638085 + " (" + "Blood bank" + ")"));
                startActivity(intent);
            }
        });

        bloodbank2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=loc:" + 26.0067894 + "," + 84.5535454 + " (" + "Blood bank" + ")"));
                startActivity(intent);
            }
        });

        bloodbank3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=loc:" + 26.0021813 + "," + 84.3554509 + " (" + "Blood bank" + ")"));
                startActivity(intent);
            }
        });

        bloodbank4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=loc:" + 26.0094865 + "," + 84.5535454 + " (" + "Blood bank" + ")"));
                startActivity(intent);
            }
        });

        bloodbank5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=loc:" + 26.0138816 + "," + 84.5655197 + " (" + "Blood bank" + ")"));
                startActivity(intent);
            }
        });

        bloodbank6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?q=loc:" + 26.0141768 + "," + 84.5588042 + " (" + "Blood bank" + ")"));
                startActivity(intent);
            }
        });
        return view;
    }
}
