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

import com.example.churchapp.ChurchIntents.ChurchHome;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.MainActivity;
import com.example.churchapp.Models.Church;
import com.example.churchapp.R;

public class CreateChurch extends AppCompatActivity
{
    //GUI
    EditText et_email;
    EditText et_password;
    EditText et_confirmPassword;
    EditText et_name;
    EditText et_address;
    EditText et_city;
    EditText et_number;
    EditText et_statement;
    Spinner sp_denomination;
    Button btn_register;
    Button btn_back;
    TextView tv_error;

    //DATABASE
    UsersTableHelper usersDb;
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent mainActivityIntent;
    Intent churchHomeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_church);

        //GUI
        et_email = findViewById(R.id.et_createChurch_email);
        et_password = findViewById(R.id.et_createChurch_password);
        et_confirmPassword = findViewById(R.id.et_createChurch_confirmPassword);
        et_name = findViewById(R.id.et_createChurch_name);
        et_address = findViewById(R.id.et_createChurch_address);
        et_city = findViewById(R.id.et_createChurch_city);
        et_number = findViewById(R.id.et_createChurch_number);
        et_statement = findViewById(R.id.et_createChurch_statement);
        sp_denomination = findViewById(R.id.sp_createChurch_denomination);
        tv_error = findViewById(R.id.tv_createChurch_error);
        btn_register = findViewById(R.id.btn_createChurch_register);
        btn_back = findViewById(R.id.btn_createChurch_back);

        //SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.denominations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_denomination.setAdapter(adapter);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        mainActivityIntent = new Intent(CreateChurch.this, MainActivity.class);
        churchHomeIntent = new Intent(CreateChurch.this, ChurchHome.class);

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
                Log.v("BUTTON CLICK", "Create Church Button Clicked");

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String confirmPassword = et_confirmPassword.getText().toString();
                String name = et_name.getText().toString();
                String address = et_address.getText().toString();
                String city = et_city.getText().toString();
                String number = et_number.getText().toString();
                String statement = et_statement.getText().toString();
                String denomination = sp_denomination.getSelectedItem().toString();

                tv_error.setVisibility(View.INVISIBLE);

                //If one of the fields are empty
                if (email.equals("") || password.equals("") || name.equals("") || address.equals("") || city.equals("") || number.equals("") || statement.equals("") || confirmPassword.equals(""))
                {
                    tv_error.setText("*Please fill out all fields*");
                    tv_error.setVisibility(View.VISIBLE); //Tell them
                }
                else
                {
                    tv_error.setVisibility(View.INVISIBLE);

                    boolean isUnique = true;

                    if (usersDb.doesEmailExist(email)) //Check if email exists in users database
                    {
                        isUnique = false;
                    }
                    if (churchesDb.doesEmailExist(email)) //Check if email exists in churches database
                    {
                        isUnique = false;
                    }

                    //If it's still unique, move on to check if password fields match
                    if (isUnique)
                    {
                        tv_error.setVisibility(View.INVISIBLE);

                        if (password.equals(confirmPassword)) //If the password fields match
                        {
                            tv_error.setVisibility(View.INVISIBLE);

                            //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                            Church church = new Church(email, password, name, denomination, statement, address, city, number);
                            churchesDb.createChurch(church); //Create the church in database
                            Session.login(church); //Log the church in
                            Log.v("CREATED CHURCH", "Created Church Account - Moving to ChurchHome");
                            startActivity(churchHomeIntent);
                        }
                        else
                        {
                            tv_error.setText("*Passwords do not match*");
                            tv_error.setVisibility(View.VISIBLE); //Tell them
                        }
                    }
                    else //If it's not unique
                    {
                        tv_error.setText("*Email already in use*");
                        tv_error.setVisibility(View.VISIBLE); //Tell them
                    }
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

    /**========================================BACK BUTTON PRESS========================================*/
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