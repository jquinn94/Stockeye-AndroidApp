package com.example.android.recappe.Recipe.Presenter;

import android.graphics.Bitmap;

import com.example.android.recappe.Database.Database;
import com.example.android.recappe.Recipe.Model.DownloadRecipeImage;
import com.example.android.recappe.Recipe.View.IView;

import java.net.MalformedURLException;
import java.net.URL;

public class RecipePresenter implements IRecipePresenter {

    //instance vars
    private IView view;
    private String email;

    //constructors
    //default
    public RecipePresenter(){

    }

    //contructor with args
    public RecipePresenter(IView view, String email){
        this.view = view;
        this.email = email;
    }

    //methods
    @Override
    public void getRecipe() throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getRecipeAndroidApp.php?email="+email);
        Database db = new Database(url, 7, this);
        db.execute();
    }

    @Override
    public void getRecipeWithIngredient(String ingredient) throws MalformedURLException {
        URL url = new URL("http://jquinn63.lampt.eeecs.qub.ac.uk/getRecipeAndroidApp.php?email="+email+"&ingredient="+ingredient);
        Database db = new Database(url, 7, this);
        db.execute();
    }

    @Override
    public void displayRecipeFromDB(String name, String time, String description, String pictureURL) {
        view.displayRecipeFromDB(name,time,description, pictureURL);
    }

    @Override
    public void getRecipePicture(String pictureURL){
        DownloadRecipeImage dri = new DownloadRecipeImage(this);
        dri.execute("http://jquinn63.lampt.eeecs.qub.ac.uk/" + pictureURL);
    }

    @Override
    public void setRecipePicture(Bitmap picture){
        view.displayRecipePicture(picture);
    }
}
