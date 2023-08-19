package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class initialActivity extends AppCompatActivity {
    private Button initialSignup,initialLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial);

        initialSignup = findViewById(R.id.initialSignup);
        initialLogin = findViewById(R.id.initialLogin);


        initialSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initial page to signup page
                Intent i1 = new Intent(initialActivity.this, SignupActivity.class);
                startActivity(i1);
            }
        });
        initialLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //initial page to signup page
                Intent i2 = new Intent(initialActivity.this, LoginActivity.class);
                startActivity(i2);
            }
        });
    }
}