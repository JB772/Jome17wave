package com.example.jome17wave.jome_message;

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
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.GroupImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

public class GroupDetailFragment extends Fragment {
    private static final String TAG = "TAG_GroupDetail";
    private MainActivity activity;
    private ImageView ivGroup;
    private TextView tvCaptain, tvGroupName, tvGroupDate,
            tvGroupLocation, tvGroupLimit, tvGroupGender, tvGroupMemo, tvWord;
    private Button btGroupTeam, btGroupSetting;
    private LinearLayout llButton;
    private ConstraintLayout clGroupMemo;
    private Bitmap bitmap;
    private CommonTask joinGroupTask, getMyGroupTask;
    private PersonalGroupBean myGroup, groupBean;
    private JomeMember selfMember;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_group_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.groupDetail);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //左上角返回箭頭

        ivGroup = view.findViewById(R.id.ivGroup);
        tvCaptain = view.findViewById(R.id.tvCaptain);
        tvGroupName = view.findViewById(R.id.tvGroupName);
        tvGroupDate = view.findViewById(R.id.tvGroupDate);
        tvGroupLocation = view.findViewById(R.id.tvGroupLocation);
        tvGroupLimit = view.findViewById(R.id.tvGroupLimit);
        tvGroupGender = view.findViewById(R.id.tvGroupGender);
        tvGroupMemo = view.findViewById(R.id.tvGroupMemo);
        tvWord = view.findViewById(R.id.tvWord);

        btGroupTeam = view.findViewById(R.id.btGroupTeam);
        btGroupSetting = view.findViewById(R.id.btGroupSetting);

        llButton = view.findViewById(R.id.llButton);
        clGroupMemo = view.findViewById(R.id.clGroupMemo);
