package com.cse489.tutorbridge.chat;

public class Message {
    private String content;
   private String currentUserId="5220603325601368084";//get from session
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
