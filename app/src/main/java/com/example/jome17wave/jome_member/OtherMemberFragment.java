package com.example.jome17wave.jome_member;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_member.Friend;
import com.example.jome17wave.task.CommonTask;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class OtherMemberFragment extends Fragment {
    private static final String TAG = "OtherMemberFragment";
    private MainActivity activity;
    private ImageView imageFProfile;
    private TextView tvFDataName, tvAverageScore, tvFriendCount, tvAssembleCount, tvJointCount;
    private ImageButton ibtFriendStory, ibtOtherMessage, ibtFriendAdd, ibtFriendPandding;
    private MemberImageTask memberImageTask;
    private CommonTask friendTask;
    private JomeMember friend;
    private String friendId;

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
        return inflater.inflate(R.layout.fragment_other_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //大頭貼
        imageFProfile = view.findViewById(R.id.imageFProfile);
        //四顆imageButton
        ibtFriendStory = view.findViewById(R.id.ibtFriendStory);
        ibtOtherMessage = view.findViewById(R.id.ibtOtherMessage);
        ibtFriendAdd = view.findViewById(R.id.ibtFriendAdd);
        ibtFriendPandding = view.findViewById(R.id.ibtFriendPandding);

        ibtFriendStory.setVisibility(View.VISIBLE);
        ibtOtherMessage.setVisibility(View.VISIBLE);
        ibtFriendAdd.setVisibility(View.GONE);
        ibtFriendPandding.setVisibility(View.GONE);
        //四格資訊
        tvFDataName = view.findViewById(R.id.tvFDataName);
        tvAverageScore = view.findViewById(R.id.tvAverageScore);
        ;
        tvFriendCount = view.findViewById(R.id.tvFriendCount);
        ;
        tvAssembleCount = view.findViewById(R.id.tvAssembleCount);
        ;
        tvJointCount = view.findViewById(R.id.tvJointCount);
        ;

        Bundle bundle = getArguments();
        if (bundle != null) {
            friend = (JomeMember) bundle.getSerializable("friend");
            Log.d(TAG, "friend: " + friend.getNickname());
            if (friend != null) {
                toolbar.setTitle(friend.getNickname());
                friendId = friend.getMember_id();
                showMember();
            }
        }
        List<JomeMember> myFriends = openFile_getFileDir("friends");
        if (myFriends != null){
            for (JomeMember myFriend : myFriends){
                while (!myFriend.getMember_id().equals(friendId)){
                    ibtOtherMessage.setVisibility(View.GONE);
                    ibtFriendStory.setVisibility(View.GONE);
                    break;
                }
            }
        }else {
            
        }


        View.OnClickListener btOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ibtFriendStory:
                        Navigation.findNavController(view).navigate(R.id.action_otherMemberFragment_to_OMemberStoryFragment);
                        break;
                    default:
                        break;
                }
            }
        };
        ibtFriendStory.setOnClickListener(btOnclick);
    }

    public void showMember() {
        String urlGetMember = Common.URL_SERVER + "jome_member/LoginServlet";
        String urlGetImage = Common.URL_SERVER + "jome_member/LoginServlet";
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        JsonObject jsonObject = new JsonObject();
        String jsonIn = "";
        Bitmap bitmap = null;

        if (Common.networkConnected(activity)) {
            //拿Member
            try {
                jsonObject.addProperty("memberId", friendId);
                jsonObject.addProperty("action", "idGet");
                friendTask = new CommonTask(urlGetMember, jsonObject.toString());
                jsonIn = friendTask.execute().get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
            int idGetResult = jsonObject.get("idGetResult").getAsInt();
            if (idGetResult == 1){
                friend = new Gson().fromJson(jsonObject.get("idMember").getAsString(), JomeMember.class);
            }else {
                friend = new JomeMember();
            }
            tvFDataName.setText(friend.getNickname());
            tvFriendCount.setText(friend.getFriendCount() + " 人");
            tvAverageScore.setText(friend.getScoreAverage());
            tvAssembleCount.setText(String.valueOf(friend.getGroupCount()));
            tvJointCount.setText(friend.getGroupCount());

            //拿圖
            try {
                memberImageTask = new MemberImageTask(urlGetImage, friendId, imageSize);
                bitmap = memberImageTask.execute().get();
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            Log.d(TAG, "bitmap2: " + bitmap);
            if (bitmap == null) {
                imageFProfile.setImageResource(R.drawable.no_image);
            } else {
                imageFProfile.setImageBitmap(bitmap);
            }
        } else {
            imageFProfile.setImageResource(R.drawable.no_image);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.member_center_tool_bar, menu);
        menu.findItem(R.id.member_check_item).setVisible(false);
        menu.findItem(R.id.member_settin_item).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(imageFProfile).popBackStack();
        }
        return true;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(friendTask != null){
            friendTask.cancel(true);
            friendTask = null;
        }

        if (memberImageTask != null){
            memberImageTask.cancel(true);
            memberImageTask = null;
        }
    }

    private List<JomeMember> openFile_getFileDir(String fileName){
        File file = new File(activity.getFilesDir(), fileName);
        try {
            FileInputStream fileInput = new FileInputStream(file);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            List<JomeMember> myFriends = (List<JomeMember>) objectInput.readObject();
            return  myFriends;
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return  null;
    }

    private int identifyRelation(String userId, String friendId){
        String url = Common.URL_SERVER + "";

        return  -1;
    }
}