package com.example.android.recappe.AccountSettings.View;

public interface IView {

    void showUserDetails(String firstNameDB, String lastNameDB, String emailDB, String passwordDB);

    void onSuccessfulUpdate();

    void onFailedAddDuplicateEmail();

    void onFailedAddOther();
}
