package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class MentorProfile extends AppCompatActivity {

    TextView jobMentorID, tutorName, tutorLocation, expYears, expSalary, tvJobDes, tvQualificationDes, expType;
    private FirebaseFirestore db;
    FirebaseStorage storage;
    DocumentReference docRef;
    StorageReference storageRef;
    StorageReference imageRef;

    ImageView profilePic;
    Button hireBtn;
    MentorProfileClass mentor;
    FirebaseAuth auth;
    ImageView tutorBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_details);

        auth = FirebaseAuth.getInstance();

        MentorProfileClass mentor = (MentorProfileClass) getIntent().getSerializableExtra("mentorDetails");
        System.out.println(mentor.toString());

        jobMentorID = findViewById(R.id.jobMentorID);
        tutorName = findViewById(R.id.tutorName);
        tutorLocation = findViewById(R.id.tutorLocation);
        expYears = findViewById(R.id.expYears);
        expSalary = findViewById(R.id.expSalary);
        tvJobDes = findViewById(R.id.tvJobDes);
        tvQualificationDes = findViewById(R.id.tvQualificationDes);
        profilePic = findViewById(R.id.profilePic);
        hireBtn = findViewById(R.id.hireBtn);
        expType = findViewById(R.id.expType);
        tutorBackBtn = findViewById(R.id.tutorBackBtn);

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageRef = storageRef.child("mentors/profile.png");


        tutorBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MentorProfile.this, DashboardActivity.class);
                startActivity(i);
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //image retrive
                // Download the image
                imageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        // Convert the byte array to a Bitmap or load it into an ImageView
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        profilePic.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });
            }
        });

        jobMentorID.setText("MentorID: "+mentor.getMentor());
        tutorName.setText(mentor.getName());
        tutorLocation.setText(mentor.getLocation());
        expYears.setText(mentor.getYear() + " years");
        expSalary.setText( "Starting from ৳" + String.valueOf(mentor.getPrice()));
        tvJobDes.setText(mentor.getDesc());
        tvQualificationDes.setText(mentor.getEducation());
        expType.setText(mentor.getExpert());

        hireBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(auth.getCurrentUser().getUid().equals(mentor.getUuid())){
                        Toast.makeText(MentorProfile.this, "You can't hire yourself", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Intent i = new Intent(MentorProfile.this, PaymentActivity.class);
                        i.putExtra("mentorId", mentor.getUuid());
                        i.putExtra("userId", auth.getCurrentUser().getUid()); //todo update userid
                        System.out.println(mentor.getUuid() + "  " + auth.getCurrentUser().getUid());
                        i.putExtra("mentorSalary", mentor.getPrice());
                        i.putExtra("mentorCategory", mentor.getExpert());
                        System.out.println(orderIdGen());
                        i.putExtra("orderId", orderIdGen());
                        startActivity(i);
                    }
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    private String orderIdGen(){
        Random random = new Random();
        // Generate a random 5-digit number
        int min = 10000;  // Minimum 5-digit number
        int max = 99999;  // Maximum 5-digit number
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}