package com.example.jome17wave.main;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.jome_group.Group;
import com.example.jome17wave.jome_group.GroupFragment;
import com.example.jome17wave.jome_loginRegister.LoginActivity;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.ImageTask;
import com.example.jome17wave.task.MemberImageTask;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class mainFragment extends Fragment {
    private String TAG = "mainFragment";
    private MainActivity activity;
    private CommonTask updateLatLngTask;
    private static final int REQ_LOGIN = 2;
    private static final int REQ_CHECK_SETTINGS = 101;
    private static final int PER_ACCESS_LOCATION = 201;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Location lastLocation;
    /* ----------------------------------------------------- */
    private RecyclerView rvNewGroup;
    private RecyclerView rvStart;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout swipeRefreshLayout1;
    private CommonTask GroupGetAllTask;
    private List<PersonalGroupBean> groups;
    private List<MemberImageTask> imageTasks;
    /* ----------------------------------------------------- */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                //每十秒抓一次位置，用秒來計很花耗電，盡量不要用秒來計
                .setInterval(5000)//單位毫秒
                //至少要多遠才算發生位移，越大越省電
                .setSmallestDisplacement(100);//單位公R

        locationCallback = new LocationCallback() {
            //十秒鐘抓一次位置與手機內存的最後一次位置資料比較，有發生位移才會呼叫onLocationResult()，重新抓資料來刷畫面
            @Override
            public void onLocationResult(LocationResult locationResult) {
                lastLocation = locationResult.getLastLocation();
                updateLastLocationInfo(lastLocation);
            }
        };
        /* ----------------------------------------------------- */
        setHasOptionsMenu(true);
        imageTasks = new ArrayList<>();
        /* ----------------------------------------------------- */
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
        /* ----------------------------------------------------- */
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
        /* ----------------------------------------------------- */
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
//        Common.loginCheck(activity, REQ_LOGIN);
        Intent loginIntend = new Intent(activity, LoginActivity.class);
        activity.startActivityForResult(loginIntend, REQ_LOGIN);
        askAccessLocationPermission();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_LOGIN){
            if (resultCode == Activity.RESULT_OK){
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
            checkLocationSettings();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PER_ACCESS_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_DENIED) {
//            textView.setText(R.string.textLocationAccessNotGrant);
        }
    }

    // 檢查裝置是否開啟Location設定
    private void checkLocationSettings() {
        // 必須將LocationRequest設定加入檢查
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder()
                        .addLocationRequest(locationRequest);
        Task<LocationSettingsResponse> task =
                LocationServices.getSettingsClient(activity)
                        .checkLocationSettings(builder.build());
        task.addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // 取得並顯示最新位置
                showLastLocation();
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    Log.e(TAG, e.getMessage());
                    try {
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        // 跳出Location設定的對話視窗
                        resolvable.startResolutionForResult(activity, REQ_CHECK_SETTINGS);
                    } catch (IntentSender.SendIntentException sendEx) {
                        Log.e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private void showLastLocation() {
        if (fusedLocationClient == null) {
            //getFusedLocationProviderClient()取得Client物件，Client端物件就是手機物件，
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
            //檢查permission是否有同意定位這件事情。
            if (ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED) {
//                textView.setText(R.string.textLocationAccessNotGrant);
                //不同意就return
                return;
            }
            //用存著結果的Task的泛型指定型
            fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        lastLocation = task.getResult();
                        updateLastLocationInfo(lastLocation);
                    }
                }
            });

            // 持續取得最新位置。looper設為null代表以現行執行緒呼叫callback方法，而非使用其他執行緒
            fusedLocationClient.requestLocationUpdates(
                    locationRequest, locationCallback, null);
        }
    }

    private void updateLastLocationInfo(Location lastLocation) {
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            JomeMember mainMember = new Gson().fromJson(memberStr, JomeMember.class);
            mainMember.setLatitude(lastLocation.getLatitude());
            mainMember.setLongitude(lastLocation.getLongitude());
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "update");
            jsonObject.addProperty("memberUp", new Gson().toJson(mainMember));
            jsonObject.addProperty("imageBase64", "noImage");
            String jsonIn = "";
            updateLatLngTask = new CommonTask(url, jsonObject.toString());
            try {
                jsonIn = updateLatLngTask.execute().get();
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
            int resultCode = -1;
            resultCode = jsonObject.get("resultCode").getAsInt();
            if (resultCode != 1) {
                /*
                網路有問題
                 */
            }
        }

    }


}