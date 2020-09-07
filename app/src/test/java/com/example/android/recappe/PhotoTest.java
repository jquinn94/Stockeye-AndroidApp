package com.example.android.recappe;

import com.example.android.recappe.MainMenu.model.Photo;
import com.example.android.recappe.MainMenu.presenter.IMainMenuPresenter;
import com.example.android.recappe.ViewStoredFoods.Presenter.ViewStoredFoodItemsPresenter;
import org.junit.Assert;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

public class PhotoTest <T> {

    Photo photo;
    T view;
    IMainMenuPresenter mainMenuPresenter;
    ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter;

    @Test
    public void testValidConstructors(){

        photo = new Photo();
        assertNotNull(photo);

        photo = new Photo(viewStoredFoodItemsPresenter, view);
        Assert.assertEquals(viewStoredFoodItemsPresenter, photo.getViewStoredFoodItemsPresenter());
        Assert.assertEquals(view, photo.getView());

        photo = new Photo(mainMenuPresenter, view);
        Assert.assertEquals(mainMenuPresenter, photo.getMainMenuPresenter());
        Assert.assertEquals(view, photo.getView());

    }
}
