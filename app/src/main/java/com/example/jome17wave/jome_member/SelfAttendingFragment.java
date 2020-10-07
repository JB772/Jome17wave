package com.example.jome17wave.jome_member;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.jome_group.Group;

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
        rvSelfRecord.setAdapter(new SelfRecordAdapter(activity, groups ));
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
            TextView tvRecordResult;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                igJSuccess = itemView.findViewById(R.id.igJSuccess);
                igJLose = itemView.findViewById(R.id.igJLose);
                igAlreadyEnd = itemView.findViewById(R.id.igAlreadyEnd);
                igWillStart = itemView.findViewById(R.id.igWillStart);
                igJSuccess.setVisibility(View.GONE);
                igJLose.setVisibility(View.GONE);
                igAlreadyEnd.setVisibility(View.GONE);
                igWillStart.setVisibility(View.GONE);
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
              if (group.getGroupStatus() == 1 || group.getGroupStatus() == 2){
                  Date assembleTime = Common.str2Date(group.getAssembleTime());      //集合時間
                  Date signUpEnd = Common.str2Date(group.getSignUpEnd());           //報名截止時間
                  Date groupEndTime = Common.str2Date(group.getGroupEndTime());     //活動結束時間
                  Date nowTime = new Date();
                  switch (group.getRole()){
                      case 1:       //團長
                          int groupStatus = group.getGroupStatus();
                          switch (groupStatus){
                              case 1:
                                  if (nowTime.before(signUpEnd)){
                                      //顯示「募集中」
                                  }else if (nowTime.after(signUpEnd) && nowTime.before(assembleTime)){
                                      viewHolder.igWillStart.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(assembleTime) && nowTime.before(groupEndTime)){
                                      //顯示「進行中」
                                  }else if (nowTime.after(groupEndTime)){
                                      viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
                                  }else {
                                      viewHolder.setIsRecyclable(false);
                                  }
                                  break;
                              case 2:
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
                              case 1:
                                  if (nowTime.before(signUpEnd)){
                                      viewHolder.igJSuccess.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(signUpEnd) && nowTime.before(assembleTime)){
                                      viewHolder.igWillStart.setVisibility(View.VISIBLE);
                                  }else if (nowTime.after(assembleTime) && nowTime.before(groupEndTime)){
                                      //顯示「進行中」

                                  }else if(nowTime.after(groupEndTime)){
                                      viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
                                  }else {
                                      viewHolder.setIsRecyclable(false);
                                  }
                                  break;
                              case 2:
                                  viewHolder.igJLose.setVisibility(View.VISIBLE);
                                  break;
                              case 3:
                                  if (nowTime.before(assembleTime)){
                                     //顯示待審核
                                  }
                                  break;
                              default:
                                  viewHolder.setIsRecyclable(false);
                          }
                          break;
                  }

              }else {
                  viewHolder.setIsRecyclable(false);
              }

            viewHolder.tvRecordResult.setText(group.toString());
        }
    }

    private List<PersonalGroupBean> getGroups() {
        List<PersonalGroupBean> groups = new ArrayList<>();
        Bundle bundle = this.getArguments();
        if (bundle != null){
            Group group = null;
            int showRecord = bundle.getInt("showRecord");

            if (showRecord == 1){
                Log.d("TAG", "attendingFragment1 : " + showRecord);
                if (new File(activity.getFilesDir(), "mainGroup").exists()){
                    groups = (List< PersonalGroupBean >) openFile_getFileDir("mainGroup");
                }
            }else if (showRecord == 2){
                Log.d("TAG", "myGroupFragment2 : " + showRecord);
                if (new File(activity.getFilesDir(), "attendGroup").exists()){
                    groups = (List<PersonalGroupBean>) openFile_getFileDir("attendGroup");
                }
            }
        }
        return groups;
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