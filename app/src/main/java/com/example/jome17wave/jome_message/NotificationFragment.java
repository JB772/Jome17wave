package com.example.jome17wave.jome_message;

import android.annotation.SuppressLint;



import android.annotation.SuppressLint;


import android.annotation.SuppressLint;

import android.content.Context;
import android.os.Bundle;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.task.CommonTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class NotificationFragment extends Fragment {
    private static final String TAG = "TAG_NotificationFragment";
    private MainActivity activity;
    private ImageView ivFriendInvitationIcon, ivNextPoint;
    private Button btFriendInvitation;
    private TextView tvFriendInvitationTitle, tvFriendInvitationDescription;
    private RecyclerView rvNotification;
    private CommonTask notificationGetAllTask;
    private List<Notify> notifications;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        activity.setTitle(R.string.notification_center);
        return inflater.inflate(R.layout.fragment_notification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.notification_center);
        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //左上角返回箭頭

        ivFriendInvitationIcon = view.findViewById(R.id.ivFriendInvitationIcon);
        ivNextPoint = view.findViewById(R.id.ivNextPoint);
        btFriendInvitation = view.findViewById(R.id.btFriendInvitation);
        tvFriendInvitationTitle = view.findViewById(R.id.tvFriendInvitationTitle);
        tvFriendInvitationDescription = view.findViewById(R.id.tvFriendInvitationDescription);
        rvNotification = view.findViewById(R.id.rvNotification);

//        btFriendInvitation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Navigation.findNavController(activity, R.id.action_notificationFragment_to_friendInvitationFragment);
//            }
//        });

        btFriendInvitation.setOnClickListener(
                Navigation.createNavigateOnClickListener(
                        R.id.action_notificationFragment_to_friendInvitationFragment));

        rvNotification.setLayoutManager(new LinearLayoutManager(activity));
        notifications = getNotifications();
        showNotification(notifications);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notification_center, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.messageCenterFragment) {
            Navigation.findNavController(rvNotification).navigate(R.id.action_notificationFragment_to_messageCenterFragment);

        }

        return super.onOptionsItemSelected(item);

//        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                Navigation.findNavController(activity, R.id.action_notificationFragment_to_messageCenterFragment);
//                return true;
//            }
//        })
    }

    @SuppressLint("LongLogTag")
    private void showNotification(List<Notify> notificationList) {
        if (notifications == null || notifications.isEmpty()){
            Common.showToast(activity, R.string.no_notifications_found);
        }
        Log.d(TAG, "showNotification");
        NotificationAdapter notificationAdapter = (NotificationAdapter) rvNotification.getAdapter();
        Log.d(TAG, "getAdapter");
        // 如果bookAdapter不存在就建立新的，否則續用舊有的
        if (notificationAdapter == null){
            Log.d(TAG, "notificationAdapter == null");
            rvNotification.setAdapter(new NotificationAdapter(activity, notifications));
        }else {
            Log.d(TAG, "notificationAdapter == null else");
            notificationAdapter.setNotifications(notifications);
            notificationAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("LongLogTag")
    private List<Notify> getNotifications() {
        List<Notify> notifications = new ArrayList<>();
        List<Notify> testNotifications = new ArrayList<>();
        testNotifications.add(
                new Notify(-1,1,"", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,2,"成功加入 夏天來囉... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,3,"失敗加入 一起去衝浪... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,4,"魯夫 要求加入 陽光沙灘... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(3,5,"衝浪消暑... 已結束，你喜歡嗎？", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(-2,6,"", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,2,"成功加入 夏天來囉... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,3,"失敗加入 一起去衝浪... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,4,"魯夫 要求加入 陽光沙灘... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(3,5,"衝浪消暑... 已結束，你喜歡嗎？", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,2,"成功加入 夏天來囉... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,3,"失敗加入 一起去衝浪... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,4,"魯夫 要求加入 陽光沙灘... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(3,5,"衝浪消暑... 已結束，你喜歡嗎？", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,2,"成功加入 夏天來囉... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,3,"失敗加入 一起去衝浪... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(1,4,"魯夫 要求加入 陽光沙灘... 的揪團", new Date(System.currentTimeMillis())));
        testNotifications.add(
                new Notify(3,5,"衝浪消暑... 已結束，你喜歡嗎？", new Date(System.currentTimeMillis())));

        notifications = testNotifications;
        return notifications;
//            List<Notify> notifications = null;
//            if (Common.networkConnected(activity)){
//                String url = Common.URL_SERVER + "NotificationServlet";
//                JsonObject jsonObject = new JsonObject();
//                jsonObject.addProperty("action", "getAll");
//                String jsonOut = jsonObject.toString();
//                notificationGetAllTask = new CommonTask(url, jsonOut);
//                try {
//                    String jsonIn = notificationGetAllTask.execute().get();
//                    Type listType = new TypeToken<Notify>() {}.getType();
//                    notifications = new Gson().fromJson(jsonIn, listType);
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//            }else {
//                Common.showToast(activity, R.string.no_network_connection_available);
//            }
//            return  notifications;
    }

    private class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<Notify> notifications;

        NotificationAdapter(Context context, List<Notify> notifications) {
            layoutInflater = LayoutInflater.from(context);
            this.notifications = notifications;
        }

        void setNotifications(List<Notify> notifications){
            this.notifications = notifications;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_notification, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final Notify notification = notifications.get(position);
            String url = Common.URL_SERVER + "NotificationServlet";
            int type = notification.getType();
//            holder.tvNotificationBody.setText(notification.getNotificationBody().toString());
//            holder.tvTitle.setText("本週");

            if (type < 0){
                holder.constraintLayoutTitle.setVisibility(View.VISIBLE);
                holder.constrainLayoutNotification.setVisibility(View.GONE);
                if (type == -1){
                    holder.tvTitle.setText("本週");
                }else {
                    holder.tvTitle.setText("更早之前");
                }

            }else{
                holder.constraintLayoutTitle.setVisibility(View.GONE);
                holder.constrainLayoutNotification.setVisibility(View.VISIBLE);
                holder.ivNotification.setImageResource(R.drawable.add_requst_icon);
                holder.tvNotificationBody.setText(notification.getNotificationBody());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("notification", notification);
//                    bundle.putSerializable("image", (Serializable) imageTasks);
//                    Navigation.findNavController(v).navigate(R.id.action_bookListFragment_to_bookDetailFragment, bundle);
                }
            });

        }

        @Override
        public int getItemCount() {
            return notifications == null ? 0 :notifications.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            private ConstraintLayout constraintLayoutTitle, constrainLayoutNotification;
            private TextView tvTitle, tvNotificationBody;
            private ImageView ivNotification;
            public MyViewHolder(View itemView) {
                super(itemView);
                constraintLayoutTitle = itemView.findViewById(R.id.constrainLayoutTitle);
                constrainLayoutNotification = itemView.findViewById(R.id.constrainLayoutNotification);
                tvTitle = itemView.findViewById(R.id.tvTitle);
                tvNotificationBody = itemView.findViewById(R.id.tvNotificationBody);
                ivNotification = itemView.findViewById(R.id.ivNotification);



            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (notificationGetAllTask != null) {
            notificationGetAllTask.cancel(true);
            notificationGetAllTask = null;
        }
    }
}