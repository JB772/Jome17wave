package com.example.jome17wave.main;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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

public class MainActivity extends AppCompatActivity {
    private final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

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
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                NavController navController = Navigation.findNavController(this, R.id.fragment);
//                navController.navigate(R.id.notificationFragment);
                Navigation.findNavController(this, R.id.mainFragment).navigate(R.id.notificationFragment);
            }
        }else {
            NavController navController = Navigation.findNavController(this, R.id.fragment);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int messageType = getIntent().getIntExtra("message", 1);
        if (messageType != 0){
            Navigation.findNavController(this, R.id.mainFragment).navigate(R.id.notificationFragment);
        }
    }
}