package com.example.jome17wave.jome_loginRegister;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFragment";
    private LoginActivity activity;
    private ImageButton ibtLogin;
    private ImageButton ibtForgetPw;
    private ImageButton ibtRegister;
    private ImageButton ibtFbLogin;
    private ImageButton ibtGoogleLogin;
    private EditText etLoginAc;
    private EditText etLoginPw;
    private CommonTask loginTask;
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (LoginActivity) getActivity();
        activity.setResult(Activity.RESULT_CANCELED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ibtLogin = view.findViewById(R.id.ibtLogin);
        ibtForgetPw = view.findViewById(R.id.ibtforgetPw);
        ibtRegister = view.findViewById(R.id.ibtRegister);
        ibtFbLogin = view.findViewById(R.id.ibtFbLogin);
        ibtGoogleLogin = view.findViewById(R.id.ibtGoogleLogin);
        etLoginAc = view.findViewById(R.id.etLoginAc);
        etLoginPw = view.findViewById(R.id.etLoginPw);

        View.OnClickListener btOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.ibtforgetPw:
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgetPwFragment);
                        break;
                    case R.id.ibtRegister:
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerMemberFragment);
                        break;
                    case R.id.ibtGoogleLogin:
                        Log.d(TAG, "GoogleLogin");
                        break;
                    case R.id.ibtFbLogin:
                        Log.d(TAG, "FbLogin");
                        break;
                    case R.id.ibtLogin:
                        Log.d(TAG, "11111111111");
                        String account = etLoginAc.getText().toString().trim();
                        String password = etLoginPw.getText().toString().trim();
                        if (account.length()<=0 || password.length() <=0){
                            Common.showToast(activity, R.string.unuseAcOrPw);
                            return;
                        }
                        if (isAccountValid(account, password)){
                            //將登入驗証結果存偏好設定檔
                            preferences = activity.getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
                            //存入偏好檔
                            preferences.edit()
                                            .putBoolean("login", true)
                                            .putString("account", account)
                                            .putString("password", password)
                                            .apply();
                            //將登入回驗碼改為ok
                            activity.setResult(Activity.RESULT_OK);
                            activity.finish();
                        }else {
                            Common.showToast(activity, R.string.logiDeline);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        ibtLogin.setOnClickListener(btOnclick);
        ibtForgetPw.setOnClickListener(btOnclick);
        ibtRegister.setOnClickListener(btOnclick);
        ibtFbLogin.setOnClickListener(btOnclick);
        ibtGoogleLogin.setOnClickListener(btOnclick);
    }

    //Server驗証帳密
    private boolean isAccountValid(String account, String password) {
        String url = Common.URL_SERVER + "jome_member/LoginServlet";
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", "checkIsValid");
        jsonObject.addProperty("account", account);
        jsonObject.addProperty("password", password);
        loginTask = new CommonTask(url, jsonObject.toString());
        boolean isAccountValid = false;
        try {
            String jsonIn = loginTask.execute().get();
            jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
            isAccountValid = jsonObject.get("isAccountValid").getAsBoolean();
        } catch (InterruptedException e) {
            Log.d(TAG, e.toString());
        } catch (ExecutionException e) {
            Log.d(TAG, e.toString());
        }
        return isAccountValid;
    }

    @Override
    public void onStart() {
        super.onStart();
        preferences = activity.getSharedPreferences(Common.PREF_FILE, Context.MODE_PRIVATE);
        boolean login = preferences.getBoolean("login", false);
        if (login){
            //若偏好檔是記登錄，便送帳密再檢查，避免不同裝置改密碼
            String account = preferences.getString("account", "");
            String password = preferences.getString("password", "");
            if (isAccountValid(account, password)){
                activity.setResult(Activity.RESULT_OK);
                activity.finish();
            }else {
                preferences.edit().putBoolean("login", false).apply();
                Common.showToast(activity, R.string.logiDeline);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (loginTask != null){
            loginTask.cancel(true);
        }
    }
}