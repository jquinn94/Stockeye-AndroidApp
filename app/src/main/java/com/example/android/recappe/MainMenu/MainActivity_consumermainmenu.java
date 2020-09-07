package com.example.android.recappe.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.recappe.FoodReport.MainActivity_foodreport;
import com.example.android.recappe.AccountSettings.MainActivity_account_settings;
import com.example.android.recappe.Preferences.MainActivity_preferences;
import com.example.android.recappe.TermsAndConsOrContactus.MainActivity_termsandcons_or_contactus;
import com.example.android.recappe.MainMenu.model.Photo;
import com.example.android.recappe.MainMenu.presenter.MainMenuPresenter;
import com.example.android.recappe.MainMenu.view.IMainMenuView;
import com.example.android.recappe.R;
import com.example.android.recappe.Recipe.MainActivity_recipe;
import com.example.android.recappe.ViewStoredFoods.MainActivity_viewstoredfooditems;
import com.example.android.recappe.landingpage.MainActivity_landingpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.net.MalformedURLException;

public class MainActivity_consumermainmenu extends AppCompatActivity implements IMainMenuView {

    //variables
    private Button scanFoodBtn, viewItemsBtn, generateRecipeBtn, signOutBtn, foodReportBtn;
    private MainMenuPresenter mainMenuPresenter;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private String email;
    private SharedPreferences foodListVariable;
    private SharedPreferences.Editor foodListEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_consumermainmenu);

        //get email address of logged in user
        foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        email = loginPreferences.getString("username","No name defined");
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putInt("account_type",0);
        loginPrefsEditor.commit();

        //creating required objects
        mainMenuPresenter = new MainMenuPresenter(this, email);

        //get list of all food names in db for if user wants to manually add food
        try {
            mainMenuPresenter.getFoodNames();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //get list of all user food names in db
        try {
            mainMenuPresenter.getUserFoodNames();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //getting items from xml
        scanFoodBtn = findViewById(R.id.scan);
        viewItemsBtn = findViewById(R.id.view);
        generateRecipeBtn = findViewById(R.id.generate);
        signOutBtn = findViewById(R.id.signOut);
        foodReportBtn = findViewById(R.id.food_report);

        //getting toolbar object and adding hamburger icon to it
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Main Menu");
        toolbar.setNavigationIcon(R.drawable.ic_hamburger);

        //getting drawer object and adding listener
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_menu);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.Open, R.string.Close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        //adding users name programmatically to drawer
        navigationView = (NavigationView)findViewById(R.id.nv);
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header);
        TextView nav_user = headerView.findViewById(R.id.name);
        //nav_user.setText(mainMenuPresenter.getUserName());
        try {
            mainMenuPresenter.getUserName(nav_user);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //adding listener for the drawer menu options
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext(), MainActivity_account_settings.class));
                        break;
                    case R.id.preferences:
                        startActivity(new Intent(getApplicationContext(), MainActivity_preferences.class));
                        break;
                    case R.id.TandC:
                        loginPrefsEditor.putInt("pageGoTo",0);
                        loginPrefsEditor.commit();
                        startActivity(new Intent(getApplicationContext(), MainActivity_termsandcons_or_contactus.class));
                        break;
                    case R.id.contactus:
                        loginPrefsEditor.putInt("pageGoTo",1);
                        loginPrefsEditor.commit();
                        startActivity(new Intent(getApplicationContext(), MainActivity_termsandcons_or_contactus.class));
                        break;
                    case R.id.DeleteAccount:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_consumermainmenu.this);
                        builder.setTitle("Are you sure you want to delete this account?");
                        builder.setCancelable(true);

                        // Set up the buttons
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    mainMenuPresenter.onDeleteAccount();
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }

                                loginPrefsEditor.clear();
                                loginPrefsEditor.commit();
                                startActivity(new Intent(getApplicationContext(),MainActivity_landingpage.class));
                                dialog.cancel();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.show();;
                        break;
                    default:
                        return true;
                }
                return true;
            }
        });


        //if the add food button is clicked
        scanFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_consumermainmenu.this);
                builder.setTitle("How would you like to add the food item?");
                builder.setCancelable(true);

                // Set up the buttons
                builder.setPositiveButton("Photographically", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainMenuPresenter.onAddFoodPhotographically();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("Manually", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_consumermainmenu.this);
                        builder.setTitle("Details of food item to add:");
                        builder.setCancelable(true);

                        foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
                        String listToParse = foodListVariable.getString("foodList", "No list available");
                        String[] foodNameList = listToParse.split(",");

                        final ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity_consumermainmenu.this,
                                android.R.layout.simple_spinner_item, foodNameList);

                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(MainActivity_businessmainmenu.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.add_food_manually_alert_dialog, null, false);

                        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                        spinner.setAdapter(adp);

                        final EditText ageOfFood = view.findViewById(R.id.foodage);
                        final EditText batchAmount = view.findViewById(R.id.batch_amount);
                        batchAmount.setVisibility(View.GONE);

                        builder.setView(view);

                        // Set up the buttons
                        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if((!ageOfFood.getText().toString().isEmpty())) {
                                    try {
                                        mainMenuPresenter.addFoodToDB(spinner.getSelectedItem().toString(), new Integer(ageOfFood.getText().toString()), "1");
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), "Must fill in all fields", Toast.LENGTH_SHORT).show();
                                }
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert1 = builder.create();
                        alert1.show();

                        dialog.cancel();
                    }
                });

                AlertDialog alert1 = builder.create();
                alert1.show();;
            }
        });

        //if the view stored food items button is clicked
        viewItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                finish();
            }
        });

        //if the generate recipe button is clicked
        generateRecipeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_consumermainmenu.this);
                builder.setTitle("Do you want to pick an ingredient to include in recipe?");
                builder.setCancelable(true);
                // Set up the buttons
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_consumermainmenu.this);
                        builder.setTitle("Select ingredient:");
                        builder.setCancelable(true);

                        foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
                        String listToParse = foodListVariable.getString("userfoodList", "No list available");
                        String[] foodNameList = listToParse.split(",");

                        final ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity_consumermainmenu.this,
                                android.R.layout.simple_spinner_item, foodNameList);

                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(MainActivity_consumermainmenu.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.add_food_manually_alert_dialog, null, false);

                        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                        spinner.setAdapter(adp);

                        final EditText ageOfFood = view.findViewById(R.id.foodage);
                        final EditText batchAmount = view.findViewById(R.id.batch_amount);
                        batchAmount.setVisibility(View.GONE);
                        ageOfFood.setVisibility(View.GONE);

                        builder.setView(view);

                        // Set up the buttons
                        builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                foodListEditor = foodListVariable.edit();
                                foodListEditor.putString("ingredient", spinner.getSelectedItem().toString());
                                foodListEditor.commit();
                                startActivity(new Intent(getApplicationContext(), MainActivity_recipe.class));
                                dialog.cancel();
                            }
                        });
                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                        AlertDialog alert1 = builder.create();
                        alert1.show();

                        dialog.cancel();

                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplicationContext(), MainActivity_recipe.class));
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();;
                //Toast.makeText(getApplicationContext(), "Generate", Toast.LENGTH_SHORT).show();
            }
        });

        //if the food report button is clicked
        foodReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity_foodreport.class));
                finish();
            }
        });

        //if the sign out button is clicked
        signOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    mainMenuPresenter.signOutRemovePushNotificationID();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                loginPrefsEditor.clear();
                loginPrefsEditor.commit();
                startActivity(new Intent(getApplicationContext(), MainActivity_landingpage.class));
                finish();
            }
        });

        //when new push notification id is generated this method is called
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("MainActivity", "getInstanceId failed", task.getException());
                            Toast.makeText(getApplicationContext(), "ERROR connecting to push notifications", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        //uploading new push notification id to the database
                        try {
                            mainMenuPresenter.updateUserPushNotificationID(token);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }


                    }
                });

    }

    @Override
    public void onGetMachineLearningFoodAnswer(String result) {
        final char[] ageOfFood = result.toCharArray();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Is this a banana? Estimated " + ageOfFood[3] + " days old");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        try {
                            mainMenuPresenter.addFoodToDB("Banana", Character.getNumericValue(ageOfFood[3]), "1");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        Toast.makeText(getApplicationContext(), "Added to stored food list", Toast.LENGTH_SHORT).show();
                    }
                });
        builder1.setNeutralButton(
                "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mainMenuPresenter.onAddFoodPhotographically();
                    }
                });
        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();

    }

    /**
     * handles the activity result of galleryPhoto and/or cameraPhoto
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 ) {
            mainMenuPresenter.uploadPhotoToServer();
        }

    }

    @Override
    public void setFoodListName(String[] listOfNames){
        //foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
        foodListEditor = foodListVariable.edit();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listOfNames.length; i++) {
            sb.append(listOfNames[i]).append(",");
        }
        foodListEditor.putString("foodList", sb.toString());
        foodListEditor.commit();
    }

    @Override
    public void setUserFoodListName(String[] listOfNames){
        //foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
        foodListEditor = foodListVariable.edit();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < listOfNames.length; i++) {
            sb.append(listOfNames[i]).append(",");
        }
        foodListEditor.putString("userfoodList", sb.toString());
        foodListEditor.commit();
    }

}
