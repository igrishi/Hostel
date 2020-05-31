package com.rishi.hostel.ModalClasses;


import java.util.Date;

public class BlogPost {
    //Modal class for blog post

    String description, imageurl, user_id;
    Date time;

    public String postid;

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public BlogPost() {
    }

    public BlogPost(String description, String imageurl, String user_id, Date time) {
        this.description = description;
        this.imageurl = imageurl;
        this.user_id = user_id;
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
