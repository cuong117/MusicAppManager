package com.example.musicupload.models;

public class User {
    String email;
    String pass;
    String name;
    boolean admin;
    public User() {

    }
    public User(boolean admin, String email, String password, String name) {
        this.email = email;
        this.pass = password;
        this.name = name;
        this.admin = admin;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}
