package com.cse489.tutorbridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cse489.tutorbridge.adapters.MentorAdapter;
import com.cse489.tutorbridge.modal.MentorProfileClass;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private FirebaseFirestore db;
    DocumentReference docRef;
    TextView userName;
    MaterialCardView offerCard;
    ListView workListView;
    ArrayList<MentorProfileClass> mentors;
    MentorAdapter adapter;
    ImageView offerImg;
    boolean isScrolling = false;
    String linkToRe = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance(); // Initialize db object
        mentors = new ArrayList<>(); // Initialize mentors list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userName = view.findViewById(R.id.userName);
        workListView = view.findViewById(R.id.workListView);
        offerCard = view.findViewById(R.id.offerCard);
        offerImg = view.findViewById(R.id.offerImg);

        Context context = view.getContext();
        SharedPreferences pref = context.getSharedPreferences("tutorBride", Context.MODE_PRIVATE);
        String name = pref.getString("Name", "");
        userName.setText(name);

        fetchMentorData(); // Fetch data from Firestore

        adapter = new MentorAdapter(context, mentors); // Pass mentors list to the adapter
        workListView.setAdapter(adapter);

        //Listview
        workListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    // ListView is being scrolled by touch input
                    offerCard.setVisibility(View.GONE);
                } else if (scrollState == SCROLL_STATE_IDLE) {
                    // ListView is not scrolling
                    offerCard.setVisibility(View.VISIBLE);
                } else if (scrollState == SCROLL_STATE_FLING) {
                    // ListView is in the process of fling scrolling
                    offerCard.setVisibility(View.GONE);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isScrolling = true;
                //offerCard.setVisibility(View.GONE);
            }
        });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                fetchOfferImg();
            }
        });

        offerImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent offer = new Intent(Intent.ACTION_VIEW, Uri.parse(linkToRe));
                startActivity(offer);
            }
        });

        return  view;
    }

    private void fetchOfferImg(){
        db.collection("offers")
                .whereEqualTo("isOffer", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        String imgUrl = document.getString("imgUrl");
                        linkToRe = document.getString("linkToRe");
                        Picasso.get().load(imgUrl).into(offerImg);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.d("HomeFragment", "Error fetching mentors: " + e.getMessage());
                });
    }

    private void fetchMentorData() {
        db.collection("mentor_list")
                .whereEqualTo("status", "Unverified")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    mentors.clear(); // Clear existing data
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        MentorProfileClass mentor = document.toObject(MentorProfileClass.class);
                        System.out.println(document.getString("mentor"));
                        mentor.setMentor(document.getString("mentor"));
                        mentors.add(mentor);
                        System.out.println(mentor.toString());
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                })
                .addOnFailureListener(e -> {
                    Log.d("HomeFragment", "Error fetching mentors: " + e.getMessage());
                });
    }
}