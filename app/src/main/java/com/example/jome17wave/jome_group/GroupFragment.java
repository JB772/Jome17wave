package com.example.jome17wave.jome_group;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class GroupFragment extends Fragment {
//    private static final String TAG = "TAG_GroupFragment";
//    private SwipeRefreshLayout swipeRefreshLayout;
//    private SwipeRefreshLayout swipeRefreshLayout1;
//    private MainActivity activity;
//    private RecyclerView rvNewGroup;
//    private SearchView searchView;
//    private CommonTask GroupGetAllTask;
//    private List<Group> groups;
//    private List<ImageTask> imageTasks;
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        activity = (MainActivity) getActivity();
//        setHasOptionsMenu(true);
//        imageTasks = new ArrayList<>();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_group, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        toolbar.setTitle("揪團列表");
//        activity.setSupportActionBar(toolbar);
//
//        rvNewGroup.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
//        groups = getGroups();
//        showGroups(groups);
//
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                showGroups(groups);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//
//        swipeRefreshLayout1.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                showGroups(groups);
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
//
//        final SearchView searchView = view.findViewById(R.id.SearchGroup);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                if (newText.isEmpty()) {
//                    showGroups(groups);
//                } else {
//                    List<Group> searchGroups = new ArrayList<>();
//                    for (Group group: groups) {
//                        if (group.getGroupName().toUpperCase().contains(newText.toUpperCase())) {
//                            searchGroups.add(group);
//                        }
//                    }
//                    showGroups(searchGroups);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return true;
//            }
//
//        });
//    }
//
//    private List<Group> getGroups() {
//        List<Group> groups = null;
//        if (Common.networkConnected(activity)) {
//            String url = Common.URL_SERVER + "JOIN_GROUPServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            GroupGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = GroupGetAllTask.execute().get();
//                Type listType = new TypeToken<List<Group>>() {
//                }.getType();
//                groups = new Gson().fromJson(jsonIn, listType);
//                Log.d(TAG,"groups:"+jsonIn);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        } else {
//            Common.showToast(activity, R.string.textNoNetwork);
//        }
//        return groups;
//    }
//
//    private void showGroups(List<Group> groups) {
//        if (groups == null || groups.isEmpty()) {
//            Common.showToast(activity, R.string.textNoGroupsFound);
//        }
//        GroupAdapter groupAdapter = (GroupAdapter) rvNewGroup.getAdapter();
//        if (groupAdapter == null) {
//            rvNewGroup.setAdapter(new GroupAdapter(activity, groups));
//        } else {
//            groupAdapter.setGroups(groups);
//            groupAdapter.notifyDataSetChanged();
//        }
//    }
//
//    private class GroupAdapter extends RecyclerView.Adapter <GroupAdapter.MyViewHolder> {
//        private LayoutInflater layoutInflater;
//        private List<Group> groups;
//        private int imageSize;
//
//        GroupAdapter(Context context, List<Group> groups) {
//            layoutInflater = LayoutInflater.from(context);
//            this.groups = groups;
//            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
//        }
//
//        void setGroups(List<Group> groups) {
//            this.groups = groups;
//        }
//
//        class MyViewHolder extends RecyclerView.ViewHolder {
//            ImageView imageView;
//            TextView tvGroupName, tvSurfPoint, tvTime;
//
//            MyViewHolder(View itemView) {
//                super(itemView);
//                imageView = itemView.findViewById(R.id.ivNewGroup);
//                tvGroupName = itemView.findViewById(R.id.tvGroupName);
//                tvSurfPoint = itemView.findViewById(R.id.tvSurfPoint);
//                tvTime = itemView.findViewById(R.id.tvTime);
//            }
//        }
//
//        @Override
//        public int getItemCount() {
//            return groups == null ? 0 : groups.size();
//        }
//
//        @NonNull
//        @Override
//        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//            View itemView = layoutInflater.inflate(R.layout.item_view_newgroup, parent, false);
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
//            final Group group = groups.get(position);
//            String url = Common.URL_SERVER + "JOIN_GROUPServlet";
//            int id = group.getGroupId();
//            ImageTask imageTask = new ImageTask(url, id, imageSize, myViewHolder.imageView);
//            imageTask.execute();
//            imageTasks.add(imageTask);
//            myViewHolder.tvGroupName.setText(group.getGroupName());
//            myViewHolder.tvSurfPoint.setText(group.getSurfPointId());
//            myViewHolder.tvTime.setText(group.getAssembleTime());
//        }
//
//    }
}