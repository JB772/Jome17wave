package com.example.jome17wave.jome_Bean;

public class ScoreBean {
    private String groupId = "";
    private String beRatedId = "";
    private String memberId = "";
    private int ratingScore = -1;
    private int scoreId;
    private String groupName, beRatedName;

    public ScoreBean(String groupId, String beRatedId, String memberId, int ratingScore) {
        super();
        this.groupId = groupId;
        this.beRatedId = beRatedId;
        this.memberId = memberId;
        this.ratingScore = ratingScore;
    }



    public ScoreBean(String groupId, String beRatedId, String memberId, int ratingScore, int scoreId, String groupName) {
        super();
        this.groupId = groupId;
        this.beRatedId = beRatedId;
        this.memberId = memberId;
        this.ratingScore = ratingScore;
        this.scoreId = scoreId;
        this.groupName = groupName;
    }

    public ScoreBean(String groupId, String beRatedId, String memberId, int ratingScore, int scoreId, String groupName, String beRatedName) {
        this.groupId = groupId;
        this.beRatedId = beRatedId;
        this.memberId = memberId;
        this.ratingScore = ratingScore;
        this.scoreId = scoreId;
        this.groupName = groupName;
        this.beRatedName = beRatedName;
    }

    public ScoreBean() {
        super();
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

    public int getRatingScore() {
        return ratingScore;
    }

    public void setRatingScore(int ratingScore) {
        this.ratingScore = ratingScore;
    }



    public int getScoreId() {
        return scoreId;
    }



    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
    }



    public String getGroupName() {
        return groupName;
    }



    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getBeRatedName() {
        return beRatedName;
    }

    public void setBeRatedName(String beRatedName) {
        this.beRatedName = beRatedName;
    }
}
