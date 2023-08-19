package com.cse489.tutorbridge;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ImageView ivBackArrow;
    private TextView btnToggleS,toggleText,tvTittle,tvPera1,tvUserName,tvEmail,tvPassword,tvConfirmPassword;
    private EditText etUserName,etEmail,etPassword,etConfirmPassword;
    private Button btnSignUpPage;
    private String err= "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //initialize find by
        ivBackArrow = findViewById(R.id.ivBackArrow);
        toggleText =findViewById(R.id.toggleTextL);
        btnToggleS = findViewById(R.id.btnToggleS);
        tvTittle = findViewById(R.id.tvTittle);
        tvPera1 = findViewById(R.id.tvPera1);
        tvUserName = findViewById(R.id.tvUserName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        tvConfirmPassword = findViewById(R.id.tvConfirmPassword);
        etUserName = findViewById(R.id.etUserName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignUpPage = findViewById(R.id.btnSignUpPage);

        //Check details by clicking button
        btnSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String ConfirmPassword = etConfirmPassword.getText().toString();

                if(!err.isEmpty())
                {
                    showErrorDialog(err);
                    err="";
                }
                if(name.length()<4 || name.length()>12 || !name.matches("[a-z A-Z]+"))
                {
                    err += "Name should have 4-12 letters\n";
                }
                if(!isValidEmail(email))
                {
                    err += "Email is Invalid\n";
                }
                if(!password.equals(ConfirmPassword))
                {
                    err += "Password didn't match\n";
                }

            }
        });

        //Go to Initial page
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2 = new Intent(SignupActivity.this,initialActivity.class);
                startActivity(i2);

            }
        });
        //change into login page

        btnToggleS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i3);
            }
        });
    }

    //All Methode
    //Error msg show
    private void showErrorDialog(String errorMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage);
        builder.setTitle("Error");
        builder.setCancelable(true);
        builder.setPositiveButton("back", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //email validity checker
    public static boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

}