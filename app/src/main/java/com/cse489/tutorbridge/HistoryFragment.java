package com.cse489.tutorbridge;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.cse489.tutorbridge.adapters.HistoryAdapter;
import com.cse489.tutorbridge.modal.HistoryClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private ArrayList<HistoryClass> histories;
    private HistoryAdapter adapter;
    private ListView historyList;
    private LottieAnimationView progressBar;
    private boolean isMentor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance(); // Initialize db object
        auth = FirebaseAuth.getInstance();
        histories = new ArrayList<>(); // Initialize histories list
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        Context context = view.getContext();

        SharedPreferences pref = context.getSharedPreferences("TutorBridge", MODE_PRIVATE);
        isMentor = pref.getBoolean("isMentor", false);

        historyList = view.findViewById(R.id.historyList);
        progressBar = view.findViewById(R.id.progressBar);

        String userId = auth.getCurrentUser().getUid();
        System.out.println(userId);
        String queryField = isMentor ? "metorId" : "userId";

        try {
            db.collection("doubt_history")
                    .whereEqualTo(queryField, userId)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        histories.clear(); // Clear existing data
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            HistoryClass history = document.toObject(HistoryClass.class);
                            histories.add(history);
                            System.out.println(histories.size());
                        }
                        adapter.notifyDataSetChanged(); // Notify the adapter that data has changed
                    })
                    .addOnFailureListener(e -> {
                        Log.d("HistoryFragment", "Error fetching history: " + e.getMessage());
                    }).getException();
        }catch (Exception e){
            e.printStackTrace();
        }

        adapter = new HistoryAdapter(context, histories); // Pass histories list to the adapter
        historyList.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(() -> {
            historyList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
        }, 1500);
    }

    private void fetchHistoryData() {

    }
}
