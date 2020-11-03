package com.example.jome17wave.jome_member;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.jome_group.Group;
import com.example.jome17wave.main.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class SelfAttendingFragment extends Fragment {
    private static final String TAG = "SelfAttendingFragment";
    private MainActivity activity;
    private RecyclerView rvSelfRecord;
    private List<PersonalGroupBean> groups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_self_attedning, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvSelfRecord = view.findViewById(R.id.rvSelfRecord);
        rvSelfRecord.setLayoutManager(new LinearLayoutManager(activity));
        groups = getGroups();
        for (PersonalGroupBean myGroup: groups){
            Log.d(TAG, "groups61 :" + myGroup.getGroupName());
        }
        SelfRecordAdapter selfRecordAdapter = new SelfRecordAdapter(activity, groups);
        selfRecordAdapter.notifyDataSetChanged();
        rvSelfRecord.setAdapter(selfRecordAdapter);


    }
    private class SelfRecordAdapter extends RecyclerView.Adapter<SelfRecordAdapter.MyViewHolder>{
        Context context;
        List<PersonalGroupBean> groups;

        public SelfRecordAdapter(Context context, List<PersonalGroupBean> groups) {
            this.context = context;
            this.groups = groups;
        }

        public void  setGroups(List<PersonalGroupBean> groups){
            this.groups = groups;
        }

        @Override
        public int getItemCount() {
            return groups == null ? 0 :groups.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView igJSuccess;
            ImageView igJLose;
            ImageView igAlreadyEnd;
            ImageView igWillStart;
            ImageView igProcessing;
            ImageView igCollecting;
            ImageView igChecking;
            TextView tvRecordResult;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                igJSuccess = itemView.findViewById(R.id.igJSuccess);
                igJLose = itemView.findViewById(R.id.igJLose);
                igAlreadyEnd = itemView.findViewById(R.id.igAlreadyEnd);
                igWillStart = itemView.findViewById(R.id.igWillStart);
                igProcessing = itemView.findViewById(R.id.igProcessing);
                igCollecting = itemView.findViewById(R.id.igCollecting);
                igChecking = itemView.findViewById(R.id.igChecking);
                igJSuccess.setVisibility(View.GONE);
                igJLose.setVisibility(View.GONE);
                igAlreadyEnd.setVisibility(View.GONE);
                igWillStart.setVisibility(View.GONE);
                igProcessing.setVisibility(View.GONE);
                igCollecting.setVisibility(View.GONE);
                igChecking.setVisibility(View.GONE);

                tvRecordResult = itemView.findViewById(R.id.tvRecordResult);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_view_member_record, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
            final PersonalGroupBean group = groups.get(position);
              if (group.getGroupStatus() == 1 || group.getGroupStatus() == 2 ){
                  Date assembleTime = Common.str2Date(group.getAssembleTime());      //集合時間
                  Date signUpEnd = Common.str2Date(group.getSignUpEnd());           //報名截止時間
                  Date groupEndTime = Common.str2Date(group.getGroupEndTime());     //活動結束時間
                  Date nowTime = new Date();
                  switch (group.getRole()){
                      case 1:       //團長
                          int groupStatus = group.getGroupStatus();
                          Log.d(TAG, "groupName: "+group.getGroupName() + "groupStatus: "+group.getGroupStatus());
                          switch (groupStatus){
                              case 1:
                                  if (nowTime.before(signUpEnd)){
                                      //顯示「募集中」
                                      viewHolder.igCollecting.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(signUpEnd) && nowTime.before(assembleTime)){
                                      //顯示「即將開始」
                                      viewHolder.igWillStart.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(assembleTime) && nowTime.before(groupEndTime)){
                                      //顯示「進行中」
                                      viewHolder.igProcessing.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(groupEndTime)){
                                      //顯示「已經結束」
                                      viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
                                  }else {
                                      viewHolder.setIsRecyclable(false);
                                  }
                                  break;
                              case 2:
                                  viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
                                  break;
                              case 3:
                                  viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
                                  break;
                              default:
                                  viewHolder.setIsRecyclable(false);
                                  break;
                          }
                          break;
                      case 2:      //團員
                          int attendStatus = group.getAttenderStatus();
                          switch (attendStatus){
                              //離開
                              case 0:
                                  viewHolder.itemView.setVisibility(View.GONE);
                              //同意加入
                              case 1:
                                  if (nowTime.before(signUpEnd)){
                                      //加入成功
                                      viewHolder.igJSuccess.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(signUpEnd) && nowTime.before(assembleTime)){
                                      //顯示「即將開始」
                                      viewHolder.igWillStart.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(assembleTime) && nowTime.before(groupEndTime)){
                                      //顯示「進行中」
                                      viewHolder.igProcessing.setVisibility(View.VISIBLE);
                                  }else if(nowTime.after(groupEndTime)){
                                      //顯示「已結束』
                                      viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
                                  }else {
                                      viewHolder.itemView.setVisibility(View.GONE);
                                  }
                                  break;
                              //拒絕
                              case 2:
                                  //顯示「加入失敗」
                                  viewHolder.igJLose.setVisibility(View.VISIBLE);
                                  break;
                              //待審
                              case 3:
                                  if (nowTime.before(assembleTime)){
                                     //顯示待審核
                                      viewHolder.igChecking.setVisibility(View.VISIBLE);
                                  }else {
                                      viewHolder.igJLose.setVisibility(View.VISIBLE);
                                  }
                                  break;
                              default:
                                  viewHolder.itemView.setVisibility(View.GONE);
                          }
                          break;
                  }

              }else if(group.getGroupStatus() == 3){
                  viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
              }else {
                  viewHolder.itemView.setVisibility(View.GONE);
              }

            viewHolder.tvRecordResult.setText(group.toString());
        }
    }

    private List<PersonalGroupBean> getGroups() {
        List<PersonalGroupBean> allGroups = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null){
            Group group = null;
            int showRecord = bundle.getInt("showRecord");

            if (showRecord == 1){
                Log.d("TAG", "myGroupFragment1 : " + showRecord);
                if (new File(activity.getFilesDir(), "mainGroups").exists()){
                    allGroups = (List< PersonalGroupBean >) openFile_getFileDir("mainGroups");
                }
            }else if (showRecord == 2){
                Log.d("TAG", "attendingFragment2 : " + showRecord);
                if (new File(activity.getFilesDir(), "attendGroups").exists()){
                    allGroups = (List<PersonalGroupBean>) openFile_getFileDir("attendGroups");
                }
            }
        }
        return allGroups;
    }

    private Object openFile_getFileDir(String fileName){
        File file = new File(activity.getFilesDir(), fileName);
        try(FileInputStream fileInput = new FileInputStream(file);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput)) {
            return objectInput.readObject();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return null;
    }
}