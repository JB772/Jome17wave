package com.example.jome17wave.jome_member;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_member.Friend;

public class OtherMemberFragment extends Fragment {
    private static final String TAG = "OtherMemberFragment";
    private MainActivity activity;
    private ImageView imageFProfile;
    private TextView tvFDataName ,tvAverageScore, tvFriendCount,tvAssembleCount, tvJointCount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_other_member, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        imageFProfile = view.findViewById(R.id.imageFProfile);
        tvFDataName = view.findViewById(R.id.tvFDataName);
        tvAverageScore = view.findViewById(R.id.tvAverageScore);;
        tvFriendCount = view.findViewById(R.id.tvFriendCount);;
        tvAssembleCount = view.findViewById(R.id.tvAssembleCount);;
        tvJointCount = view.findViewById(R.id.tvJointCount);;
        Bundle bundle = getArguments();
        if (bundle != null) {
            Friend friend = (Friend) bundle.getSerializable("friend");
            Log.d(TAG, friend.getNameFriend());
            if (friend != null) {
                imageFProfile.setImageResource(friend.getImageFriendId());
                tvFDataName.setText(friend.getNameFriend());
                tvAverageScore.setText("0 分");
                tvAssembleCount.setText("0 次");
                tvJointCount.setText("0 次");
                tvFriendCount.setText("0 人");
            }
        }
    }


}