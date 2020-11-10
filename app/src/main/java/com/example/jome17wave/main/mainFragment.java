package com.example.jome17wave.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jome17wave.Common;
import com.example.jome17wave.FcmSender;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.GroupImageTask;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class mainFragment extends Fragment {
    private String TAG = "mainFragment";
    public static MainActivity activity;
    private static final int REQ_LOGIN = 2;
    private static final int PER_ACCESS_LOCATION = 201;
    private RecyclerView rvNewGroup;
    private RecyclerView rvStart;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CommonTask GroupGetAllTask;
    private List<GroupImageTask> imageTasks;
    private  List<PersonalGroupBean> startGroups = new ArrayList<>();
    private  List<PersonalGroupBean> newGroups = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
        imageTasks = new ArrayList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("揪團列表");
        activity.setSupportActionBar(toolbar);

        rvNewGroup = view.findViewById(R.id.rvNewGroup);
        rvNewGroup.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        rvStart = view.findViewById(R.id.rvStart);
        rvStart.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

        diverseStartGroups();
        diverseNewGroups();
        showGroups(newGroups);
        showStartGroups(startGroups);



        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                diverseStartGroups();
                diverseNewGroups();
                showGroups(newGroups);
                showStartGroups(startGroups);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        final SearchView searchView = view.findViewById(R.id.SearchGroup);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showGroups(newGroups);
                } else {
                    List<PersonalGroupBean> searchGroups = new ArrayList<>();
                    for (PersonalGroupBean group : newGroups) {
                        if (group.getGroupName().toUpperCase().contains(newText.toUpperCase())) {
                            searchGroups.add(group);
                        }
                    }
                    showGroups(searchGroups);
                }

                if (newText.isEmpty()) {
                    showStartGroups(startGroups);
                } else {
                    List<PersonalGroupBean> searchGroups = new ArrayList<>();
                    for (PersonalGroupBean group: startGroups) {
                        if (group.getGroupName().toUpperCase().contains(newText.toUpperCase())) {
                            searchGroups.add(group);
                        }
                    }
                    showStartGroups(searchGroups);
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

        });
    }

    private void diverseNewGroups(){
        List<PersonalGroupBean> groups = getGroups();//all
        newGroups.clear();
        if (groups != null){
            for (PersonalGroupBean group : groups) {
                if (group.getGroupStatus() == 1) {
Log.d(TAG, "groupHeadName :" + group.getGroupName() + "surfPointId :" + group.getSurfPointId());
                    newGroups.add(group);
                }
            }
        }
    }

    private void diverseStartGroups() {
        List<PersonalGroupBean> groups = getGroups();       //all
        startGroups.clear();
        if (groups != null){
            for (PersonalGroupBean group : groups) {
                if (group.getGroupStatus() == 2) {
Log.d(TAG, "groupHeadName :" + group.getGroupName() + "surfPointId :" + group.getSurfPointId() + "\t");
                    startGroups.add(group);
                }
            }
        }
    }

    private List<PersonalGroupBean> getGroups() {
        List<PersonalGroupBean> groups = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "/jome_member/GroupOperateServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
//            jsonObject.addProperty("memberId", memberId);
            String jsonOut = jsonObject.toString();
            GroupGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = GroupGetAllTask.execute().get();
                jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
                String groupsStr = jsonObject.get("allGroup").getAsString();
                Type listType = new TypeToken<List<PersonalGroupBean>>() {
                }.getType();
                groups = new Gson().fromJson(groupsStr, listType);
                Log.d(TAG, "groups:" + jsonIn);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(activity, R.string.textNoNetwork);
        }
        return groups;
    }

    private void showGroups(List<PersonalGroupBean> groups) {
        if (groups == null || groups.isEmpty()) {
            Common.showToast(activity, R.string.textNoGroupsFound);
        }
        GroupAdapter groupAdapter = (GroupAdapter) rvNewGroup.getAdapter();
        if (groupAdapter == null) {
            rvNewGroup.setAdapter(new GroupAdapter(activity, groups));
        } else {
            groupAdapter.setGroups(groups);
            groupAdapter.notifyDataSetChanged();
        }
    }

    private class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<PersonalGroupBean> groups;
        private int imageSize;

        GroupAdapter(Context context, List<PersonalGroupBean> groups) {
            layoutInflater = LayoutInflater.from(context);
            this.groups = groups;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        void setGroups(List<PersonalGroupBean> groups) {
            this.groups = groups;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvGroupName, tvSurfPoint, tvTime;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivNewGroup);
                tvGroupName = itemView.findViewById(R.id.tvGroupName);
                tvSurfPoint = itemView.findViewById(R.id.tvSurfPoint);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
        }

        @Override
        public int getItemCount() {
            return groups == null ? 0 : groups.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_newgroup, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final PersonalGroupBean group = groups.get(position);
            String url = Common.URL_SERVER + "/jome_member/GroupOperateServlet";
            String id = group.getGroupId();
            GroupImageTask imageTask = new GroupImageTask(url, id, imageSize, myViewHolder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);
            myViewHolder.tvGroupName.setText(group.getGroupName());
            myViewHolder.tvSurfPoint.setText(group.getSurfName());
            myViewHolder.tvTime.setText(group.getAssembleTime());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newGroup", group);
                    Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_fragmentGroupDetail, bundle);
                }
            });
        }

    }


    private void showStartGroups(List<PersonalGroupBean> startGroups) {
        if (startGroups == null || startGroups.isEmpty()) {
            Common.showToast(activity, R.string.textNoGroupsFound);
        }
        StartGroupAdapter groupAdapter = (StartGroupAdapter) rvStart.getAdapter();
        if (groupAdapter == null) {
            rvStart.setAdapter(new StartGroupAdapter(activity, startGroups));
        } else {
            groupAdapter.setStartGroups(startGroups);
            groupAdapter.notifyDataSetChanged();
        }
    }

    private class StartGroupAdapter extends RecyclerView.Adapter<StartGroupAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<PersonalGroupBean> startGroups;
        private int imageSize;

        StartGroupAdapter(Context context, List<PersonalGroupBean> startGroups) {
            layoutInflater = LayoutInflater.from(context);
            this.startGroups = startGroups;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        public void setStartGroups(List<PersonalGroupBean> startGroups) {
            this.startGroups = startGroups;
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView tvGroupName, tvSurfPoint, tvTime;

            MyViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ivStartGroup);
                tvGroupName = itemView.findViewById(R.id.tvGroupName);
                tvSurfPoint = itemView.findViewById(R.id.tvSurfPoint);
                tvTime = itemView.findViewById(R.id.tvTime);
            }
        }

        @Override
        public int getItemCount() {
            return startGroups == null ? 0 : startGroups.size();
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_startgroup, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
            final PersonalGroupBean startGroup = startGroups.get(position);
            String url = Common.URL_SERVER + "/jome_member/GroupOperateServlet";
            String id = startGroup.getGroupId();
            GroupImageTask imageTask = new GroupImageTask(url, id, imageSize, myViewHolder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);
            myViewHolder.tvGroupName.setText(startGroup.getGroupName());
            myViewHolder.tvSurfPoint.setText(startGroup.getSurfName());
            myViewHolder.tvTime.setText(startGroup.getAssembleTime());
            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("newGroup", startGroup);
                    Navigation.findNavController(v).navigate(R.id.action_mainFragment_to_fragmentGroupDetail, bundle);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Common.loginCheck(this, REQ_LOGIN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult");
                getTokenId();
                askAccessLocationPermission();
            }
        }
    }
    /**
     * 取得手機tokenId
     */
    private void getTokenId(){

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()){
                    if(task.getResult() != null){
                        String tokenId = task.getResult();

                        String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
                        JomeMember mainMember = new Gson().fromJson(memberStr, JomeMember.class);
                        mainMember.setToken_id(tokenId);
                        Common.usePreferences(activity, Common.PREF_FILE).edit()
                                                    .putString("registrationToken", tokenId)
                                                    .putString("loginMember", new Gson().toJson(mainMember))
                                                    .apply();
                        new FcmSender().sendTokenToServer(tokenId, activity);
                    }
                }
            }
        });
    }

    /**
     * 請求手機定位Permission
     */
    private void askAccessLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int result = ActivityCompat.checkSelfPermission(activity, permissions[0]);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, PER_ACCESS_LOCATION);
        }
        else {
//            intent service開始抓位置
            Intent intent = new Intent(activity, MyLocationService.class);
            activity.startService(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PER_ACCESS_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//            textView.setText(R.string.textLocationAccessNotGrant);
        } else {
            Intent intent = new Intent(activity, MyLocationService.class);
            activity.startService(intent);
        }
    }


}