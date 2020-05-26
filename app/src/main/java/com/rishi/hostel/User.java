package com.rishi.hostel;

public class User {
    private static String name,rollno,roomno;
    private static String branch,bloodgrp,image_url;

    public User(String rollno, String roomno, String branch, String bloodgrp, String name, String image_url) {
        User.rollno = rollno;
        User.roomno = roomno;
        User.branch = branch;
        User.bloodgrp = bloodgrp;
        User.name =name;
        User.image_url =image_url;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        User.name = name;
    }

    public static String getImage_url() {
        return image_url;
    }

    static void setImage_url(String image_url) {
        User.image_url = image_url;
    }

    public static String getRollno() {
        return rollno;
    }

    static void setRollno(String rollno) {
        User.rollno = rollno;
    }

    public static String getRoomno() {
        return roomno;
    }

    static void setRoomno(String roomno) {
        User.roomno = roomno;
    }

    public static String getBranch() {
        return branch;
    }

    static void setBranch(String branch) {
        User.branch = branch;
    }

    public static String getBloodgrp() {
        return bloodgrp;
    }

    static void setBloodgrp(String bloodgrp) {
        User.bloodgrp = bloodgrp;
    }
}
