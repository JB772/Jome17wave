package com.example.jome17wave.jome_member;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.R;
import com.example.jome17wave.jome_loginRegister.LoginActivity;
import com.example.jome17wave.MainActivity;

public class MemberProfileFragment extends Fragment {
    private static final String TAG = "MemberProfileFragment";
    private MainActivity activity;
    private ImageView igMember;
    private ImageButton btConnectUs;
    private ImageButton btLogOut;
    private ConstraintLayout clFriendList;
    private ConstraintLayout clScore;
    private ConstraintLayout clGroupRecord;
    private ConstraintLayout clJoinRecord;
    private TextView tvFriendList;
    private TextView tvScore;
    private TextView tvGroupRecord;
    private TextView tvJoinRecord;
    private JomeMember jomeMember;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.BottomBarMember);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        igMember = view.findViewById(R.id.igMember);
        btConnectUs = view.findViewById(R.id.btConnectUs);
        btLogOut = view.findViewById(R.id.btLogOut);
        clFriendList = view.findViewById(R.id.clFriendList);
        clScore = view.findViewById(R.id.clScore);
        clGroupRecord = view.findViewById(R.id.clGroupRecord);
        clJoinRecord = view.findViewById(R.id.clJoinRecord);
        tvFriendList = view.findViewById(R.id.tvFriendList);
        tvScore = view.findViewById(R.id.tvScore);
        tvGroupRecord = view.findViewById(R.id.tvGroupRecord);
        tvJoinRecord = view.findViewById(R.id.tvJoinRecord);

        //設定click監聽器
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btConnectUs:
                        break;
                    case R.id.btLogOut:
                        //清空preferences檔
                        // 轉跳到login頁面
                        Intent intentLoginActivity = new Intent();
                        intentLoginActivity.setClass(activity, LoginActivity.class);
                        startActivity(intentLoginActivity);
                        break;
                    case R.id.clFriendList:
                        Navigation.findNavController(view).navigate(R.id.action_memberProfileFragment_to_friendsListFragment);
                        break;
                    case R.id.clScore:
                        Navigation.findNavController(view).navigate(R.id.action_memberProfileFragment_to_profileScoreFragment);
                        break;
                    case R.id.clGroupRecord:
                        Navigation.findNavController(view).navigate(R.id.action_memberProfileFragment_to_selfGroupFragment);
                        break;
                    case R.id.clJoinRecord:
                        Navigation.findNavController(view).navigate(R.id.action_memberProfileFragment_to_selfRecordFragment);
                        break;
                    default:
                        break;
                }
            }
        };
        btLogOut.setOnClickListener(onClickListener);
        clFriendList.setOnClickListener(onClickListener);
        clScore.setOnClickListener(onClickListener);
        clGroupRecord.setOnClickListener(onClickListener);
        clJoinRecord.setOnClickListener(onClickListener);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.member_center_tool_bar, menu);
        menu.findItem(R.id.member_check_item).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(igMember).popBackStack();
                break;
            case R.id.member_settin_item:
                Navigation.findNavController(igMember).navigate(R.id.action_memberProfileFragment_to_modifyProfileFragment);
                break;
            default:
                break;
        }
        return true;

    }
}