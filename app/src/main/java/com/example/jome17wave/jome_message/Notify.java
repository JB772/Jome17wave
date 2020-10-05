package com.example.jome17wave.jome_message;

import java.io.Serializable;
import java.util.Date;

public class Notify implements Serializable{
    private int notificationId, type, notificationBody;
    private String memberId, notificationDetail;
    private Date buildDate;

    public Notify() {
        super();
    }

    public Notify(int notificationId, int type, int notificationBody, String memberId) {
        super();
        this.notificationId = notificationId;
        this.type = type;
        this.notificationBody = notificationBody;
        this.memberId = memberId;
    }



    public Notify(int notificationId, int type, int notificationBody, String memberId, String notificationDetail,
                  Date buildDate) {
        super();
        this.notificationId = notificationId;
        this.type = type;
        this.notificationBody = notificationBody;
        this.memberId = memberId;
        this.notificationDetail = notificationDetail;
        this.buildDate = buildDate;
    }

    public Notify(int notificationId, int type, int notificationBody, String memberId, String notificationDetail) {
        this.notificationId = notificationId;
        this.type = type;
        this.notificationBody = notificationBody;
        this.memberId = memberId;
        this.notificationDetail = notificationDetail;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(int notificationBody) {
        this.notificationBody = notificationBody;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }

    public String getNotificationDetail() {
        return notificationDetail;
    }

    public void setNotificationDetail(String notificationDetail) {
        this.notificationDetail = notificationDetail;
    }




}