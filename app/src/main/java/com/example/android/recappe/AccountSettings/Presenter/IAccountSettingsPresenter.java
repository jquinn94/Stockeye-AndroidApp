package com.example.android.recappe.AccountSettings.Presenter;

import java.net.MalformedURLException;

public interface IAccountSettingsPresenter {

    void getUserDetails() throws MalformedURLException;

    void showUserDetails(String firstNameDB, String lastNameDB, String emailDB, String passwordDB);

    void updateUserDetails(String firstNameDB, String lastNameDB, String emailDB, String passwordDB) throws MalformedURLException;

    void onSuccessfulUpdate();

    void onFailureUpdateDuplicateEmail();

    void onFailureUpdateOther();
}
