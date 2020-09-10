package com.example.jome17wave.jome_member;

import java.io.Serializable;

public class Friend implements Serializable {
    //    private Bitmap imageFriend;
    private int imageFriendId;
    private String nameFriend;

    public Friend() {
    }

    public Friend(int imageFriendId, String nameFriend) {
        this.imageFriendId = imageFriendId;
        this.nameFriend = nameFriend;
    }

    //    public Bitmap getImageFriend() {
//        return imageFriend;
//    }
//
//    public void setImageFriend(Bitmap imageFriend) {
//        this.imageFriend = imageFriend;
//    }


    public int getImageFriendId() {
        return imageFriendId;
    }

    public void setImageFriendId(int imageFriendId) {
        this.imageFriendId = imageFriendId;
    }

    public String getNameFriend() {
        return nameFriend;
    }

    public void setNameFriend(String nameFriend) {
        this.nameFriend = nameFriend;
    }

}
