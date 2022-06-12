package com.example.musicupload.account;

import java.util.AbstractCollection;

public class Account {
    String email;
    String password;
    boolean admin;
    public Account() {

    }
    public Account(String email, String password, boolean admin) {
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return admin;
    }
}
