package com.example.jome17wave.jome_group;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.jome17wave.Common;
import com.example.jome17wave.FcmSender;
import com.example.jome17wave.R;
import com.example.jome17wave.jome_Bean.JomeMember;
import com.example.jome17wave.jome_Bean.PersonalGroupBean;
import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.CommonTask;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AddGroupFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private final static String TAG = "TAG_AddGroupFragment";
    private MainActivity activity;
    private ImageView ivAddGroup;
    private ImageButton addGroupImage;
    private Spinner spSurfPoint;
    private int surfPoint = 1;
    private TextView tvGroupDate, tvGroupTime;
    private EditText addGroupName, addGroupPeople, addGroupNotice;
    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private static int year, month, day, hour, minute;
    private Uri imageUri;
    private JomeMember member;
    private String pickDateTime;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_group, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("新增揪團");
        final NavController navController = Navigation.findNavController(view);

        Button BtFast = view.findViewById(R.id.BtFast);
        BtFast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addGroupName.setText("大家一起出來浪");
                addGroupPeople.setText("4");
                addGroupNotice.setText("請自備盥洗用具以及毛巾");
            }
        });


        ivAddGroup = view.findViewById(R.id.ivAddGroup);
        ivAddGroup.setImageResource(R.drawable.surf1);
        addGroupName = view.findViewById(R.id.addGroupName);
        addGroupPeople = view.findViewById(R.id.addGroupPeople);
        addGroupNotice = view.findViewById(R.id.addGroupNotice);
        spSurfPoint = view.findViewById(R.id.spSurfPoint);
        spSurfPoint.setSelection(0, true);
        spSurfPoint.setOnItemSelectedListener(listener);

        tvGroupDate = view.findViewById(R.id.tvGroupDate);
        tvGroupDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                        AddGroupFragment.this,
                        AddGroupFragment.year,
                        AddGroupFragment.month,
                        AddGroupFragment.day);

                DatePicker datePicker = datePickerDialog.getDatePicker();
                Calendar calendar = Calendar.getInstance();
                datePicker.setMinDate(calendar.getTimeInMillis());
                calendar.add(Calendar.MONTH, 6);
                datePicker.setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();
            }
        });

        tvGroupTime = view.findViewById(R.id.tvGroupTime);
        tvGroupTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, AddGroupFragment.this,
                        AddGroupFragment.hour, AddGroupFragment.minute, false).show();
            }
        });
        showNow();
        /* --------------------------- 新增照片(可拍照或選照片) -------------------------- */
        addGroupImage = view.findViewById(R.id.addGroupImage);
        addGroupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                final String[] photo = {"相機", "相簿"};
                builder.setItems(photo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            File file = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                            file = new File(file, "picture.jpg");
                            imageUri = FileProvider.getUriForFile(activity, activity.getOpPackageName() + ".provider", file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            if (intent.resolveActivity(activity.getPackageManager()) != null) {
                                startActivityForResult(intent, REQ_TAKE_PICTURE);
                            } else {
                                Common.showToast(activity, R.string.textNoCameraAppFound);
                            }
                        } else {
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(intent, REQ_PICK_PICTURE);
                        }
                    }
                })
                        .setTitle("選擇圖片來源")
                        .setCancelable(true)
                        .show();
            }
        });
        /* --------------------------- 新增照片(可拍照或選照片) -------------------------- */

        Button btAddNewGroup = view.findViewById(R.id.btAddNewGroup);
        btAddNewGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memberStr = Common.usePreferences(activity, Common.PREF_FILE).getString("loginMember", "");
                member = new Gson().fromJson(memberStr, JomeMember.class);
                String memberId = member.getMemberId();
                boolean textError = true;
                String name = addGroupName.getText().toString().trim();
                String people = addGroupPeople.getText().toString().trim();
                String notice = "";
//                String surfPoint = spSurfPoint.toString().trim();
//                int intSurfPoint = 0;
//                switch (surfPoint){
//                    case "金山中角灣浪點":
//                        intSurfPoint = 1;
//                        break;
//                    case "翡翠灣浪點":
//                        intSurfPoint = 2;
//                        break;
//                    case "福隆浪點":
//                        intSurfPoint = 3;
//                        break;
//                    case "雙獅浪點":
//                        intSurfPoint = 4;
//                        break;
//                    case "烏石港北堤浪點":
//                        intSurfPoint = 5;
//                        break;
//                    case "金樽浪點":
//                        intSurfPoint = 6;
//                        break;
//                    case "南灣浪點":
//                        intSurfPoint = 7;
//                        break;
//                    case "佳樂水浪點":
//                        intSurfPoint = 8;
//                        break;
//                    case "漁光島馬場浪點":
//                        intSurfPoint = 9;
//                        break;
//                    case "竹南浪點":
//                        intSurfPoint = 10;
//                        break;
//
//                }

                if (name.isEmpty()) {
                    addGroupName.setError("請輸入團名");
                    textError = false;
                }
                if (tvGroupDate.length() <= 0) {
                    textError = false;
                    Common.showToast(activity, R.string.addGroupDate);
                }
                if (tvGroupTime.length() <= 0) {
                    textError = false;
                    Common.showToast(activity, R.string.addGroupTime);
                }
                if (people.isEmpty()) {
                    addGroupPeople.setError("請輸入人數");
                    textError = false;
                }
                if (!textError) {
                    return;
                }
                if (!addGroupNotice.getText().toString().isEmpty()) {
                    notice = addGroupNotice.getText().toString();
                }

