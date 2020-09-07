package com.example.android.recappe.Preferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.R;

public class MainActivity_preferences extends AppCompatActivity {

    private Toolbar toolbar;
    private SharedPreferences loginPreferences;
    private int accountType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_preferences);

        //getting toolbar object and adding back icon to it and title
        toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Preferences");
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow_dark);

        //get email address and account type of logged in user
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
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
                    Toast.makeText(getApplicationContext(), "ERROR", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
