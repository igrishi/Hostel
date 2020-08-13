package com.rishi.hostel.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rishi.hostel.ModelClasses.User;
import com.rishi.hostel.R;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_profile_fragment,container,false);
        String name_t = User.getName();
        String roll_t = User.getRollno();
        String branch_t = User.getBranch();
        String blood_gt = User.getBloodgrp();
        String room_t = User.getRollno();
        String image_url = User.getImage_url();
        TextView name = v.findViewById(R.id.user_profile_name);
        TextView email = v.findViewById(R.id.user_profile_email);
        TextView roll = v.findViewById(R.id.user_profile_roll);
        TextView branch = v.findViewById(R.id.user_profile_branch);
        TextView blood_g = v.findViewById(R.id.user_profile_bloodg);
        TextView room = v.findViewById(R.id.user_profile_room);
        CircleImageView image = v.findViewById(R.id.user_profile_image);

        Glide.with(Objects.requireNonNull(getContext())).load(image_url).into(image);
        name.setText(name_t);
        roll.setText(roll_t);
        branch.setText(branch_t);
        blood_g.setText(blood_gt);
        room.setText(room_t);

        return v;
    }
}
