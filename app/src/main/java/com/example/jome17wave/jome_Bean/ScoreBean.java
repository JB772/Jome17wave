package com.example.jome17wave.jome_Bean;

public class ScoreBean {
    private int scoreId = -1;
    private String beRatedId = "";
    private String memberId = "";
    private int score = -1;

    public ScoreBean(String beRatedId, String memberId) {
        this.beRatedId = beRatedId;
        this.memberId = memberId;
    }

    public ScoreBean() {
    }

    public int getScoreId() {
        return scoreId;
    }

    public void setScoreId(int scoreId) {
        this.scoreId = scoreId;
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
}
