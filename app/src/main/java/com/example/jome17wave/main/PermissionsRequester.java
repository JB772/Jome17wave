package com.example.jome17wave.main;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.lang.ref.WeakReference;

final class PermissionsRequester {
    private static final String[] PERMISSIONS ={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private final WeakReference<Activity> activityWeakReference;

    static PermissionsRequester newInstance(@NonNull Activity activity) {
        WeakReference<Activity> activityWeakReference = new WeakReference<>(activity);
        return new PermissionsRequester(activityWeakReference);
    }

    private PermissionsRequester(@NonNull WeakReference<Activity> activityWeakReference) {
        this.activityWeakReference = activityWeakReference;
    }

    boolean hasPermissions() {
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            for (String permission : PERMISSIONS) {
                if (ActivityCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    void requestPermissions() {
        Activity activity = activityWeakReference.get();
        if (activity != null) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 0);
        }
    }

}
