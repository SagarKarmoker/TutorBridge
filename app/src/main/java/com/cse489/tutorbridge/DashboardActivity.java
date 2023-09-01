package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.cse489.tutorbridge.chat.ChatMainActivity;
import com.cse489.tutorbridge.chat.char_history_activity;
import com.cse489.tutorbridge.utility.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity{
    LottieAnimationView connectCheck;
    BottomNavigationView navBar;
    Fragment selectedFragment;

    NetworkChangeListener networkChangeListener;
    FrameLayout fragment_container;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        navBar = findViewById(R.id.bottom_navigation);
        connectCheck = findViewById(R.id.connectCheck);
        fragment_container = findViewById(R.id.fragment_container);

        // Register the network change listener
        networkChangeListener = new NetworkChangeListener(connectCheck, navBar, fragment_container);
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);

        // By default fragment is home
        selectedFragment = new HomeFragment();
        if (selectedFragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, selectedFragment)
                    .commit();
        }

        navBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if(R.id.home == item.getItemId()){
                    selectedFragment = new HomeFragment();
                }
                if(R.id.chat == item.getItemId()){
                    //selectedFragment = new ChatPageFragment(); //update it new ChatFragment()
                    Intent i = new Intent(DashboardActivity.this, char_history_activity.class);
                    startActivity(i);
                }
                if(R.id.history == item.getItemId()){
                    selectedFragment = new HistoryFragment();
                }
                if(R.id.profile == item.getItemId()){
                    selectedFragment = new ProfileFragment();
                }

                if (selectedFragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment)
                            .commit();
                    return true; // Return true to indicate the item was selected
                }

                return false; // Return false if no fragment was selected
            }
        });
    }

    @Override
    protected void onStart() {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,intentFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }

}