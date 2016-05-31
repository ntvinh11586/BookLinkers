package com.example.vinh.DataObjects;

/**
 * Created by vinh on 31/05/16.
 */
public class User {
    public String username;
    public String email;
    public String password;
    public String address;
    public String name;

    public User(String username, String password, String email, String name, String address) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.name = name;
        this.address = address;
    }
}
