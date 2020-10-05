package com.example.jome17wave.jome_member;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class MemberProfileFragment extends Fragment {
    private static final String TAG = "MemberProfileFragment";
    private static final int REQ_LOGIN = 2;
    private MainActivity activity;
    private Bitmap bitmap = null;
    private ImageView igMember;
    private ImageButton btConnectUs, btLogOut;
    private ConstraintLayout clFriendList, clScore, clGroupRecord, clJoinRecord;
    private TextView tvFriendList, tvScore, tvGroupRecord, tvJoinRecord, tvMemberNickname;
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
        clJoinRecord.setVisibility(View.GONE);
        tvMemberNickname = view.findViewById(R.id.tvMemberNickname);
        tvFriendList = view.findViewById(R.id.tvFriendList);
        tvScore = view.findViewById(R.id.tvScore);
        tvGroupRecord = view.findViewById(R.id.tvGroupRecord);
        tvJoinRecord = view.findViewById(R.id.tvJoinRecord);
        //貼照片及資料
        showMember();
        //設定click監聽器
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.btConnectUs:
                        break;

                    case R.id.btLogOut:
                        //清空preferences檔
                        boolean preferencesClear = false;
                        boolean imageProfileClear = false;
                        preferencesClear = activity.deleteSharedPreferences(Common.PREF_FILE);
                        if (bitmap != null){
                            imageProfileClear = activity.deleteFile( "imageProfile");
                            // 確認preferences刪除後，轉跳到login頁面
                            if (preferencesClear == true && imageProfileClear == true){
                                activity.deleteFile("friends");
                                new MemberProfileFragment().onDestroy();
                                Intent intentLoginActivity = new Intent(activity, LoginActivity.class);
                                startActivity(intentLoginActivity);
                            }else {
                                Common.showToast(activity, R.string.no_network_connection_available);
                            }
                        }else {
                            if (preferencesClear == true){
                                activity.deleteFile("friends");
                                new MemberProfileFragment().onDestroy();
                                Intent intentLoginActivity = new Intent(activity, LoginActivity.class);
                                startActivity(intentLoginActivity);
                            }else {
                                Common.showToast(activity, R.string.no_network_connection_available);
                            }
                        }



                        break;
                    case R.id.clFriendList:
                        Navigation.findNavController(view).navigate(R.id.action_memberProfileFragment_to_friendsListFragment);
                        break;
                    case R.id.clScore:
                        Navigation.findNavController(view).navigate(R.id.action_memberProfileFragment_to_profileScoreFragment);
                        break;
                    case R.id.clGroupRecord:
                        Navigation.findNavController(view).navigate(R.id.action_memberProfileFragment_to_myRecordFragment);
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
        int imageSize = getResources().getDisplayMetrics().widthPixels / 3;
        if (Common.usePreferences(activity, Common.PREF_FILE).equals(null)){
            jomeMember = new JomeMember();
        }else {
            String jsonMember = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
            jomeMember = new Gson().fromJson(jsonMember, JomeMember.class);
        }
        String memberID = jomeMember.getMember_id();


        //先檢查手機有沒有存大頭貼，如果沒有再檢查連線取照片
        bitmap = loadFile_getFilesDir("imageProfile");
        Log.d(TAG, "bitmap1: " + bitmap);
        if ( bitmap == null){
            if(Common.networkConnected(activity)){
                try {
                    bitmap = new MemberImageTask(url, memberID, imageSize).execute().get();
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
                Log.d(TAG, "bitmap2: " + bitmap);
                if (bitmap == null){
                    igMember.setImageResource(R.drawable.no_image);
                }else {
                    igMember.setImageBitmap(bitmap);
                    saveFile_getFilesDir("imageProfile", bitmap);
                }
            }else {
                igMember.setImageResource(R.drawable.no_image);
            }
        }else {
            igMember.setImageBitmap(bitmap);
        }

        tvMemberNickname.setText(jomeMember.getNickname());
        tvFriendList.setText(jomeMember.getFriendCount() + " 人");
        Log.d(TAG, "ScoreAverage: " +jomeMember.getScoreAverage() );
        tvScore.setText(jomeMember.getScoreAverage() );
        tvGroupRecord.setText(String .valueOf(jomeMember.getGroupCount()));
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
                //轉到修改頁面
                Navigation.findNavController(igMember).navigate(R.id.action_memberProfileFragment_to_modifyProfileFragment);
                break;
            default:
                break;
        }
        return true;
    }

    private void saveFile_getFilesDir(String fileName, Bitmap bitmap){
        File file = new File(activity.getFilesDir(), fileName);
        Log.d(TAG, "getFilesDir() path: " + file.getPath());
        try (ObjectOutputStream ojOutStream = new ObjectOutputStream(new FileOutputStream(file))){
            ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baOutputStream);
            byte[] imageProfile = baOutputStream.toByteArray();
            ojOutStream.writeObject(imageProfile);
        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        }
    }

    private Bitmap loadFile_getFilesDir(String fileName){
        File file = new File(activity.getFilesDir(), fileName);
        try (ObjectInputStream ojInputStream = new ObjectInputStream(new FileInputStream(file))){
            Log.d(TAG, "getFilesDir() path:"+file.getPath());
            byte[] imageByte = (byte[]) ojInputStream.readObject();
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);

            return bitmap;

        } catch (FileNotFoundException e) {
            Log.d(TAG, e.toString());
        } catch (IOException e) {
            Log.d(TAG, e.toString());
        } catch (ClassNotFoundException e) {
            Log.d(TAG, e.toString());
        }
        return null;
    }
}