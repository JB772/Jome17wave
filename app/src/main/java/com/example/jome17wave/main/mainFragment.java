package com.example.jome17wave.main;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.task.CommonTask;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;


public class mainFragment extends Fragment {
    private String TAG = "mainFragment";
    public static MainActivity activity;
    private CommonTask updateLatLngTask;
    private static final int REQ_LOGIN = 2;
    private static final int REQ_CHECK_SETTINGS = 101;
    private static final int PER_ACCESS_LOCATION = 201;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private MyLocationService myLocationService;
    private ServiceConnection serConnection = new serConnection();
    private boolean myBound = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
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
        Common.loginCheck(this, REQ_LOGIN);
    }

    private class serConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyLocationService.LocalBinder binder = (MyLocationService.LocalBinder) service;
            myLocationService = binder.getService();
            myBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBound = false;
        }
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
            //intent service開始抓位置
//            checkLocationSettings();
            Intent intent = new Intent(activity, MyLocationService.class);
            activity.startService(intent);

//            //onBindService開始抓位置
//        Intent myLocationSerIntent = new Intent(activity, MyLocationService.class);
//        activity.bindService(myLocationSerIntent, serConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PER_ACCESS_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//            textView.setText(R.string.textLocationAccessNotGrant);
        } else {
            Intent intent = new Intent(activity, MyLocationService.class);
            activity.startService(intent);
        }
    }




}