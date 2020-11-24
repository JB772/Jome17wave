package com.example.jome17wave.jome_message;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.jome17wave.Common;
import com.example.jome17wave.FcmSender;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.FriendListBean;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.ImageTask;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class FindNewFriendFragment extends Fragment {
    private static final String TAG = "TAG_FindNewFriend";
    private MainActivity activity;
    private CommonTask searchAccountTask, addNewFriendTask, agreeFriendTask, declineFriendTask;
    private List<ImageTask> imageTasks;
    private EditText etFindNewFriend;
    private Button btSearch, btAddNewFriend;
    private ConstraintLayout constraintLayoutProfile, constrainLayoutSearch;
    private ImageView ivOtherProfileImg;
    private TextView tvOtherName, tvWasFriend;
    private ImageButton ibtAgree,ibtDecline;
    private FriendListBean friendListBean = new FriendListBean();
    private FindNewFriend addNewFriend;
    private JomeMember newFriend;
    private String friendRelation;
    private Bitmap bitmap = null;
    private LinearLayout answerLinearLayout;


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

    @SuppressLint("LongLogTag")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.findNewFriendTitle);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //左上角返回箭頭

        etFindNewFriend = view.findViewById(R.id.etFindStranger);
        btSearch = view.findViewById(R.id.btSearch);
        ivOtherProfileImg = view.findViewById(R.id.ivOtherProfileImg);
        tvOtherName = view.findViewById(R.id.tvOtherName);
        btAddNewFriend = view.findViewById(R.id.btAddNewFriend);
        tvWasFriend = view.findViewById(R.id.tvWasFriend);
        ibtAgree = view.findViewById(R.id.ibtAgree);
        ibtDecline = view.findViewById(R.id.ibtDecline);
        answerLinearLayout = view.findViewById(R.id.answerLinearLayout);

        constrainLayoutSearch = view.findViewById(R.id.constrainLayoutSearch);
        constraintLayoutProfile = view.findViewById(R.id.constraintLayoutProfile);

        constraintLayoutProfile.setVisibility(View.GONE);

//        if (account == null){
//            constraintLayoutProfile.setVisibility(View.GONE);
//        }

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etFindNewFriend.getText().toString().trim();
                Log.d(TAG, "account: " + account);
                FindNewFriend findNewFriend = new FindNewFriend();
                findNewFriend.setMemberAccount(account);
                Log.d(TAG, "setMemberAccount: " + account);
                if (account.isEmpty()){
                    Common.showToast(activity, R.string.no_information_found);
                }else {
                    newFriend = getStranger(findNewFriend);
                    showNewFriendProfile(newFriend);

                }
            }
        });

        //點擊 “加入好友”按鈕
        btAddNewFriend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View v) {
                String url = Common.URL_SERVER + "FriendServlet";
                JsonObject jsonObject = new JsonObject();
                String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
                JomeMember member = new Gson().fromJson(memberStr,JomeMember.class);
//                    String inviteId = member.getMember_id();
//                Log.d(TAG, "inviteId: " + member.getMember_id());
                friendListBean.setInvite_M_ID(member.getMemberId());
                friendListBean.setAccept_M_ID(newFriend.getMemberId());
                friendListBean.setFriend_Status(3);

                jsonObject.addProperty("action", "addNewFriend");
                jsonObject.addProperty("addNewFriend", new Gson().toJson(friendListBean));
                jsonObject.addProperty("friendId", "");
                String jsonOut = jsonObject.toString();
                addNewFriendTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = addNewFriendTask.execute().get();
                    JsonObject jo = new Gson().fromJson(jsonIn, JsonObject.class);
                    int resultCode = jo.get("resultCode").getAsInt();
//                    Log.d(TAG, "resultCode: " + resultCode);
//                    friendListBean = new Gson().fromJson(jo.get("FriendListBean").getAsString(), FriendListBean.class);
                    if (resultCode == 1){
                        Log.d(TAG, "if resultCode == 1 ");
                        Common.showToast(activity, R.string.friend_invitation_send);
                        btAddNewFriend.setVisibility(View.GONE);
                        tvWasFriend.setText(R.string.pedding);
                        tvWasFriend.setVisibility(View.VISIBLE);

                        //發送FCM
                        FriendListBean afterRelation = new Gson().fromJson(jo.get("afterRelation").getAsString(), FriendListBean.class);
                        FcmSender fcmSender = new FcmSender();
                        fcmSender.friendFcmSender(activity, afterRelation);

                    }else {
                        Common.showToast(activity, R.string.friend_invitation_fail);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        });

        //點擊 ”同意“按鈕
        ibtAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Common.URL_SERVER + "FriendServlet";
                JsonObject jsonObject = new JsonObject();
                String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
                JomeMember member = new Gson().fromJson(memberStr,JomeMember.class);
                friendListBean.setInvite_M_ID(newFriend.getMemberId());
                friendListBean.setAccept_M_ID(member.getMemberId());
