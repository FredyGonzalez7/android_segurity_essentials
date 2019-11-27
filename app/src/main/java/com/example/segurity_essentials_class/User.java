package com.example.segurity_essentials_class;

public class User {
    private String email;
    private String uid;

    public User(String email, String uid) {
        this.email = email;
        this.uid = uid;
    }
    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }



}
