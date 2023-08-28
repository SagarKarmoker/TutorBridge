package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ImageView ivBackArrow;
    private TextView btnToggleS,toggleText,tvTittle,tvPera1,tvUserName,tvEmail,tvPassword,tvConfirmPassword;
    private EditText etUserName,etEmail,etPassword,etConfirmPassword;
    private Button btnSignUpPage;
    private ProgressBar progressBar;
    private static final String TAG = "SignupActivity";

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
        progressBar = findViewById(R.id.progressBar);

        //Check details by clicking button
        btnSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String ConfirmPassword = etConfirmPassword.getText().toString();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(SignupActivity.this,"Please enter your full name",Toast.LENGTH_LONG).show();
                    etUserName.setError("Name is required");
                    etUserName.requestFocus();
                } else if (name.length()<4 || name.length()>12 || !name.matches("[a-z A-Z]+")) {
                    Toast.makeText(SignupActivity.this,"Name is invalid",Toast.LENGTH_LONG).show();
                    etUserName.setError("Name should have 4-12 letters");
                    etUserName.requestFocus();
                } else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(SignupActivity.this, "Please enter your email address", Toast.LENGTH_LONG).show();
                    etEmail.setError("Email is required");
                    etEmail.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(SignupActivity.this, "Please re-enter your email address", Toast.LENGTH_LONG).show();
                    etEmail.setError("Valid email is required");
                    etEmail.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(SignupActivity.this, "Please enter your password", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password is required");
                    etPassword.requestFocus();
                } else if (password.length()<6) {
                    Toast.makeText(SignupActivity.this, "Password is too small", Toast.LENGTH_LONG).show();
                    etPassword.setError("Password should be at least 6 digits");
                    etPassword.requestFocus();
                } else if (TextUtils.isEmpty(ConfirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Please confirm your password", Toast.LENGTH_LONG).show();
                    etConfirmPassword.setError("Password confirmation is required");
                    etConfirmPassword.requestFocus();
                } else if (!password.equals(ConfirmPassword)) {
                    Toast.makeText(SignupActivity.this, "Please Enter the same password", Toast.LENGTH_LONG).show();
                    etConfirmPassword.setError("Password didn't match");
                    etConfirmPassword.requestFocus();
                } else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(name,email,password);
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

    //register user using given credentials
    private void registerUser(String name, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(SignupActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(SignupActivity.this,"User SignUp Complete",Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();

                            //send verification email
                            firebaseUser.sendEmailVerification();

                            //open user profile after successful signup registration
                            Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else{
                            try{
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e){
                                etPassword.setError("Your Password is so weak. Try using mixture of Alphabets and Number and Special character");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e){
                                etEmail.setError("This email is already registered. Use another email");
                                etEmail.requestFocus();
                                progressBar.setVisibility(View.GONE);
                            } catch (Exception e) {
                                Log.e(TAG,e.getMessage());
                                Toast.makeText(SignupActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }

                    }
                });
    }




}
