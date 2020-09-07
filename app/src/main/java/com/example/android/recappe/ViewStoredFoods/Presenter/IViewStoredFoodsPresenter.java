package com.example.android.recappe.ViewStoredFoods.Presenter;

import com.example.android.recappe.ViewStoredFoods.View.IViewStoredFoodItemsView;
import java.net.MalformedURLException;

public interface IViewStoredFoodsPresenter {

    void getFoodItemsForUser() throws MalformedURLException;
    void getFoodItemsForUserSorted() throws MalformedURLException;
    void getFoodItemsForUserSorted2() throws MalformedURLException;
    void deleteFoodFromDB(String name, int foodID) throws MalformedURLException;
    void updateFoodFromDBGetPhoto(int foodID, IViewStoredFoodItemsView viewStoredFoodItemsView);
    void uploadPhotoToServer();
    void onGetMachineLearningFoodAnswer(String result);
    void updateFoodInDB(int foodID, int newAge, String newName, String newBatchSize) throws MalformedURLException;
    void updateFoodUsedAmountInDB(int foodID, int newAmountUsed) throws MalformedURLException;
    void updateFoodThrownOutAmountInDB(int foodID, int newAmountThrownOut) throws MalformedURLException;


}
