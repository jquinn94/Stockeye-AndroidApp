package com.example.android.recappe.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.android.recappe.FoodReport.MainActivity_foodreport;
import com.example.android.recappe.AccountSettings.MainActivity_account_settings;
import com.example.android.recappe.Preferences.MainActivity_preferences;
import com.example.android.recappe.TermsAndConsOrContactus.MainActivity_termsandcons_or_contactus;
import com.example.android.recappe.MainMenu.presenter.MainMenuPresenter;
import com.example.android.recappe.MainMenu.view.IMainMenuView;
import com.example.android.recappe.R;
import com.example.android.recappe.ViewStoredFoods.MainActivity_viewstoredfooditems;
import com.example.android.recappe.landingpage.MainActivity_landingpage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
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

import java.net.MalformedURLException;

public class MainActivity_businessmainmenu extends AppCompatActivity implements IMainMenuView {

    //vars
    private Button signOutBtn, scanFoodBtn, viewItemsBtn, foodReportBtn;
    private MainMenuPresenter mainMenuPresenter;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private String email;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;
    private String userInput;
    private SharedPreferences foodListVariable;
    private SharedPreferences.Editor foodListEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_businessmainmenu);

        signOutBtn = findViewById(R.id.signOut2);
        scanFoodBtn = findViewById(R.id.scan2);
        viewItemsBtn = findViewById(R.id.view2);
        foodReportBtn = findViewById(R.id.food_report);

        //get email address of logged in user
        foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        email = loginPreferences.getString("username","No name defined");
        loginPrefsEditor = loginPreferences.edit();
        loginPrefsEditor.putInt("account_type",1);
        loginPrefsEditor.commit();

        //creating required objects
        mainMenuPresenter = new MainMenuPresenter(this, email);

        //get list of all food names in db for if user wants to manually add food
        try {
            mainMenuPresenter.getFoodNames();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //getting toolbar object and adding hamburger icon to it
        toolbar = findViewById(R.id.my_toolbar2);
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
                        startActivity(new Intent(getApplicationContext(), MainActivity_termsandcons_or_contactus.class));
                        //Toast.makeText(getApplicationContext(), "Settings",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.contactus:
                        loginPrefsEditor.putInt("pageGoTo",1);
                        startActivity(new Intent(getApplicationContext(), MainActivity_termsandcons_or_contactus.class));
                        //Toast.makeText(getApplicationContext(), "My Cart",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.DeleteAccount:
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_businessmainmenu.this);
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

        //if the scan food button is clicked
        scanFoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_businessmainmenu.this);
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_businessmainmenu.this);
                        builder.setTitle("Details of food item to add:");
                        builder.setCancelable(true);

                        foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
                        String listToParse = foodListVariable.getString("foodList", "No list available");
                        String[] foodNameList = listToParse.split(",");

                        final ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity_businessmainmenu.this,
                                android.R.layout.simple_spinner_item, foodNameList);

                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(MainActivity_businessmainmenu.LAYOUT_INFLATER_SERVICE);
                        View view = inflater.inflate(R.layout.add_food_manually_alert_dialog, null, false);

                        final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                        spinner.setAdapter(adp);

                        final EditText ageOfFood = view.findViewById(R.id.foodage);
                        final EditText batchAmount = view.findViewById(R.id.batch_amount);

                        builder.setView(view);

                        // Set up the buttons
                        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if((!ageOfFood.getText().toString().isEmpty()) && (!batchAmount.getText().toString().isEmpty())) {
                                    try {
                                        mainMenuPresenter.addFoodToDB(spinner.getSelectedItem().toString(), new Integer(ageOfFood.getText().toString()), batchAmount.getText().toString());
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
                        alert1.show();;

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

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_businessmainmenu.this);
                        builder.setTitle("How many are in the batch?");
                        builder.setCancelable(true);

                        // Set up the input
                        final EditText input = new EditText(MainActivity_businessmainmenu.this);
                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                        builder.setView(input);

                        // Set up the buttons
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userInput = input.getText().toString();
                                try {
                                    mainMenuPresenter.addFoodToDB("Banana", Character.getNumericValue(ageOfFood[3]), userInput);
                                    Toast.makeText(getApplicationContext(), "Added to stored food list", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
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
                        alert1.show();;
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
    public void setUserFoodListName(String[] listOfNames) {

    }
}
