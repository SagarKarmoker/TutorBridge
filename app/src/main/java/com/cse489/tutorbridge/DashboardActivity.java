package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class DashboardActivity extends AppCompatActivity {
    BottomNavigationView navBar;
    Fragment selectedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        navBar = findViewById(R.id.bottom_navigation);

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
                    selectedFragment = new ChatPageFragment(); //update it new ChatFragment()
                }
                if(R.id.history == item.getItemId()){
                    selectedFragment = new ItemFragment();
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
}