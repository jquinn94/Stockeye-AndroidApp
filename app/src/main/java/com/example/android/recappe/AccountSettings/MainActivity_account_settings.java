package com.example.android.recappe.AccountSettings;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.recappe.AccountSettings.Presenter.AccountSettingsPresenter;
import com.example.android.recappe.AccountSettings.Presenter.IAccountSettingsPresenter;
import com.example.android.recappe.AccountSettings.View.IView;
import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.R;
import com.example.android.recappe.landingpage.MainActivity_landingpage;

import java.net.MalformedURLException;

public class MainActivity_account_settings extends AppCompatActivity implements IView {

    private Toolbar toolbar;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private int accountType;
    private String email;
    private EditText firstName, lastName, emailInput, password;
    private Button submitBtn;
    private IAccountSettingsPresenter accountSettingsPresenter;
    private String firstNameForDB, lastNameForDB, emailForDB, passwordForDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_account_settings);

        //getting items from xml
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        emailInput = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        submitBtn = findViewById(R.id.submitChangesButton);

        //getting toolbar object and adding back icon to it and title
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Settings");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);

        //get email address and account type of logged in user
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        email = loginPreferences.getString("username","No name defined");
        accountType = loginPreferences.getInt("account_type",2);
        loginPrefsEditor = loginPreferences.edit();

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


        accountSettingsPresenter = new AccountSettingsPresenter(email, this);
        try {
            accountSettingsPresenter.getUserDetails();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameForDB = firstName.getHint().toString();
                lastNameForDB = lastName.getHint().toString();
                emailForDB = emailInput.getHint().toString();
                passwordForDB = password.getHint().toString();

                if(!firstName.getText().toString().isEmpty() || !lastName.getText().toString().isEmpty() || !emailInput.getText().toString().isEmpty() || !password.getText().toString().isEmpty()) {

                    if (!firstName.getText().toString().isEmpty()) {
                        firstNameForDB = firstName.getText().toString();
                    }

                    if (!lastName.getText().toString().isEmpty()) {
                        lastNameForDB = lastName.getText().toString();
                    }

                    if (!emailInput.getText().toString().isEmpty()) {
                        emailForDB = emailInput.getText().toString();
                    }

                    if (!password.getText().toString().isEmpty()) {
                        passwordForDB = password.getText().toString();
                    }

                    if(passwordForDB.length() >= 6){
                        if(passwordForDB.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")){
                            if(Patterns.EMAIL_ADDRESS.matcher(emailForDB).matches()){
                                try {
                                    accountSettingsPresenter.updateUserDetails(firstNameForDB, lastNameForDB, emailForDB, passwordForDB);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "Not a valid email address", Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(getApplicationContext(), "Password must contain an uppercase letter, lowercase letter and a number", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Password must be greater than 6 characters", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Must change one of the fields", Toast.LENGTH_SHORT).show();
                }

                /*
                if(!firstName.getText().toString().isEmpty() && !lastName.getText().toString().isEmpty() && !emailInput.getText().toString().isEmpty() && !password.getText().toString().isEmpty()){
                    try {
                        accountSettingsPresenter.updateUserDetails(firstName.getText().toString(), lastName.getText().toString(), emailInput.getText().toString(), password.getText().toString());
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "All inputs need to be filled out", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    @Override
    public void showUserDetails(String firstNameDB, String lastNameDB, String emailDB, String passwordDB) {
        firstName.setHint(firstNameDB);
        lastName.setHint(lastNameDB);
        emailInput.setHint(emailDB);
        password.setHint(passwordDB);
    }

    @Override
    public void onSuccessfulUpdate() {
        loginPrefsEditor.putString("username", emailForDB);
        loginPrefsEditor.putString("password", passwordForDB);
        loginPrefsEditor.commit();
        startActivity(new Intent(getApplicationContext(), MainActivity_account_settings.class));
    }

    @Override
    public void onFailedAddDuplicateEmail() {
        Toast.makeText(getApplicationContext(), "Email has been used by another user", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailedAddOther() {
        Toast.makeText(getApplicationContext(), "Error, try again", Toast.LENGTH_SHORT).show();
    }
}
