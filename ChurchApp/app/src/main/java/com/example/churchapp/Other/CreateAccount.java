package com.example.churchapp.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;
import com.example.churchapp.UserIntents.UserNoChurchHome;

public class CreateAccount extends AppCompatActivity
{
    //GUI
    EditText et_email;
    EditText et_password;
    EditText et_confirmPassword;
    EditText et_firstname;
    EditText et_lastname;
    EditText et_city;
    Spinner sp_denomination;
    Button btn_register;
    Button btn_back;
    TextView tv_error;
    TextView tv_emailError;

    //DATABASE
    UsersTableHelper usersDb;
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent mainActivityIntent;
    Intent userNoChurchHome;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        //GUI
        et_email = findViewById(R.id.et_createAccount_email);
        et_password = findViewById(R.id.et_createAccount_password);
        et_confirmPassword = findViewById(R.id.et_createAccount_confirmPassword);
        et_firstname = findViewById(R.id.et_createAccount_firstname);
        et_lastname = findViewById(R.id.et_createAccount_lastname);
        et_city = findViewById(R.id.et_createAccount_city);
        sp_denomination = findViewById(R.id.sp_createAccount_denomination);
        btn_register = findViewById(R.id.btn_createAccount_register);
        btn_back = findViewById(R.id.btn_createAccount_back);
        tv_error = findViewById(R.id.tv_createAccount_error);
        tv_emailError = findViewById(R.id.tv_createAccount_emailError);

        //SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.denominations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_denomination.setAdapter(adapter);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        mainActivityIntent = new Intent(CreateAccount.this, MainActivity.class);
        userNoChurchHome = new Intent(CreateAccount.this, UserNoChurchHome.class);

        //FUNCTIONS
        registerButtonClick();
        backButtonClick();
        denominationSelect();
    }

    /**========================================REGISTER BUTTON CLICK========================================*/
    private void registerButtonClick()
    {
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Create Account Button Clicked");

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String confirmPassword = et_confirmPassword.getText().toString();
                String firstname = et_firstname.getText().toString();
                String lastname = et_lastname.getText().toString();
                String city = et_city.getText().toString();
                String denomination = sp_denomination.getSelectedItem().toString();

                tv_emailError.setVisibility(View.INVISIBLE);

                boolean isUnique = true;
                if (usersDb.doesEmailExist(email)) //Check if email exists in users database
                {
                    isUnique = false;
                }
                if (churchesDb.doesEmailExist(email)) //Check if email exists in churches database
                {
                    isUnique = false;
                }

                //If the email is still unique, move on
                if (isUnique)
                {
                    tv_emailError.setVisibility(View.INVISIBLE);

                    //One of the fields are empty
                    if (email.equals("") || password.equals("") || firstname.equals("") || lastname.equals("") || city.equals("") || confirmPassword.equals(""))
                    {
                        tv_error.setText("*Please fill out all fields*");
                        tv_error.setVisibility(View.VISIBLE); //Tell them
                    }
                    else //Fields are filled out
                    {
                        tv_error.setVisibility(View.INVISIBLE);

                        if (!password.equals(confirmPassword)) //Check if password fields match
                        {
                            tv_error.setText("*Passwords do not match*");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            tv_error.setVisibility(View.INVISIBLE);

                            //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                            User user = new User(email, password, firstname, lastname, "", denomination, city);
                            usersDb.createUser(user); //Create the user in the database
                            Session.login(user); //Log the User in
                            startActivity(userNoChurchHome);
                            Log.v("CREATED USER", "Created User Account - Moving to UserNoChurchHome");
                        }
                    }
                }
                else //Email taken
                {
                    tv_emailError.setVisibility(View.VISIBLE); //Tell them
                    tv_error.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    /**========================================SPINNER ITEM SELECTED (DENOMINATION)========================================*/
    private void denominationSelect()
    {
        sp_denomination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id)
            {
                String text = parent.getItemAtPosition(i).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**========================================BACK BUTTON CLICK========================================*/
    private void backButtonClick()
    {
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Back Button Clicked - Moving to MainActivity");
                startActivity(mainActivityIntent);
            }
        });
    }
}