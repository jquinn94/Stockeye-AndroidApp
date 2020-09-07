package com.example.android.recappe.Database;

import android.os.AsyncTask;

import com.example.android.recappe.AccountSettings.Presenter.IAccountSettingsPresenter;
import com.example.android.recappe.FoodReport.Presenter.IFoodReportPresenter;
import com.example.android.recappe.Login.presenter.ILoginPresenter;
import com.example.android.recappe.MainMenu.presenter.IMainMenuPresenter;
import com.example.android.recappe.Recipe.Presenter.IRecipePresenter;
import com.example.android.recappe.SignUp.Presenter.ISignUpPresenter;
import com.example.android.recappe.ViewStoredFoods.View.IViewStoredFoodItemsView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;

public class Database extends AsyncTask<Void, Void, String> implements IDatabase {

    //vars
    private URL url;
    private int check;
    private IMainMenuPresenter mainMenuPresenter;
    private IViewStoredFoodItemsView viewStoredFoodItemsView;
    private ILoginPresenter loginPresenter;
    private ISignUpPresenter signUpPresenter;
    private IFoodReportPresenter foodReportPresenter;
    private IRecipePresenter recipePresenter;
    private IAccountSettingsPresenter accountSettingsPresenter;
    private StringBuilder sb = new StringBuilder();

    //constructors
    //default
    public Database() {
    }

    //with vars
    public Database(URL url, int check){
        this.setUrl(url);
        this.setCheck(check);
    }

    public Database(URL url, int check, ILoginPresenter loginPresenter){
        this.setLoginPresenter(loginPresenter);
        this.setCheck(check);
        this.setUrl(url);
    }

    public Database(URL url, int check, IViewStoredFoodItemsView viewStoredFoodItemsView){
        this.setUrl(url);
        this.setCheck(check);
        this.setViewStoredFoodItemsView(viewStoredFoodItemsView);
    }

    public Database(URL url, int check, IMainMenuPresenter mainMenuPresenter) {
        this.setMainMenuPresenter(mainMenuPresenter);
        this.setUrl(url);
        this.setCheck(check);
    }

    public Database(URL url, int check, ISignUpPresenter signUpPresenter){
        this.setUrl(url);
        this.setCheck(check);
        this.setSignUpPresenter(signUpPresenter);
    }

    public Database(URL url, int check, IFoodReportPresenter foodReportPresenter){
        this.setUrl(url);
        this.setCheck(check);
        this.setFoodReportPresenter(foodReportPresenter);
    }

    public Database(URL url, int check, IRecipePresenter recipePresenter){
        this.setUrl(url);
        this.setCheck(check);
        this.setRecipePresenter(recipePresenter);
    }

    public Database(URL url, int check, IAccountSettingsPresenter accountSettingsPresenter){
        this.setUrl(url);
        this.setCheck(check);
        this.setAccountSettingsPresenter(accountSettingsPresenter);
    }

