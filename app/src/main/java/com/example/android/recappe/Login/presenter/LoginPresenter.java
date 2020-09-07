package com.example.android.recappe.Login.presenter;

import com.example.android.recappe.Database.Database;
import com.example.android.recappe.Login.model.User;
import com.example.android.recappe.Login.view.ILoginView;

import java.net.MalformedURLException;
import java.net.URL;

public class LoginPresenter implements ILoginPresenter {

    //instance vars
    private ILoginView loginView;

    //constructors
    //default constructor
    public LoginPresenter() {
    }

    //constructor with args
    public LoginPresenter(ILoginView loginView) {
        this.loginView = loginView;
    }

    //methods
    @Override
    public void onCheckLogin(String email, String password) throws MalformedURLException {
        User user = new User(email,password);
        int isLoginValid = user.isValid();

        switch(isLoginValid){
            case 0:
                URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/loginandroidapp.php?email="+email+"&password="+password);
                Database database = new Database(url, 3, this);
                database.execute();
                break;
            case 1:
                loginView.onLoginFailure("Username empty");
                break;
            case 2:
                loginView.onLoginFailure("Not valid email");
                break;
            case 3:
                loginView.onLoginFailure("Password field empty");
                break;
            case 4:
                loginView.onLoginFailure("Password too short");
                break;

        }

    }

    @Override
    public void onConsumerLoginSuccess() {
        loginView.onConsumerLoginSuccess();
    }

    @Override
    public void onBusinessLoginSuccess() {
        loginView.onBusinessLoginSuccess();
    }

    @Override
    public void onLoginFailure() {
        loginView.onLoginFailure("Email or password are incorrect");
    }

}

