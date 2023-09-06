package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;

public class RequestPayment extends AppCompatActivity {
    TextView balanceTv;
    RadioGroup rePayRadioGroup;
    TextInputEditText amountRePay, accountNumber,bankName;
    TextInputLayout bankCard;
    MaterialButton rePayBtn;
    double curBalance = 0;
    String method = "";
    ImageView rePayBackBtn, paymentHistoryBtn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_payment);

        balanceTv = findViewById(R.id.balanceTv);
        rePayRadioGroup = findViewById(R.id.rePayRadioGroup);
        rePayBtn  = findViewById(R.id.rePayBtn);
        amountRePay = findViewById(R.id.amountRePay);
        accountNumber = findViewById(R.id.accountNumber);
        bankCard = findViewById(R.id.bankCard);
        bankName = findViewById(R.id.bankName);
        rePayBackBtn = findViewById(R.id.rePayBackBtn);
        paymentHistoryBtn = findViewById(R.id.paymentHistoryBtn);

        rePayBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RequestPayment.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        paymentHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RequestPayment.this, "Withdraw History Coming soon", Toast.LENGTH_SHORT).show();
            }
        });

        rePayRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.bkashBtn) {
                    // Handle Bkash selection
                    method = "Bkash";
                    bankCard.setVisibility(View.GONE);
                } else if (checkedId == R.id.nagadBtn) {
                    // Handle Nagad selection
                    method = "Nagad";
                    bankCard.setVisibility(View.GONE);
                } else if (checkedId == R.id.cardBtn) {
                    // Handle Visa/MasterCard selection
                    method = "Visa/MasterCard";
                    bankCard.setVisibility(View.GONE);
                } else if (checkedId == R.id.bankBtn) {
                    // Handle Bank selection
                    method = "Bank";
                    bankCard.setVisibility(View.VISIBLE);
                }
            }
        });

        SharedPreferences pref = this.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        String json = pref.getString("CurrentUser", "");
        boolean isMentor = pref.getBoolean("isMentor", false);

        Gson gson = new Gson();

        try {
            MentorProfileClass currentUser = gson.fromJson(json, MentorProfileClass.class);
            balanceTv.setText("৳" + currentUser.getWallet());
            curBalance = currentUser.getWallet();
        }catch (Exception e){
            e.printStackTrace();
        }

        rePayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amountRePayText = Double.parseDouble(amountRePay.getText().toString());
                String accountNumberText = accountNumber.getText().toString();
                String bankNameText = bankName.getText().toString();

                if(rePayRadioGroup.isSelected() && amountRePayText > 0 && !accountNumberText.isEmpty() && amountRePayText <= curBalance){
                    Toast.makeText(RequestPayment.this, "Coming soon", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RequestPayment.this, "Enter correct Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}