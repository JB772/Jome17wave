package com.example.jome17wave.jome_Bean;

import java.io.Serializable;
import java.util.Date;

public class FriendListBean implements Serializable {
    private int uId = 0;
    private String invite_M_ID = "";
    private String inviteName = "";
    private String accept_M_ID = "";
    private String acceptName = "";
    private int friend_Status = -1;
    private Date modify_Date = new Date();

    public FriendListBean(int uId, String invite_M_ID, String inviteName, String accept_M_ID, int friend_Status,
                          Date modify_Date) {
        this.uId = uId;
        this.invite_M_ID = invite_M_ID;
        this.inviteName = inviteName;
        this.accept_M_ID = accept_M_ID;
        this.friend_Status = friend_Status;
        this.modify_Date = modify_Date;
    }

    public FriendListBean(String invite_M_ID, String inviteName, String accept_M_ID, String acceptName, int friend_Status) {
        this.invite_M_ID = invite_M_ID;
        this.inviteName = inviteName;
        this.accept_M_ID = accept_M_ID;
        this.acceptName = acceptName;
        this.friend_Status = friend_Status;
    }



    public FriendListBean(String invite_M_ID, String accept_M_ID, int friend_Status) {
        this.invite_M_ID = invite_M_ID;
        this.accept_M_ID = accept_M_ID;
        this.friend_Status = friend_Status;
    }

    public FriendListBean(String invite_M_ID, String accept_M_ID) {
        this.invite_M_ID = invite_M_ID;
        this.accept_M_ID = accept_M_ID;
    }

    public FriendListBean() {
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getInvite_M_ID() {
        return invite_M_ID;
    }

    public void setInvite_M_ID(String invite_M_ID) {
        this.invite_M_ID = invite_M_ID;
    }

    public String getInviteName() {
        return inviteName;
    }

    public void setInviteName(String inviteName) {
        this.inviteName = inviteName;
    }

    public String getAccept_M_ID() {
        return accept_M_ID;
    }

    public void setAccept_M_ID(String accept_M_ID) {
        this.accept_M_ID = accept_M_ID;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public int getFriend_Status() {
        return friend_Status;
    }

    public void setFriend_Status(int friend_Status) {
        this.friend_Status = friend_Status;
    }

    public Date getModify_Date() {
        return modify_Date;
    }

    public void setModify_Date(Date modify_Date) {
        this.modify_Date = modify_Date;
    }
}
