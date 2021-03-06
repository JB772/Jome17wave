package com.example.jome17wave.jome_member;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_group.Group;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SelfGroupFragment extends Fragment {
    private static final String TAG = "SelfGroupFragment";
    private MainActivity activity;
    private RecyclerView rvSelfGroup;
    private List<Group> groups;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity)getActivity();
//        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_self_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        Toolbar toolbar = view.findViewById(R.id.toolbar);
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rvSelfGroup = view.findViewById(R.id.rvSelfGroup);
        rvSelfGroup.setLayoutManager(new LinearLayoutManager(activity));
        groups = getGroups();
        rvSelfGroup.setAdapter(new SelfGroupAdapter(activity, groups));
    }



    private class SelfGroupAdapter extends RecyclerView.Adapter<SelfGroupAdapter.MyViewHolder> {
        Context context;
        List<Group> groups;

        public SelfGroupAdapter(Context context, List<Group> groups) {
            this.context = context;
            this.groups = groups;
        }

        public void  setGroups(List<Group> groups){
            this.groups = groups;
        }

        @Override
        public int getItemCount() {
            return groups.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView igJSuccess;
            ImageView igJLose;
            ImageView igAlreadyEnd;
            ImageView igWillStart;
            TextView tvRecordResult;
            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                igJSuccess = itemView.findViewById(R.id.igJSuccess);
                igJLose = itemView.findViewById(R.id.igJLose);
                igAlreadyEnd = itemView.findViewById(R.id.igAlreadyEnd);
                igWillStart = itemView.findViewById(R.id.igWillStart);
                igJSuccess.setVisibility(View.GONE);
                igJLose.setVisibility(View.GONE);
                igAlreadyEnd.setVisibility(View.GONE);
                igWillStart.setVisibility(View.GONE);
                tvRecordResult = itemView.findViewById(R.id.tvRecordResult);
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(context).inflate(R.layout.item_view_member_record, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int position) {
            final Group group = groups.get(position);
            viewHolder.tvRecordResult.setText(group.toString());
            if (group.getRole() == 1){
                switch (group.getStatus()){
                    case 0:
                        viewHolder.setIsRecyclable(false);
                    case 1:
                        viewHolder.igWillStart.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        viewHolder.igAlreadyEnd.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }else {
                viewHolder.itemView.setVisibility(View.GONE);
            }
        }
    }
    private List<Group> getGroups() {
        List<Group> getGroupsList = new ArrayList<>();
        getGroupsList.add(new Group("陽光沙灘", new Date(), 2, 1, 1));
        getGroupsList.add(new Group("沙灘比基尼", new Date(), 2, 1, 1));
        getGroupsList.add(new Group("沙灘比丘尼", new Date(), 1, 1, 1));
        getGroupsList.add(new Group("小熊維尼", new Date(), 2, 1, 1));
        getGroupsList.add(new Group("測試不該出現", new Date(), 2, 1, 2));
        return getGroupsList;
    }

//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.member_center_tool_bar, menu);
//        menu.findItem(R.id.member_settin_item).setVisible(false);
//        menu.findItem(R.id.member_check_item).setVisible(false);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == android.R.id.home){
//            Navigation.findNavController(rvSelfGroup).popBackStack();
//        }
//        return true;
//    }
}