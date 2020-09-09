package com.example.jome17wave.jome_map;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.jome17wave.MainActivity;
import com.example.jome17wave.R;
import com.google.android.gms.maps.MapView;

public class MapDetailFragment extends Fragment {
    private MainActivity activity;
    private TextView tvName, tvType, tvTidal, tvDirection, tvLevel;
    private NavController navController;
    private ImageView imageView18, imageView19;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (MainActivity) getActivity();
        setHasOptionsMenu(true);
        navController = Navigation.findNavController(activity, R.id.fragment);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        activity.setTitle("浪點資訊");
        return inflater.inflate(R.layout.fragment_map_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setTitle("浪點資訊");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        final NavController navController = Navigation.findNavController(view);
        Bundle bundle = getArguments();
        Map map = (Map) bundle.getSerializable("map");
        tvName = view.findViewById(R.id.tvName);
        tvType = view.findViewById(R.id.tvType);
        tvTidal = view.findViewById(R.id.tvTidal);
        tvDirection = view.findViewById(R.id.tvDirection);
        tvLevel = view.findViewById(R.id.tvLevel);



        tvName.setText(map.getName());
        tvType.setText(map.getType());
        tvTidal.setText(map.getTidal());
        tvDirection.setText(map.getDirection());
        tvLevel.setText(map.getLevel());

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                navController.popBackStack();
                return true;
            default:
                break;
        }
        return true;
    }
}