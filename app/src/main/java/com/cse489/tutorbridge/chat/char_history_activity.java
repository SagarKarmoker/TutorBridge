package com.cse489.tutorbridge.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.cse489.tutorbridge.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class char_history_activity extends AppCompatActivity {

    ListView Listview;
    List<User> userList;
    List<String> chatroomId;

    DatabaseReference chatRoomsRef;
    String UserID1="-5448039680139312301"; //suppose I am login in userId1


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_char_history);

        Listview = findViewById(R.id.listview);
        chatroomId=new  ArrayList<>();
        userList = new ArrayList<>();

        Listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>parent, final View view, int
                    position, long id) {

                User clickedEvent = userList.get(position); // retrive clickble object that type is EventObject
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

    private void loadData()
    {

        ArrayList<String> participantUserIds = new ArrayList<>();

        chatRoomsRef = FirebaseDatabase.getInstance().getReference().child("chatRooms");

        chatRoomsRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                participantUserIds.clear();

                for (DataSnapshot chatRoomSnapShot : dataSnapshot.getChildren()) {
                    DataSnapshot participantSnapShot=chatRoomSnapShot.child("participents");
                    if (participantSnapShot.hasChild(UserID1))
                    {
                        for (DataSnapshot participantIdSnapShot : participantSnapShot.getChildren()) {
                            if(!participantIdSnapShot.getKey().equals(UserID1))
                            {
                                chatroomId.add(chatRoomSnapShot.getKey());
                                participantUserIds.add(participantIdSnapShot.getKey());
                            }


                        }
                    }


                }

                fetchAndAdapt(participantUserIds);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void fetchAndAdapt(ArrayList<String> participantUserIds) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("user");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot userIdSnapshot : snapshot.getChildren())
                {

                    for (String userId : participantUserIds) {

                        if(userIdSnapshot.getKey().contains(userId)) {

                            String username =userIdSnapshot.child("name").getValue(String.class);
                            String email =userIdSnapshot.child("email").getValue(String.class);
                            User user =new User(username,email);
                            userList.add(user);
                        }
                    }
                }
                ListAdapter adapter = new ListAdapter(char_history_activity.this, userList);
               Listview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }


}