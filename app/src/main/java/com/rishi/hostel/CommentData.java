package com.rishi.hostel;

public class CommentData {
    private String username,comment;

    public String getUsername() {
        return username;
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

    public CommentData(String username, String comment) {
        this.username = username;
        this.comment = comment;
    }
}