//        clGroupMemo.setVisibility(View.GONE);

        showGroupDetail();

        //點擊 “查看團員”按鈕
        btGroupTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("groupData", myGroup);
                Navigation.findNavController(v)
                        .navigate(R.id.action_fragmentGroupDetail_to_groupAttenderFragment, bundle);
            }
        });


        //點擊 “修改揪團”按鈕
        //點擊 “申請加入”按鈕
        btGroupSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btGroupSetting.getText() == "修改揪團"){
                    //點擊 “修改揪團”按鈕
                    Bundle bundle = getArguments();
                    Navigation.findNavController(v)
                            .navigate(R.id.action_friendInvitationFragment_to_findNewFriendFragment, bundle);
                    Log.d(TAG, "action箭頭指向錯誤：要去修改揪團才對啦");

                }else if(v.getId() == R.id.btGroupSetting) {
                    //點擊 “申請加入”按鈕
                    String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
                    JsonObject jsonObject = new JsonObject();
                    groupBean.setMemberId(selfMember.getMemberId());
                    groupBean.setRole(2);
                    groupBean.setAttenderStatus(3);

                    jsonObject.addProperty("action", "joinGroup");
                    jsonObject.addProperty("groupBean", new Gson().toJson(groupBean));
                    joinGroupTask = new CommonTask(url, jsonObject.toString());
                    try {
                        String jsonIn = joinGroupTask.execute().get();
                        jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
                    } catch (Exception e) {
                        Log.d(TAG, e.toString());
                    }
                    int resultCode = jsonObject.get("joinResult").getAsInt();
                    if (resultCode == 1){
                        llButton.setVisibility(View.GONE);
                        tvWord.setText("揪團申請審核中");
                        tvWord.setVisibility(View.VISIBLE);
                        Common.showToast(activity, R.string.change_successful);
                        FcmSender fcmSender = new FcmSender();
                        String groupHeadId = jsonObject.get("groupHeadId").getAsString();
                        PersonalGroupBean fcmGroup = new PersonalGroupBean();
                        fcmGroup.setMemberId(groupHeadId);
                        fcmGroup.setRole(2);
                        fcmGroup.setAttenderStatus(3);
                        fcmGroup.setNickname(selfMember.getNickname());
                        fcmGroup.setGroupName(groupBean.getGroupName());
                        fcmSender.groupFcmSender(activity, fcmGroup);
                    }else {
                        Common.showToast(activity, R.string.change_fail);
                    }
                }else {
                    //  動作失效
                    Common.showToast(activity,R.string.change_fail);
                }
            }
        });

    }

    private void showGroupDetail() {
        Bundle bundle = getArguments();
        if (bundle != null){
            Log.d(TAG, "bundle is not null~~~~~~~~~~~~~~~");
            groupBean = (PersonalGroupBean)bundle.getSerializable("newGroup");
        }else {
            Log.d(TAG, "bundle is null~~~~~~~~~~~~~~~");
            return;
        }
//            Log.d(TAG,"bundleGroupBean: " +  groupBean.getMemberId());
            String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
            int imageSize = getResources().getDisplayMetrics().widthPixels / 2;


            if(Common.networkConnected(activity)){
                try {
                    bitmap = new GroupImageTask(url, groupBean.getGroupId(), imageSize).execute().get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                Log.d(TAG, "bitmap2: " + bitmap);
                if (bitmap == null){
                    ivGroup.setImageResource(R.drawable.no_image);
                }else {
                    ivGroup.setImageBitmap(bitmap);
                }

                JsonObject jsonObject = new JsonObject();
                selfMember = Common.getSelfFromPreference(activity);
                String myMemberId = selfMember.getMemberId();

                if (myMemberId != null) {
                    jsonObject.addProperty("action", "getMyGroup");
                    jsonObject.addProperty("memberId", myMemberId);
                    jsonObject.addProperty("groupId", groupBean.getGroupId());
                }
                String jsonOut = jsonObject.toString();
                Log.d(TAG, "jsonOut193: " + jsonOut);
//                String jsonOut = new Gson().toJson(jsonObject);
                getMyGroupTask = new CommonTask(url, jsonOut);
                try {
                    String inStr = getMyGroupTask.execute().get();
                    Log.d(TAG, "inStr: " + inStr);
                    JsonObject jsonIn = new Gson().fromJson(inStr, JsonObject.class);
                    myGroup = new Gson().fromJson(jsonIn.get("myGroup").getAsString(), PersonalGroupBean.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else {
                ivGroup.setImageResource(R.drawable.no_image);
                Common.showToast(activity, R.string.no_network_connection_available);
            }



            tvCaptain.setText(groupBean.getNickname());
            tvGroupName.setText(groupBean.getGroupName());
            tvGroupDate.setText(groupBean.getAssembleTime());
            tvGroupLocation.setText(groupBean.getSurfName());
            tvGroupLimit.setText(String.valueOf(groupBean.getGroupLimit())+ " 人" );

            tvGroupGender.setVisibility(View.GONE);
//            tvGroupGender.setText(groupBean.getGender());

            //備註內文
            if (groupBean.getNotice() != ""){
                clGroupMemo.setVisibility(View.VISIBLE);
                tvGroupMemo.setText(groupBean.getNotice());
            }

            Date signUPEndTime = Common.str2Date(groupBean.getSignUpEnd());
            //判斷角色
            int status = myGroup.getAttenderStatus();
            if (status == 1 && groupBean.getMemberId() == selfMember.getMemberId()){
                //  我是團長 - 按鈕：查看團員、修改揪團
                tvWord.setVisibility(View.GONE);
                llButton.setVisibility(View.VISIBLE);
            }else if (status == 1){
                //  已入團的路人 - 按鈕：查看團員
                tvWord.setVisibility(View.GONE);
                btGroupSetting.setVisibility(View.GONE);
                llButton.setVisibility(View.VISIBLE);
            }else  if (status == 3) {
                //  加團審核中的路人 - 文字：揪團申請審核中
                llButton.setVisibility(View.GONE);
                tvWord.setText("揪團申請審核中");
            }else  if (status == -1 || status == 0 || status == 2){
                // 路人
                if (groupBean.getGroupStatus() == 3){
                    //  揪團結束 - 文字：已結束
                    llButton.setVisibility(View.GONE);
                    tvWord.setText("揪團已結束");
                    tvWord.setVisibility(View.VISIBLE);
                }else if (groupBean.getGroupStatus() == 2){
                    //  未結束，但滿團 - 文字：已滿團
                    llButton.setVisibility(View.GONE);
                    tvWord.setText("此團人數已滿");
                    tvWord.setVisibility(View.VISIBLE);
                }else if(new Date().after(signUPEndTime)){
                    //  表定時間前1分鐘，未滿團 - 文字：即將開始
                    llButton.setVisibility(View.GONE);
                    tvWord.setText("此揪團即將開始");
                    tvWord.setVisibility(View.VISIBLE);
                }else{
                    //  未結束也未滿團 - 按鈕：申請加入
                    tvWord.setVisibility(View.GONE);
                    btGroupTeam.setVisibility(View.GONE);
                    btGroupSetting.setText("申請加入");
                    btGroupSetting.setVisibility(View.VISIBLE);
                    llButton.setVisibility(View.VISIBLE);
                }

            }else {
                tvWord.setVisibility(View.GONE);
                llButton.setVisibility(View.GONE);
            }

    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.creat, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(llButton).popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}