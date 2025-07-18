package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private ImageView ivBackArrowL;
    private TextView btnToggleL,toggleText,tvTittle,tvPera1,tvEmail,tvPassword;
    private EditText etEmail,etPassword;
    private Button btnLoginPage;
    public static final String SHARED_PREFS = "sharedPrefs";
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;
    private static final String TAG = "LoginActivity";
    private  ImageView viewPass;

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
        tvEmail = findViewById(R.id.tvEmail);
        tvPassword = findViewById(R.id.tvPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLoginPage = findViewById(R.id.btnLoginPage);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();

        preferences = this.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        edit = preferences.edit();

        //password hide or show
        viewPass = findViewById(R.id.viewPass);
        viewPass.setImageResource(R.drawable.icon_hide);
        viewPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    // If password is visible then Hide it
                    etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    // Change Icon
                    viewPass.setImageResource(R.drawable.icon_hide);
                } else {
                    etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    viewPass.setImageResource(R.drawable.icon_visible);
                }
            }
        });


        //Go to signup page
        ivBackArrowL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i4 = new Intent(LoginActivity.this,initialActivity.class);
                startActivity(i4);

            }
        });
        //go to OTP page
        btnLoginPage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String textEmail = etEmail.getText().toString();
                String textPass = etPassword.getText().toString();

                if (TextUtils.isEmpty(textEmail)) {
                    Toast.makeText(LoginActivity.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()) {
                    Toast.makeText(LoginActivity.this, "Please re-enter your email address", Toast.LENGTH_LONG).show();
                    etEmail.setError("Valid email is required");
                    etEmail.requestFocus();
                } else if (TextUtils.isEmpty(textPass)) {
                    Toast.makeText(LoginActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password is required");
                    etPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(textEmail,textPass);
                }

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
    private void loginUser(String email, String password) {
        authProfile.signInWithEmailAndPassword(email,password).addOnCompleteListener(LoginActivity.this,new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"You are logged in now",Toast.LENGTH_SHORT).show();
                    edit.putString("docPath", authProfile.getUid());
                    edit.putBoolean("TutorIsLoggedIn", true);
                    edit.apply();
                    Intent i4 = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(i4);

                }
                else {
                    Toast.makeText(LoginActivity.this,"Enter a valid Email!",Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    
}
