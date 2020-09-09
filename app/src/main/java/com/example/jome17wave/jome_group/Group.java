package com.example.jome17wave.jome_group;

import java.io.Serializable;

public class Group implements Serializable {
    private int id ;
    private String name ;
    private int assembleTime;
    private int endGroup;
    private int signupendTime;
    private int modify;
    private int surfpoints;
    private int grouplimit;
    private int genders;
    private String notice;
    private double image;
    private int status;


    public Group(int id, String name, int assembleTime, int endGroup, int signupendTime,  int modify, int surfpoints,
                 int grouplimit, int genders, String notice, double image, int status) {
        super();
        this.id = id;
        this.name = name;
        this.assembleTime = assembleTime;
        this.endGroup = endGroup;
        this.signupendTime = signupendTime;
        this.modify = modify;
        this.surfpoints = surfpoints;
        this.grouplimit = grouplimit;
        this.genders = genders;
        this.notice = notice;
        this.image = image;
        this.status = status;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAssembleTime() {
        return assembleTime;
    }

    public void setAssembleTime(int assembleTime) {
        this.assembleTime = assembleTime;
    }

    public int getEndGroup() {
        return endGroup;
    }

    public void setEndGroup(int endGroup) {
        this.endGroup = endGroup;
    }

    public int getSignupendTime() {
        return signupendTime;
    }

    public void setSignupendTime(int signupendTime) {
        this.signupendTime = signupendTime;
    }

    public int getModify() {
        return modify;
    }

    public void setModify(int modify) {
        this.modify = modify;
    }

    public int getSurfpoints() {
        return surfpoints;
    }

    public void setSurfpoints(int surfpoints) {
        this.surfpoints = surfpoints;
    }

    public int getGrouplimit() {
        return grouplimit;
    }

    public void setGrouplimit(int grouplimit) {
        this.grouplimit = grouplimit;
    }

    public int getGenders() {
        return genders;
    }

    public void setGenders(int genders) {
        this.genders = genders;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public double getImage() {
        return image;
    }

    public void setImage(double image) {
        this.image = image;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

