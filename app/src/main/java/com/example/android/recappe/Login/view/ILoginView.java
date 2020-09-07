package com.example.android.recappe.Login.view;

import com.example.android.recappe.Login.model.User;

public interface ILoginView {

    void onBusinessLoginSuccess();

    void onConsumerLoginSuccess();

    void onLoginFailure(String message);

}