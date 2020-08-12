package com.rishi.hostel.ModelClasses;

public class NoticeModel {
    private String heading,desc;
    private boolean college;

    public NoticeModel(String heading, String desc,boolean college) {
        this.heading = heading;
        this.desc = desc;
        this.college=college;
    }

    public NoticeModel(){

    }

    public boolean getCollege() {
        return college;
    }

    public void setCollege(boolean college) {
        this.college = college;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
