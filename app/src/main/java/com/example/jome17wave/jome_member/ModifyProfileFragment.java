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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.Common;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ModifyProfileFragment extends Fragment {
    private static final String TAG = "ModifyProfileFragment";
    private MainActivity activity;
    private JomeMember loginMember;
    private ImageView imageModify;
    private TextView tvAccount, tvNickname, tvGender;
    private EditText etModifyPW, etCheckPw, etModifyNn;


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
        return inflater.inflate(R.layout.fragment_modify_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.memberModify);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //大頭貼
        imageModify = view.findViewById(R.id.imageModify);
        Bitmap bitmap = loadFile_getFilesDir("imageProfile");
        if(bitmap == null){
            imageModify.setImageResource(R.drawable.no_image);
        }else {
            imageModify.setImageBitmap(bitmap);
        }

        tvAccount = view.findViewById(R.id.tvAccount);
        tvNickname = view.findViewById(R.id.tvNickname);
        tvGender = view.findViewById(R.id.tvGender);
        etModifyPW = view.findViewById(R.id.etModifyPW);
        etCheckPw = view.findViewById(R.id.etCheckPw);
        etModifyNn = view.findViewById(R.id.etModifyNn);
        //個人資料
        String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
        loginMember = new Gson().fromJson(memberStr, JomeMember.class);
        int genderStr = -1;
        switch (loginMember.getGender()){
            case 1:
                genderStr = R.string.male;
                break;
            case 2:
                genderStr = R.string.female;
                break;
            case 3:
                genderStr = R.string.thirdsex;
                break;
            default:
                break;
        }
        tvGender.setText(genderStr);
        tvAccount.setText(loginMember.getAccount());
        tvNickname.setText(loginMember.getNickname());
        etModifyPW.setText(loginMember.getPassword());
        etModifyNn.setText(loginMember.getNickname());






    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.member_center_tool_bar, menu);
        menu.findItem(R.id.member_settin_item).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                Navigation.findNavController(imageModify).popBackStack();
                break;
            case R.id.member_check_item:
                //送出修改資料，如果成功則秀toast
                Common.showToast(activity, R.string.successModify);
                //反回前頁
                Navigation.findNavController(imageModify).popBackStack();
                break;
            default:
                break;
        }
        return true;
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

    private JomeMember submitModifyData(){

        String nickname = etModifyNn.getText().toString().trim();
        String modifyPw = etModifyPW.getText().toString().trim();
        String checkPw = etCheckPw.getText().toString().trim();
        if (nickname.equals("")|| nickname.isEmpty()){
            Common.showToast(activity, R.string.Nickname);
        }else {
            loginMember.setNickname(nickname);
        }
        if (modifyPw.equals("")|| modifyPw.isEmpty()){
            Common.showToast(activity, R.string.passwordIsError);
        }
        if (checkPw.equals("")|| checkPw.isEmpty()){
            Common.showToast(activity, R.string.passwordIsError);
        }
        if (modifyPw.equals(checkPw)){
            loginMember.setPassword(modifyPw);
        }else {
            Common.showToast(activity, R.string.passwordIsError);
        }

        return loginMember;
    }
}