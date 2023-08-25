package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cse489.tutorbridge.modal.HistoryClass;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        HistoryClass history = (HistoryClass) getIntent().getSerializableExtra("order");
        assert history != null;
        System.out.println(history.toString());
    }
}