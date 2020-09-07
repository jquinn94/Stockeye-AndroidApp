package com.example.android.recappe;

import android.app.ProgressDialog;
import com.example.android.recappe.MainMenu.model.Server;
import com.example.android.recappe.MainMenu.presenter.IMainMenuPresenter;
import com.example.android.recappe.ViewStoredFoods.Presenter.ViewStoredFoodItemsPresenter;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ServerTest {

    Server server;
    ProgressDialog pd;
    IMainMenuPresenter mainMenuPresenter;
    ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter;
    URL url;

    @Test
    public void testValidConstructors(){

        server = new Server();
        assertNotNull(server);

        server = new Server(pd, mainMenuPresenter, url);
        Assert.assertEquals(mainMenuPresenter, server.getMainMenuPresenter());
        Assert.assertEquals(pd, server.getPd());
        Assert.assertEquals(url, server.getUrlForPythonScript());

        server = new Server(pd, viewStoredFoodItemsPresenter, url);
        Assert.assertEquals(pd, server.getPd());
        Assert.assertEquals(viewStoredFoodItemsPresenter, server.getViewStoredFoodItemsPresenter());
        Assert.assertEquals(url, server.getUrlForPythonScript());

    }
}
