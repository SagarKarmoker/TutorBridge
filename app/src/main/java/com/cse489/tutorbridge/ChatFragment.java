package com.cse489.tutorbridge;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.cse489.tutorbridge.chat.ChatMainActivity;
import com.cse489.tutorbridge.chat.ListAdapter;
import com.cse489.tutorbridge.modal.OrderModal;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ChatFragment extends Fragment {

    ListView listView;
    List<OrderModal> orderList;
    List<String> chatroomId;

    DatabaseReference chatRoomsRef;
    String userID1 = "";
    FirebaseAuth auth;
    FirebaseFirestore db;
    SharedPreferences pref;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        Context context = view.getContext();

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userID1 = auth.getCurrentUser().getUid();
        pref = context.getSharedPreferences("TutorBridge", Context.MODE_PRIVATE);

        listView = view.findViewById(R.id.listview);
        chatroomId = new ArrayList<>();
        orderList = new ArrayList<>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OrderModal clickedOrder = orderList.get(position);
                String key = chatroomId.get(position);

                Intent intent = new Intent(getActivity(), ChatMainActivity.class);
                intent.putExtra("UserName", clickedOrder.getOrderId());
                intent.putExtra("ChatRoomId", key);
                startActivity(intent);
            }
        });

        loadData();

        return view;
    }

    private void loadData() {
        ArrayList<String> participantUserIds = new ArrayList<>();
        chatRoomsRef = FirebaseDatabase.getInstance().getReference().child("chatRooms");

        chatRoomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantUserIds.clear();

                for (DataSnapshot chatRoomSnapShot : dataSnapshot.getChildren()) {
                    DataSnapshot participantSnapShot = chatRoomSnapShot.child("participants");
                    if (participantSnapShot.hasChild(userID1)) {
                        for (DataSnapshot participantIdSnapShot : participantSnapShot.getChildren()) {
                            if (participantIdSnapShot.getKey().equals(userID1)) {
                                chatroomId.add(chatRoomSnapShot.getKey());
                                participantUserIds.add(participantIdSnapShot.getKey());
                            }
                        }
                    }
                }

                boolean isMentor = pref.getBoolean("isMentor", false);
                if (isMentor) {
                    fetchAndAdaptOrderMentor(participantUserIds);
                } else {
                    fetchAndAdaptOrderUser(participantUserIds);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle cancellation
            }
        });
    }

    private void fetchAndAdaptOrderMentor(ArrayList<String> participantUserIds) {
        CollectionReference userRef = db.collection("doubt_history");

        orderList.clear();
        Set<String> addedOrderIds = new HashSet<>();

        for (String userId : participantUserIds) {
            userRef
                    .whereEqualTo("metorId", userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                OrderModal order = document.toObject(OrderModal.class);
                                String orderId = order.getOrderId();

                                if (!addedOrderIds.contains(orderId)) {
                                    orderList.add(order);
                                    addedOrderIds.add(orderId);
                                }
                            }

                            Collections.reverse(orderList);
                            updateUI();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }
    }

    private void fetchAndAdaptOrderUser(ArrayList<String> participantUserIds) {
        CollectionReference userRef = db.collection("doubt_history");

        orderList.clear();
        Set<String> addedOrderIds = new HashSet<>();

        for (String userId : participantUserIds) {
            userRef
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                OrderModal order = document.toObject(OrderModal.class);
                                String orderId = order.getOrderId();

                                if (!addedOrderIds.contains(orderId)) {
                                    orderList.add(order);
                                    addedOrderIds.add(orderId);
                                }
                            }

                            updateUI();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure
                        }
                    });
        }
    }

    private void updateUI() {
        // Make sure to check if the fragment is still attached to the activity
        if (isAdded()) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ListAdapter adapter = new ListAdapter(getActivity(), orderList);
                    listView.setAdapter(adapter);
                    Log.d("UIUpdate", "UI updated successfully.");
                }
            });
        }
    }

}
