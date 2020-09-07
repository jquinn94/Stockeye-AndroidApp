package com.example.android.recappe.Recipe;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.R;
import com.example.android.recappe.Recipe.Presenter.RecipePresenter;
import com.example.android.recappe.Recipe.View.IView;

import java.net.MalformedURLException;

public class MainActivity_recipe extends AppCompatActivity implements IView {

    private ImageView imageView;
    private TextView recipeName, recipeTime, recipeDescription;
    private RecipePresenter recipePresenter;
    private SharedPreferences loginPreferences;
    private String email, ingredient;
    private Toolbar toolbar;
    private int accountType;
    private SharedPreferences foodListVariable;
    private SharedPreferences.Editor foodListEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recipe);

        //getting items from xml file
        imageView = findViewById(R.id.food_image);
        recipeName = findViewById(R.id.recipe_name_data);
        recipeTime = findViewById(R.id.recipe_time_data);
        recipeDescription = findViewById(R.id.recipe_description_data);

        //getting toolbar object and adding back icon to it and title
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Recipe");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);

        //get email address and account type of logged in user
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        email = loginPreferences.getString("username","No name defined");
        accountType = loginPreferences.getInt("account_type",2);

        //getting user chosen ingredient
        foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
        ingredient = foodListVariable.getString("ingredient","No name defined");

        //instantiating recipe presenter and using it to get a recipe to display
        recipePresenter = new RecipePresenter(this, email);

        if(ingredient.equals("No name defined")){
            try {
                recipePresenter.getRecipe();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }else{
            try {
                foodListEditor = foodListVariable.edit();
                foodListEditor.clear();
                foodListEditor.commit();
                recipePresenter.getRecipeWithIngredient(ingredient);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        //listener to check if back button has been pressed which diverts back to main menu page
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accountType == 0){
                    startActivity(new Intent(getApplicationContext(), MainActivity_consumermainmenu.class));
                    finish();
                }else if(accountType == 1){
                    startActivity(new Intent(getApplicationContext(), MainActivity_businessmainmenu.class));
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void displayRecipeFromDB(String name, String time, String description, String pictureURL) {

        if(name.equals("null")){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("No recipes available with your stored foods");
            builder.setCancelable(true);

            builder.setPositiveButton(
                    "Ok",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            if(accountType == 0){
                                startActivity(new Intent(getApplicationContext(), MainActivity_consumermainmenu.class));
                                finish();
                            }else if(accountType == 1){
                                startActivity(new Intent(getApplicationContext(), MainActivity_businessmainmenu.class));
                                finish();
                            }else{
                                Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            AlertDialog alert1 = builder.create();
            alert1.show();
        }else{
            recipeName.setText(name);
            recipeTime.setText(time);
            recipeDescription.setText(description);
            recipePresenter.getRecipePicture(pictureURL);
        }


    }

    @Override
    public void displayRecipePicture(Bitmap picture) {
        imageView.setImageBitmap(picture);
    }
}
