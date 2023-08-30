package com.cse489.tutorbridge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MentorProfile extends AppCompatActivity {

    TextView jobMentorID, tutorName, tutorLocation, expYears, expSalary, tvJobDes, tvQualificationDes;
    private FirebaseFirestore db;
    FirebaseStorage storage;
    DocumentReference docRef;
    StorageReference storageRef;
    StorageReference imageRef;

    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_details);

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

        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        imageRef = storageRef.child("mentors/profile.png");

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                //TODO fix user hardcoded
                docRef = db.collection("mentor_list").document("XPyCnt3KehThLKSa87uG");

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

        jobMentorID.setText("MentorID: "+mentor.getMentorid());
        tutorName.setText(mentor.getName());
        tutorLocation.setText(mentor.getLocation());
        expYears.setText(mentor.getYear() + " years");
        expSalary.setText( "Starting from ৳" + String.valueOf(mentor.getPrice()));
        tvJobDes.setText(mentor.getExpert());
        tvQualificationDes.setText(mentor.getEducation());
    }
}