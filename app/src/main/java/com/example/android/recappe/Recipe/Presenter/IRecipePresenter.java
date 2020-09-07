package com.example.android.recappe.Recipe.Presenter;

import android.graphics.Bitmap;

import java.net.MalformedURLException;

public interface IRecipePresenter {

    void getRecipe() throws MalformedURLException;

    void displayRecipeFromDB(String name, String time, String description, String pictureURL);

    void setRecipePicture(Bitmap picture);

    void getRecipePicture(String pictureURL);

    void getRecipeWithIngredient(String ingredient) throws MalformedURLException;
}
