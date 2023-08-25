package com.cse489.tutorbridge;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.cse489.tutorbridge.modal.MentorProfileClass;

public class MentorProfile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mentor_profile);

        MentorProfileClass mentor = (MentorProfileClass) getIntent().getSerializableExtra("mentorDetails");
        System.out.println(mentor.toString());

    }
}