package com.example.android.recappe.FoodReport.Presenter;

import java.net.MalformedURLException;
import java.text.ParseException;

public interface IFoodReportPresenter {

    void getFoodItems(String dateFrom, String dateTo) throws MalformedURLException, ParseException;
    void setFoodItems(String[] foodNames, String[] batchAmount, String[] foodUsed, String[] foodThrownOut);
}
