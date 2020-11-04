package com.example.jome17wave;

import android.content.Context;

import com.example.jome17wave.jome_Bean.FriendListBean;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.task.CommonTask;
import com.google.gson.JsonObject;

public class FcmSender {

    /**
     * 將registration token傳送至server
     */
    public void sendTokenToServer(String token, Context context) {
        if (Common.networkConnected(context)) {
            String url = Common.URL_SERVER + "jome_member/FcmBasicServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "register");
            jsonObject.addProperty("registrationToken", token);
            String jsonOut = jsonObject.toString();
            CommonTask commonTask  = new CommonTask(url, jsonOut);
            commonTask.execute();
        } else {
            Common.showToast(context, R.string.textNoNetwork);
        }
    }

    /**
     *  好友Fcm
     */
    public void friendFcmSender(Context context, FriendListBean relationBean){
        if (Common.networkConnected(context)){
            String url = Common.URL_SERVER + "jome_member/FcmBasicServlet";
            JomeMember mainMember = Common.getSelfFromPreference(context);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "singleFcm");
            jsonObject.addProperty("title", "好友通知");
            if (mainMember.getMember_id().equals(relationBean.getInvite_M_ID())){
                String bodyStr = " ";
                jsonObject.addProperty("memberId", relationBean.getAccept_M_ID());
                switch (relationBean.getFriend_Status()){
                    case 1:
                        bodyStr = mainMember.getNickname() + "已同意你的好友邀請";
                        break;

                    case 3:
                        bodyStr = mainMember.getNickname() + "發送好友邀請給你";
                        break;
                }
                jsonObject.addProperty("body", bodyStr);
            }




        }
    }

    /**
     * 揪團Fcm
     */
    public void groupFcmSender(){

    }

    /**
     * 評分Fcm
     */
    public void scoreFcmSender(){

    }
}
