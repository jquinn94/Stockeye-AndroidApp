package com.example.android.recappe.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.recappe.Login.view.ILoginView;
import com.example.android.recappe.Login.presenter.ILoginPresenter;
import com.example.android.recappe.Login.presenter.LoginPresenter;
import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.R;
import com.example.android.recappe.landingpage.MainActivity_landingpage;

import java.net.MalformedURLException;

public class MainActivity_login extends AppCompatActivity implements ILoginView{

    //variables
    private EditText email, password;
    private Button loginButton, cancelButton;
    private CheckBox checkBox;
    private ILoginPresenter loginPresenter;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    private int accountType;
    private String emailString, passwordString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //getting objects from xml
        email = findViewById(R.id.email_address);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        cancelButton = findViewById(R.id.cancelButton);
        checkBox = findViewById(R.id.checkbox);

        //getting login preferences
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        loginPresenter = new LoginPresenter(this);

        //checking if login preferences contain anything and skipping login page if they are
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            accountType = loginPreferences.getInt("account_type", -1);

            if(accountType == 0){
                startActivity(new Intent(this, MainActivity_consumermainmenu.class));
                finish();
            }else if(accountType == 1){
                startActivity(new Intent(this, MainActivity_businessmainmenu.class));
                finish();
            }else{
                Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
            }

        }

        //if sign in button is pressed
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting the users entries in string form
                emailString = email.getText().toString();
                passwordString = password.getText().toString();

                //checking if the remember me check box is ticked
                if (checkBox.isChecked() && !emailString.isEmpty() && !passwordString.isEmpty()) {
                    //saves users inputs if remember me button is pressed
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", emailString);
                    loginPrefsEditor.putString("password", passwordString);
                    loginPrefsEditor.commit();
                } else {
                    //clears any saved preferences but still need email for future pages
                    loginPrefsEditor.clear();
                    loginPrefsEditor.putString("username", emailString);
                    loginPrefsEditor.commit();
                }

                //creates login presenter
                try {
                    loginPresenter.onCheckLogin(emailString, passwordString);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });

        //checks if cancel button is pressed and returns to previous page if it has been
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity_landingpage.class));
                finish();
            }
        });

    }

    @Override
    public void onBusinessLoginSuccess() {
        loginPrefsEditor.putInt("account_type", 1);
        loginPrefsEditor.commit();
        startActivity(new Intent(this, MainActivity_businessmainmenu.class));
        finish();
    }

    @Override
    public void onConsumerLoginSuccess() {
        loginPrefsEditor.putInt("account_type", 0);
        loginPrefsEditor.commit();
        startActivity(new Intent(this, MainActivity_consumermainmenu.class));
        finish();
    }

    @Override
    public void onLoginFailure(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
