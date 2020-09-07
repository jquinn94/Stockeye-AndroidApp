package com.example.android.recappe.MainMenu.model;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.android.recappe.MainMenu.presenter.IMainMenuPresenter;
import com.example.android.recappe.MainMenu.view.IMainMenuView;
import com.example.android.recappe.ViewStoredFoods.Presenter.ViewStoredFoodItemsPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class Server extends AsyncTask<String, String, String> {

    //instance vars
    //variables for progress dialog and text box
    private ProgressDialog pd;
    private URL urlForPythonScript;
    private IMainMenuPresenter mainMenuPresenter;
    private ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter;
    private int check = 0;

    //constructors
    //default constructor
    public Server(){

    }

    //constructor with var
    public Server(ProgressDialog pd, IMainMenuPresenter mainMenuPresenter, URL url){
        this.setPd(pd);
        this.setMainMenuPresenter(mainMenuPresenter);
        this.setUrlForPythonScript(url);
    }

    public Server(ProgressDialog pd, ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter, URL url){
        this.setPd(pd);
        this.setViewStoredFoodItemsPresenter(viewStoredFoodItemsPresenter);
        this.setUrlForPythonScript(url);
        this.setCheck(1);
    }

    //methods
    /**
     * launches progress dialog
     */
    @Override
    protected void onPreExecute(){
        pd.show();
    }

    /**
     * accesses machine learning python script on server and returns answer
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        URL imageRecognitionScript = null;
        imageRecognitionScript = this.urlForPythonScript;

        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(imageRecognitionScript.openStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }


        String inputLine = "";
        String answer = "";
        while (true) {
            try {
                if (!((inputLine = in.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            answer = inputLine;
        }


        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return answer;
    }

    @Override
    protected void onPostExecute(String result) {
        pd.dismiss();

        if(check == 1){
            viewStoredFoodItemsPresenter.onGetMachineLearningFoodAnswer(result);
        }else{
            mainMenuPresenter.onGetMachineLearningFoodAnswer(result);
        }

    }

    //getters and setters
    public ProgressDialog getPd() {
        return pd;
    }

    public void setPd(ProgressDialog pd) {
        this.pd = pd;
    }

    public URL getUrlForPythonScript() {
        return urlForPythonScript;
    }

    public void setUrlForPythonScript(URL urlForPythonScript) {
        this.urlForPythonScript = urlForPythonScript;
    }

    public IMainMenuPresenter getMainMenuPresenter() {
        return mainMenuPresenter;
    }

    public void setMainMenuPresenter(IMainMenuPresenter mainMenuPresenter) {
        this.mainMenuPresenter = mainMenuPresenter;
    }

    public ViewStoredFoodItemsPresenter getViewStoredFoodItemsPresenter() {
        return viewStoredFoodItemsPresenter;
    }

    public void setViewStoredFoodItemsPresenter(ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter) {
        this.viewStoredFoodItemsPresenter = viewStoredFoodItemsPresenter;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) {
        this.check = check;
    }
}
