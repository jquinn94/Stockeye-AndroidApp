package com.example.android.recappe.ViewStoredFoods;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.InputType;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.recappe.AccountSettings.MainActivity_account_settings;
import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.Preferences.MainActivity_preferences;
import com.example.android.recappe.R;
import com.example.android.recappe.ViewStoredFoods.Presenter.ViewStoredFoodItemsPresenter;
import com.example.android.recappe.ViewStoredFoods.View.IViewStoredFoodItemsView;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity_viewstoredfooditems extends AppCompatActivity implements IViewStoredFoodItemsView {

    //vars
    private ViewStoredFoodItemsPresenter viewStoredFoodItemsPresenter;
    private String email;
    private LinearLayout linearLayout;
    private SharedPreferences loginPreferences;
    private int accountType;
    private Toolbar toolbar;
    private String m_Text = "0";
    private String[] foodNames;
    private SharedPreferences foodListVariable;
    private SharedPreferences.Editor foodListEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_viewstoredfooditems);

        //getting objects from from xml layout
        linearLayout = findViewById(R.id.exampletest);

        //getting toolbar object and adding back icon to it and title
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Food items");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);

        //get email address of logged in user
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        email = loginPreferences.getString("username","No name defined");

        //create presenter object and pass it email address
        viewStoredFoodItemsPresenter = new ViewStoredFoodItemsPresenter(email, this);

        //getting the users stored food items
        try {
            viewStoredFoodItemsPresenter.getFoodItemsForUser();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        accountType = loginPreferences.getInt("account_type",2);

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
                    Toast.makeText(getApplicationContext(), "ERROR signing out", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onSuccessOfFoodItemsFromDB(final String[] foodNames, String[] datesAdded, String[] foodID, String[] age, final String[] batchAmount, String[] thrownOutAmount, String[] usedAmount, String[] foodExpiryDays) throws ParseException {
        // Dynamically add textviews to display information retrieved from database
        for(int loop = 0; loop < foodNames.length; loop++){
            final String foodName = foodNames[loop];
            final Integer foodIDAsInt = new Integer(foodID[loop]);
            final int batchAmountLeft = new Integer(batchAmount[loop]) - (new Integer(thrownOutAmount[loop]) + new Integer(usedAmount[loop]));
            TextView textView1 = new TextView(this);
            textView1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textView1.setText(foodNames[loop]);
            textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
            textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)

            //add clickable listener so user can click on listed food and choose what to do with it
            textView1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(v.getContext());
                    builder1.setMessage("What would you like to do?");
                    builder1.setCancelable(true);

                    builder1.setPositiveButton(
                            "Delete food item",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.cancel();
                                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity_viewstoredfooditems.this);
                                    builder2.setMessage("Are you sure you want to delete " + foodName + "?");
                                    builder2.setCancelable(true);

                                    builder2.setPositiveButton(
                                            "Confirm",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                     try {
                                                        viewStoredFoodItemsPresenter.deleteFoodFromDB(foodName, foodIDAsInt);
                                                    } catch (MalformedURLException e) {
                                                        e.printStackTrace();
                                                    }
                                                    Toast.makeText(getApplicationContext(), "Food deleted", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                                                    dialog.cancel();
                                                }
                                            });

                                    builder2.setNegativeButton(
                                            "Cancel",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Toast.makeText(getApplicationContext(), "Cancel", Toast.LENGTH_SHORT).show();
                                                    dialog.cancel();
                                                }
                                            });

                                    AlertDialog alert2 = builder2.create();
                                    alert2.show();
                                }
                            });
                    builder1.setNegativeButton(
                            "Update",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    AlertDialog.Builder builder4 = new AlertDialog.Builder(MainActivity_viewstoredfooditems.this);
                                    builder4.setMessage("What would you like to update?");
                                    builder4.setCancelable(true);

                                    builder4.setPositiveButton(
                                            "Update details",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    AlertDialog.Builder builder3 = new AlertDialog.Builder(MainActivity_viewstoredfooditems.this);
                                                    builder3.setMessage("Update via photograph or manually?");
                                                    builder3.setCancelable(true);

                                                    builder3.setPositiveButton(
                                                            "Manual",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    dialog.cancel();

                                                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_viewstoredfooditems.this);
                                                                    builder.setTitle("Updated food details:");
                                                                    builder.setCancelable(true);

                                                                    foodListVariable = getSharedPreferences("foodListPrefs", MODE_PRIVATE);
                                                                    String listToParse = foodListVariable.getString("foodList", "No list available");
                                                                    String[] foodNameList = listToParse.split(",");

                                                                    //String[] foodNames = MainMenuPresenter.getFoodNames();
                                                                    final ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity_viewstoredfooditems.this,
                                                                            android.R.layout.simple_spinner_item, foodNameList);

                                                                    LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(MainActivity_viewstoredfooditems.LAYOUT_INFLATER_SERVICE);
                                                                    View view = inflater.inflate(R.layout.add_food_manually_alert_dialog, null, false);

                                                                    final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
                                                                    spinner.setAdapter(adp);

                                                                    final EditText ageOfFood = view.findViewById(R.id.foodage);
                                                                    final EditText batchAmount = view.findViewById(R.id.batch_amount);

                                                                    if (accountType == 0) {
                                                                        batchAmount.setVisibility(View.GONE);
                                                                    }

                                                                    builder.setView(view);

                                                                    // Set up the buttons
                                                                    builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which) {
                                                                            String finalBatchAmount = "1";
                                                                            if (accountType == 1) {
                                                                                finalBatchAmount = batchAmount.getText().toString();
                                                                            }
                                                                            if ((!ageOfFood.getText().toString().isEmpty()) && (!finalBatchAmount.isEmpty())) {
                                                                                try {
                                                                                    viewStoredFoodItemsPresenter.updateFoodInDB(foodIDAsInt, new Integer(ageOfFood.getText().toString()), spinner.getSelectedItem().toString(), finalBatchAmount);
                                                                                    startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                                                                                } catch (MalformedURLException e) {
                                                                                    e.printStackTrace();
                                                                                }
                                                                            } else {
                                                                                Toast.makeText(getApplicationContext(), "Input left blank", Toast.LENGTH_SHORT).show();
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
                                                                    ;

                                                                    //startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                                                                    dialog.cancel();
                                                                }
                                                            });

                                                    builder3.setNegativeButton(
                                                            "Photograph",
                                                            new DialogInterface.OnClickListener() {
                                                                public void onClick(DialogInterface dialog, int id) {
                                                                    viewStoredFoodItemsPresenter.updateFoodFromDBGetPhoto(foodIDAsInt, MainActivity_viewstoredfooditems.this);
                                                                    dialog.cancel();
                                                                }
                                                            });

                                                    AlertDialog alert3 = builder3.create();
                                                    alert3.show();
                                                    dialog.cancel();
                                                }
                                            });
                                    builder4.setNegativeButton(
                                            "Food used",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    if (accountType == 1) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_viewstoredfooditems.this);
                                                        builder.setTitle("Amount of food batch used:");
                                                        builder.setCancelable(true);

                                                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(MainActivity_viewstoredfooditems.LAYOUT_INFLATER_SERVICE);
                                                        View view = inflater.inflate(R.layout.adjust_foodused_or_foodthrownout, null, false);

                                                        final EditText amountUsed = view.findViewById(R.id.used_amount);
                                                        final EditText amountThrownOut = view.findViewById(R.id.thrown_out_amount);
                                                        amountThrownOut.setVisibility(View.GONE);

                                                        builder.setView(view);

                                                        // Set up the buttons
                                                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if((!amountUsed.getText().toString().isEmpty())) {
                                                                    if(new Integer(amountUsed.getText().toString()) <= new Integer(batchAmountLeft)){
                                                                        try {
                                                                            viewStoredFoodItemsPresenter.updateFoodUsedAmountInDB(foodIDAsInt, new Integer(amountUsed.getText().toString()));
                                                                        } catch (MalformedURLException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }else{
                                                                        Toast.makeText(getApplicationContext(), "Must be less than or equal to batch amount left", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }else{
                                                                    Toast.makeText(getApplicationContext(), "Must fill in all fields", Toast.LENGTH_SHORT).show();
                                                                }
                                                                startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
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
                                                    }else{
                                                        try {
                                                            viewStoredFoodItemsPresenter.updateFoodUsedAmountInDB(foodIDAsInt, 1);
                                                        } catch (MalformedURLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                                                        dialog.cancel();
                                                    }

                                                }
                                            });
                                    builder4.setNeutralButton(
                                            "Food thrown out",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    if (accountType == 1) {
                                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_viewstoredfooditems.this);
                                                        builder.setTitle("Amount of food batch thrown out:");
                                                        builder.setCancelable(true);

                                                        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(MainActivity_viewstoredfooditems.LAYOUT_INFLATER_SERVICE);
                                                        View view = inflater.inflate(R.layout.adjust_foodused_or_foodthrownout, null, false);

                                                        final EditText amountUsed = view.findViewById(R.id.used_amount);
                                                        final EditText amountThrownOut = view.findViewById(R.id.thrown_out_amount);
                                                        amountUsed.setVisibility(View.GONE);

                                                        builder.setView(view);

                                                        // Set up the buttons
                                                        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int which) {
                                                                if((!amountThrownOut.getText().toString().isEmpty())) {
                                                                    if(new Integer(amountThrownOut.getText().toString()) <= new Integer(batchAmountLeft)){
                                                                        try {
                                                                            viewStoredFoodItemsPresenter.updateFoodThrownOutAmountInDB(foodIDAsInt, new Integer(amountThrownOut.getText().toString()));
                                                                        } catch (MalformedURLException e) {
                                                                            e.printStackTrace();
                                                                        }
                                                                    }else{
                                                                        Toast.makeText(getApplicationContext(), "Must be less than or equal to batch amount left", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }else{
                                                                    Toast.makeText(getApplicationContext(), "Must fill in all fields", Toast.LENGTH_SHORT).show();
                                                                }
                                                                startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
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
                                                    }else{
                                                        try {
                                                            viewStoredFoodItemsPresenter.updateFoodThrownOutAmountInDB(foodIDAsInt, 1);
                                                        } catch (MalformedURLException e) {
                                                            e.printStackTrace();
                                                        }
                                                        startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                                                        dialog.cancel();
                                                    }
                                                }
                                            });

                                    AlertDialog alert2 = builder4.create();
                                    alert2.show();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();

                }
            });
            linearLayout.addView(textView1);

            TextView textView2 = new TextView(this);
            textView2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.UK);
            Date date = sdf.parse(datesAdded[loop]);
            String date1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
            textView2.setText("Date added: " + date1);

            textView2.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            textView2.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
            linearLayout.addView(textView2);

            TextView textView3 = new TextView(this);
            textView3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            textView3.setText("Age in days: " + age[loop]);
            textView3.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
            textView3.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
            linearLayout.addView(textView3);

            TextView textView4 = new TextView(this);
            if(accountType == 1){
                textView4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                textView4.setText("Batch amount left: " + batchAmountLeft + "/" + batchAmount[loop]);
                textView4.setTextSize(TypedValue.COMPLEX_UNIT_SP,10);
                textView4.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
                linearLayout.addView(textView4);
            }

            /*Date todaysDate = new Date(); // This object contains the current date value
            int expiryDays = new Integer(foodExpiryDays[loop]);
            long daysDiff1 = todaysDate.getTime() - date.getTime();
            long daysDiff = (daysDiff1 / (1000 * 60 * 60 * 24)) - new Integer(age[loop]);*/

            int expiryDays = new Integer(foodExpiryDays[loop]);
            int foodAge = new Integer(age[loop]);

            //System.out.println("*******************************" + daysDiff);

            if(foodAge < (expiryDays-3)){
                textView1.setBackgroundColor(Color.rgb(3,172,19));
                textView2.setBackgroundColor(Color.rgb(3,172,19));
                textView3.setBackgroundColor(Color.rgb(3,172,19));
                if(accountType == 1){
                    textView4.setBackgroundColor(Color.rgb(3,172,19));
                }
            }else if (foodAge < expiryDays){
                textView1.setBackgroundColor(Color.rgb(253,106,2));
                textView2.setBackgroundColor(Color.rgb(253,106,2));
                textView3.setBackgroundColor(Color.rgb(253,106,2));
                if(accountType == 1){
                    textView4.setBackgroundColor(Color.rgb(253,106,2));
                }
            }else{
                textView1.setBackgroundColor(Color.rgb(194,24,7));
                textView2.setBackgroundColor(Color.rgb(194,24,7));
                textView3.setBackgroundColor(Color.rgb(194,24,7));
                if(accountType == 1){
                    textView4.setBackgroundColor(Color.rgb(194,24,7));
                }
            }

            View view = new View(this);
            view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
            view.setBackgroundColor(Color.parseColor("#c0c0c0"));
            linearLayout.addView(view);



        }
    }

    @Override
    public void onSuccessfulUpdate(String result, final int foodID){
        final char[] ageOfFood = result.toCharArray();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Is this a banana? Estimated " + ageOfFood[3] + " days old");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        //if business account then batch amount can be updated too
                        if(accountType == 1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity_viewstoredfooditems.this);
                            builder.setTitle("Updated batch amount?");
                            builder.setCancelable(true);

                            // Set up the input
                            final EditText input = new EditText(MainActivity_viewstoredfooditems.this);

                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                            builder.setView(input);

                            // Set up the buttons
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    m_Text = input.getText().toString();
                                    try {
                                        viewStoredFoodItemsPresenter.updateFoodInDB(foodID, Character.getNumericValue(ageOfFood[3]),"Banana", m_Text);
                                        Toast.makeText(getApplicationContext(), "Updated to stored food list", Toast.LENGTH_SHORT).show();
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
                        else{
                            try {
                                viewStoredFoodItemsPresenter.updateFoodInDB(foodID, Character.getNumericValue(ageOfFood[3]),"Banana", m_Text);
                                Toast.makeText(getApplicationContext(), "Updated to stored food list", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), MainActivity_viewstoredfooditems.class));
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
        builder1.setNeutralButton(
                "Retry",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewStoredFoodItemsPresenter.updateFoodFromDBGetPhoto(foodID, MainActivity_viewstoredfooditems.this);
                        dialog.cancel();
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

    @Override
    public void onFailureOfFoodItemsFromDB(){
        TextView textView1 = new TextView(this);
        textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        textView1.setText("No food items stored");
        textView1.setTextColor(Color.rgb(255,255,255));
        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        textView1.setPadding(20, 20, 20, 20);// in pixels (left, top, right, bottom)
        linearLayout.addView(textView1);

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
            viewStoredFoodItemsPresenter.uploadPhotoToServer();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.action_settings:
            //    return true;

            case R.id.filterearliest:
                try {
                    linearLayout.removeAllViews();
                    viewStoredFoodItemsPresenter.getFoodItemsForUser();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.filterlatest:
                try {
                    linearLayout.removeAllViews();
                    viewStoredFoodItemsPresenter.getFoodItemsForUserSorted();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.filtersoonest:
                try {
                    linearLayout.removeAllViews();
                    viewStoredFoodItemsPresenter.getFoodItemsForUserSorted2();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return true;

        }

        return true;
    }

}
