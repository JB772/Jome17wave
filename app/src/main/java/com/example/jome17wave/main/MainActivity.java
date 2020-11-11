package com.example.jome17wave.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.jome17wave.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);


        //設定app在背景時收到FCM，會自動顯示notification（前景時則不會自動顯示）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT));
        }

        // 當notification被點擊時才會取得自訂資料
        //notification點擊開啟，開的是activity，開activity用intent()，intent可以帶bundle
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String data = bundle.getString("data");
            Log.d(TAG, "data :" + data);
            if (data.equals("messageCenter")){
                Navigation.findNavController(this, R.id.fragment).navigate(R.id.notificationFragment);
            }
        }else {
            Navigation.findNavController(this, R.id.fragment).navigate(R.id.mainFragment);
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        int messageType = getIntent().getIntExtra("message", 1);
//        if (messageType == 1){
//            Navigation.findNavController(this, R.id.fragment).navigate(R.id.notificationFragment);
//        }else {
//            Navigation.findNavController(this, R.id.fragment).navigate(R.id.mainFragment);
//        }
//    }

    @SuppressWarnings("deprecation")
    public static void generateNotification(Context context, String message) throws UnsupportedEncodingException {
        String messagepar = new String(message.getBytes(), "UTF-8");
        int icon = R.drawable.icon_awesome_home;
        long when = System.currentTimeMillis();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //代表fragment所綁定的activity，這個需要寫全路徑
        Intent intent = new Intent(context, MainActivity.class);

        //傳遞參數，然後根據參數進行判斷需要跳轉的fragment界面
        intent.putExtra("messageType", 1);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(false);
//        builder.setTicker("this is ticker text");
        builder.setContentTitle(context.getString(R.string.app_name));
//        builder.setContentText("You have a new message");
        builder.setSmallIcon(icon);
        builder.setContentIntent(pIntent);
        builder.setOngoing(true);
        builder.setSubText(messagepar);   //API level 16
        builder.setNumber(100);
        builder.build();

        Notification notification = builder.getNotification();
        int identify = (int) when;
        notificationManager.notify(identify, notification);


    }

}