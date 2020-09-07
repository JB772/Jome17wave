package com.example.jome17wave.jome_message;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.jome_loginRegister.MainActivity;
import com.example.jome17wave.R;


public class NotificationFragment extends Fragment {
    private static final String TAG = "TAG_NotificationFragment";
    private MainActivity activity;
    private ImageView ivFriendInvitationIcon, ivNextPoint;
    private Button btFriendInvitation;
    private TextView tvFriendInvitationTitle, tvFriendInvitationDescription;
    private RecyclerView recyclerView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();

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
        toolbar.setTitle("測");
        activity.setSupportActionBar(toolbar);

        ivFriendInvitationIcon = view.findViewById(R.id.ivFriendInvitationIcon);
        ivNextPoint = view.findViewById(R.id.ivNextPoint);
        btFriendInvitation = view.findViewById(R.id.btFriendInvitation);
        tvFriendInvitationTitle = view.findViewById(R.id.tvFriendInvitationTitle);
        tvFriendInvitationDescription = view.findViewById(R.id.tvFriendInvitationDescription);
        recyclerView = view.findViewById(R.id.recyclerView);


    }
}