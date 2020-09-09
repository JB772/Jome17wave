package com.example.jome17wave.jome_message;

import java.io.Serializable;
import java.util.Date;

public class Notify implements Serializable {
    private int notificationId, type;
    private String notificationBody;
    private Date buildDate;

    public Notify() {
    }

    public Notify(int notificationId, int type, String notificationBody, Date buildDate) {
        this.notificationId = notificationId;
        this.type = type;
        this.notificationBody = notificationBody;
        this.buildDate = buildDate;
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

    public String getNotificationBody() {
        return notificationBody;
    }

    public void setNotificationBody(String notificationBody) {
        this.notificationBody = notificationBody;
    }

    public Date getBuildDate() {
        return buildDate;
    }

    public void setBuildDate(Date buildDate) {
        this.buildDate = buildDate;
    }
}
