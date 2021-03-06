package com.example.jome17wave.jome_message;

import java.io.Serializable;
import java.util.Date;

public class FriendInvitation implements Serializable {
    private int uId, inviteMId, acceptMId, friendStatus;
    private String acceptMName;
    private Date motifyDate;

    public FriendInvitation() {
    }

    public FriendInvitation(int uId, int acceptMId, int friendStatus, String acceptMName, Date motifyDate) {
        this.uId = uId;
        this.acceptMId = acceptMId;
        this.friendStatus = friendStatus;
        this.acceptMName = acceptMName;
        this.motifyDate = motifyDate;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public int getInviteMId() {
        return inviteMId;
    }

    public void setInviteMId(int inviteMId) {
        this.inviteMId = inviteMId;
    }

    public int getAcceptMId() {
        return acceptMId;
    }

    public void setAcceptMId(int acceptMId) {
        this.acceptMId = acceptMId;
    }

    public int getFriendStatus() {
        return friendStatus;
    }

    public void setFriendStatus(int friendStatus) {
        this.friendStatus = friendStatus;
    }

    public Date getMotifyDate() {
        return motifyDate;
    }

    public void setMotifyDate(Date motifyDate) {
        this.motifyDate = motifyDate;
    }

    public String getAcceptMName() {
        return acceptMName;
    }

    public void setAcceptMName(String acceptMName) {
        this.acceptMName = acceptMName;
    }
}
