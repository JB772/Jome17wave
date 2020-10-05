package com.example.jome17wave.jome_member;

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
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jome17wave.Common;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FriendsListFragment extends Fragment {
    private static final String TAG ="FriendsListFragment";
    private MainActivity activity;
    private RecyclerView rvMFriendsList;
    private SearchView sVFriendList;
    private ConstraintLayout itemFriendCL;
    private CommonTask getFriendsTask;
    private MemberImageTask getFriendImageTask;
    private List<JomeMember> friends;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friends_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("好友列表");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sVFriendList = view.findViewById(R.id.sVFriendList);
        sVFriendList.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                FriendAdapter friendAdapter = (FriendAdapter) rvMFriendsList.getAdapter();
                if (friendAdapter != null){
                    if (newText.isEmpty()){
                        friendAdapter.setFriends(friends);
                    }else {
                        List<JomeMember>searchFriends = new ArrayList<>();
                        for (JomeMember friend:friends){
                            if (friend.getNickname().toUpperCase().contains(newText.toUpperCase())){
                                searchFriends.add(friend);
                            }
                        }
                        friendAdapter.setFriends(searchFriends);
                    }
                    friendAdapter.notifyDataSetChanged();
                    return true;
                }
                return false;
            }
        });
        rvMFriendsList = view.findViewById(R.id.rvMFriendsList);
        rvMFriendsList.setLayoutManager(new LinearLayoutManager(activity));
        friends = getFriends();
        rvMFriendsList.setAdapter(new FriendAdapter(activity, friends));
    }

    private class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.MyViewHolder> {
        Context context;
        List<JomeMember> friends;
        private int imageSize;

        public FriendAdapter(Context context, List<JomeMember> friends) {
            this.context = context;
            this.friends = friends;
            imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        }

        public void setFriends(List<JomeMember> friends) {
            this.friends = friends;
        }

        @Override
        public int getItemCount() {
            return friends.size() ;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            ImageView imageFriend;
            TextView tvFriendName;
            ImageButton ibtMessage, ibtDelete;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                imageFriend = itemView.findViewById(R.id.imageFriend);
                tvFriendName = itemView.findViewById(R.id.tvFriendName);
                ibtMessage = itemView.findViewById(R.id.ibtMessage);
                ibtDelete = itemView.findViewById(R.id.ibtDelete);
                ibtDelete.setVisibility(View.GONE);
                itemFriendCL = itemView.findViewById(R.id.itemFriendCL);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_view_member_friend, parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
            final JomeMember friend = friends.get(position);
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            String memberId = friend.getMember_id();
            getFriendImageTask = new MemberImageTask(url, memberId, imageSize, viewHolder.imageFriend);
            getFriendImageTask.execute();
            viewHolder.tvFriendName.setText(friend.getNickname());

            viewHolder.ibtMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //轉到聊天室
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("friend", friend);
                    Navigation.findNavController(rvMFriendsList)
                            .navigate(R.id.action_friendsListFragment_to_listToOtherFragment, bundle);
                }
            });
        }
    }

    private List<JomeMember> getFriends() {
        String url = Common.URL_SERVER + "jome_member/CenterServiceServlet";
        List<JomeMember> myFriends = new ArrayList<>();
        String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
        JomeMember member = new Gson().fromJson(memberStr, JomeMember.class);
        if (Common.networkConnected(activity)){
        JsonObject jsonOut = new JsonObject();
        jsonOut.addProperty("action", "getFriendList");
        jsonOut.addProperty("memberId", member.getMember_id());
        getFriendsTask = new CommonTask(url,jsonOut.toString());
        try {
            String inStr = getFriendsTask.execute().get();
            Log.d(TAG, "inStr: " + inStr);
            JsonObject jsonIn = new Gson().fromJson(inStr, JsonObject.class);
            String myFriendsStr = jsonIn.get("friends").getAsString();

            Log.d(TAG, "myFriendStr:" + myFriendsStr);
            Type listType = new TypeToken<List<JomeMember>>(){}.getType();
            myFriends = new Gson().fromJson(myFriendsStr, listType);
            saveFriendList_getFileDir("friends", myFriends);
        } catch (InterruptedException e) {
            Log.d(TAG, e.toString());
        } catch (ExecutionException e) {
            Log.d(TAG, e.toString());
            }
        }
        return myFriends;
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
            Navigation.findNavController(rvMFriendsList).popBackStack();
        }
        return true;
    }

    private void saveFriendList_getFileDir(String fileName, List<JomeMember> friends){
        File file = new File(activity.getFilesDir(), fileName);
        try {
            FileOutputStream fileOutput = new FileOutputStream(file);
            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
            objectOutput.writeObject(friends);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getFriendsTask != null){
            getFriendsTask.cancel(true);
            getFriendsTask = null;
        }
        if (getFriendImageTask != null){
            getFriendImageTask.cancel(true);
            getFriendImageTask = null;
        }
    }
}