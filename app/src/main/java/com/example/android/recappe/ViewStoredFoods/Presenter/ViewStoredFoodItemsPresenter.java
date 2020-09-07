package com.example.android.recappe.ViewStoredFoods.Presenter;

import com.example.android.recappe.MainMenu.model.Photo;
import com.example.android.recappe.ViewStoredFoods.View.IViewStoredFoodItemsView;
import com.example.android.recappe.Database.Database;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewStoredFoodItemsPresenter implements IViewStoredFoodsPresenter {

    //vars
    private String email;
    private IViewStoredFoodItemsView viewStoredFoodItemsView;
    private Photo photo;
    private int foodID;

    //constructors
    //default
    public ViewStoredFoodItemsPresenter() {
    }

    public ViewStoredFoodItemsPresenter(String email, IViewStoredFoodItemsView viewStoredFoodItemsView) {
        this.email = email;
        this.viewStoredFoodItemsView = viewStoredFoodItemsView;
    }

    //methods
    @Override
    public void getFoodItemsForUser() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getfooditemsandroidapp.php?email="+this.email);
        Database db = new Database(url, 2, viewStoredFoodItemsView);
        db.execute();
    }

    @Override
    public void getFoodItemsForUserSorted() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getFoodItemsSortedAndroidApp.php?email="+this.email);
        Database db = new Database(url, 2, viewStoredFoodItemsView);
        db.execute();
    }

    @Override
    public void getFoodItemsForUserSorted2() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getFoodItemsSorted2AndroidApp.php?email="+this.email);
        Database db = new Database(url, 2, viewStoredFoodItemsView);
        db.execute();
    }

    @Override
    public void deleteFoodFromDB(String name, int foodID) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/deleteFoodItemsAndroidApp.php?foodID="+foodID);
        Database db = new Database(url, 0);
        db.execute();
    }

    @Override
    public void updateFoodFromDBGetPhoto(int foodID, IViewStoredFoodItemsView viewStoredFoodItemsView) {
        this.foodID = foodID;
        photo = new Photo(this, viewStoredFoodItemsView);
        photo.getPhoto(viewStoredFoodItemsView);
    }

    @Override
    public void uploadPhotoToServer() {
        String imageUploadPath = photo.getMyPic().getPath();
        photo.setMyPic(new File(imageUploadPath));
        photo.uploadAndPredictPhoto();
    }

    @Override
    public void onGetMachineLearningFoodAnswer(String result){
        viewStoredFoodItemsView.onSuccessfulUpdate(result, foodID);
    }

    @Override
    public void updateFoodInDB(int foodID, int newAge, String newName, String newBatchSize) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/UpdateFoodInDBAndroidApp.php?food_id="+foodID+"&food_name="+newName+"&age_in_days="+newAge+"&batch_size="+newBatchSize);
        Database db = new Database(url, 0);
        db.execute();
    }

    @Override
    public void updateFoodUsedAmountInDB(int foodID, int newAmountUsed) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/UpdateFoodAmountUsedInDBAndroidApp.php?food_id="+foodID+"&amount_used="+newAmountUsed);
        Database db = new Database(url, 0);
        db.execute();
    }

    @Override
    public void updateFoodThrownOutAmountInDB(int foodID, int newAmountThrownOut) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/UpdateFoodThrownOutAmountInDBAndroidApp.php?food_id="+foodID+"&amount_thrown_out="+newAmountThrownOut);
        Database db = new Database(url, 0);
        db.execute();
    }
}
