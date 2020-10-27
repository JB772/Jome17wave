package com.example.jome17wave.jome_group;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.jome17wave.R;
import com.example.jome17wave.main.MainActivity;

import java.util.Calendar;

public class AddGroupFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private final static String TAG = "TAG_AddGroupFragment";
    private MainActivity activity;
    private ImageView ivAddGroup;
    private ImageButton addGroupImage;
    private TextView tvGroupDate, tvGroupTime;
    private EditText addGroupName, addGroupPeople, addGroupNotice;
    private byte[] image;
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQ_PICK_PICTURE = 1;
    private static final int REQ_CROP_PICTURE = 2;
    private static int year, month, day, hour, minute;


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


        ivAddGroup = view.findViewById(R.id.ivAddGroup);
        addGroupName = view.findViewById(R.id.addGroupName);
        addGroupPeople = view.findViewById(R.id.addGroupPeople);
        addGroupNotice = view.findViewById(R.id.addGroupNotice);

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

                        }
                    }
                })
                        .setTitle("選擇圖片來源")
                        .setCancelable(true)
                        .show();
            }
        });
    }

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

    private void updateDisplay() {
        tvGroupDate.setText(new StringBuilder().append(year).append("-")
        .append(pad(month + 1)).append("-").append(pad(day)));

        tvGroupTime.setText(new StringBuilder().append(hour).append(":")
        .append(pad(minute)));
    }

    private String pad(int number) {
        if (number >= 10) {
            return String.valueOf(number);
        } else {
            return "0" + number;
        }
    }
}