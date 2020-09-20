package com.example.jome17wave.jome_Bean;

public class JomeMember {
    private String member_id = "";
    private byte[] image = null;
    private String account = "";
    private String password = "";
    private int gender = -1;
    private int account_status = -1;
    private String phone_number = "";
    private String nickname = "";
    private double latitude = 0.0;
    private double longitude = 0.0;
    private String token_id = "";
    //畫面所需欄位
    private Integer friendCount = 0;
    private Double scoreAverage = 0.0;
    private Integer groupCount = 0;

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

    public Integer getFriendCount() {
        return friendCount;
    }

    public void setFriendCount(Integer friendCount) {
        this.friendCount = friendCount;
    }

    public Double getScoreAverage() {
        return scoreAverage;
    }

    public void setScoreAverage(Double scoreAverage) {
        this.scoreAverage = scoreAverage;
    }

    public Integer getGroupCount() {
        return groupCount;
    }

    public void setGroupCount(Integer groupCount) {
        this.groupCount = groupCount;
    }
}
