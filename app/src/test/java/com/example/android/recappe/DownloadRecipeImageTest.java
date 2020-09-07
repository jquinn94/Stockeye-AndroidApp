package com.example.android.recappe;

import com.example.android.recappe.Recipe.Model.DownloadRecipeImage;
import com.example.android.recappe.Recipe.Presenter.IRecipePresenter;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

public class DownloadRecipeImageTest {

    DownloadRecipeImage downloadRecipeImage;
    IRecipePresenter recipePresenter;

    @Test
    public void testValidConstructors(){

        downloadRecipeImage = new DownloadRecipeImage();
        Assert.assertNotNull(downloadRecipeImage);

        downloadRecipeImage = new DownloadRecipeImage(recipePresenter);
        Assert.assertEquals(recipePresenter, downloadRecipeImage.getRecipePresenter());

    }
}
