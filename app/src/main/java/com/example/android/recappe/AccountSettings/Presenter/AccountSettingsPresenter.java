package com.example.android.recappe.AccountSettings.Presenter;

import com.example.android.recappe.AccountSettings.View.IView;
import com.example.android.recappe.Database.Database;

import java.net.MalformedURLException;
import java.net.URL;

public class AccountSettingsPresenter implements IAccountSettingsPresenter {

    //instance vars
    private String email;
    private IView view;

    //constructors
    //default
    public AccountSettingsPresenter(){

    }

    //with args
    public AccountSettingsPresenter(String email,IView view){
        this.email = email;
        this.view = view;
    }

    @Override
    public void getUserDetails() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getUserInfoAndroidApp.php?email="+this.email);
        Database db = new Database(url, 8, this);
        db.execute();

    }

    @Override
    public void showUserDetails(String firstNameDB, String lastNameDB, String emailDB, String passwordDB){
        view.showUserDetails(firstNameDB,lastNameDB,emailDB,passwordDB);
    }

    @Override
    public void updateUserDetails(String firstNameDB, String lastNameDB, String emailDB, String passwordDB) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/UpdateUsersInfoDBAndroidApp.php?email="+this.email+"&email_to_change="+emailDB+"&first_name="+firstNameDB+"&last_name="+lastNameDB+"&password="+passwordDB);
        Database db = new Database(url, 9, this);
        db.execute();
    }

    @Override
    public void onSuccessfulUpdate(){
        view.onSuccessfulUpdate();
    }

    @Override
    public void onFailureUpdateDuplicateEmail(){
        view.onFailedAddDuplicateEmail();
    }

    @Override
    public void onFailureUpdateOther(){
        view.onFailedAddOther();
    }
}
