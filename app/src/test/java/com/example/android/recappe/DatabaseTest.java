package com.example.android.recappe;

import com.example.android.recappe.AccountSettings.Presenter.IAccountSettingsPresenter;
import com.example.android.recappe.Database.Database;
import com.example.android.recappe.FoodReport.Presenter.IFoodReportPresenter;
import com.example.android.recappe.Login.presenter.ILoginPresenter;
import com.example.android.recappe.MainMenu.presenter.IMainMenuPresenter;
import com.example.android.recappe.Recipe.Presenter.IRecipePresenter;
import com.example.android.recappe.SignUp.Presenter.ISignUpPresenter;
import com.example.android.recappe.ViewStoredFoods.View.IViewStoredFoodItemsView;

import org.junit.Assert;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URL;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class DatabaseTest {

    //variables
    Database database;
    URL url;
    int validCheck, invalidCheck1, invalidCheck2;
    ILoginPresenter loginPresenter;
    IViewStoredFoodItemsView viewStoredFoodItemsView;
    IMainMenuPresenter mainMenuPresenter;
    ISignUpPresenter signUpPresenter;
    IFoodReportPresenter foodReportPresenter;
    IRecipePresenter recipePresenter;
    IAccountSettingsPresenter accountSettingsPresenter;

    @BeforeEach
    void setUp() throws Exception {
        url = new URL("http://test.com");
        validCheck = 1;
        invalidCheck1 = -1;
        invalidCheck2 = 0;
    }

    @Test
    public void testValidConstructors(){

        database = new Database();
        assertNotNull(database);

        database = new Database(url,validCheck);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());

        database = new Database(url, validCheck, loginPresenter);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());
        Assert.assertEquals(loginPresenter, database.getLoginPresenter());

        database = new Database(url, validCheck, viewStoredFoodItemsView);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());
        Assert.assertEquals(loginPresenter, database.getViewStoredFoodItemsView());

        database = new Database(url, validCheck, mainMenuPresenter);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());
        Assert.assertEquals(loginPresenter, database.getMainMenuPresenter());

        database = new Database(url, validCheck, signUpPresenter);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());
        Assert.assertEquals(loginPresenter, database.getSignUpPresenter());

        database = new Database(url, validCheck, foodReportPresenter);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());
        Assert.assertEquals(loginPresenter, database.getFoodReportPresenter());

        database = new Database(url, validCheck, recipePresenter);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());
        Assert.assertEquals(loginPresenter, database.getRecipePresenter());

        database = new Database(url, validCheck, accountSettingsPresenter);
        Assert.assertEquals(url, database.getUrl());
        Assert.assertEquals(validCheck, database.getCheck());
        Assert.assertEquals(loginPresenter, database.getAccountSettingsPresenter());

    }


    @Test
    public void testInvalidCheckValue() {
        Database database = new Database();
        try {
            database.setCheck(invalidCheck1);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("Number must be 1 or greater"));
        }

        try {
            database.setCheck(invalidCheck2);
        } catch (IllegalArgumentException ex) {
            assertThat(ex.getMessage(), containsString("Number must be 1 or greater"));
        }

    }


}