package com.cse489.tutorbridge.chat;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import com.cse489.tutorbridge.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;


public class ChatMainActivity extends AppCompatActivity {
    ArrayList<String> usersArrayList;
    DatabaseReference rootNode;
    String UserID1="", chatroomkey; //suppose admin sent message retrive id from session id
    String UserID2=""; // retrive id from getTag() when user intaract with UI
    TextView dummy_user_name;
    EditText massageEd;
    List<Message> massageList;
    ListView Listview;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_main);

        Intent i = getIntent();
        UserID1 = i.getStringExtra("mentorid");
        UserID2 = i.getStringExtra("userid");
        Log.d("User INFO", UserID1+ " " + UserID2);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        rootNode = database.getReference();
        dummy_user_name=findViewById(R.id.user_name);
        massageEd=findViewById(R.id.message_edit_text);
        massageList=new ArrayList<>();
        Listview=findViewById(R.id.msg_list_view);
        Listview.setDivider(null); // Remove the divider
        Listview.setDividerHeight(0);
        send=findViewById(R.id.send_button);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String massage=massageEd.getText().toString();
                if (!massage.isEmpty()){
                    sentMassage(massage,UserID2);
                }
                massageEd.setText("");
            }
        });
        //chatroomkey= generateChatRoomKey(UserID1,UserID2);//call for generated chatroom id when two user start conversation for first time
        //createChatRoom(chatroomkey,"hi",UserID1,UserID2,UserID1);//call when newly two user start conversation for first time

        //createUser("Arifin","arifinzaman1010@gmail.com");//call when  user sign up

        try {
            if(!UserID1.isEmpty() && !UserID2.isEmpty()){
                chatroomkey = generateChatRoomKey(UserID1,UserID2);//call for generated chatroom id when two user start conversation for first time
                createChatRoom(chatroomkey,"Doubt Solve Started", UserID1, UserID2, UserID1); //call when newly two user start conversation for first time

                Log.d("User chatroomkey", chatroomkey);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


//        Intent intent = getIntent();
//        if (intent.hasExtra("UserName")) {
//            String userName = intent.getStringExtra("UserName");
//            chatroomkey = intent.getStringExtra("ChatRoomId");
//            dummy_user_name.setText(userName);
//
//        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadMassage();
    }

    private void loadMassage() {
        DatabaseReference messageListRef = rootNode.child("chatRooms").child(chatroomkey).child("messageList");

        messageListRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                massageList.clear();

                for (DataSnapshot msgListSnapshot : dataSnapshot.getChildren()) {

                  String msg=msgListSnapshot.child("Message").getValue(String.class);
                  String senderId=msgListSnapshot.child("Sender").getValue(String.class);
                  Message msgObj=new Message(msg,senderId);
                  massageList.add(msgObj);
                }
                MassageAdapter adapter = new MassageAdapter(ChatMainActivity.this, massageList);
                Listview.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
//        Message newMessage = new Message(massage); // Assuming you have a Message class
//        MessageAdapter adapter =new MessageAdapter()
    }

    private String generateChatRoomKey(String sender_id, String recever_id) {
        // Sort the user IDs alphabetically to ensure consistent keys regardless of order
        String sortedUserIds = sender_id+"_"+recever_id;

        // You can further hash or manipulate the concatenated string to create a unique key
        // For simplicity, let's assume the sorted string is the key
        return sortedUserIds;
    }
    public void sentMassage(String massage,String senderID)
{
    DatabaseReference messageListRef = rootNode.child("chatRooms").child(chatroomkey).child("messageList");

    // Create the message list child node

    DatabaseReference newMessageRef = messageListRef.push();

    newMessageRef.child("Message").setValue(massage);
    newMessageRef.child("Sender").setValue(senderID);
    newMessageRef.child("timestamp").setValue(System.currentTimeMillis());
}
    public void createChatRoom(String chatroomkey, String initialMessage,String UserID1,String UserID2, String senderID) {
        DatabaseReference chatRoomRef = rootNode.child("chatRooms").child(chatroomkey);

        // Create the message list child node
        DatabaseReference messageListRef = chatRoomRef.child("messageList");
        DatabaseReference newMessageRef = messageListRef.push();

        newMessageRef.child("Message").setValue(initialMessage);
        newMessageRef.child("Sender").setValue(senderID);
        newMessageRef.child("timestamp").setValue(System.currentTimeMillis());


        // Create the participent info child node

        DatabaseReference messageInfoRef = chatRoomRef.child("participents");
        messageInfoRef.child(UserID1).setValue(true);
        messageInfoRef.child(UserID2).setValue(true);

    }
    public void createUser(String name,String email)
    {
        User newUser = new User(name,email);
       UUID userId= UUID.randomUUID();
// Generate a new UUID for the user
       String combinedId = userId.getMostSignificantBits() + "";
// Store the user data under the generated UUID
        DatabaseReference userRef = rootNode.child("user").child(combinedId);
        userRef.setValue(newUser);
    }
}
