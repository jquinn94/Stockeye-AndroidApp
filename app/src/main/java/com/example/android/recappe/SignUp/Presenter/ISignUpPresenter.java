package com.example.android.recappe.SignUp.Presenter;

import java.net.MalformedURLException;

public interface ISignUpPresenter {

    public void addUserToDB(String email, String password, String accountType, String firstName, String lastName) throws MalformedURLException;

    public void onSuccessfulAddToDB();

    public void onFailAddToDB();
}