//                friendListBean.setFriend_Status(3);
                jsonObject.addProperty("action", "clickAgree");
                jsonObject.addProperty("agreeBean", new Gson().toJson(friendListBean));
                jsonObject.addProperty("friendId", "");
                String jsonOut = jsonObject.toString();
                agreeFriendTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = agreeFriendTask.execute().get();
                    JsonObject jo = new Gson().fromJson(jsonIn, JsonObject.class);
                    int resultCode = jo.get("resultCode").getAsInt();
//                    Log.d(TAG, "resultCode: " + resultCode);
//                    friendListBean = new Gson().fromJson(jo.get("FriendListBean").getAsString(), FriendListBean.class);
                    if (resultCode == 1){
//                        Log.d(TAG, "if resultCode == 1 ");
                        Common.showToast(activity, R.string.was_friend);
//                        ibtAgree.setVisibility(View.GONE);
//                        ibtDecline.setVisibility(View.GONE);
                        answerLinearLayout.setVisibility(View.GONE);
                        tvWasFriend.setVisibility(View.VISIBLE);

                        //發送FCM
                        FriendListBean afterRelation = new Gson().fromJson(jo.get("afterRelation").getAsString(), FriendListBean.class);
                        Log.d(TAG, "afterRelation: " + afterRelation);
                        FcmSender fcmSender = new FcmSender();
                        fcmSender.friendFcmSender(activity, afterRelation);

                    }else {
                        Common.showToast(activity, R.string.change_fail);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        });

        //點擊 “拒絕”按鈕
        ibtDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Common.URL_SERVER + "FriendServlet";
                JsonObject jsonObject = new JsonObject();
                String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
                JomeMember member = new Gson().fromJson(memberStr,JomeMember.class);
                friendListBean.setInvite_M_ID(member.getMemberId());
                friendListBean.setAccept_M_ID(newFriend.getMemberId());
                friendListBean.setFriend_Status(3);
                jsonObject.addProperty("action", "clickDecline");
                jsonObject.addProperty("declineBean", new Gson().toJson(friendListBean));
                jsonObject.addProperty("friendId", "");
                String jsonOut = jsonObject.toString();
                declineFriendTask = new CommonTask(url, jsonOut);
                try {
                    String jsonIn = declineFriendTask.execute().get();
                    JsonObject jo = new Gson().fromJson(jsonIn, JsonObject.class);
                    int resultCode = jo.get("resultCode").getAsInt();
//                    Log.d(TAG, "resultCode: " + resultCode);
//                    friendListBean = new Gson().fromJson(jo.get("FriendListBean").getAsString(), FriendListBean.class);
                    if (resultCode == 1){
//                        Log.d(TAG, "if resultCode == 1 ");
                        Common.showToast(activity, R.string.decline_friend);
//                        ibtAgree.setVisibility(View.GONE);
//                        ibtDecline.setVisibility(View.GONE);
                        answerLinearLayout.setVisibility(View.GONE);
                        btAddNewFriend.setVisibility(View.VISIBLE);
//                        tvWasFriend.setText(R.string.decline_friend);
//                        tvWasFriend.setVisibility(View.VISIBLE);
                    }else {
                        Common.showToast(activity, R.string.change_fail);
                    }
                } catch (Exception e) {
                    Log.d(TAG, e.toString());
                }
            }
        });


    }

    @SuppressLint("ResourceAsColor")
    private void showNewFriendProfile(final JomeMember newFriend) {
        int imageSize;
        imageSize = getResources().getDisplayMetrics().widthPixels/4;
        final FindNewFriend findNewFriend = new FindNewFriend();
        if (newFriend == null){
            Common.showToast(activity, R.string.no_information_found);
        }else{
            constraintLayoutProfile.setVisibility(View.VISIBLE);
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            String memberID = newFriend.getMemberId();
            try {
                bitmap = new MemberImageTask(url, memberID, imageSize).execute().get();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }
            if (bitmap == null){
                ivOtherProfileImg.setImageResource(R.drawable.no_image);
            }else {
                ivOtherProfileImg.setImageBitmap(bitmap);
            }
//            ImageTask imageTask = new ImageTask(url, id, imageSize, ivOtherProfileImg);
//            imageTasks.add(memberImageTask);
            tvOtherName.setText(newFriend.getNickname());

            Log.d(TAG, "showNewFriendProfile裡的friendRelation: "+friendRelation);
            switch (friendRelation){
                case "insert":
                    //顯示“加入好友”按鈕
                    btAddNewFriend.setVisibility(View.VISIBLE);
                    tvWasFriend.setVisibility(View.GONE);
                    answerLinearLayout.setVisibility(View.GONE);
//                    ibtAgree.setVisibility(View.GONE);
//                    ibtDecline.setVisibility(View.GONE);
                    break;
                case "wasFriend":
                    //顯示 “已成為好友”
                    btAddNewFriend.setVisibility(View.GONE);
                    tvWasFriend.setVisibility(View.VISIBLE);
                    answerLinearLayout.setVisibility(View.GONE);
//                    ibtAgree.setVisibility(View.GONE);
//                    ibtDecline.setVisibility(View.GONE);
                    break;
                case "pedding":
                    //顯示 “等待回覆中”
                    btAddNewFriend.setVisibility(View.GONE);
                    tvWasFriend.setText(R.string.pedding);
                    tvWasFriend.setVisibility(View.VISIBLE);
                    answerLinearLayout.setVisibility(View.GONE);
//                    ibtAgree.setVisibility(View.GONE);
//                    ibtDecline.setVisibility(View.GONE);
                    break;
                case "response":
                    //顯示 同意/拒絕 按鈕
                    btAddNewFriend.setVisibility(View.GONE);
                    tvWasFriend.setVisibility(View.GONE);
                    answerLinearLayout.setVisibility(View.VISIBLE);
//                    ibtAgree.setVisibility(View.VISIBLE);
//                    ibtDecline.setVisibility(View.VISIBLE);
                    break;
                case "myself":
                    btAddNewFriend.setVisibility(View.GONE);
                    tvWasFriend.setText(R.string.myself);
//                    tvWasFriend.setBackgroundColor(R.color.colorWhite);
//                    tvWasFriend.setTextColor(R.color.colorPurple);
                    tvWasFriend.setVisibility(View.VISIBLE);
                    answerLinearLayout.setVisibility(View.GONE);
                    break;
            }
        }
    }

    @SuppressLint("LongLogTag")
    private JomeMember getStranger(FindNewFriend findNewFriend) {
        if (Common.networkConnected(activity)){
            String url = Common.URL_SERVER + "FriendServlet";
            JsonObject jsonObject = new JsonObject();

            String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            JomeMember member = new Gson().fromJson(memberStr,JomeMember.class);
            String inviteId = member.getMemberId();
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
                Log.d(TAG, "getStranger裡的friendRelation: "+friendRelation);

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