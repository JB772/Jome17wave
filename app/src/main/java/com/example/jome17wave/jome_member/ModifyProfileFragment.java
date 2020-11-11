package com.example.jome17wave.jome_member;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.jome17wave.Common;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;

public class ModifyProfileFragment extends Fragment {
    private static final String TAG = "ModifyProfileFragment";
    private static final int REQ_TAKE_PICTURE = 101;
    private static final int REQ_PICK_PICTURE = 102;
    private static final int REQ_CROP_PICTURE = 103;
    private static final int PER_EXTERNAL_STORAGE = 201;
    private Uri contentUri;
    private File file;
    private MainActivity activity;
    private JomeMember loginMember;
    private ImageView imageModify;
    private TextView tvAccount, tvNickname, tvGender;
    private EditText etModifyPW, etCheckPw, etModifyNn;
    private ImageButton ibtCamera;
    private byte[] image = null;
    private CommonTask updateLatLngTask;


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
        if (new File(activity.getFilesDir(), "imageProfile").exists()){
            imageModify.setImageBitmap(loadFile_getFilesDir("imageProfile"));
        }else {
            imageModify.setImageResource(R.drawable.no_image);
        }

        ibtCamera = view.findViewById(R.id.ibtCamera);
        ibtCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File dir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                if (dir != null && !dir.exists()) {
                    if (!dir.mkdirs()) {
                        Log.e(TAG, getString(R.string.textDirNotCreated));
                        return;
                    }
                }
                file = new File(dir, "picture.jpg");
                contentUri = FileProvider.getUriForFile(
                        activity, activity.getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri);
                if (intent.resolveActivity(activity.getPackageManager()) != null) {
                    startActivityForResult(intent, REQ_TAKE_PICTURE);
                } else {
                    Toast.makeText(activity, R.string.textNoCameraAppFound,
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvAccount = view.findViewById(R.id.tvAccount);
        tvNickname = view.findViewById(R.id.tvNickname);
        tvGender = view.findViewById(R.id.tvGender);
        etModifyPW = view.findViewById(R.id.etModifyPW);
        etCheckPw = view.findViewById(R.id.etCheckPw);
        etModifyNn = view.findViewById(R.id.etModifyNn);
        //個人資料
//        String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
//        loginMember = new Gson().fromJson(memberStr, JomeMember.class);
        loginMember = Common.getSelfFromPreference(activity);
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

        imageModify.setOnClickListener(v -> {
            askExternalStoragePermission();
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(activity.getPackageManager()) != null){
                startActivityForResult(intent, REQ_PICK_PICTURE);
            }else {
                Toast.makeText(activity, R.string.no_network_connection_available, Toast.LENGTH_SHORT);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case REQ_TAKE_PICTURE:
                    crop(contentUri);
                    break;
                case REQ_PICK_PICTURE:
                    crop(intent.getData());
                    break;
                case REQ_CROP_PICTURE:
                    handleCropResult(intent);
                    break;

            }
        }
    }

    private void crop(Uri sourceImageUri) {
        File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        file = new File(file, "picture_cropped.jpg");
        Uri destinationUri = Uri.fromFile(file);
        //來源路徑，存放路徑
        UCrop.of(sourceImageUri, destinationUri)
//                .withAspectRatio(16, 9) // 設定裁減比例
//                .withMaxResultSize(500, 500) // 設定結果尺寸不可超過指定寬高
                .start(activity, this, REQ_CROP_PICTURE);
    }

    //抓到圖就放到view及存byte[]用來更新
    private void handleCropResult(Intent intent) {
        Uri resultUri = UCrop.getOutput(intent);
//        Log.d(TAG, "resultUri :" + resultUri.toString());
        if (resultUri == null) {
            return;
        }
        Bitmap bitmap = null;
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
                bitmap = BitmapFactory.decodeStream(
                        activity.getContentResolver().openInputStream(resultUri));
            } else {
                ImageDecoder.Source source =
                        ImageDecoder.createSource(activity.getContentResolver(), resultUri);
                bitmap = ImageDecoder.decodeBitmap(source);
            }
            ByteArrayOutputStream byteArrayOutput = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutput);
            image = byteArrayOutput.toByteArray();
            Log.d(TAG, "handleCropResult 222: image" + image.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            imageModify.setImageBitmap(bitmap);
            Log.d(TAG, "imageModify can save 228:" + bitmap.toString());
        } else {
            imageModify.setImageResource(R.drawable.no_image);
        }
    }

    private void askExternalStoragePermission() {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE
        };

        int result = ContextCompat.checkSelfPermission(activity, permissions[0]);
        if (result == PackageManager.PERMISSION_DENIED) {
            requestPermissions(permissions, PER_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PER_EXTERNAL_STORAGE) {
            // 如果user不同意將資料儲存至外部儲存體的公開檔案，就將儲存按鈕設為disable
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(activity, R.string.textShouldGrant, Toast.LENGTH_SHORT).show();
                imageModify.setEnabled(false);
            } else {
                imageModify.setEnabled(true);
            }
        }
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
                if (submitModifyData() == 1){
                    Bitmap bitmapNewProfile = BitmapFactory.decodeByteArray(image, 0, image.length);
                    saveFile_getFilesDir("imageProfile", bitmapNewProfile);
                    Common.showToast(activity, R.string.successModify);
                    //反回前頁
                    Navigation.findNavController(imageModify).popBackStack();
                }else {
                    Common.showToast(activity, R.string.no_network_connection_available);
                }

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

    private int submitModifyData(){
        String nickname = etModifyNn.getText().toString().trim();
        String modifyPw = etModifyPW.getText().toString().trim();
        String checkPw = etCheckPw.getText().toString().trim();
        if (nickname.equals("")|| nickname.isEmpty()){
            Common.showToast(activity, R.string.Nickname);
        }else {
            loginMember.setNickname(nickname);
        }
        if (modifyPw.equals("")|| modifyPw.isEmpty()){
            etCheckPw.setError(getString(R.string.passwordIsError));
            Common.showToast(activity, R.string.passwordIsError);
            return -1;
        }
        if (checkPw.equals("")|| checkPw.isEmpty()){
            etCheckPw.setError(getString(R.string.passwordIsError));
            Common.showToast(activity, R.string.passwordIsError);
            return -1;
        }
        if (modifyPw.equals(checkPw)){
            loginMember.setPassword(modifyPw);
        }else {
            etCheckPw.setError(getString(R.string.passwordIsError));
            Common.showToast(activity, R.string.passwordIsError);
            return -1;
        }

        //上傳更新
        int resultCode = -1;
        if (Common.networkConnected(activity) == true){
            String url = Common.URL_SERVER + "jome_member/LoginServlet";
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", "update");
            jsonObject.addProperty("memberUp", new Gson().toJson(loginMember));
            if (image != null){
                jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
            }
            String jsonIn = "";
            updateLatLngTask = new CommonTask(url, jsonObject.toString());
            try {
                jsonIn = updateLatLngTask.execute().get();
            } catch (ExecutionException e) {
                Log.e(TAG, e.toString());
            } catch (InterruptedException e) {
                Log.e(TAG, e.toString());
            }
            jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
            resultCode = jsonObject.get("resultCode").getAsInt();
        }

        return resultCode;
    }
}