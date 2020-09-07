package com.example.android.recappe.Login.presenter;

import java.net.MalformedURLException;

public interface ILoginPresenter {

    void onCheckLogin(String email, String password) throws MalformedURLException;

    void onConsumerLoginSuccess();

    void onBusinessLoginSuccess();

    void onLoginFailure();

}
