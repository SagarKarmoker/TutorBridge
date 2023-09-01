package com.cse489.tutorbridge.chat;

import java.util.UUID;

public class User {




    private String name;
    private String email;


        public User() {
        }



    public User(String name, String email) {



        this.name = name;
        this.email = email;
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }


    }

