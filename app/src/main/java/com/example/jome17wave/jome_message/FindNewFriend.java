package com.example.jome17wave.jome_message;

public class FindNewFriend {
    private int memberId, friendStatus;
    private String memberAccount, nickName;

    public FindNewFriend() {
    }

    public FindNewFriend(int memberId, int friendStatus, String memberAccount, String nickName) {
        this.memberId = memberId;
        this.friendStatus = friendStatus;
        this.memberAccount = memberAccount;
        this.nickName = nickName;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(int friendStatus) {
        this.friendStatus = friendStatus;
    }

    public String getMemberAccount() {
        return memberAccount;
    }

    public void setMemberAccount(String memberAccount) {
        this.memberAccount = memberAccount;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
