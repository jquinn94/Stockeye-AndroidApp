package com.example.android.recappe.MainMenu.presenter;

import android.widget.TextView;
import java.net.MalformedURLException;

public interface IMainMenuPresenter {

    void onAddFoodPhotographically();
    void getFoodNames() throws MalformedURLException;
    void getUserFoodNames() throws MalformedURLException;
    void uploadPhotoToServer();
    void getUserName(TextView textView) throws MalformedURLException;
    void setUserName(String name, int size);
    void updateUserPushNotificationID(String id) throws MalformedURLException;
    void addFoodToDB(String name, int ageInDays, String batchAmount) throws MalformedURLException;
    void onGetMachineLearningFoodAnswer(String result);
    void signOutRemovePushNotificationID() throws MalformedURLException;
    void setFoodList(String[] foodList);
    void setUserFoodList(String[] foodList);
    void onDeleteAccount() throws MalformedURLException;

}
