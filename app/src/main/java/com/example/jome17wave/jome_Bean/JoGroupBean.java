package com.example.jome17wave.jome_Bean;

public class JoGroupBean {
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

    public JoGroupBean(String groupId, String groupName, String assembleTime, String groupEndTime, String signUpEnd,
                 int surfPointId, int groupLimit, int gender, int groupStatus, String notice, byte[] gImage) {
        super();
        this.groupId = groupId;
        this.groupName = groupName;
        this.assembleTime = assembleTime;
        this.groupEndTime = groupEndTime;
        this.signUpEnd = signUpEnd;
        this.surfPointId = surfPointId;
        this.groupLimit = groupLimit;
        this.gender = gender;
        this.groupStatus = groupStatus;
        this.notice = notice;
        this.gImage = gImage;
    }

    public JoGroupBean() {
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
