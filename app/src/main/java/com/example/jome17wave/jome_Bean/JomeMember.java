package com.example.jome17wave.jome_Bean;

import java.io.Serializable;
import java.util.Date;

public class JomeMember implements Serializable {
    private String member_id;
    private Date build_date;
    private Date modify_date;
    private int account_status;
    private String phone_number;
    private String nickname;
    private String account;
    private String password;
    private int gender;
    private double latitude;
    private double longitude;
    private String token_id;
    private byte[] image;
    private String friendCount = "";
    private String scoreAverage = "";
    private String groupCount = "";
    private String createGroupCount = "";

    public JomeMember() {
    }

    public JomeMember(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public JomeMember(String member_id ,String account, String password, int gender, String phone_number, String nickname) {
        this.member_id = member_id;
        this.account = account;
        this.password = password;
        this.gender = gender;
        this.phone_number = phone_number;
        this.nickname = nickname;
    }


    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public int getAccount_status() {
        return account_status;
    }

    public void setAccount_status(int account_status) {
        this.account_status = account_status;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public String getToken_id() {
        return token_id;
    }

    public void setToken_id(String token_id) {
        this.token_id = token_id;
    }

    public Date getBuild_date() {
        return build_date;
    }

    public void setBuild_date(Date build_date) {
        this.build_date = build_date;
    }

    public Date getModify_date() {
        return modify_date;
    }

    public void setModify_date(Date modify_date) {
        this.modify_date = modify_date;
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

    public String getCreateGroupCount() {
        return createGroupCount;
    }

    public void setCreateGroupCount(String createGroupCount) {
        this.createGroupCount = createGroupCount;
    }
}
