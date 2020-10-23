package com.example.jome17wave;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jome17wave.main.MainActivity;
import com.example.jome17wave.task.MemberImageTask;

import java.util.List;

public class RatingPageFragment extends Fragment {
    private String TAG = "TAG_RatingPage";
    private MainActivity activity;
    private ImageView ivRatedGroupImage;
    private TextView tvRatedGroupName;
    private Button btRatingSubmit;
    private Bitmap bitmap;
    private RecyclerView rvRatingList;
    private List<MemberImageTask> imageTasks;
//    private List<評分Bean> ratings;


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
        return inflater.inflate(R.layout.fragment_rating_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.ratingPageTitle);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);  //左上角返回箭頭

        ivRatedGroupImage = view.findViewById(R.id.ivRatedGroupImage);
        tvRatedGroupName = view.findViewById(R.id.tvRatedGroupName);
        btRatingSubmit = view.findViewById(R.id.btRatingSubmit);

        

    }
}