    //methods
    @Override
    protected String doInBackground(Void... voids) {
        try {
            URL url = this.url;
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String json;
            while ((json = bufferedReader.readLine()) != null) {
                sb.append(json + "\n");
            }
            return sb.toString().trim();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            if(this.check == 1){
                getUserDetailsResult(s);
            } else if(this.check == 2){
                checkIfFoodsInDB(s);
            } else if(this.check == 3){
                checkIfUserInDB(s);
            } else if(this.check == 4){
                getUserInsertResult(s);
            } else if(this.check == 5){
                getFoodListFromDB(s);
            } else if(this.check == 6){
                getFoodReportFromDB(s);
            } else if(this.check == 7){
                getRecipeFromDB(s);
            } else if(this.check == 8){
                getUserDetailsFromDB(s);
            } else if (this.check == 9){
                checkUserDetailsWereUpdated(s);
            } else if(this.check == 10){
                getUserFoodListFromDB(s);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getUserDetailsResult(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);
        String[] dbReturnFirstName = new String[jsonArray.length()];
        String[] dbReturnLastName = new String[jsonArray.length()];
        String[] dbReturnAccountType = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbReturnFirstName[i] = obj.getString("first_name");
            dbReturnLastName[i] = obj.getString("last_name");
            dbReturnAccountType[i] = obj.getString("account_typ");
        }

        if(!dbReturnFirstName[0].equals("null")){
            if(dbReturnAccountType[0].equals("0")){
                if(dbReturnFirstName[0].length() < 7 && dbReturnLastName[0].length() < 7){
                    mainMenuPresenter.setUserName(dbReturnFirstName[0] + " " + dbReturnLastName[0], 0);
                }else{
                    mainMenuPresenter.setUserName(dbReturnFirstName[0] + " " + dbReturnLastName[0], 1);
                }
            }else{
                mainMenuPresenter.setUserName("Company Account", 1);
            }
        } else{
            mainMenuPresenter.setUserName("Name Error", 0);
        }

    }

    @Override
    public void checkIfFoodsInDB(String json) throws JSONException, ParseException {
        JSONArray jsonArray = new JSONArray(json);
        String[] dbReturnFoodName = new String[jsonArray.length()];
        String[] dbReturnDateAdded = new String[jsonArray.length()];
        String[] dbReturnFoodID = new String[jsonArray.length()];
        String[] dbReturnAge = new String[jsonArray.length()];
        String[] dbBatchAmount = new String[jsonArray.length()];
        String[] dbFoodUsedAmount = new String[jsonArray.length()];
        String[] dbFoodThrownOutAmount = new String[jsonArray.length()];
        String[] dbFoodExpiryDays = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbReturnFoodName[i] = obj.getString("food_name");
            dbReturnDateAdded[i] = obj.getString("date_added");
            dbReturnFoodID[i] = obj.getString("userfood_id");
            dbReturnAge[i] = obj.getString("age_in_days");
            dbBatchAmount[i] = obj.getString("batch_amount");
            dbFoodUsedAmount[i] = obj.getString("food_used");
            dbFoodThrownOutAmount[i] = obj.getString("food_thrown_out");
            dbFoodExpiryDays[i] = obj.getString("food_length_days");
        }

        if(dbReturnFoodName.length > 0){
            viewStoredFoodItemsView.onSuccessOfFoodItemsFromDB(dbReturnFoodName, dbReturnDateAdded, dbReturnFoodID, dbReturnAge, dbBatchAmount, dbFoodThrownOutAmount, dbFoodUsedAmount, dbFoodExpiryDays);
        } else{
            viewStoredFoodItemsView.onFailureOfFoodItemsFromDB();
        }

    }

