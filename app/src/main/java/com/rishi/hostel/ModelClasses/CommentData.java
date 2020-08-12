package com.rishi.hostel.ModelClasses;

public class CommentData {
    private String username, comment, imageurl;

    public String getUsername() {
        return username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public CommentData() {
    }

    public CommentData(String username, String comment, String imageurl) {
        this.username = username;
        this.comment = comment;
    }
}
