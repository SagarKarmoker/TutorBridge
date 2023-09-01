package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

public class initialActivity extends AppCompatActivity {
    private Button initialSignup,initialLogin;
    private ProgressBar progressBar;
    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        initialSignup = findViewById(R.id.initialSignup);
        initialLogin = findViewById(R.id.initialLogin);
        progressBar = findViewById(R.id.progressBar);


        initialSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                //initial page to signup page
                Intent i1 = new Intent(initialActivity.this, SignupActivity.class);
                startActivity(i1);
                progressBar.setVisibility(View.GONE);
            }
        });
        initialLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                //initial page to signup page
                Intent i2 = new Intent(initialActivity.this, LoginActivity.class);
                startActivity(i2);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        preferences = this.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        if (preferences.getBoolean("TutorIsLoggedIn", false)){
            Intent i = new Intent(initialActivity.this, DashboardActivity.class);
            startActivity(i);
        }
    }
}
