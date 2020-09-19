package com.example.jome17wave.jome_member;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_loginRegister.LoginActivity;
import com.example.jome17wave.MainActivity;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;

public class MemberProfileFragment extends Fragment {
    private static final String TAG = "MemberProfileFragment";
    private static final int REQ_LOGIN = 2;
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
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(false);

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
        //貼照片及資料
//        showMember();
        //設定click監聽器
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btConnectUs:
                        break;
                    case R.id.btLogOut:
                        //清空preferences檔
                        boolean preferencesClear = false;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            preferencesClear = activity.deleteSharedPreferences(Common.PREF_FILE);
                        }
                        // 確認preferences刪除後，轉跳到login頁面
                        if (preferencesClear == true){
                            Intent intentLoginActivity = new Intent(activity, LoginActivity.class);
//                        intentLoginActivity.setClass(activity, LoginActivity.class);
                            startActivity(intentLoginActivity);
                        }
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

    public void showMember(){
        String url = Common.URL_SERVER + "jome_member/LoginServlet";
        if (Common.usePreferences(activity, Common.PREF_FILE).equals(null)){
            jomeMember = new JomeMember();
        }else {
            String jsonMember = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            Log.d(TAG, jsonMember);
            jomeMember = new Gson().fromJson(jsonMember, JomeMember.class);
        }
        String memberID = jomeMember.getMember_id();
        int imageSize = getResources().getDisplayMetrics().widthPixels / 4;
        Bitmap bitmap = null;
        //檢查連線取照片
        if(Common.networkConnected(activity)){
            try {
                bitmap = new MemberImageTask(url, memberID, imageSize).execute().get();
            } catch (Exception e) {
                Log.d(TAG, e.toString());
            }

            if (bitmap == null){
                igMember.setImageResource(R.drawable.no_image);
            }else {
                igMember.setImageBitmap(bitmap);
            }
        }

        tvFriendList.setText(jomeMember.getFriendCount());
        tvScore.setText(String.valueOf(jomeMember.getScoreAverage()));
        tvGroupRecord.setText(jomeMember.getGroupCount());
    }

    @Override
    public void onStart() {
        super.onStart();
        Common.loginCheck(activity, REQ_LOGIN);
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