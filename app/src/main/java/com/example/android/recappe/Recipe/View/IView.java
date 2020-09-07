package com.example.android.recappe.Recipe.View;

import android.graphics.Bitmap;

public interface IView {

    void displayRecipeFromDB(String name, String time, String description, String pictureURL);

    void displayRecipePicture(Bitmap picture);

}
