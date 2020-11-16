package com.example.jome17wave.jome_Bean;

import java.io.Serializable;

public class JomeMember implements Serializable {
    private String memberId;
    private int accountStatus;
    private String phoneNumber;
    private String nickname;
    private String account;
    private String password;
    private int gender;
    private double latitude;
    private double longitude;
    private String tokenId;
    private byte[] image;
    private String friendCount = "";
    private String scoreAverage = "";
    private String beRankedCount = "0";
    private String groupCount = "";
    private String createGroupCount = "";

    public JomeMember() {
    }

    public JomeMember(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public JomeMember(String memberId, double latitude, double longitude) {
        this.memberId = memberId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public JomeMember(String memberId, String account, String password, int gender, String phoneNumber, String nickname) {
        this.memberId = memberId;
        this.account = account;
        this.password = password;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.nickname = nickname;
    }


    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(String friendCount) {
        this.friendCount = friendCount;
    }

    public String getScoreAverage() {
        return scoreAverage;
    }

    public void setScoreAverage(String scoreAverage) {
        this.scoreAverage = scoreAverage;
    }

    public String getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(String groupCount) {
        this.groupCount = groupCount;
    }

    public String getBeRankedCount() {
        return beRankedCount;
    }

    public void setBeRankedCount(String beRankedCount) {
        this.beRankedCount = beRankedCount;
    }

    public String getCreateGroupCount() {
        return createGroupCount;
    }

    public void setCreateGroupCount(String createGroupCount) {
        this.createGroupCount = createGroupCount;
    }
}
