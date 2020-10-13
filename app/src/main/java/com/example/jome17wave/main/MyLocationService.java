package com.example.jome17wave.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceFragment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jome17wave.Common;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.task.CommonTask;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

public class MyLocationService extends Service {
    private final String TAG = "MyLocationService";
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private PowerManager.WakeLock wakeLock;
    private CommonTask updateLatLngTask;


    @Override
    public void onCreate() {
        super.onCreate();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                //每十秒抓一次位置，用秒來計很花耗電，盡量不要用秒來計
                .setInterval(3000)//單位毫秒
                //至少要多遠才算發生位移，越大越省電
                .setSmallestDisplacement(300);//單位公R

        locationCallback = new LocationCallback() {
            //十秒鐘抓一次位置與手機內存的最後一次位置資料比較，有發生位移才會呼叫onLocationResult()，重新抓資料來刷畫面
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
                updateLastLocationInfo(lastLocation);
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "MyLocationService");
        checkLocationSettings();
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "MyLocationService.onBind: here");
        checkLocationSettings();
        return new LocalBinder();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("InvalidWakeLockTag")
    private void acquireWakeLock(){
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if (powerManager != null && wakeLock == null){
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MyWakLock");
            // 提供timeout時間以避免事情做完了還佔用著wakelock，一般設10分鐘，如果10沒鐘後背景程式沒做事就還CPU，如果有在做就繼續
            wakeLock.acquire(10 * 60 * 1000);
            Log.d(TAG, "acquireWakeLock");
        }
    }

    private void releaseWakeLock(){
        if(wakeLock != null){
            wakeLock.release();
            wakeLock = null;
        }
    }

    public class LocalBinder extends Binder{
        MyLocationService getService() {
            return MyLocationService.this;
        }
    }
    //開新的執行緒
    // 開啟執行緒

    // 檢查裝置是否開啟Location設定
    private void checkLocationSettings() {
        // 必須將LocationRequest設定加入檢查
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // 取得並顯示最新位置
                showLastLocation();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    Log.e(TAG, e.getMessage());
//                    try {
//                        ResolvableApiException resolvable = (ResolvableApiException) e;
//                        // 跳出Location設定的對話視窗
//                        resolvable.startResolutionForResult(activity, REQ_CHECK_SETTINGS);
//                    } catch (IntentSender.SendIntentException sendEx) {
//                        Log.e(TAG, e.getMessage());
//                    }
                }
            }
        });
    }

    private void showLastLocation() {
        if (fusedLocationClient == null) {
            //getFusedLocationProviderClient()取得Client物件，Client端物件就是手機物件，
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            //檢查permission是否有同意定位這件事情。
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED) {
//                textView.setText(R.string.textLocationAccessNotGrant);
                //不同意就return
                return;
            }
            //用存著結果的Task的泛型指定型
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();
                        Log.d(TAG, "Lat :"+String.valueOf(lastLocation.getLatitude()));
                        updateLastLocationInfo(lastLocation);
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }

    private void updateLastLocationInfo(Location lastLocation){
        Log.d(TAG, lastLocation.toString());
        String url = Common.URL_SERVER + "jome_member/LoginServlet";
        if (Common.networkConnected(this)) {
            String memberStr = Common.usePreferences(this, Common.PREF_FILE).getString("loginMember", "");
            JomeMember mainMember = new Gson().fromJson(memberStr, JomeMember.class);
            mainMember.setLatitude(lastLocation.getLatitude());
            mainMember.setLongitude(lastLocation.getLongitude());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "update");
            jsonObject.addProperty("memberUp", new Gson().toJson(mainMember));
            jsonObject.addProperty("imageBase64", "noImage");
            String jsonIn = "";
            updateLatLngTask = new CommonTask(url, jsonObject.toString());
            try {
                jsonIn = updateLatLngTask.execute().get();
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
            int resultCode = jsonObject.get("resultCode").getAsInt();
            if (resultCode != 1) {
                /*
                網路有問題
                 */
            }
        }
    }
}