//                String assembleDate = tvGroupDate.getText().toString().trim();
//                String assembleTime = tvGroupTime.getText().toString().trim();

                String assembleDateTime = pickDateTime;
                Log.d(TAG, "assembleDateTime: " + assembleDateTime);
                String groupEndTime = Common.getGroupEndTime(assembleDateTime);
                String SignUpEndTime = Common.getSignUpEnd(assembleDateTime);

                if (Common.networkConnected(activity)) {
                    String url = Common.URL_SERVER + "/jome_member/GroupOperateServlet";
                    PersonalGroupBean personalGroupBean = new PersonalGroupBean(memberId, 1, 1, Common.getDateTimeId(), name, assembleDateTime,
                            groupEndTime, SignUpEndTime, surfPoint, Integer.valueOf(people), 1, notice);
                    JsonObject jsonObject = new JsonObject();
                    jsonObject.addProperty("action", "creatAGroup");
                    jsonObject.addProperty("inGroup", new Gson().toJson(personalGroupBean));
                    if (image != null) {
                        jsonObject.addProperty("imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
                    } else {
                        Common.showToast(activity, R.string.addImagePlease);
                        return;
                    }
                    int count = 0;
                    String jsonIn = "";
                    try {
                        jsonIn = new CommonTask(url, jsonObject.toString()).execute().get();
                    } catch (Exception e) {
                        Log.e(TAG, e.toString());
                    }
                    jsonObject = new Gson().fromJson(jsonIn, JsonObject.class);
                    int createResult = jsonObject.get("creatResult").getAsInt();
                    if (createResult == 1) {
                        Common.showToast(activity, R.string.createGroupSuccessfully);
                        FcmSender fcmSender = new FcmSender();
                        fcmSender.scoreFcmSender(activity, personalGroupBean);
                    } else {
                        Common.showToast(activity, R.string.createGroupFail);
                    }
                } else {
                    Common.showToast(activity,R.string.textNoNetwork);
                }
                navController.popBackStack();
            }
        });
    }

    Spinner.OnItemSelectedListener listener = new Spinner.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            spSurfPoint.setSelection(position, true);
            surfPoint = position +1;
//            surfPoint = position + 1;
            Log.d(TAG, "surfPoint123: " + position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    /* --------------------------- 選擇日期與時間 -------------------------- */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        AddGroupFragment.year = year;
        AddGroupFragment.month = month;
        AddGroupFragment.day = day;
        updateDisplay();
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        AddGroupFragment.hour = hour;
        AddGroupFragment.minute = minute;
        updateDisplay();
    }

    private void showNow() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
    }


//    String assembleTime = tvGroupTime.getText().toString().trim();
//    String assembleDate = tvGroupDate.getText().toString().trim();
//    String groupEndTime = Common.getGroupEndTime(assembleTime);



    private void updateDisplay() {
        tvGroupDate.setText(new StringBuilder().append(year).append("-")
        .append(pad(month + 1)).append("-").append(pad(day)));


        tvGroupTime.setText(new StringBuilder().append(hour).append(":")
        .append(pad(minute)));

        pickDateTime = new StringBuilder().append(year).append("-")
                .append(pad(month + 1)).append("-").append(pad(day)).append(" ").append(hour).append(":")
                .append(pad(minute)).toString();

    }

    private String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        } else {
            return "0" + number;
        }
    }
    /* --------------------------- 選擇日期與時間 -------------------------- */

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_TAKE_PICTURE:
                    crop(imageUri);
                case REQ_PICK_PICTURE:
                    crop(intent.getData());
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
        UCrop.of(sourceImageUri, destinationUri).start(activity, this, REQ_CROP_PICTURE);
    }

    private void handleCropResult(Intent intent) {
        Uri resultUri = UCrop.getOutput(intent);
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
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            image = out.toByteArray();
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
        if (bitmap != null) {
            ivAddGroup.setImageBitmap(bitmap);
        } else {
            ivAddGroup.setImageResource(R.drawable.no_image);
        }
    }

}