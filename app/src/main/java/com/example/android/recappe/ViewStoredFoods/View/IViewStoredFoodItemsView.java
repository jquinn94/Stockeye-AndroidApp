package com.example.android.recappe.ViewStoredFoods.View;

import java.text.ParseException;

public interface IViewStoredFoodItemsView {

    void onSuccessOfFoodItemsFromDB(String[] foodNames, String[] datesAdded, String[] foodID, String[] age, String[] batchAmount, String[] thrownOutAmount, String[] usedAmount, String[] foodExpiryDays) throws ParseException;
    void onFailureOfFoodItemsFromDB();
    void onSuccessfulUpdate(String result, int foodID);

}
