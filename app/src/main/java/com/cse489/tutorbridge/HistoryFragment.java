package com.cse489.tutorbridge;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.airbnb.lottie.LottieAnimationView;
import com.cse489.tutorbridge.adapters.HistoryAdapter;
import com.cse489.tutorbridge.modal.HistoryClass;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FirebaseFirestore db;
    DocumentReference docRef;
    ArrayList<HistoryClass> histories;
    HistoryAdapter adapter;
    ListView historyList;
    LottieAnimationView progressBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance(); // Initialize db object
        histories = new ArrayList<>(); // Initialize mentors list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        Context context = view.getContext();

        historyList = view.findViewById(R.id.historyList);
        progressBar = view.findViewById(R.id.progressBar);

        fetchMentorData(); // Fetch data from Firestore

        historyList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        adapter = new HistoryAdapter(context, histories); // Pass mentors list to the adapter
        historyList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                historyList.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        }, 1500);

    }

    private void fetchMentorData() {
        db.collection("doubt_history")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    histories.clear(); // Clear existing data
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        HistoryClass history = document.toObject(HistoryClass.class);
                        //document.getString("mentor")
                        histories.add(history);
                        System.out.println(history.toString());
                    }
                    adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                })
                .addOnFailureListener(e -> {
                    Log.d("HistoryFragment", "Error fetching mentors: " + e.getMessage());
                });
    }
}