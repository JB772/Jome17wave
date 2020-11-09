package com.example.jome17wave;

import android.content.Context;
import android.util.Log;

import com.example.jome17wave.jome_Bean.FriendListBean;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.task.CommonTask;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

public class FcmSender {
    private static final String TAG = "FcmSender: ";
    private final String url = Common.URL_SERVER + "jome_member/FcmBasicServlet";
    private String titleStr = "";
    private String bodyStr = "";
    private String dataStr = "";
    private CommonTask fcmTask;

    /**
     * 將registration token傳送至server
     */
    public void sendTokenToServer(String token, Context context) {
        if (Common.networkConnected(context)) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "register");
            jsonObject.addProperty("registrationToken", token);
            String jsonOut = jsonObject.toString();
            CommonTask commonTask = new CommonTask(url, jsonOut);
            commonTask.execute();
        } else {
            Common.showToast(context, R.string.textNoNetwork);
        }
    }

    /**
     * 好友Fcm
     */
    public void friendFcmSender(Context context, FriendListBean relationBean) {
        if (Common.networkConnected(context)) {
            JomeMember mainMember = Common.getSelfFromPreference(context);
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "singleFcm");
            titleStr = "好友通知";
            if (mainMember.getMember_id().equals(relationBean.getInvite_M_ID())) {
                jsonObject.addProperty("memberId", relationBean.getAccept_M_ID());
                switch (relationBean.getFriend_Status()) {
                    case 1:
                        bodyStr = mainMember.getNickname() + "已同意你的好友邀請";
                        dataStr = "messageCenter";
                        break;

                    case 3:
                        bodyStr = mainMember.getNickname() + "發送好友邀請給你";
                        dataStr = "messageCenter";
                        break;

                    default:
                        return;
                }
            } else if (mainMember.getMember_id().equals(relationBean.getAccept_M_ID())) {
                jsonObject.addProperty("memberId", relationBean.getInvite_M_ID());
                switch (relationBean.getFriend_Status()) {
                    case 1:
                        bodyStr = mainMember.getNickname() + "已同意你的好友邀請";
                        dataStr = "messageCenter";
                        break;
                    case 3:
                        bodyStr = mainMember.getNickname() + "發送好友邀請給你";
                        dataStr = "messageCenter";
                        break;

                    default:
                        return;
                }
            }
            jsonObject.addProperty("title", titleStr);
            jsonObject.addProperty("body", bodyStr);
            jsonObject.addProperty("data", dataStr);
            fcmTask = new CommonTask(url, jsonObject.toString());
            try {
                String senderResult = fcmTask.execute().get();
                Log.d(TAG, senderResult);
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }


        }
    }

    /**
     * 揪團Fcm
     */
    public void groupFcmSender(Context context, PersonalGroupBean personGroup) {
        if (Common.networkConnected(context)) {
            JsonObject jsonObject = new JsonObject();
            if (personGroup.getRole() == 1) {
                jsonObject.addProperty("action", "groupFcm");
                jsonObject.addProperty("disGroupId", personGroup.getGroupId());
                titleStr = "解散通知";
                bodyStr = personGroup.getGroupName() + "揪團已解散";
            } else if (personGroup.getRole() == 2) {
                jsonObject.addProperty("action", "singleFcm");
                jsonObject.addProperty("memberId", personGroup.getMemberId());
                titleStr = "揪團通知";
                switch (personGroup.getAttenderStatus()) {
                    case 0:
                        bodyStr = personGroup.getNickname() + "已經離開" + personGroup.getGroupName() + "揪團";
                        break;
                    case 1:
                        bodyStr = personGroup.getNickname() + "成功加入" + personGroup.getGroupName() + "揪團";
                        break;
                    case 2:
                        bodyStr = personGroup.getNickname() + "沒有加入" + personGroup.getGroupName() + "揪團";
                        break;
                    case 3:
                        bodyStr = personGroup.getNickname() + "要求加入" + personGroup.getGroupName() + "揪團";
                        break;
                }
            }

            dataStr = "messageCenter";
            jsonObject.addProperty("title", titleStr);
            jsonObject.addProperty("body", bodyStr);
            jsonObject.addProperty("data", dataStr);
            Log.d(TAG, 128 + " :" + jsonObject.toString());
            fcmTask = new CommonTask(url, jsonObject.toString());
            try {
                String senderResult = fcmTask.execute().get();
                Log.d(TAG, senderResult);
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    /**
     * 評分Fcm
     */
    public void scoreFcmSender() {

    }
}
