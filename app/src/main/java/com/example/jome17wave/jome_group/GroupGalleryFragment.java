package com.example.jome17wave.jome_group;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jome17wave.R;

import java.util.ArrayList;

public class GroupGalleryFragment extends AppCompatActivity {
    private final String image_titles[] = {
            "surf1",
            "surf2",
            "surf3",

    };

    private final Integer image_ids[] = {
            R.drawable.surf1,
            R.drawable.surf2,
            R.drawable.surf3,

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<CreateList> createList = prepareData();
        MyAdapter adapter = new MyAdapter(getApplicationContext(), createList);
        recyclerView.setAdapter(adapter);
    }
    private ArrayList<CreateList> prepareData() {

        ArrayList<CreateList> theimage = new ArrayList<>();
        for (int i = 0; i < image_titles.length; i++) {
            CreateList createlist = new CreateList();
            createlist.setImage_title(image_titles[i]);
            createlist.setImage_id(image_ids[i]);
            theimage.add(createlist);
        }
        return theimage;
    }
}
