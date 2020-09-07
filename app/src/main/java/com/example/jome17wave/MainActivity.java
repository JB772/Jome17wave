package com.example.jome17wave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
<<<<<<< HEAD
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.Activity;
=======
import androidx.navigation.NavDestination;
import androidx.navigation.NavHostController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.graphics.drawable.ColorDrawable;
>>>>>>> justin_branch
import android.os.Bundle;

<<<<<<< HEAD
=======

import com.google.android.material.bottomnavigation.BottomNavigationItemView;

>>>>>>> 08bd34064e6b161e5729222300dcec74f21cdb4e
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
<<<<<<< HEAD
=======

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        NavController navController = Navigation.findNavController(this, R.id.fragment);
//        NavigationUI.setupWithNavController(bottomNavigationView, navController);


=======
        //設定BottomBar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

//        //設定appBar
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
>>>>>>> justin_branch
>>>>>>> 08bd34064e6b161e5729222300dcec74f21cdb4e

    }
}