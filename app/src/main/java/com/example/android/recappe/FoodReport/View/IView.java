package com.example.android.recappe.FoodReport.View;

import android.view.View;

import java.text.ParseException;
import java.util.Map;

public interface IView {

    void setFoodReportItems(String[] foodNamesList, String[] batchAmountList, String[] foodUsed, String[] foodThrownOut);
    void updateLabel() throws ParseException;
}
