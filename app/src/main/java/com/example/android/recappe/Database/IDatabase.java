package com.example.android.recappe.Database;

import org.json.JSONException;

import java.text.ParseException;

public interface IDatabase {

    void getUserDetailsResult(String json) throws JSONException;
    void checkIfFoodsInDB(String json) throws JSONException, ParseException;
    void checkIfUserInDB(String json) throws JSONException;
    void getUserInsertResult(String json);
    void getFoodListFromDB(String json) throws JSONException;
    void getUserFoodListFromDB(String json) throws JSONException;
    void getFoodReportFromDB(String json) throws JSONException;
    void getRecipeFromDB(String json) throws JSONException;
    void getUserDetailsFromDB(String json) throws JSONException;
    void checkUserDetailsWereUpdated(String json);

}
