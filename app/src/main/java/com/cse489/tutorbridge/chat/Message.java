package com.cse489.tutorbridge.chat;

import com.google.firebase.auth.FirebaseAuth;

public class Message {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private String content;
   private String currentUserId=auth.getCurrentUser().getUid();//get from session
    private String senderId;

    public Message(String content,String senderId) {
        this.content = content;
        this.senderId=senderId;
        //this.sentByCurrentUser = sentByCurrentUser;
    }

    public String getContent() {
        return content;
    }

    public boolean isCurrentUserId() {
        if (senderId.equals(currentUserId))
        {
            return true;
        }
        else{
            return false;
        }

    }
}
