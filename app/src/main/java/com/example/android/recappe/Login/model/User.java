package com.example.android.recappe.Login.model;

import android.util.Patterns;

import androidx.core.util.PatternsCompat;

import com.example.android.recappe.Database.Database;
import com.example.android.recappe.Login.view.ILoginView;

import java.net.MalformedURLException;
import java.net.URL;


public class User implements IUser {

    //instance vars
    private String email;
    private String password;

    //constructors
    //default
    public User() {
    }

    //constructor with args
    public User(String email, String password) {
        this.setEmail(email);
        this.setPassword(password);
    }

    //methods

    @Override
    public int isValid() {
        if(email.isEmpty()){
            return 1;
        }

        if(!PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()){
            return 2;
        }

        if(password.isEmpty()){
            return 3;
        }

        if(password.length() < 5){
            return 4;
        }

        return 0;
    }

    //getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
