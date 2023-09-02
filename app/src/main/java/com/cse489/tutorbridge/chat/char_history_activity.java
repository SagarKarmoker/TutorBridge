package com.cse489.tutorbridge.chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.cse489.tutorbridge.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class char_history_activity extends AppCompatActivity {

    ListView Listview;
    List<OrderModal> orderList;
    List<String> chatroomId;

    DatabaseReference chatRoomsRef;
    String UserID1 = ""; // suppose I am login in userId1
    FirebaseAuth auth;
    FirebaseFirestore db;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_history);
        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        UserID1 = auth.getCurrentUser().getUid();
        pref = this.getSharedPreferences("TutorBridge", MODE_PRIVATE);

        Listview = findViewById(R.id.listview);
        chatroomId = new ArrayList<>();
        orderList = new ArrayList<>();

        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int
                    position, long id) {

                OrderModal clickedEvent = orderList.get(position); // retrieve clickable object that type is EventObject
                String key = chatroomId.get(position);

                Intent i = new Intent(char_history_activity.this, ChatMainActivity.class);
                i.putExtra("UserName", clickedEvent.getOrderId());
                i.putExtra("ChatRoomId", key);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {
        ArrayList<String> participantUserIds = new ArrayList<>();
        chatRoomsRef = FirebaseDatabase.getInstance().getReference().child("chatRooms");

        chatRoomsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantUserIds.clear();

                for (DataSnapshot chatRoomSnapShot : dataSnapshot.getChildren()) {
                    DataSnapshot participantSnapShot = chatRoomSnapShot.child("participents");
                    if (participantSnapShot.hasChild(UserID1)) {
                        //System.out.println(participantSnapShot.hasChild(UserID1));
                        for (DataSnapshot participantIdSnapShot : participantSnapShot.getChildren()) {
                            if (participantIdSnapShot.getKey().equals(UserID1)) {
                                chatroomId.add(chatRoomSnapShot.getKey());
                                participantUserIds.add(participantIdSnapShot.getKey());
                            }
                        }
                    }
                }
                System.out.println(participantUserIds.size());
                fetchAndAdaptOrder(participantUserIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle cancellation
            }
        });
    }

    private void fetchAndAdaptOrder(ArrayList<String> participantUserIds) {
        // Define your Firestore collection reference
        CollectionReference userRef = db.collection("doubt_history"); // Change to "user_info"

        // Clear the userList if needed
        orderList.clear();

        boolean isMentor = pref.getBoolean("isMentor", false);

        // Create a Set to keep track of added orderIds
        Set<String> addedOrderIds = new HashSet<>();

        // Iterate over the userIds and query Firestore
        for (String userId : participantUserIds) {
            userRef
                    .whereEqualTo(isMentor ? "mentorId" : "userId", userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Access document data using document.getData()
                                OrderModal order = document.toObject(OrderModal.class);
                                String orderId = order.getOrderId();

                                // Check if the orderId has already been added
                                if (!addedOrderIds.contains(orderId)) {
                                    orderList.add(order);
                                    addedOrderIds.add(orderId);
                                }
                            }

                            // Update the UI after fetching data
                            ListAdapter adapter = new ListAdapter(char_history_activity.this, orderList);
                            Listview.setAdapter(adapter);
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


    private void fetchAndAdaptMentor(ArrayList<String> participantUserIds) {
        // Define your Firestore collection reference
        CollectionReference userRef = db.collection("mentor_list"); // Change to "user_info"

        // Clear the userList if needed
        orderList.clear();

        // Iterate over the userIds and query Firestore
        for (String userId : participantUserIds) {
            userRef
                    .whereEqualTo("uuid", userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Access document data using document.getData()
//                                String username = document.getString("name");
//                                String email = document.getString("email");
//                                OrderModal order = new OrderModal(username, email);
//                                System.out.println(username + email);
//                                userList.add(user);
                            }

                            // Update the UI after fetching data
                            ListAdapter adapter = new ListAdapter(char_history_activity.this, orderList);
                            Listview.setAdapter(adapter);
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
    private void fetchAndAdaptUser(ArrayList<String> participantUserIds) {
        // Define your Firestore collection reference
        CollectionReference userRef = db.collection("user_info"); // Change to "user_info"

        // Clear the userList if needed
        orderList.clear();

        // Iterate over the userIds and query Firestore
        for (String userId : participantUserIds) {
            userRef
                    .whereEqualTo("uuid", userId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                // Access document data using document.getData()
//                                String username = document.getString("name");
//                                String email = document.getString("email");
//                                User user = new User(username, email);
//                                System.out.println(username + email);
//                                userList.add(user);
                            }

                            // Update the UI after fetching data
                            ListAdapter adapter = new ListAdapter(char_history_activity.this, orderList);
                            Listview.setAdapter(adapter);
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
}
