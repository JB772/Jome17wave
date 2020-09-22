package com.example.jome17wave.jome_message;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.jome17wave.Common;
import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FindNewFriendFragment extends Fragment {
    private static final String TAG = "TAG_FindNewFriendFragment";
    private MainActivity activity;
    private CommonTask searchAccountTask, addNewFriendTask;
    private List<ImageTask> imageTasks;
    private EditText etFindNewFriend;
    private Button btSearch, btAddNewFriend;
    private ConstraintLayout constraintLayoutProfile, constrainLayoutSearch;
    private ImageView ivOtherProfileImg;
    private TextView tvOtherName;
    private FindNewFriend addNewFriend;
    private JomeMember newFriend;
    private String friendRelation;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        setHasOptionsMenu(true);
        imageTasks = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_find_new_friend, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.findNewFriendTitle);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //左上角返回箭頭

        etFindNewFriend = view.findViewById(R.id.etFindNewFriend);
        btSearch = view.findViewById(R.id.btSearch);
        ivOtherProfileImg = view.findViewById(R.id.ivOtherProfileImg);
        tvOtherName = view.findViewById(R.id.tvOtherName);
        btAddNewFriend = view.findViewById(R.id.btAddNewFriend);
        constrainLayoutSearch = view.findViewById(R.id.constrainLayoutSearch);
        constraintLayoutProfile = view.findViewById(R.id.constraintLayoutProfile);

        final String account = etFindNewFriend.getText().toString();
        FindNewFriend findNewFriend = new FindNewFriend();
        findNewFriend.setMemberAccount(account);
        constraintLayoutProfile.setVisibility(View.GONE);

//        if (account == null){
//            constraintLayoutProfile.setVisibility(View.GONE);
//        }

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account == null){
                    Common.showToast(activity, R.string.no_information_found);
                }else {
                    newFriend = getStranger();
                    showNewFriendProfile(newFriend);
                }
            }
        });



    }

    private void showNewFriendProfile(JomeMember newFriend) {
        int imageSize;
        imageSize = getResources().getDisplayMetrics().widthPixels/4;
        final FindNewFriend findNewFriend = new FindNewFriend();
        if (newFriend == null){
            Common.showToast(activity, R.string.no_information_found);
        }else{
            constraintLayoutProfile.setVisibility(View.VISIBLE);
            String url = Common.URL_SERVER + "FindNewFriendServlet";
            int id = findNewFriend.getMemberId();
            ImageTask imageTask = new ImageTask(url, id, imageSize, ivOtherProfileImg);
            imageTask.execute();
            imageTasks.add(imageTask);
            tvOtherName.setText(findNewFriend.getNickName());

            switch (friendRelation){
                case "insert":
                    //顯示insert按鈕
                    break;
                case "wasFriend":
                    //顯示 “已成為好友”
                    break;
                case "pedding":
                    //顯示 “等待回覆中”
                    break;
                case "response":
                    //顯示 同意/拒絕 按鈕
                    break;
            }


            btAddNewFriend.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View v) {
                    String url = Common.URL_SERVER + "FindNewFriendServlet";
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "addNewFriend");
                    jsonObject.addProperty("addNewFriend",findNewFriend.getMemberId());
                    String jsonOut = jsonObject.toString();
                    addNewFriendTask = new CommonTask(url, jsonOut);
                    try {
                        String jsonIn = addNewFriendTask.execute().get();
                        Type listType = new TypeToken<FindNewFriend>(){}.getType();
                        addNewFriend = new Gson().fromJson(jsonIn, listType);
                        if (findNewFriend.getFriendStatus() == 3){
                            Common.showToast(activity, R.string.friend_invitation_send);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }

                }
            });
        }

    }

    @SuppressLint("LongLogTag")
    private JomeMember getStranger() {
        FindNewFriend findNewFriend = new FindNewFriend();
        if (Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "FindNewFriendServlet";
            JsonObject jsonObject = new JsonObject();

            String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            JomeMember member = new Gson().fromJson(memberStr,JomeMember.class);
            String inviteId = member.getMember_id();
            if (inviteId != null) {
                jsonObject.addProperty("action", "searchMember");
                jsonObject.addProperty("account", findNewFriend.getMemberAccount());
                jsonObject.addProperty("inviteId", inviteId);
            }
            String jsonOut = jsonObject.toString();
            searchAccountTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = searchAccountTask.execute().get();
                Type listType = new TypeToken<FindNewFriend>() {}.getType();
                jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
                newFriend = new Gson().fromJson(jsonObject.get("theStranger").getAsString(), JomeMember.class);
                friendRelation = jsonObject.get("friendRelation").getAsString();

            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
        }else {
            Common.showToast(activity, R.string.no_network_connection_available);
        }
        return newFriend;
    }



    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.find_new_friend, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(etFindNewFriend).popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}