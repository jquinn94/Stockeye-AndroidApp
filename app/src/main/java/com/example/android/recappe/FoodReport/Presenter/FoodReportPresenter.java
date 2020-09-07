package com.example.android.recappe.FoodReport.Presenter;

import com.example.android.recappe.Database.Database;
import com.example.android.recappe.FoodReport.View.IView;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class FoodReportPresenter implements IFoodReportPresenter {

    //instance vars
    private String email;
    private IView view;

    //constructors
    //default
    public FoodReportPresenter(){

    }

    //with args
    public FoodReportPresenter(String email, IView view){
        this.email = email;
        this.view = view;
    }

    //methods
    @Override
    public void getFoodItems(String dateFrom, String dateTo) throws MalformedURLException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.UK);
        Date dateFrom1 = sdf.parse(dateFrom);
        Date dateTo1 = sdf.parse(dateTo);
        String dateFrom2 = new SimpleDateFormat("yyyy-MM-dd").format(dateFrom1);
        String dateTo2 = new SimpleDateFormat("yyyy-MM-dd").format(dateTo1);
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getFoodReportFromDBAndroidApp.php?email="+this.email+"&date_from="+dateFrom2+"&date_to="+dateTo2);
        Database db = new Database(url, 6, this);
        db.execute();
    }

    @Override
    public void setFoodItems(String[] foodNames, String[] batchAmount, String[] foodUsed, String[] foodThrownOut) {
        view.setFoodReportItems(foodNames, batchAmount, foodUsed, foodThrownOut);
    }

}
