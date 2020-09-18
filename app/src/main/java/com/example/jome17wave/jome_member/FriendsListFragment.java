package com.example.jome17wave.jome_member;

import android.content.Context;
import android.os.Bundle;
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

import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;

import java.util.ArrayList;
import java.util.List;

public class FriendsListFragment extends Fragment {
    private static final String TAG ="FriendsListFragment";
    private MainActivity activity;
    private RecyclerView rvMFriendsList;
    private SearchView sVFriendList;
    private ConstraintLayout itemFriendCL;
    private List<Friend> friends;


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
                        List<Friend>searchFriends = new ArrayList<>();
                        for (Friend friend:friends){
                            if (friend.getNameFriend().toUpperCase().contains(newText.toUpperCase())){
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
        List<Friend> friends;

        public FriendAdapter(Context context, List<Friend> friends) {
            this.context = context;
            this.friends = friends;
        }

        public void setFriends(List<Friend> friends) {
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
            final Friend friend = friends.get(position);
//            viewHolder.imageFriend.setImageBitmap(friend.getImageFriend());
            viewHolder.imageFriend.setImageResource(friend.getImageFriendId());
            viewHolder.tvFriendName.setText(friend.getNameFriend());
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
                            .navigate(R.id.action_friendsListFragment_to_turnOtherMemberFragment, bundle);
                }
            });
        }
    }

    private List<Friend> getFriends() {
        List<Friend> testFriends = new ArrayList<>();
        testFriends.add(new Friend(R.drawable.no_image, "Kitty"));
        testFriends.add(new Friend(R.drawable.no_image, "Ocean"));
        testFriends.add(new Friend(R.drawable.no_image, "WANG"));
        return testFriends;
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
}