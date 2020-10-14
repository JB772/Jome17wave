package com.example.jome17wave.jome_member;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.jome17wave.Common;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.task.CommonTask;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MyRecordFragment extends Fragment {
    private String TAG = "MyRecordFragment";
    private MainActivity activity;
    private ViewPager2 vpMyRecord;
    private TabLayout tabMyRecord;
    private List<String> tabTitles;
    private CommonTask recordTask;

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
        return inflater.inflate(R.layout.fragment_my_record, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        vpMyRecord = view.findViewById(R.id.vpMyRecord);
        tabMyRecord = view.findViewById(R.id.tabMyRecord);

        tabTitles = new ArrayList<>();
        tabTitles.add(getString(R.string.myGroup));
        tabTitles.add(getString(R.string.myAttending));

        Bundle bundle = getArguments();
        if (bundle != null){
            String memberId = "";
            memberId =  (String) bundle.getSerializable("member");
            if (memberId != null || memberId != ""){
                getGroups(memberId);
            }
        }else if (new File(activity.getFilesDir(), "otherMemberId").exists()){
            File file = new File(activity.getFilesDir(), "otherMemberId");
            String otherMemberId = null;
            try {
                FileInputStream fileInput = new FileInputStream(file);
                ObjectInputStream objectInput = new ObjectInputStream(fileInput);
                otherMemberId = (String)objectInput.readObject();
                Log.d(TAG, "otherMemberId95 :" + otherMemberId);
            } catch (FileNotFoundException e) {
                Log.d(TAG, e.toString());
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            } catch (ClassNotFoundException e) {
                Log.d(TAG, e.toString());
            }
            if (otherMemberId != null){
                getGroups(otherMemberId);
            }
        }

        vpMyRecord.setAdapter(new MyRecordAdapter(getActivity()));
        new TabLayoutMediator(tabMyRecord, vpMyRecord, (tab, position) -> tab.setText(tabTitles.get(position))).attach();
    }


    private class MyRecordAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragments = new ArrayList<>();

        public MyRecordAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);

            //左頁
            Bundle attendingBundle = new Bundle();
            attendingBundle.putInt("showRecord", 1);
            Fragment attendingFragment = new SelfAttendingFragment();
            attendingFragment.setArguments(attendingBundle);
            fragments.add(attendingFragment);

            //右頁
            Bundle myGroupBundle = new Bundle();
            myGroupBundle.putInt("showRecord", 2);
            Fragment myGroupFragment = new SelfAttendingFragment();
            myGroupFragment.setArguments(myGroupBundle);
            fragments.add(myGroupFragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Fragment fragment = fragments.get(position);
//            notifyDataSetChanged();
            return fragment;
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    private void getGroups(String memberId) {
//        String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
//        JomeMember member = new Gson().fromJson(memberStr, JomeMember.class);
        if (Common.networkConnected(activity)) {
            String url = Common.URL_SERVER + "jome_member/GroupOperateServlet";
            JsonObject jsonOut = new JsonObject();
            jsonOut.addProperty("action", "getSelfRecord");
            jsonOut.addProperty("memberId", memberId);
            String jsonStr = "";
            recordTask = new CommonTask(url, jsonOut.toString());
            try {
                jsonStr = recordTask.execute().get();
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            JsonObject jsonIn = new Gson().fromJson(jsonStr, JsonObject.class);
            List<PersonalGroupBean> myGroups = null;
            int myGroupsResult = jsonIn.get("myGroupsResult").getAsInt();
            if (myGroupsResult == 1){
                Type listType = new TypeToken<List<PersonalGroupBean>>() {
                }.getType();
                myGroups = new Gson().fromJson(jsonIn.get("myGroups").getAsString(), listType);
            }
            List<PersonalGroupBean> mainGroups = new ArrayList<>();
            List<PersonalGroupBean> attendGroups = new ArrayList<>();
            for (PersonalGroupBean myGroup: myGroups){
                if (myGroup.getRole() == 1){
                    mainGroups.add(myGroup);
                }else if (myGroup.getRole() ==2){
                    attendGroups.add(myGroup);
                }
            }

            saveGroup_getFilesDir("mainGroups", mainGroups) ;
            saveGroup_getFilesDir("attendGroups", attendGroups) ;
        } else {
            Common.showToast(activity, R.string.no_network_connection_available);
        }
    }

    private void saveGroup_getFilesDir(String fileName, List<PersonalGroupBean> groups) {
        File file = new File(activity.getFilesDir(), fileName);
        try (FileOutputStream fileOutput = new FileOutputStream(file);
             ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput)) {
            objectOutput.writeObject(groups);
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (recordTask != null) {
            recordTask.cancel(true);
            recordTask = null;
        }
        if(new File(activity.getFilesDir(), "mainGroups").exists()){
            activity.deleteFile("mainGroups");
        }
        if(new File(activity.getFilesDir(), "attendGroups").exists()){
            activity.deleteFile("attendGroups");
        }
        if (new File(activity.getFilesDir(), "otherMemberId").exists()){
            activity.deleteFile("otherMemberId");
        }
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
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(vpMyRecord).popBackStack();
        }
        return true;
    }
}