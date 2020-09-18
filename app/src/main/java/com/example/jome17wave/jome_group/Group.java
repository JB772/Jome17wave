package com.example.jome17wave.jome_group;

import com.example.jome17wave.Common;

import java.io.Serializable;
import java.util.Date;

public class Group implements Serializable {
    private int id ;
    private String name ;
    private Date assembleTime;
    private int surfPoints;
    private int groupLimit;
    private int genders;
    private String notice;
    private int status;
    private int attendStatus;
    private int role;

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
//    private int endGroup;
//    private int assembleTime;
//    private int signupendTime;
//    private int modify;
//    private double image;


    public Group(String name, Date assembleTime, int status, int attendStatus, int role) {
        this.name = name;
        this.assembleTime = assembleTime;
        this.status = status;
        this.attendStatus = attendStatus;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getAssembleTime() {
        return assembleTime;
    }

    public void setAssembleTime(Date assembleTime) {
        this.assembleTime = assembleTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAttendStatus() {
        return attendStatus;
    }

    public void setAttendStatus(int attendStatus) {
        this.attendStatus = attendStatus;
    }

    //    public Group(int id, String name, int assembleTime, int endGroup, int signupendTime,  int modify, int surfpoints,
//                 int grouplimit, int genders, String notice, double image, int status) {
//        super();
//        this.id = id;
//        this.name = name;
//        this.assembleTime = assembleTime;
//        this.endGroup = endGroup;
//        this.signupendTime = signupendTime;
//        this.modify = modify;
//        this.surfpoints = surfpoints;
//        this.grouplimit = grouplimit;
//        this.genders = genders;
//        this.notice = notice;
//        this.image = image;
//        this.status = status;
//    }

    @Override
    public String toString() {
        return Common.getYYYYmmDDhhMM(assembleTime) + name ;
    }
}

