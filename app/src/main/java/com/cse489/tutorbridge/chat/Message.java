package com.cse489.tutorbridge.chat;

import com.google.firebase.auth.FirebaseAuth;

public class Message {
    private String content;
    private String senderId;

    public Message(String content, String senderId) {
        this.content = content;
        this.senderId = senderId;
    }

    public String getContent() {
        return content;
    }

    public boolean isSentByCurrentUser() {
        // Get the current user ID when needed
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String currentUserId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;

        // Check if the sender ID matches the current user's ID
        return currentUserId != null && currentUserId.equals(senderId);
    }
}
