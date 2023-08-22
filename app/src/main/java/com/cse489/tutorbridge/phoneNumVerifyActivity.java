package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class phoneNumVerifyActivity extends AppCompatActivity {

    private Button btnSendOTP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_num_verify);

        //
        btnSendOTP = findViewById(R.id.btnSendOTP);



        //go to OTP page
        btnSendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i4 = new Intent(phoneNumVerifyActivity.this, OtpConfirmActivity.class);
                startActivity(i4);

            }
        });
    }
}