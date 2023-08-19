package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    private ImageView ivBackArrowL;
    private TextView btnToggleL,toggleText,tvTittle,tvPera1,tvUserName,tvPassword;
    private EditText etUserName,etPassword;
    public static final String SHARED_PREFS = "sharedPrefs";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize find by
        ivBackArrowL = findViewById(R.id.ivBackArrowL);
        toggleText =findViewById(R.id.toggleTextL);
        btnToggleL = findViewById(R.id.btnToggleL);
        tvTittle = findViewById(R.id.tvTittle);
        tvPera1 = findViewById(R.id.tvPera1);
        tvUserName = findViewById(R.id.tvUserName);
        tvPassword = findViewById(R.id.tvPassword);
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);






        //Go to signup page
        ivBackArrowL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i4 = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i4);

            }
        });
        //change into Signup page
        btnToggleL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i3);
            }
        });
    }

    //All Methode
}