package com.cse489.tutorbridge.modal;

public class User {
    String uuid, name, date, email;
    public User(){

    }
    public User(String uuid, String name, String date, String email){
        this.uuid = uuid;
        this.name = name;
        this.date = date;
        this.email = email;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
