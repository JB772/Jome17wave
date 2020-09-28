package com.example.jome17wave.jome_member;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_message.FriendInvitationFragment;

import java.util.List;

public class MyRecordFragment extends Fragment {
    private MainActivity activity;
    private ViewPager2 vpMyRecord;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpMyRecord = view.findViewById(R.id.vpMyRecord);
//        vpMyRecord.setAdapter(new MyRecordAdapter());
    }

//    private class MyRecordAdapter extends RecyclerView.Adapter<MyRecordAdapter.PagerViewHolder> {
//
//
//        public class PagerViewHolder extends RecyclerView.ViewHolder{
//            Fragment fragment;
//
//            public PagerViewHolder(@NonNull View itemView) {
//                super(itemView);
//                Fragment fragment = itemView.findViewById(R.layout.item_view_pager2);


}