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
import android.widget.ImageButton;
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
    private ImageButton ibtFriendStory, ibtOtherMessage, ibtFriendAdd, ibtFriendPandding;

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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ibtFriendStory = view.findViewById(R.id.ibtFriendStory);
        ibtOtherMessage = view.findViewById(R.id.ibtOtherMessage);
        ibtFriendAdd = view.findViewById(R.id.ibtFriendAdd);


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
        View.OnClickListener btOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
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

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.member_center_tool_bar, menu);
        menu.findItem(R.id.member_check_item).setVisible(false);
        menu.findItem(R.id.member_settin_item).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            Navigation.findNavController(imageFProfile).popBackStack();
        }
        return true;
    }
}