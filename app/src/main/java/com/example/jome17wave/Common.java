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

import androidx.fragment.app.Fragment;

import com.example.jome17wave.jome_loginRegister.LoginActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    public static boolean networkConnected(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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

    public static String date2Str (Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return String.valueOf(sdf.format(date));
    }

    public static String date2StrHm (Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return String.valueOf(sdf.format(date));
    }

    public static Date str2Date(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = sdf.parse(dateString);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getGroupEndTime(String assembleStr){
        Date assembleTime = str2Date(assembleStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(assembleTime);
        calendar.add(Calendar.HOUR, 2);
        return date2StrHm (calendar.getTime());
    }

    public static String getSignUpEnd(String assembleStr){
        Date assembleTime = str2Date(assembleStr);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(assembleTime);
        calendar.add(Calendar.MINUTE, -30);
        return date2StrHm(calendar.getTime());
    }

    public static String getDateTimeId(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSSSS");
        String dateTimeId = String.valueOf(sdf.format(new Date()));
        return dateTimeId;
    }


//    檢驗是否登入，用在頁面onStart
    public static void loginCheck(Fragment fragment, int REQ_LOGIN){
        Intent loginIntend = new Intent(fragment.getActivity(), LoginActivity.class);
        fragment.startActivityForResult(loginIntend, REQ_LOGIN);
        
/**
 *      private static final int REQ_LOGIN = 2;
 */
    }

    //取用偏好設定檔
    public static SharedPreferences usePreferences(Context context, String prefName){
         SharedPreferences preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
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


