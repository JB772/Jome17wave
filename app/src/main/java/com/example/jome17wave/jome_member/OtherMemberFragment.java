package com.example.jome17wave.jome_member;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.task.MemberImageTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutionException;

public class OtherMemberFragment extends Fragment {
    private static final String TAG = "OtherMemberFragment";
    private MainActivity activity;
    private ImageView imageFProfile;
    private TextView tvFDataName, tvAverageScore, tvFriendCount, tvAssembleCount, tvJointCount;
    private ImageButton ibtFriendStory, ibtOtherMessage, ibtFriendAdd, ibtFriendPandding;
    private MemberImageTask memberImageTask;
    private JomeMember friend;

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
        //四顆imageButton
        ibtFriendStory = view.findViewById(R.id.ibtFriendStory);
        ibtOtherMessage = view.findViewById(R.id.ibtOtherMessage);
        ibtFriendAdd = view.findViewById(R.id.ibtFriendAdd);
        ibtFriendPandding = view.findViewById(R.id.ibtFriendPandding);

        ibtFriendStory.setVisibility(View.GONE);
        ibtOtherMessage.setVisibility(View.GONE);
        ibtFriendAdd.setVisibility(View.GONE);
        ibtFriendPandding.setVisibility(View.GONE);
        //四格資訊
        tvFDataName = view.findViewById(R.id.tvFDataName);
        tvAverageScore = view.findViewById(R.id.tvAverageScore);
        ;
        tvFriendCount = view.findViewById(R.id.tvFriendCount);
        ;
        tvAssembleCount = view.findViewById(R.id.tvAssembleCount);
        ;
        tvJointCount = view.findViewById(R.id.tvJointCount);
        ;
        showMember();
        toolbar.setTitle(friend.getNickname());

        View.OnClickListener btOnclick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.ibtFriendStory:
                        File file = new File(activity.getFilesDir(), "otherMemberId");
                        try {
                            FileOutputStream fileOutput = new FileOutputStream(file);
                            ObjectOutputStream objectOutput = new ObjectOutputStream(fileOutput);
                            objectOutput.writeObject(friend.getMember_id());
                        } catch (FileNotFoundException e) {
                            Log.e(TAG, e.toString());
                        } catch (IOException e) {
                            Log.e(TAG, e.toString());
                        }
                        Navigation.findNavController(view).navigate(R.id.action_otherMemberFragment_to_myRecordFragment2);
                        break;
                    default:
                        break;
                }
            }
        };
        ibtFriendStory.setOnClickListener(btOnclick);
    }

    public void showMember() {
        JsonObject jsonObject = new JsonObject();
        String jsonIn = "";
        jsonIn = (String)openFile_getFileDir("otherMember");
        jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
        int idGetResult = -1;
        int relationCode = -1;
        idGetResult = jsonObject.get("idGetResult").getAsInt();
        if (idGetResult == 1){
            friend = new Gson().fromJson(jsonObject.get("idMember").getAsString(), JomeMember.class);
            relationCode = jsonObject.get("relationCode").getAsInt();
        }else {
            friend = new JomeMember();
        }
        switch (relationCode){
            case 1:     //朋友
                ibtFriendStory.setVisibility(View.VISIBLE);
                ibtOtherMessage.setVisibility(View.VISIBLE);
                break;
            case 2:     //有拒絕記錄
                ibtFriendAdd.setVisibility(View.VISIBLE);
                break;
            case 3:     //我等別人回應
                ibtFriendPandding.setVisibility(View.VISIBLE);
                break;
            case 4:     //別人邀請，等待我回應
                break;
            default:
                ibtFriendAdd.setVisibility(View.VISIBLE);
                break;
        }
        tvFDataName.setText(friend.getNickname());
        tvFriendCount.setText(friend.getFriendCount() + " 人");
        tvAverageScore.setText(friend.getScoreAverage());
        tvAssembleCount.setText(String.valueOf(friend.getGroupCount()));
        tvJointCount.setText(friend.getGroupCount());

        //拿圖
        Bitmap bitmap = null;
        if (new File(activity.getFilesDir(), "OMProfile").exists()){
            byte[] imageByte = (byte[]) openFile_getFileDir("OMProfile");
            bitmap = BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length);
            imageFProfile.setImageBitmap(bitmap);
        }else {
            if (Common.networkConnected(activity)) {
                String url = Common.URL_SERVER + "jome_member/LoginServlet";
                String friendId = friend.getMember_id();
                int imageSize = getResources().getDisplayMetrics().widthPixels / 3;

                try {
                    memberImageTask = new MemberImageTask(url, friendId, imageSize);
                    bitmap = memberImageTask.execute().get();
                } catch (ExecutionException e) {
                    Log.e(TAG, e.toString());
                } catch (InterruptedException e) {
                    Log.e(TAG, e.toString());
                }
                Log.d(TAG, "bitmap2: " + bitmap);
                if (bitmap == null) {
                    imageFProfile.setImageResource(R.drawable.no_image);
                } else {
                    imageFProfile.setImageBitmap(bitmap);
                    saveFile_getFilesDir("OMProfile", bitmap);
                }
            } else {
                imageFProfile.setImageResource(R.drawable.no_image);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.member_center_tool_bar, menu);
        menu.findItem(R.id.member_check_item).setVisible(false);
        menu.findItem(R.id.member_settin_item).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Navigation.findNavController(imageFProfile).popBackStack();

        }
        return true;
    }

    private Object openFile_getFileDir(String fileName){
        File file = new File(activity.getFilesDir(), fileName);
        try {
            FileInputStream fileInput = new FileInputStream(file);
            ObjectInputStream objectInput = new ObjectInputStream(fileInput);
            return  objectInput.readObject();
        } catch (FileNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        } catch (ClassNotFoundException e) {
            Log.e(TAG, e.toString());
        }
        return  null;
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

    @Override
    public void onStop() {
        super.onStop();
        if (memberImageTask != null){
            memberImageTask.cancel(true);
            memberImageTask = null;
        }
    }
}