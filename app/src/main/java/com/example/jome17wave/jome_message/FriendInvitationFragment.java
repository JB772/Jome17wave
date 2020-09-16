package com.example.jome17wave.jome_message;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.ImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class FriendInvitationFragment extends Fragment {
    private static  final String TAG = "TAG_friendInvitationFragment";
    private MainActivity activity;
    private RecyclerView rvFriendInvitation;
    private CommonTask invitationGetAllTask;
    private List<FriendInvitation> invitations;
    private List<ImageTask> imageTasks;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        setHasOptionsMenu(true);
        imageTasks = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_invitation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.friend_Invitation);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //左上角返回箭頭

        rvFriendInvitation = view.findViewById(R.id.rvFriendInvitation2);
        rvFriendInvitation.setLayoutManager(new LinearLayoutManager(activity));
        invitations = getInvitations();
        showInvitation(invitations);




    }

    private void showInvitation(List<FriendInvitation> invitations) {
        if (invitations == null || invitations.isEmpty()){
            Common.showToast(activity, R.string.no_invitations_found);
        }
        FriendInvitationFragment.FriendInvitationAdapter friendInvitationAdapter =
                (FriendInvitationFragment.FriendInvitationAdapter) rvFriendInvitation.getAdapter();

        // 如果bookAdapter不存在就建立新的，否則續用舊有的
        if (friendInvitationAdapter == null){
            rvFriendInvitation.setAdapter(new FriendInvitationFragment.FriendInvitationAdapter(activity, invitations));
        }else {
            friendInvitationAdapter.setFriendInvitations(invitations);
            friendInvitationAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("LongLogTag")
    private List<FriendInvitation> getInvitations() {
        List<FriendInvitation> invitations = new ArrayList<>();
        List<FriendInvitation> testInvitations = new ArrayList<>();
        testInvitations.add(
                new FriendInvitation(1, 2, 3, "王小美", new Date(System.currentTimeMillis())));
        testInvitations.add(
                new FriendInvitation(2, 3, 3, "李阿智", new Date(System.currentTimeMillis())));
        testInvitations.add(
                new FriendInvitation(3, 4, 3, "廖凡凡", new Date(System.currentTimeMillis())));
        testInvitations.add(
                new FriendInvitation(1, 5, 3, "孫美美", new Date(System.currentTimeMillis())));
        testInvitations.add(
                new FriendInvitation(1, 2, 3, "王小美", new Date(System.currentTimeMillis())));
        testInvitations.add(
                new FriendInvitation(2, 3, 3, "李阿智", new Date(System.currentTimeMillis())));
        testInvitations.add(
                new FriendInvitation(3, 4, 3, "廖凡凡", new Date(System.currentTimeMillis())));
        testInvitations.add(
                new FriendInvitation(1, 5, 3, "孫美美", new Date(System.currentTimeMillis())));

        invitations = testInvitations;
        return invitations;
//        List<FriendInvitation> invitations = null;
//        if (Common.networkConnected(activity)){
//            String url = Common.URL_SERVER + "FriendInvitationServlet";
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("action", "getAll");
//            String jsonOut = jsonObject.toString();
//            invitationGetAllTask = new CommonTask(url, jsonOut);
//            try {
//                String jsonIn = invitationGetAllTask.execute().get();
//                Type listType = new TypeToken<FriendInvitation>(){}.getType();
//                invitations = new Gson().fromJson(jsonIn, listType);
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//        }else {
//            Common.showToast(activity, R.string.no_network_connection_available);
//        }
//        return invitations;
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.friend_invitation, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.findNewFriendFragment:
                Navigation.findNavController(rvFriendInvitation).navigate(R.id.action_friendInvitationFragment_to_findNewFriendFragment);
                break;
            case android.R.id.home:
                Navigation.findNavController(rvFriendInvitation).popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public class FriendInvitationAdapter extends RecyclerView.Adapter<FriendInvitationFragment.MyViewHolder> {
        private  List<FriendInvitation> invitations;
        private  LayoutInflater layoutInflater;
        private  int imageSize;

        public FriendInvitationAdapter(Context context, List<FriendInvitation> invitations) {
            layoutInflater = LayoutInflater.from(context);
            this.invitations = invitations;
            imageSize = getResources().getDisplayMetrics().widthPixels/4;
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.item_view_friend_invitation, parent, false);
            return  new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            final FriendInvitation friendInvitation = invitations.get(position);
            String url = Common.URL_SERVER + "FriendInvitationServlet";
            int id = friendInvitation.getInviteMId();
            ImageTask imageTask = new ImageTask(url, id, imageSize, holder.ivProfileImg);
            imageTask.execute();
            imageTasks.add(imageTask);
            holder.tvName.setText(friendInvitation.getAcceptMName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("friendInvitation",friendInvitation);
//                    Navigation.findNavController(view, R.id.action) // 未指定頁面
                    Log.d(TAG, "尚未指定轉跳頁面");
                }
            });


        }

        @Override
        public int getItemCount() {
            return invitations == null ? 0 : invitations.size();
        }

        void setFriendInvitations(List<FriendInvitation> invitations) {
            invitations = this.invitations;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivProfileImg;
        private TextView tvName;
        private ImageButton ibtAgree, ibtDecline;
        public MyViewHolder(View itemView) {
            super(itemView);
            ivProfileImg = itemView.findViewById(R.id.ivProfileImg);
            tvName = itemView.findViewById(R.id.tvName);
            ibtAgree = itemView.findViewById(R.id.ibtAgree);
            ibtDecline = itemView.findViewById(R.id.ibtDecline);

            ibtAgree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FriendInvitation friendInvitation = new FriendInvitation();
                    int friendStatus = friendInvitation.getFriendStatus();
                    if (friendStatus != 1){
                        friendInvitation.setFriendStatus(1);
                    }
                }
            });

            ibtDecline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FriendInvitation friendInvitation = new FriendInvitation();
                    int friendStatus = friendInvitation.getFriendStatus();
                    if (friendStatus != 1){
                        friendInvitation.setFriendStatus(1);
                    }
                }
            });



        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (invitationGetAllTask != null){
            invitationGetAllTask.cancel(true);
            invitationGetAllTask = null;
        }

        if (imageTasks != null && imageTasks.size() > 0){
            for (ImageTask imageTask : imageTasks){
                imageTask.cancel(true);
            }
            imageTasks.clear();
        }
    }
}