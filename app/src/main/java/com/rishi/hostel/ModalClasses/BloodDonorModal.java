package com.rishi.hostel.ModalClasses;

public class BloodDonorModal {
    private String name,rollno,roomno,image_url,branch;

    public BloodDonorModal(String name, String rollno, String roomno, String image_url, String branch) {
        this.name = name;
        this.rollno = rollno;
        this.roomno = roomno;
        this.image_url = image_url;
        this.branch = branch;
    }

    public BloodDonorModal(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRollno() {
        return rollno;
    }

    public void setRollno(String rollno) {
        this.rollno = rollno;
    }

    public String getRoomno() {
        return roomno;
    }

    public void setRoomno(String roomno) {
        this.roomno = roomno;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
