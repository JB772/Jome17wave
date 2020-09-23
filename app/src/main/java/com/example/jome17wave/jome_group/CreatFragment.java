package com.example.jome17wave.jome_group;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.example.jome17wave.task.ImageTask;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CreatFragment extends Fragment {
    private final static String TAG = "TAG_CreatFragment";
    private List<ImageTask> imageTasks;
    private MainActivity activity;
    private ImageView ivPhoto, btPickPicture, title, date, ivtime, location, Gender, bt_add;
    private EditText etTitle, etDate, etTime;
    private TextView tvLocation, tvGender;
    private DatePickerDialog mDatePickerDialog;
    private Spinner mSpnLocation;
    private byte[] image;
    private static final int REQ_PICK_IMAGE = 1;
    private int mYear, mMonth, mDay, mHour, mMinute;
    Button btnDatePicker, btnTimePicker;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        Spinner spinner = new Spinner(activity);
        final String[] location = {"新北市", "金山浪點（中角灣)", "福隆浪點", "宜蘭縣", "烏石港浪點（北堤）", "外澳浪點", "台東縣", "金樽浪點"};
        ArrayAdapter<String> LocationList = new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item,location);
        spinner.setAdapter(LocationList);

    }





    @NonNull
    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_creat, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivPhoto = view.findViewById(R.id.ivPhoto);
        btPickPicture = view.findViewById(R.id.btPickPicture);
        title = view.findViewById(R.id.title);
        date = view.findViewById(R.id.ivDate);
        ivtime = view.findViewById(R.id.ivTime);
        location = view.findViewById(R.id.location);
        Gender = view.findViewById(R.id.Gender);
        bt_add = view.findViewById(R.id.bt_add);
        etTitle = view.findViewById(R.id.etTitle);
        etDate = view.findViewById(R.id.etDate);
        etTime = view.findViewById(R.id.etTime);
        tvLocation = view.findViewById(R.id.tvLocation);
        tvGender = view.findViewById(R.id.tvGender);
        btnDatePicker = (Button) view.findViewById(R.id.btn_date);
        btnTimePicker = (Button) view.findViewById(R.id.btn_time);

        Button btn_date = view.findViewById(R.id.btn_date);
        btn_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (v == btnDatePicker) {

                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(activity,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    etDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();
                }
            }
        });

        Button btn_time = view.findViewById(R.id.btn_time);
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == btnTimePicker) {

                    // Get Current Time
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(activity,
                            new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                    etTime.setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, false);
                    timePickerDialog.show();
                }
            }
        });
    }
}
