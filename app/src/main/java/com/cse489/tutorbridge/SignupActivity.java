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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.cse489.tutorbridge.modal.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {
    private ImageView ivBackArrow;
    private TextView btnToggleS, toggleText, tvTittle, tvPera1, tvUserName, tvEmail, tvPassword, tvConfirmPassword;
    private EditText etUserName, etEmail, etPassword, etConfirmPassword;
    private Button btnSignUpPage;
    CheckBox mentorCheck;
    private ProgressBar progressBar;
    private static final String TAG = "SignupActivity";

    private String err = "";
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();

        //initialize find by
        ivBackArrow = findViewById(R.id.ivBackArrow);
        toggleText = findViewById(R.id.toggleTextL);
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
        mentorCheck = findViewById(R.id.mentorCheck);

        //Check details by clicking button
        btnSignUpPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etUserName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String ConfirmPassword = etConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(SignupActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    etUserName.setError("Name is required");
                    etUserName.requestFocus();
                } else if (name.length() < 4 || name.length() > 12) {
                    Toast.makeText(SignupActivity.this, "Name is invalid", Toast.LENGTH_LONG).show();
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
                } else if (password.length() < 6) {
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
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(name, email, password);
                }


            }
        });

        //Go to Initial page
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i2 = new Intent(SignupActivity.this, initialActivity.class);
                startActivity(i2);

            }
        });
        //change into login page

        btnToggleS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i3 = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i3);
            }
        });
    }

    //All Methode

    //register user using given credentials
    private void registerUser(String name, String email, String password) {
        FirebaseAuth auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User registration successful
                            Toast.makeText(SignupActivity.this, "User SignUp Complete", Toast.LENGTH_LONG).show();
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String date = String.valueOf(System.currentTimeMillis());

                            // Determine the Firestore collection based on mentorCheck
                            CollectionReference userRef = db.collection(mentorCheck.isChecked() ? "mentor_list" : "user_info");

                            // Send verification email
                            firebaseUser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Email sent successfully, you can proceed
                                    if (mentorCheck.isChecked()) {
                                        // Create a MentorProfileClass object
                                        MentorProfileClass mentorObj = new MentorProfileClass(
                                                auth.getUid(), name, "", email, "", "", "", "", "Unverified", "Dhaka", "", date, 0, 0
                                        );
                                        addUserToMentorDB(mentorObj, userRef);
                                    } else {
                                        // Create a User object
                                        User userObj = new User(auth.getUid(), name, date, email);
                                        addUserToDB(userObj, userRef);
                                    }

                                    // Open user profile after successful signup registration
                                    progressBar.setVisibility(View.GONE);
                                    Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("docPath", auth.getUid());
                                    startActivity(intent);
                                    finish();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle email sending failure
                                    Log.e(TAG, "Error sending verification email", e);
                                    progressBar.setVisibility(View.GONE);
                                }
                            });

                        } else {
                            // Handle authentication failure
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                etPassword.setError("Your Password is weak. Try using a combination of letters, numbers, and special characters.");
                                etPassword.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                etEmail.setError("This email is already registered. Use another email.");
                                etEmail.requestFocus();
                            } catch (Exception e) {
                                Log.e(TAG, "Error during registration", e);
                                Toast.makeText(SignupActivity.this, "Registration failed. Please try again later.", Toast.LENGTH_LONG).show();
                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }




    private void addUserToMentorDB(MentorProfileClass userObj, CollectionReference userRef) {
        // Add the user to Firestore
        userRef.add(userObj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // User data added to Firestore successfully
                        Log.d(TAG, "User data added to Firestore successfully");
                        // You can perform additional actions here if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to add user data to Firestore
                        Log.e(TAG, "Error adding user data to Firestore", e);
                        // You can show an error message to the user or take other actions
                    }
                });
    }

    private void addUserToDB(User userObj, CollectionReference userRef) {
        // Add the user to Firestore
        userRef.add(userObj)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // User data added to Firestore successfully
                        Log.d(TAG, "User data added to Firestore successfully");
                        // You can perform additional actions here if needed
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle the failure to add user data to Firestore
                        Log.e(TAG, "Error adding user data to Firestore", e);
                        // You can show an error message to the user or take other actions
                    }
                });
    }

}
