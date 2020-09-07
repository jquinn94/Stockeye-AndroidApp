package com.example.android.recappe.SignUp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.android.recappe.Database.Database;
import com.example.android.recappe.MainMenu.MainActivity_businessmainmenu;
import com.example.android.recappe.MainMenu.MainActivity_consumermainmenu;
import com.example.android.recappe.R;
import com.example.android.recappe.SignUp.Presenter.SignUpPresenter;
import com.example.android.recappe.SignUp.View.ISignUpView;
import com.example.android.recappe.landingpage.MainActivity_landingpage;

import java.net.MalformedURLException;

public class MainActivity_signup extends AppCompatActivity implements ISignUpView {

    private Spinner staticSpinner;
    private ArrayAdapter<CharSequence> staticAdapter;
    private Button signUpBtn, cancelBtn;
    private EditText email, password, firstName, lastName, passwordConfirm;
    private String text, emailString, passwordString, firstNameString, lastNameString, passwordConfirmString;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_signup);

        //create required object
        final SignUpPresenter signUpPresenter = new SignUpPresenter(this);

        //setting up object to store users details
        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        //setting up drop down list
        staticSpinner = (Spinner) findViewById(R.id.static_spinner);
        staticAdapter = ArrayAdapter.createFromResource(this, R.array.drop_down_list_array, R.layout.simple_spinner_item);
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        staticSpinner.setAdapter(staticAdapter);

        //getting layout items
        signUpBtn = findViewById(R.id.signUpButton);
        cancelBtn = findViewById(R.id.cancelButton);

        //checks if cancel button is pressed and returns to previous page if it has been
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting user inputted items
                email = findViewById(R.id.email_address);
                password = findViewById(R.id.password);
                passwordConfirm = findViewById(R.id.passwordconfirm);
                firstName = findViewById(R.id.first_name);
                lastName = findViewById(R.id.last_name);
                text = staticSpinner.getSelectedItem().toString();

                //converting to strings
                emailString = email.getText().toString();
                passwordString = password.getText().toString();
                passwordConfirmString = passwordConfirm.getText().toString();
                firstNameString = firstName.getText().toString();
                lastNameString = lastName.getText().toString();

                //checks if all inputs have been filled out
                if(!text.isEmpty() && !emailString.isEmpty() && !passwordString.isEmpty() && !firstNameString.isEmpty() && !lastNameString.isEmpty()){
                    String accountType = "";

                    if(passwordString.length() < 6){
                        Toast.makeText(getApplicationContext(), "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                    }else{
                        if(!passwordString.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")){
                            Toast.makeText(getApplicationContext(), "Password must contain an uppercase letter, lowercase letter and a number", Toast.LENGTH_SHORT).show();
                        }else{
                            if(!Patterns.EMAIL_ADDRESS.matcher(emailString).matches()){
                                Toast.makeText(getApplicationContext(), "Not a valid email address", Toast.LENGTH_SHORT).show();
                            }else{
                                if(!passwordString.equals(passwordConfirmString)){
                                    Toast.makeText(getApplicationContext(), "Passwords don't match", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(text.equals("Consumer")){
                                        accountType = "0";
                                    }else{
                                        accountType = "1";
                                    }

                                    try {
                                        signUpPresenter.addUserToDB(emailString,passwordString,accountType, firstNameString, lastNameString);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                }else{
                    Toast.makeText(getApplicationContext(), "One or more input needs filled out", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //checks if cancel button is pressed and returns to previous page if it has been
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity_landingpage.class));
                finish();
            }
        });
    }

    @Override
    public void onSuccessAdd() {

        //clears any saved preferences but still need email for future pages
        loginPrefsEditor.clear();
        loginPrefsEditor.putString("username", emailString);
        loginPrefsEditor.commit();

        if(text.equals("Consumer")){
            loginPrefsEditor.putInt("account_type", 0);
            loginPrefsEditor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity_consumermainmenu.class));
            finish();
        }else{
            loginPrefsEditor.putInt("account_type", 1);
            loginPrefsEditor.commit();
            startActivity(new Intent(getApplicationContext(), MainActivity_businessmainmenu.class));
            finish();
        }

    }

    @Override
    public void onFailedAdd() {
        Toast.makeText(getApplicationContext(), "Email has already been used", Toast.LENGTH_SHORT).show();
    }
}
