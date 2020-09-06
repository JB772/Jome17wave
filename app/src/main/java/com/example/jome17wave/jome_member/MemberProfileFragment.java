package com.example.jome17wave.jome_member;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.jome17wave.R;

public class MemberProfileFragment extends Fragment {
    private static final String TAG = "MemberProfileFragment";
    private Activity activity;
    //    private ImageButton btModifyMember;
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
        activity = getActivity();
//    actionBar = ((AppCompatActivity)activity).getActionBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_member_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        clFriendList.setOnClickListener(onClickListener);
        clScore.setOnClickListener(onClickListener);
        clGroupRecord.setOnClickListener(onClickListener);
        clJoinRecord.setOnClickListener(onClickListener);

    }
}