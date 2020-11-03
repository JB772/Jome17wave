package com.example.jome17wave.main;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMessagingService ";
    @SuppressLint("LongLogTag")
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();
        String title = "";
        String body = "";
        if (notification != null){
            title = notification.getTitle();
            body = notification.getBody();
        }
        //取得自訂資料
        Map<String, String> map = remoteMessage.getData();
        String data = map.get("data");
        Log.d(TAG, "title: " + title + "\nbody: " + body + "\ndata: " + data);
    }

    /**
     *  當registration token更新時呼叫，將新的token傳送至server
     */
    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(@NonNull String tokenId) {
        Log.d( TAG, "onNewToken: " + tokenId);
        Common.sendTokenToServer(tokenId, this);
    }

    private void sendNotification(String title, String body){
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null){
            return;
        }
        String channelId = getString(R.string.default_notification_channel_id);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "channel name", NotificationManager.IMPORTANCE_DEFAULT);
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                                                .setSmallIcon(android.R.drawable.ic_dialog_info)
                                                .setContentTitle(title)
                                                .setContentText(body)
                                                .setAutoCancel(true)
                                                .setSound(defaultSoundUri)
                                                .setContentIntent(pendingIntent)
                                                .build();
        notificationManager.notify(0, notification);
    }
}
