package com.example.jome17wave.jome_Bean;

public class PersonalGroupBean {
    private String memberId = "";
    private int attenderId = -1;
    private int attenderStatus = -1;
    private int role = -1;
    private String groupId = "";
    private String groupName = "";
    private String assembleTime = "";
    private String groupEndTime = "";
    private String signUpEnd = "";
    private int surfPointId = -1;
    private int groupLimit = -1;
    private int gender = -1;
    private int groupStatus = -1;
    private String notice = "";
    private byte[] gImage = null;

    public PersonalGroupBean(String memberId, int attenderId, String groupId) {
        this.memberId = memberId;
        this.attenderId = attenderId;
        this.groupId = groupId;
    }

    public PersonalGroupBean() {
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getAttenderId() {
        return attenderId;
    }

    public void setAttenderId(int attenderId) {
        this.attenderId = attenderId;
    }

    public int getAttenderStatus() {
        return attenderStatus;
    }

    public void setAttenderStatus(int attenderStatus) {
        this.attenderStatus = attenderStatus;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAssembleTime() {
        return assembleTime;
    }

    public void setAssembleTime(String assembleTime) {
        this.assembleTime = assembleTime;
    }

    public String getGroupEndTime() {
        return groupEndTime;
    }

    public void setGroupEndTime(String groupEndTime) {
        this.groupEndTime = groupEndTime;
    }

    public String getSignUpEnd() {
        return signUpEnd;
    }

    public void setSignUpEnd(String signUpEnd) {
        this.signUpEnd = signUpEnd;
    }

    public int getSurfPointId() {
        return surfPointId;
    }

    public void setSurfPointId(int surfPointId) {
        this.surfPointId = surfPointId;
    }

    public int getGroupLimit() {
        return groupLimit;
    }

    public void setGroupLimit(int groupLimit) {
        this.groupLimit = groupLimit;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getGroupStatus() {
        return groupStatus;
    }

    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public byte[] getgImage() {
        return gImage;
    }

    public void setgImage(byte[] gImage) {
        this.gImage = gImage;
    }
}
