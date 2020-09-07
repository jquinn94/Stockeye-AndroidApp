package com.example.android.recappe.MainMenu.presenter;

import android.content.SharedPreferences;
import android.util.TypedValue;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.android.recappe.Database.Database;
import com.example.android.recappe.MainMenu.model.Photo;
import com.example.android.recappe.MainMenu.view.IMainMenuView;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainMenuPresenter extends AppCompatActivity implements IMainMenuPresenter {

    //instance vars
    private IMainMenuView mainMenuView;
    private Photo photo;
    private String imageUploadPath = "";
    private TextView textView;
    private String email;

    //constructors
    //default
    public MainMenuPresenter() {
    }

    //with vars
    public MainMenuPresenter(IMainMenuView mainMenuView, String email) {
        this.mainMenuView = mainMenuView;
        this.email = email;
    }

    //methods
    @Override
    public void onAddFoodPhotographically() {
        photo = new Photo(this, this.mainMenuView);
        photo.getPhoto(this.mainMenuView);
    }

    @Override
    public void getFoodNames() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getFoodListFromDBAndroidApp.php");
        Database db = new Database(url, 5,this);
        db.execute();
    }

    @Override
    public void getUserFoodNames() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getFoodListFromDBAndroidApp.php?email="+this.email);
        Database db = new Database(url, 10,this);
        db.execute();
    }

    @Override
    public void uploadPhotoToServer() {
        imageUploadPath = photo.getMyPic().getPath();
        photo.setMyPic(new File(imageUploadPath));
        photo.uploadAndPredictPhoto();
    }

    @Override
    public void getUserName(TextView textView) throws MalformedURLException {
        this.textView = textView;
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getusernameandroidapp.php?email="+this.email);
        Database db = new Database(url, 1,this);
        db.execute();
    }

    @Override
    public void setUserName(String name, int size) {
        if(size == 0){
            textView.setText(name);
        }else if(size == 1){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,30);
            textView.setText(name);
        }

    }

    @Override
    public void updateUserPushNotificationID(String id) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/updateuserpushnotificationidandroidapp.php?email="+this.email+"&id="+id);
        Database db = new Database(url, 0,this);
        db.execute();
    }

    @Override
    public void addFoodToDB(String name, int ageInDays, String batchAmount) throws MalformedURLException {
        Date date = new Date(); // This object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/AddFoodToDBAndroidApp.php?email="+this.email+"&food_name="+name+"&age_in_days="+ageInDays+"&date_added="+formatter.format(date)+"&batch_amount="+batchAmount);
        Database db = new Database(url, 0,this);
        db.execute();
    }

    @Override
    public void onGetMachineLearningFoodAnswer(String result){
        mainMenuView.onGetMachineLearningFoodAnswer(result);
    }

    @Override
    public void signOutRemovePushNotificationID() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/removeUserPushNotificationIDFromDBAndroidApp.php?email="+this.email);
        Database database = new Database(url, 0);
        database.execute();
    }

    @Override
    public void onDeleteAccount() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/deleteAccountAndroidApp.php?email="+this.email);
        Database database = new Database(url, 0);
        database.execute();
    }

    @Override
    public void setFoodList(String[] foodList){
        mainMenuView.setFoodListName(foodList);
    }

    @Override
    public void setUserFoodList(String[] foodList) {
        mainMenuView.setUserFoodListName(foodList);
    }
}
