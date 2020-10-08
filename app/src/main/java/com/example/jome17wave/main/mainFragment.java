package com.example.jome17wave.main;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_loginRegister.LoginActivity;
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
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;


public class mainFragment extends Fragment {
    private String TAG = "mainFragment";
    private MainActivity activity;
    private CommonTask updateLatLngTask;
    private static final int REQ_LOGIN = 2;
    private static final int REQ_CHECK_SETTINGS = 101;
    private static final int PER_ACCESS_LOCATION = 201;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                //每十秒抓一次位置，用秒來計很花耗電，盡量不要用秒來計
                .setInterval(5000)//單位毫秒
                //至少要多遠才算發生位移，越大越省電
                .setSmallestDisplacement(100);//單位公R

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getActivity().setTitle("首頁");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        Common.loginCheck(activity, REQ_LOGIN);
//        Intent loginIntend = new Intent(activity, LoginActivity.class);
//        activity.startActivityForResult(loginIntend, REQ_LOGIN);
//        askAccessLocationPermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN){
            if (resultCode == Activity.RESULT_OK){
                Log.d(TAG, "onActivityResult");
                askAccessLocationPermission();
            }
        }
    }
    // 請求user同意定位
    private void askAccessLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int result = ActivityCompat.checkSelfPermission(activity, permissions[0]);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, PER_ACCESS_LOCATION);
        }else {
            checkLocationSettings();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PER_ACCESS_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//            textView.setText(R.string.textLocationAccessNotGrant);
        }
    }

    // 檢查裝置是否開啟Location設定
    private void checkLocationSettings() {
        // 必須將LocationRequest設定加入檢查
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(activity)
                        .checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
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
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        // 跳出Location設定的對話視窗
                        resolvable.startResolutionForResult(activity, REQ_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private void showLastLocation() {
        if (fusedLocationClient == null) {
            //getFusedLocationProviderClient()取得Client物件，Client端物件就是手機物件，
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            //檢查permission是否有同意定位這件事情。
            if (ActivityCompat.checkSelfPermission(activity,
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
                        updateLastLocationInfo(lastLocation);
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }

    private void updateLastLocationInfo(Location lastLocation) {
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
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
            int resultCode = -1;
            resultCode = jsonObject.get("resultCode").getAsInt();
            if (resultCode != 1) {
                /*
                網路有問題
                 */
            }
        }

    }


}