package com.example.jome17wave.jome_group;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.example.jome17wave.Common;
import com.example.jome17wave.FcmSender;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class GroupAttenderFragment extends Fragment {
    private static final String TAG = "GroupAttenderFragment";
    private MainActivity activity;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvWaitAudit, rvHasJoin;
    private CommonTask getAttenderTask, updateAttenderTask;
    private String groupId = "";
    private int hasJoinCount = -1;
    private String selfId = "";
    private int selfRole = -1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_group_attender, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("揪團成員");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        rvWaitAudit = view.findViewById(R.id.rvWaitAudit);
        rvWaitAudit.setLayoutManager(new LinearLayoutManager(activity));
        rvHasJoin = view .findViewById(R.id.rvHasJoin);
        rvHasJoin.setLayoutManager(new LinearLayoutManager(activity));
        Bundle bundle = getArguments();
        if (bundle != null){
            PersonalGroupBean groupData = (PersonalGroupBean) bundle.getSerializable("groupData");
            if (groupData != null){
                groupId = groupData.getGroupId();
            }
        }
        List<PersonalGroupBean> attenders = getAllAttenders(groupId);
        selfId = Common.getSelfFromPreference(activity).getMember_id();
        for (PersonalGroupBean attender: attenders){
            if (selfId.equals(attender.getMemberId())){
                selfRole = attender.getRole();
            }
        }
        showAllAttender(attenders);
        swipeRefreshLayout = view.findViewById(R.id.attenderSwipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showAllAttender(getAllAttenders(groupId));
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void showAllAttender(List<PersonalGroupBean> allAttenders) {
        List<PersonalGroupBean> waitAuditAttenders = new ArrayList<>();
        List<PersonalGroupBean> joinAttenders = new ArrayList<>();

        for (PersonalGroupBean attender : allAttenders){
            if (attender.getAttenderStatus() == 3){
                waitAuditAttenders.add(attender);
            }else if(attender.getAttenderStatus() == 1){
                joinAttenders.add(attender);
            }
        }
        hasJoinCount = joinAttenders.size();

        //報名者的recycle
        if (waitAuditAttenders == null || waitAuditAttenders.isEmpty()){
//            Common.showToast(activity, "目前沒人報名");
        }
        AttendersAdapter waitAuditAttender = (AttendersAdapter) rvWaitAudit.getAdapter();
        if (waitAuditAttender == null){
            rvWaitAudit.setAdapter(new AttendersAdapter(activity, waitAuditAttenders));
        }else {
            waitAuditAttender.setAttenders(waitAuditAttenders);
            waitAuditAttender.notifyDataSetChanged();
        }

        //團員的recycle
        AttendersAdapter joinAttender = (AttendersAdapter) rvHasJoin.getAdapter();
        if(joinAttender == null){
            rvHasJoin.setAdapter(new AttendersAdapter(activity, joinAttenders));
        }else {
            joinAttender.setAttenders(joinAttenders);
            joinAttender.notifyDataSetChanged();
        }
    }

    private List<PersonalGroupBean> getAllAttenders(String groupId) {
        List<PersonalGroupBean> allAttenders = new ArrayList<>();
        String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
        if (Common.networkConnected(activity)){
            JsonObject jsonOut = new JsonObject();
            jsonOut.addProperty("action", "getAllAttenders");
            jsonOut.addProperty("groupId", groupId);
            String inStr = "";
            getAttenderTask = new CommonTask(url, jsonOut.toString());
            try {
                inStr = getAttenderTask.execute().get();
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            JsonObject jsonIn = new Gson().fromJson(inStr, JsonObject.class);
            String attendersStr = jsonIn.get("allAttenders").getAsString();
            Type listType = new TypeToken<List<PersonalGroupBean>>(){}.getType();
            allAttenders = new Gson().fromJson(attendersStr, listType);
        }
        return allAttenders;
    }

    private class AttendersAdapter extends RecyclerView.Adapter<AttendersAdapter.MyViewHolder>{
        private LayoutInflater layoutInflater;
        private List<PersonalGroupBean> attenders;
        private int imageSize;

        public AttendersAdapter(Context context, List<PersonalGroupBean> attenders) {
            layoutInflater = LayoutInflater.from(context);
            this.attenders = attenders;
            imageSize = getResources().getDisplayMetrics().widthPixels / 5;
        }

        void setAttenders(List<PersonalGroupBean> attenders){
            this.attenders = attenders;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView ivProfileImg;
            TextView tvAttenderName;
            ConstraintLayout clAuditMember;
            ImageButton ibtAgreeAttender;
            ImageButton ibtDeclineAttender;
            ConstraintLayout clKickMember;
            TextView tvGroupRole;
            ImageButton ibtMemberExit;
            ImageView ivGroupFull;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                ivProfileImg = itemView.findViewById(R.id.ivProfileImg);
                tvAttenderName = itemView.findViewById(R.id.tvAttenderName);
                clAuditMember = itemView.findViewById(R.id.clAuditMember);
                ibtAgreeAttender = itemView.findViewById(R.id.ibtAgreeAttender);
                ibtDeclineAttender = itemView.findViewById(R.id.ibtDeclineAttender);
                clKickMember = itemView.findViewById(R.id.clKickMember);
                tvGroupRole = itemView.findViewById(R.id.tvGroupRole);
                ibtMemberExit = itemView.findViewById(R.id.ibtMemberExit);
//                ibtMemberExit.setVisibility(View.GONE);
                ivGroupFull = itemView.findViewById(R.id.ivGroupFull);

            }
        }
        @Override
        public int getItemCount() {
            return attenders == null ? 0 : attenders.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_attender, parent, false);
            return new  MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull AttendersAdapter.MyViewHolder myViewHolder, int position) {
            final PersonalGroupBean attender = attenders.get(position);

            //取得大頭貼
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            String memberId = attender.getMemberId();
            MemberImageTask attenderImageTask = new MemberImageTask(url, memberId, imageSize, myViewHolder.ivProfileImg);
            attenderImageTask.execute();

            //設定資料

            if (hasJoinCount > 0 && hasJoinCount >= attender.getGroupLimit()){
                myViewHolder.clAuditMember.setVisibility(View.GONE);
                myViewHolder.clKickMember.setVisibility(View.VISIBLE);
                if (attender.getAttenderStatus() == 3){
                    myViewHolder.clKickMember.setVisibility(View.GONE);
                    myViewHolder.ivGroupFull.setVisibility(View.VISIBLE);
                }else if (attender.getAttenderStatus() == 1){
                    myViewHolder.ivGroupFull.setVisibility(View.GONE);
                    if (attender.getRole() == 1){
                        myViewHolder.tvGroupRole.setText("團長");
                    }else  if(attender.getRole() == 2){
                        myViewHolder.tvGroupRole.setText("團員");
                    }
                }
            }else {
                myViewHolder.ivGroupFull.setVisibility(View.GONE);
                if(attender.getAttenderStatus() == 3){
                    myViewHolder.tvGroupRole.setText("waitAudit");
                    myViewHolder.clAuditMember.setVisibility(View.VISIBLE);
                    myViewHolder.clKickMember.setVisibility(View.GONE);
                }else if (attender.getAttenderStatus() == 1){
                    myViewHolder.clKickMember.setVisibility(View.VISIBLE);
                    myViewHolder.clAuditMember.setVisibility(View.GONE);
                    if (attender.getRole() == 1){
                        myViewHolder.tvGroupRole.setText("團長");
                    }else  if(attender.getRole() == 2){
                        myViewHolder.tvGroupRole.setText("團員");
                    }
                }
            }

            String attenderId = attender.getMemberId();
            myViewHolder.ibtMemberExit.setVisibility(View.INVISIBLE);
                if (selfRole == 1){
                    myViewHolder.ibtMemberExit.setVisibility(View.VISIBLE);
                }else if (selfRole == 2){
                    if (selfId.equals(attenderId) && selfRole == 2){
                        myViewHolder.ibtMemberExit.setVisibility(View.VISIBLE);
                    }else{
                        myViewHolder.ibtMemberExit.setVisibility(View.INVISIBLE);
                    }
                }


            myViewHolder.tvAttenderName.setText(attender.getNickname());

            //按鈕監聽器
            View.OnClickListener btAuditMemberOnclick = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JsonObject jsonObject = new JsonObject();
                    int attenderStatus = -1;
                    String action = "";
                    switch (v.getId()){
                        case R.id.ibtAgreeAttender:
                            action = "auditAttender";
                            attenderStatus = 1;
                            break;
                        case R.id.ibtDeclineAttender:
                            action = "auditAttender";
                            attenderStatus = 2;
                            break;
                        case R.id.ibtMemberExit:
                            action = "dropOutGroup";
                            attenderStatus = 0;
                            break;
                    }
                    PersonalGroupBean updateAttender = new PersonalGroupBean(attender.getMemberId(), attender.getAttenderId(), groupId, attenderStatus, attender.getRole());
                    updateAttender.setSurfPointId(attender.getSurfPointId());
                    updateAttender.setSurfName(attender.getSurfName());
                    jsonObject.addProperty("action", action);
                    jsonObject.addProperty("auditAttender", new Gson().toJson(updateAttender));
                    if (Common.networkConnected(activity)){
                        String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
                        String inStr = "";
                        updateAttenderTask = new CommonTask(url, jsonObject.toString());
                        try {
                            inStr = updateAttenderTask.execute().get();
                        } catch (ExecutionException e) {
                            Log.e(TAG, e.toString());
                        } catch (InterruptedException e) {
                            Log.e(TAG, e.toString());
                        }
                        jsonObject = new Gson().fromJson(inStr, JsonObject.class);
                        int dropResult = jsonObject.get("dropResult").getAsInt();
                        int auditResult = jsonObject.get("auditResult").getAsInt();
                        String attendersStr = jsonObject.get("afterAttenders").getAsString();
                        Type listType = new TypeToken<List<PersonalGroupBean>>(){}.getType();
                        List<PersonalGroupBean> afterUpdateAttenders = new Gson().fromJson(attendersStr, listType);
                        FcmSender fcmSender = new FcmSender();
                        if (auditResult == 1 || dropResult == 1){
                            showAllAttender(afterUpdateAttenders);
                            updateAttender.setNickname(attender.getNickname());
                            fcmSender.groupFcmSender(activity, updateAttender);
                        }else if(dropResult == 3){
                            PersonalGroupBean disGroupBean = afterUpdateAttenders.get(0);
                            disGroupBean.setRole(1);
                            fcmSender.groupFcmSender(activity, disGroupBean);
                        }else {
                            Common.showToast(activity, R.string.no_network_connection_available);
                        }
                    }
                    if (v.getId()==R.id.ibtMemberExit ){
                        Navigation.findNavController(rvWaitAudit).navigate(R.id.mainFragment);
                    }
                }
            };
            myViewHolder.ibtAgreeAttender.setOnClickListener(btAuditMemberOnclick);
            myViewHolder.ibtDeclineAttender.setOnClickListener(btAuditMemberOnclick);
            myViewHolder.ibtMemberExit.setOnClickListener(btAuditMemberOnclick);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.member_center_tool_bar, menu);
        menu.findItem(R.id.member_check_item).setVisible(false);
        menu.findItem(R.id.member_settin_item).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Navigation.findNavController(rvWaitAudit).popBackStack();
        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getAttenderTask != null){
            getAttenderTask.cancel(true);
            getAttenderTask = null;
        }
        if (updateAttenderTask != null){
            updateAttenderTask.cancel(true);
            updateAttenderTask = null;
        }
    }
}