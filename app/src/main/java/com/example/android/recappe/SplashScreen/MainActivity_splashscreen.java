package com.example.android.recappe.SplashScreen;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import com.example.android.recappe.R;
import com.example.android.recappe.landingpage.MainActivity_landingpage;

public class MainActivity_splashscreen extends AppCompatActivity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_splashscreen);

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                startActivity(new Intent(MainActivity_splashscreen.this, MainActivity_landingpage.class));
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
