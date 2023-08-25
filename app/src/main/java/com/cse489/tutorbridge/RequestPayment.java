package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RequestPayment extends AppCompatActivity {
    TextView balanceTv;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_payment);

        balanceTv = findViewById(R.id.balanceTv);

        Intent i = getIntent();
        double curBalance = i.getDoubleExtra("balance", 0);
        balanceTv.setText("৳" + curBalance);
    }
}