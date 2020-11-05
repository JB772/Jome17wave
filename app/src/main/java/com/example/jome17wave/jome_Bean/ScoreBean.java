package com.example.jome17wave.jome_Bean;

public class ScoreBean {
    private String groupId = "";
    private String beRatedId = "";
    private  String beRatedName = "";
    private String memberId = "";
    private int score = -1;

    public ScoreBean(String groupId, String beRatedId, String memberId, int score) {
        this.groupId = groupId;
        this.beRatedId = beRatedId;
        this.memberId = memberId;
        this.score = score;
    }

    public ScoreBean(String groupId, String beRatedId, String beRatedName, String memberId, int score) {
        this.groupId = groupId;
        this.beRatedId = beRatedId;
        this.beRatedName = beRatedName;
        this.memberId = memberId;
        this.score = score;
    }

    public ScoreBean() {
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getBeRatedId() {
        return beRatedId;
    }

    public void setBeRatedId(String beRatedId) {
        this.beRatedId = beRatedId;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getBeRatedName() {
        return beRatedName;
    }

    public void setBeRatedName(String beRatedName) {
        this.beRatedName = beRatedName;
    }
}
