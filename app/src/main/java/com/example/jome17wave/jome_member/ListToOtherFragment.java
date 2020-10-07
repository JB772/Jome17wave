package com.example.jome17wave.jome_member;

import android.app.Activity;
import android.os.Bundle;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

public class ListToOtherFragment extends Fragment {
        private String TAG = "ListToOtherFragment";
        private MainActivity activity;
        private View fragment;
        private CommonTask friendTask;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list_to_other, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fragment = view.findViewById(R.id.fragment6);
        Bundle bundle = getArguments();
        if (bundle != null) {
            JomeMember friend = (JomeMember) bundle.getSerializable("friend");
            if (friend != null) {
                String friendId = friend.getMember_id();
                String maiMemberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
                String mainId = new Gson().fromJson(maiMemberStr, JomeMember.class).getMember_id();
                JsonObject jsonObject = new JsonObject();
                String jsonIn = "";
                if (Common.networkConnected(activity)){
                    String url = Common.URL_SERVER + "jome_member/LoginServlet";
                    try {
                        jsonObject.addProperty("action", "idGet");
                        jsonObject.addProperty("mainId", mainId);
                        jsonObject.addProperty("friendId", friendId);
                        friendTask = new CommonTask(url, jsonObject.toString());
                        jsonIn = friendTask.execute().get();
                        saveFriendList_getFileDir("otherMember", jsonIn);
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }



        }
    }

    private void saveFriendList_getFileDir(String fileName, String jsonIn){
        File file = new File(activity.getFilesDir(), fileName);
        try {
            FileOutputStream fileOutput = new FileOutputStream(file);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(jsonIn);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(friendTask != null){
            friendTask.cancel(true);
            friendTask = null;
        }
        activity.deleteFile("otherMember");
        activity.deleteFile("OMProfile");
    }
}