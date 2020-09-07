package com.example.android.recappe.landingpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.android.recappe.Login.MainActivity_login;
import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.R;
import com.example.android.recappe.SignUp.MainActivity_signup;

public class MainActivity_landingpage extends AppCompatActivity {

    //vars
    private Button signInBtn, signUpBtn;
    private SharedPreferences loginPreferences;
    private Boolean saveLogin;
    private int accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_landingpage);

        //getting buttons from xml
        signInBtn = findViewById(R.id.signInButton);
        signUpBtn = findViewById(R.id.signUpButton);

        //getting saved login preferences
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);

        //checking if there are any saved login preferences and skipping login pages if there are
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

        //going to login page if login button is pressed
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity_login.class));
                finish();
            }
        });

        //going to login page if login button is pressed
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity_signup.class));
                finish();
            }
        });
    }
}
