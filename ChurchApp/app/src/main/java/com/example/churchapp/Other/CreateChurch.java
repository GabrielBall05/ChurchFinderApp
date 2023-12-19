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
import com.example.churchapp.MainActivity;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;
import com.example.churchapp.UserNoChurchIntents.UserNoChurchHome;

public class CreateChurch extends AppCompatActivity
{
    //GUI
    EditText et_email;
    EditText et_password;
    EditText et_name;
    EditText et_address;
    EditText et_city;
    EditText et_number;
    EditText et_statement;
    Spinner sp_denomination;
    Button btn_register;
    Button btn_back;
    TextView tv_fieldsError;
    TextView tv_emailError;

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
        et_name = findViewById(R.id.et_createChurch_name);
        et_address = findViewById(R.id.et_createChurch_address);
        et_city = findViewById(R.id.et_createChurch_city);
        et_number = findViewById(R.id.et_createChurch_number);
        et_statement = findViewById(R.id.et_createChurch_statement);
        sp_denomination = findViewById(R.id.sp_createChurch_denomination);
        tv_fieldsError = findViewById(R.id.tv_createChurch_fieldsError);
        tv_emailError = findViewById(R.id.tv_createChurch_emailError);
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
        churchHomeIntent = new Intent(CreateChurch.this, UserNoChurchHome.class);

        //FUNCTIONS
        registerButtonClick();
        backButtonClick();
        denominationSelect();
    }

    /**========================================REGISTER BUTTON PRESS========================================*/
    private void registerButtonClick()
    {
        btn_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON PRESS", "Create Church Button Press");

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                String name = et_name.getText().toString();
                String address = et_address.getText().toString();
                String city = et_city.getText().toString();
                String number = et_number.getText().toString();
                String statement = et_statement.getText().toString();
                String denomination = sp_denomination.getSelectedItem().toString();

                tv_emailError.setVisibility(View.INVISIBLE);

                //One of the fields are empty
                if (email.equals("") || password.equals("") || name.equals("") || address.equals("") || city.equals("") || number.equals("") || statement.equals(""))
                {
                    tv_fieldsError.setVisibility(View.VISIBLE); //Tell them
                }
                else
                {
                    tv_fieldsError.setVisibility(View.INVISIBLE);

                    boolean isUnique = true;

                    if (usersDb.doesEmailExist(email)) //Check if email exists in users database
                    {
                        isUnique = false;
                    }
                    if (churchesDb.doesEmailExist(email)) //Check if email exists in churches database
                    {
                        isUnique = false;
                    }

                    //If it's still unique, create the account
                    if (isUnique)
                    {
                        tv_emailError.setVisibility(View.INVISIBLE);

                        //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                        Church church = new Church(email, password, name, denomination, statement, address, city, number);
                        churchesDb.createChurch(church);
                        startActivity(churchHomeIntent);
                        Log.v("BUTTON PRESS", "Create Church Button Press - Moving to ChurchHome");
                    }
                    else //If it's not unique
                    {
                        tv_emailError.setVisibility(View.VISIBLE); //Tell them
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
                //Log.v("SPINNER ITEM SELECTED", "Selected spinner item");
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
                Log.v("BUTTON PRESS", "Back Button Press (From CreateChurch)");
            }
        });
    }
}