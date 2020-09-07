package com.example.android.recappe.SignUp.Presenter;

import android.view.View;

import com.example.android.recappe.Database.Database;
import com.example.android.recappe.SignUp.View.ISignUpView;

import java.net.MalformedURLException;
import java.net.URL;

public class SignUpPresenter implements ISignUpPresenter {

    //vars
    private ISignUpView signUpView;

    //constructors
    //default
    public SignUpPresenter() {
    }

    //with vars
    public SignUpPresenter(ISignUpView signUpView) {
        this.signUpView = signUpView;
    }

    //methods
    @Override
    public void addUserToDB(String email, String password, String accountType, String firstName, String lastName) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/putUserInDBAndroidApp.php?email="+email+"&password="+password+"&accounttype="+accountType+"&firstname="+firstName+"&lastname="+lastName);
        Database database = new Database(url, 4, this);
        database.execute();
    }

    @Override
    public void onSuccessfulAddToDB() {
        signUpView.onSuccessAdd();
    }

    @Override
    public void onFailAddToDB() {
        signUpView.onFailedAdd();
    }


}