    @Override
    public void checkIfUserInDB(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);
        String[] dbReturn = new String[jsonArray.length()];
        String[] dbAccountTypeReturn = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbReturn[i] = obj.getString("email");// + " " + obj.getString("password");
            dbAccountTypeReturn[i] = obj.getString("account_typ");
        }

        if(dbReturn.length == 1){
            if(dbAccountTypeReturn[0].equals("0")){
                loginPresenter.onConsumerLoginSuccess();
            }else if(dbAccountTypeReturn[0].equals("1")) {
                loginPresenter.onBusinessLoginSuccess();
            }
        } else{
            loginPresenter.onLoginFailure();
        }

    }

    @Override
    public void getUserInsertResult(String json){
        if(json.contains("Success")){
            signUpPresenter.onSuccessfulAddToDB();
        }else if (json.contains("Duplicate entry")){
            signUpPresenter.onFailAddToDB();
        }
    }

    @Override
    public void getFoodListFromDB(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);
        String[] dbReturn = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbReturn[i] = obj.getString("food_name");
        }

        if(!dbReturn[0].equals("null")){
            mainMenuPresenter.setFoodList(dbReturn);
        }

    }

    @Override
    public void getUserFoodListFromDB(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);
        String[] dbReturn = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbReturn[i] = obj.getString("food_name");
        }

        if(dbReturn.length > 0){
            mainMenuPresenter.setUserFoodList(dbReturn);
        }

    }

    @Override
    public void getFoodReportFromDB(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);
        String[] dbFoodList = new String[jsonArray.length()];
        String[] dbBatchAmountList = new String[jsonArray.length()];
        String[] dbFoodUsed = new String[jsonArray.length()];
        String[] dbFoodThrownOut = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbFoodList[i] = obj.getString("food_name");
            dbBatchAmountList[i] = obj.getString("batch_amount");
            dbFoodUsed[i] = obj.getString("food_used");
            dbFoodThrownOut[i] = obj.getString("food_thrown_out");
        }

        //if(dbFoodList.length > 0){
            foodReportPresenter.setFoodItems(dbFoodList, dbBatchAmountList, dbFoodUsed, dbFoodThrownOut);
        //}

    }

    @Override
    public void getRecipeFromDB(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);
        String[] dbRecipeName = new String[jsonArray.length()];
        String[] dbRecipeTime = new String[jsonArray.length()];
        String[] dbRecipeDescription = new String[jsonArray.length()];
        String[] dbPictureURL = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbRecipeName[i] = obj.getString("recipe_name");
            dbRecipeTime[i] = obj.getString("recipe_time_to_make_mins");
            dbRecipeDescription[i] = obj.getString("recipe_description");
            dbPictureURL[i] = obj.getString("image_url");
        }

        if(dbRecipeName.length > 0){
            recipePresenter.displayRecipeFromDB(dbRecipeName[0], dbRecipeTime[0], dbRecipeDescription[0], dbPictureURL[0]);
        }else{
            recipePresenter.displayRecipeFromDB("null", "null", "null", "null");
        }


    }

    @Override
    public void getUserDetailsFromDB(String json) throws JSONException{
        JSONArray jsonArray = new JSONArray(json);
        String[] dbFirstName = new String[jsonArray.length()];
        String[] dbLastName = new String[jsonArray.length()];
        String[] dbEmail = new String[jsonArray.length()];
        String[] dbPassword = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            dbFirstName[i] = obj.getString("first_name");
            dbLastName[i] = obj.getString("last_name");
            dbEmail[i] = obj.getString("email");
            dbPassword[i] = obj.getString("password");
        }

        if(dbFirstName.length > 0){
            accountSettingsPresenter.showUserDetails(dbFirstName[0], dbLastName[0], dbEmail[0], dbPassword[0]);
        }else{
            accountSettingsPresenter.showUserDetails("null", "null", "null", "null");
        }
    }

    @Override
    public void checkUserDetailsWereUpdated(String json){
        if(json.contains("Success")){
            accountSettingsPresenter.onSuccessfulUpdate();
        }else if (json.contains("Duplicate entry")){
            accountSettingsPresenter.onFailureUpdateDuplicateEmail();
        }else{
            accountSettingsPresenter.onFailureUpdateOther();
        }
    }

    //getters and setters
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public int getCheck() {
        return check;
    }

    public void setCheck(int check) throws IllegalArgumentException {
        if(check < 0){
            throw new IllegalArgumentException("Number must be 1 or greater");
        }else{
            this.check = check;
        }

    }

    public IMainMenuPresenter getMainMenuPresenter() {
        return mainMenuPresenter;
    }

    public void setMainMenuPresenter(IMainMenuPresenter mainMenuPresenter) {
        this.mainMenuPresenter = mainMenuPresenter;
    }

    public IViewStoredFoodItemsView getViewStoredFoodItemsView() {
        return viewStoredFoodItemsView;
    }

    public void setViewStoredFoodItemsView(IViewStoredFoodItemsView viewStoredFoodItemsView) {
        this.viewStoredFoodItemsView = viewStoredFoodItemsView;
    }

    public ILoginPresenter getLoginPresenter() {
        return loginPresenter;
    }

    public void setLoginPresenter(ILoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    public ISignUpPresenter getSignUpPresenter() {
        return signUpPresenter;
    }

    public void setSignUpPresenter(ISignUpPresenter signUpPresenter) {
        this.signUpPresenter = signUpPresenter;
    }

    public IFoodReportPresenter getFoodReportPresenter() {
        return foodReportPresenter;
    }

    public void setFoodReportPresenter(IFoodReportPresenter foodReportPresenter) {
        this.foodReportPresenter = foodReportPresenter;
    }

    public IRecipePresenter getRecipePresenter() {
        return recipePresenter;
    }

    public void setRecipePresenter(IRecipePresenter recipePresenter) {
        this.recipePresenter = recipePresenter;
    }

    public IAccountSettingsPresenter getAccountSettingsPresenter() {
        return accountSettingsPresenter;
    }

    public void setAccountSettingsPresenter(IAccountSettingsPresenter accountSettingsPresenter) {
        this.accountSettingsPresenter = accountSettingsPresenter;
    }
}
