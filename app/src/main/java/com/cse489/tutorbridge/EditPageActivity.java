package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditPageActivity extends AppCompatActivity {
    private EditText etUserName,etPhone,etAddress,etEducation,etExpert,etYears,etPrice,etDescription;
    private Button btnSaveInfo;
    private ProgressBar progressBar;
    private FirebaseAuth authProfile;

    SharedPreferences preferences;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_page);

        etUserName = findViewById(R.id.etUserName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etEducation = findViewById(R.id.etEducation);
        etExpert = findViewById(R.id.etExpert);
        etYears = findViewById(R.id.etYears);
        etPrice = findViewById(R.id.etPrice);
        etDescription = findViewById(R.id.etDescription);
        btnSaveInfo = findViewById(R.id.btnSaveInfo);
        progressBar = findViewById(R.id.progressBar);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if(firebaseUser == null)
        {
            Toast.makeText(EditPageActivity.this,"Something went wrong !",Toast.LENGTH_LONG).show();

        } else{
            checkIfEmailVerified(firebaseUser);
        }
        

        btnSaveInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //collecting data
                String textName = etUserName.getText().toString();
                String textPhone = etPhone.getText().toString();
                String textAddress = etAddress.getText().toString();
                String textEducation = etEducation.getText().toString();
                String textExpert = etExpert.getText().toString();
                String textYears = etYears.getText().toString();
                String textPrice = etPrice.getText().toString();
                String textDescription = etDescription.getText().toString();

                //validate mobile number
                String mobileReg = "^(01[3-9]\\d{8})$";
                Matcher mobileMatcher;
                Pattern mobilePattern = Pattern.compile(mobileReg);
                mobileMatcher = mobilePattern.matcher(textPhone);


                if (TextUtils.isEmpty(textName)) {
                    Toast.makeText(EditPageActivity.this, "Please enter your full name", Toast.LENGTH_LONG).show();
                    etUserName.setError("Name is required");
                    etUserName.requestFocus();
                } else if (textName.length() < 4 || textName.length() > 22 || !textName.matches("[a-z A-Z]+")) {
                    Toast.makeText(EditPageActivity.this, "Name is invalid", Toast.LENGTH_LONG).show();
                    etUserName.setError("Name should have 4-22 letters");
                    etUserName.requestFocus();
                } else if (TextUtils.isEmpty(textPhone)) {
                    Toast.makeText(EditPageActivity.this, "Please enter your Phone Number", Toast.LENGTH_LONG).show();
                    etPhone.setError("Phone number is required");
                    etPhone.requestFocus();
                } else if (textPhone.length()!=11) {
                    Toast.makeText(EditPageActivity.this, "Please enter valid phone Number", Toast.LENGTH_LONG).show();
                    etPhone.setError("Phone number should be of 11 digits");
                    etPhone.requestFocus();
                } else if (!mobileMatcher.find()) {
                    Toast.makeText(EditPageActivity.this, "Please enter valid phone Number", Toast.LENGTH_LONG).show();
                    etPhone.setError("Phone number is invalid !");
                    etPhone.requestFocus();
                } else if (TextUtils.isEmpty(textAddress)) {
                    Toast.makeText(EditPageActivity.this, "Please enter your Present Address", Toast.LENGTH_LONG).show();
                    etAddress.setError("Address is required");
                    etAddress.requestFocus();
                } else if (TextUtils.isEmpty(textEducation)) {
                    Toast.makeText(EditPageActivity.this, "Please enter your Educational Qualification", Toast.LENGTH_LONG).show();
                    etEducation.setError("Educational Qualification is required");
                    etEducation.requestFocus();
                } else if (TextUtils.isEmpty(textExpert)) {
                    Toast.makeText(EditPageActivity.this, "Please enter your area of expertise", Toast.LENGTH_LONG).show();
                    etExpert.setError("Expertise is required");
                    etExpert.requestFocus();
                } else if (TextUtils.isEmpty(textYears)) {
                    Toast.makeText(EditPageActivity.this, "Please enter your years of experience in this field", Toast.LENGTH_LONG).show();
                    etYears.setError("Years of experience is required");
                    etYears.requestFocus();
                } else if (TextUtils.isEmpty(textPrice)) {
                    Toast.makeText(EditPageActivity.this, "Please enter your expected hourly price", Toast.LENGTH_LONG).show();
                    etPrice.setError("Price is required");
                    etPrice.requestFocus();
                } else if (TextUtils.isEmpty(textDescription)) {
                    Toast.makeText(EditPageActivity.this, "Please write a short description", Toast.LENGTH_LONG).show();
                    etDescription.setError("Description is required");
                    etDescription.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUserInfo(textName,textPhone,textAddress,textEducation,textExpert,textYears,textPrice,textDescription);
                }
            }
        });



    }

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if(!firebaseUser.isEmailVerified())
        {
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //Setup the Alert Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(EditPageActivity.this);
        builder.setTitle("Email is not verified");
        builder.setMessage("Please verify your email now. Can't login without it");
        //open email apps if click verify button
        builder.setPositiveButton("Verify Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }



    //all methode
    private void registerUserInfo(String textName, String textPhone, String textAddress, String textEducation, String textExpert, String textYears, String textPrice, String textDescription) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        //enter data into firebase realtime database
        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textName,textPhone,textAddress,textEducation,
                textExpert,textYears,textPrice,textDescription);

//        (String name, String phone, String education, String expert, String desc, String status, String location, String year, String date, double price, double wallet)
        MentorProfileClass mentor = new MentorProfileClass(textName,textPhone,textEducation, textExpert, textDescription, "Verified", textAddress, textYears,String.valueOf(System.currentTimeMillis()) , Double.parseDouble(textPrice), 0);



        // Get a reference to your Firestore database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        preferences = this.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        boolean isMentor = preferences.getBoolean("isMentor", false);

//        if(isMentor){
//
//        }else{
//
//        }

        // Get a reference to your Firestore database
        // Define a collection reference (you can change "mentor_list" to your desired collection name)
        CollectionReference userDetailRef = db.collection("mentor_list");

        // Use a query to find the document with the specified UID
        Query query = userDetailRef.whereEqualTo("uuid", auth.getCurrentUser().getUid());

        // Execute the query
        query.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        // Check if the query returned any documents
                        if (!queryDocumentSnapshots.isEmpty()) {
                            // Assuming there's only one document with the same UID, you can update it
                            QueryDocumentSnapshot documentSnapshot = (QueryDocumentSnapshot) queryDocumentSnapshots.getDocuments().get(0);

                            // Create a map with the fields you want to update
                            Map<String, Object> updates = new HashMap<>();
//                            String name, String phone, String education, String expert, String desc, String status, String location, String year, String date, double price, double wallet
//                            textName,textPhone,textEducation, textExpert, textDescription, "Verified", textAddress, textYears,String.valueOf(System.currentTimeMillis()) , Double.parseDouble(textPrice), 0
                            updates.put("name", textName);
                            updates.put("phone", textPhone);
                            updates.put("education", textEducation);
                            updates.put("expert", textExpert);
                            updates.put("desc", textDescription);
                            updates.put("status", "Verified");
                            updates.put("location", textAddress);
                            updates.put("year", textYears);
                            updates.put("date", String.valueOf(System.currentTimeMillis()));
                            updates.put("price", Double.parseDouble(textPrice));

                            // Update the document with the new user details
                            userDetailRef.document(documentSnapshot.getId())
                                    .update(updates)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(EditPageActivity.this, "User Details Updated", Toast.LENGTH_LONG).show();
                                            // Optionally, you can finish the activity or perform other actions
                                            Intent i = new Intent(EditPageActivity.this, DashboardActivity.class);
                                            startActivity(i);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(EditPageActivity.this, "Failed to update user details", Toast.LENGTH_LONG).show();
                                        }
                                    });
                        } else {
                            // Handle the case where no document with the specified UID was found
                            Toast.makeText(EditPageActivity.this, "User not found", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditPageActivity.this, "Error while searching for user", Toast.LENGTH_LONG).show();
                    }
                });
    }
}