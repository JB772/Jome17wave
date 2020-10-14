package com.example.jome17wave.jome_message;

import android.graphics.Bitmap;
import android.icu.text.CaseMap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.jome_group.Group;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.concurrent.ExecutionException;

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
    private CommonTask joinGroupTask;


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

        showGroupDetail();

        //點擊 “查看團員”按鈕
        btGroupTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = getArguments();
                Navigation.findNavController(v)
                        .navigate(R.id.action_friendInvitationFragment_to_findNewFriendFragment, bundle);
                Log.d(TAG, "action箭頭指向錯誤：要去查看團員才對啦");
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

                }else if(btGroupSetting.getText() == "申請加入"){
                    //點擊 “申請加入”按鈕
                    String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
                    JsonObject jsonObject = new JsonObject();

                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        PersonalGroupBean groupBean = (PersonalGroupBean) bundle.getSerializable("groupBean");

                        String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
                        JomeMember myself = new Gson().fromJson(memberStr, JomeMember.class);
                        groupBean.setMemberId(myself.getMember_id());

                        jsonObject.addProperty("action", "joinGroup");
                        jsonObject.addProperty("groupBean", new Gson().toJson(groupBean));
//                        jsonObject.addProperty("myselfId", myself.getMember_id());
                        String jsonOut = jsonObject.toString();
                        joinGroupTask = new CommonTask(url, jsonOut);
                        try {
                            String jsonIn = joinGroupTask.execute().get();
                            JsonObject jo = new Gson().fromJson(jsonIn, JsonObject.class);
                            int resultCode = jo.get("joinResult").getAsInt();
                            if (resultCode == 1){
                                Common.showToast(activity, R.string.change_successful);
                            }else {
                                Common.showToast(activity, R.string.change_fail);
                            }
                        } catch (Exception e) {
                            Log.d(TAG, e.toString());
                        }

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
            PersonalGroupBean groupBean = (PersonalGroupBean)bundle.getSerializable("newGroup");
            Log.d(TAG,"bundleGroupBean" +  groupBean.getMemberId());
            String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
            int imageSize = getResources().getDisplayMetrics().widthPixels / 3;

            if(Common.networkConnected(activity)){
                try {
                    bitmap = new MemberImageTask(url, groupBean.getGroupId(), imageSize).execute().get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                Log.d(TAG, "bitmap2: " + bitmap);
                if (bitmap == null){
                    ivGroup.setImageResource(R.drawable.no_image);
                }else {
                    ivGroup.setImageBitmap(bitmap);
                }
            }else {
                ivGroup.setImageResource(R.drawable.no_image);
            }

            tvCaptain.setText(groupBean.getNickname());
            tvGroupName.setText(groupBean.getGroupName());
            tvGroupDate.setText(groupBean.getGroupEndTime());
            tvGroupLocation.setText(groupBean.getSurfName());
            tvGroupLimit.setText(String.valueOf(groupBean.getGroupLimit()));

            tvGroupGender.setVisibility(View.GONE);
//            tvGroupGender.setText(groupBean.getGender());

            //備註內文
            if (groupBean.getNotice() != null ){
                tvGroupMemo.setText(groupBean.getNotice());
            }else {
                clGroupMemo.setVisibility(View.GONE);
            }

            //判斷角色
            int status = groupBean.getAttenderStatus();
            String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            JomeMember myself = new Gson().fromJson(memberStr,JomeMember.class);


            if (status == 1 && groupBean.getMemberId() == myself.getMember_id()){
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
                if (groupBean.getGroupStatus() == 2){
                    //  揪團結束 - 文字：已結束
                    llButton.setVisibility(View.GONE);
                    tvWord.setText("揪團已結束");
                    tvWord.setVisibility(View.VISIBLE);
                }else if (groupBean.getGroupLimit() == groupBean.getJoinCountNow()){
                    //  未結束，但滿團 - 文字：已滿團
                    llButton.setVisibility(View.GONE);
                    tvWord.setText("此團人數已滿");
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

    }


}