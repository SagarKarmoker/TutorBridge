package com.cse489.tutorbridge.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.cse489.tutorbridge.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NetworkChangeListener extends BroadcastReceiver {
    private LottieAnimationView connectCheck;
    private BottomNavigationView navBar;
    private FrameLayout fragment_container;

    // Constructor to pass references of views
    public NetworkChangeListener(LottieAnimationView connectCheck, BottomNavigationView navBar, FrameLayout fragment_container) {
        this.connectCheck = connectCheck;
        this.navBar = navBar;
        this.fragment_container = fragment_container;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.isConnectedToInternet(context)) {
            // Hide the navBar and show the connectCheck view
            connectCheck.setVisibility(View.VISIBLE);
            //navBar.setVisibility(View.GONE);
            fragment_container.setVisibility(View.GONE);
        } else {
            // Show the navBar and hide the connectCheck view
            connectCheck.setVisibility(View.GONE);
            //navBar.setVisibility(View.VISIBLE);
            fragment_container.setVisibility(View.VISIBLE);
        }
    }
}