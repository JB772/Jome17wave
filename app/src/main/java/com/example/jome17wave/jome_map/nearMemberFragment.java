package com.example.jome17wave.jome_map;

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

public class nearMemberFragment extends Fragment {
    private static final String TAG = "OtherMemberFragment";
    private MainActivity activity;
    private ImageView imageFProfile;
    private TextView tvFDataName;
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


        Bundle bundle = getArguments();
        if (bundle != null) {
            friend = (JomeMember) bundle.getSerializable("friend");
            Log.d(TAG, "friend: " + friend.getNickname());
            toolbar.setTitle(friend.getNickname());
            tvFDataName = view.findViewById(R.id.tvFDataName);
            tvFDataName.setText(friend.getNickname());
        }
    }
}