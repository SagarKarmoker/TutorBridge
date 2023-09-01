package com.cse489.tutorbridge.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.cse489.tutorbridge.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;

public class char_history_activity extends AppCompatActivity {

    ListView Listview;
    List<User> userList;
    List<String> chatroomId;

    DatabaseReference chatRoomsRef;
    String UserID1 = ""; // suppose I am login in userId1
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_history);
        db = FirebaseFirestore.getInstance();

        auth = FirebaseAuth.getInstance();

        UserID1 = auth.getCurrentUser().getUid();

        Listview = findViewById(R.id.listview);
        chatroomId = new ArrayList<>();
        userList = new ArrayList<>();

        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int
                    position, long id) {

                User clickedEvent = userList.get(position); // retrieve clickable object that type is EventObject
                String key = chatroomId.get(position);

                Intent i = new Intent(char_history_activity.this, ChatMainActivity.class);
                i.putExtra("UserName", clickedEvent.getName());
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
                fetchAndAdapt(participantUserIds);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle cancellation
            }
        });
    }

    private void fetchAndAdapt(ArrayList<String> participantUserIds) {
        // Define your Firestore collection reference
        CollectionReference userRef = db.collection("mentor_list"); // Change to "user_info"

        // Clear the userList if needed
        userList.clear();

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
                                String username = document.getString("name");
                                String email = document.getString("email");
                                User user = new User(username, email);
                                System.out.println(username + email);
                                userList.add(user);
                            }

                            // Update the UI after fetching data
                            ListAdapter adapter = new ListAdapter(char_history_activity.this, userList);
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
