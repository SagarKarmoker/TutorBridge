package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.cse489.tutorbridge.chat.ChatMainActivity;
import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.cse489.tutorbridge.modal.User;
import com.cse489.tutorbridge.chat.char_history_activity;
import com.cse489.tutorbridge.utility.NetworkChangeListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DashboardActivity extends AppCompatActivity{
    LottieAnimationView connectCheck;
    BottomNavigationView navBar;
    Fragment selectedFragment;

    NetworkChangeListener networkChangeListener;
    FrameLayout fragment_container;

    FirebaseFirestore db;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    FirebaseAuth auth;
    String json;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        preferences = this.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        edit = preferences.edit();

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
                    //selectedFragment = new ProfileFragment();
                    selectedFragment = new MyProfileFragment();

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

        // Serialize the object to JSON
        gson = new Gson();

        String docPath = preferences.getString("docPath", "");
        Log.d("DOCPAth", docPath);

        try {
            // Query "mentor_list" collection
            db.collection("mentor_list")
                    .whereEqualTo("uuid", auth.getCurrentUser().getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            // If the document is found in "mentor_list" collection
                            if (!queryDocumentSnapshots.isEmpty()) {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                    MentorProfileClass mentor = document.toObject(MentorProfileClass.class); //todo got data
                                    System.out.println(mentor.getUuid());
                                    System.out.println(mentor.toString());
                                    json = gson.toJson(mentor);
                                    edit.putString("CurrentUser", json);
                                    edit.putBoolean("isMentor", true);
                                    edit.apply();
                                }
                            } else {
                                // Document not found in "mentor_list" collection
                                // Check the "user_info" collection
                                checkUserInfoCollection(docPath);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }

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

    // Function to check "user_info" collection if document is not found in "mentor_list"
    private void checkUserInfoCollection(String docPath) {
        db.collection("user_info")
                .whereEqualTo("uuid", auth.getCurrentUser().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // If the document is found in "user_info" collection
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Access document data using document.getData()
                                Map<String, Object> data = document.getData();
                                // Access specific fields in the document
                                String name = (String) data.get("name");
                                String uuid = (String) data.get("uuid");
                                String date = (String) data.get("date");
                                User u = new User(uuid, name, date);
                                json = gson.toJson(u);
                                edit.putString("CurrentUser", json);
                                edit.putBoolean("isMentor", false);
                                edit.apply();
                                System.out.println("Found in user_info: " + name + uuid + date);
                            }
                        } else {
                            // Document not found in "user_info" collection either
                            System.out.println("Document not found in user_info collection.");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle failure
                    }
                });
    }
}