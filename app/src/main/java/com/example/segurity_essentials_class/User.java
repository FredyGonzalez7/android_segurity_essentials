package com.example.segurity_essentials_class;

public class User {
    private String email;
    private String uid;
    private String name;

    public User(String email, String uid, String name) {
        this.email = email;
        this.uid = uid;
        this.name = name;
    }
    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
}
