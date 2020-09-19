package com.example.jome17wave;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.example.jome17wave.jome_loginRegister.LoginActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Common {

    private static final String TAG = "TAG_Common";
    //    public static String URL_SERVER = "http://192.168.196.189:8080/Spot_MySQL_Web/";
    public static String URL_SERVER = "http://10.0.2.2:8080/Jome17wave_Web/";

    public final static String PREF_FILE = "preference";





    /**
     * 檢查是否有網路連線
     */
    public static boolean networkConnected(Activity activity) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // API 23支援getActiveNetwork()
                Network network = connectivityManager.getActiveNetwork();
                // API 21支援getNetworkCapabilities()
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null) {
                    String msg = String.format(Locale.getDefault(),
                            "TRANSPORT_WIFI: %b%nTRANSPORT_CELLULAR: %b%nTRANSPORT_ETHERNET: %b%n",
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR),
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
                    Log.d(TAG, msg);
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                // API 29將NetworkInfo列為deprecated
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }
        }
        return false;
    }

    public static void showToast(Context context, int messageResId) {
        Toast.makeText(context, messageResId, Toast.LENGTH_SHORT).show();
    }

    public static String getYYYYmmDD(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formatTime = String.valueOf(sdf.format(date));
        return formatTime;
    }

    public static String getDateTimeId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSSS");
        String dateTimeId = String.valueOf(sdf.format(new Date()));
        return dateTimeId;
    }

//    檢驗是否登入，用在頁面onStart
    public static void loginCheck(Activity activity, int REQ_LOGIN){
        Intent loginIntend = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(loginIntend, REQ_LOGIN);
/**
 *      private static final int REQ_LOGIN = 2;
 */
    }

    //取用偏好設定檔
    public static SharedPreferences usePreferences(Activity activity, String prefName){
         SharedPreferences preferences = activity.getSharedPreferences(prefName, Context.MODE_PRIVATE);
         return preferences;
/**                            .edit()                   開始編輯
 *                           .putBoolean(key, value)
 *                           .putString(key, value)
 *                           .putString(key, value)
 *                               ...
 *                           .apply();                 完成編輯
 */

/**
 *                           .getString(key, value)     取用偏好設定檔
 */
    }
}


