package com.example.jome17wave.main;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
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
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.jome_group.Group;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.MemberImageTask;
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
    private MyLocationService myLocationService;
    private ServiceConnection serConnection = new serConnection();
    private boolean myBound = false;
    private RecyclerView rvNewGroup;
    private RecyclerView rvStart;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private CommonTask GroupGetAllTask;
    private List<PersonalGroupBean> groups;
    private List<MemberImageTask> imageTasks;
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
//        getActivity().setTitle("首頁");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("揪團列表");
        activity.setSupportActionBar(toolbar);

        rvNewGroup = view.findViewById(R.id.rvNewGroup);
        rvNewGroup.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));
        groups = getGroups();
        showGroups(groups);

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout1 = view.findViewById(R.id.swipeRefreshLayout1);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showGroups(groups);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                showGroups(groups);
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        final SearchView searchView = view.findViewById(R.id.SearchGroup);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    showGroups(groups);
                } else {
                    List<PersonalGroupBean> searchGroups = new ArrayList<>();
                    for (PersonalGroupBean group: groups) {
                        if (group.getGroupName().toUpperCase().contains(newText.toUpperCase())) {
                            searchGroups.add(group);
                        }
                    }
                    showGroups(searchGroups);
                }
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

        });
    }

    private List<PersonalGroupBean> getGroups() {
        List<PersonalGroupBean> groups = null;
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "JOIN_GROUPServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "getAll");
            String jsonOut = jsonObject.toString();
            GroupGetAllTask = new CommonTask(url, jsonOut);
            try {
                String jsonIn = GroupGetAllTask.execute().get();
                Type listType = new TypeToken<List<Group>>() {
                }.getType();
                groups = new Gson().fromJson(jsonIn, listType);
                Log.d(TAG,"groups:"+jsonIn);
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

    private class GroupAdapter extends RecyclerView.Adapter <GroupAdapter.MyViewHolder> {
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
            String url = Common.URL_SERVER + "JOIN_GROUPServlet";
            String id = group.getGroupId();
            MemberImageTask imageTask = new MemberImageTask(url, id, imageSize, myViewHolder.imageView);
            imageTask.execute();
            imageTasks.add(imageTask);
            myViewHolder.tvGroupName.setText(group.getGroupName());
//            myViewHolder.tvSurfPoint.setText(group.getSurfPointId());
//            myViewHolder.tvTime.setText(group.getAssembleTime());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Common.loginCheck(this, REQ_LOGIN);
    }

    private class serConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyLocationService.LocalBinder binder = (MyLocationService.LocalBinder) service;
            myLocationService = binder.getService();
            myBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            myBound = false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN){
            if (resultCode == Activity.RESULT_OK){
                Log.d(TAG, "onActivityResult");
                askAccessLocationPermission();
            }
        }
    }
    // 請求user同意定位
    private void askAccessLocationPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        int result = ActivityCompat.checkSelfPermission(activity, permissions[0]);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, PER_ACCESS_LOCATION);
        }else {
            //intent service開始抓位置
            Intent intent = new Intent(activity, MyLocationService.class);
            activity.startService(intent);

//            //onBindService開始抓位置
//        Intent myLocationSerIntent = new Intent(activity, MyLocationService.class);
//        activity.bindService(myLocationSerIntent, serConnection, Context.BIND_AUTO_CREATE);
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