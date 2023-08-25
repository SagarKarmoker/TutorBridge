package com.cse489.tutorbridge.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.cse489.tutorbridge.MentorProfile;
import com.cse489.tutorbridge.R;
import com.cse489.tutorbridge.modal.HistoryClass;
import com.cse489.tutorbridge.modal.MentorProfileClass;

import java.util.ArrayList;

public class MentorAdapter extends ArrayAdapter<MentorProfileClass> {
    private final Context context;
    private final ArrayList<MentorProfileClass> mentors;

    public MentorAdapter(@NonNull Context context, ArrayList<MentorProfileClass> mentors) {
        super(context, -1, mentors);
        this.context = context;
        this.mentors = mentors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) { // it will be called for each row (0 to n-1)

        // inflater is renderer
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.work_row, parent, false); // works as a view

        ImageView workImg = rowView.findViewById(R.id.workImg);
        TextView workTitle = rowView.findViewById(R.id.workTitle);
        TextView workSubTitle = rowView.findViewById(R.id.workSubTitle);
        TextView workInstitute = rowView.findViewById(R.id.workInstitute);
        TextView workLocation = rowView.findViewById(R.id.workLocation);
        Button guideMeBtn = rowView.findViewById(R.id.guideMeBtn);

        MentorProfileClass profile = mentors.get(position);
        //Log.d("inAdapter", profile.toString());
        workTitle.setText(profile.getName());
        workSubTitle.setText(profile.getExpert());
        workInstitute.setText(profile.getEducation());
        workLocation.setText(profile.getLocation());
        //TODO set the image
        //workImg.setImageBitmap();

        guideMeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MentorProfile.class);
                i.putExtra("mentorDetails", profile);
                context.startActivity(i);
            }
        });


        return rowView;
    }
}
