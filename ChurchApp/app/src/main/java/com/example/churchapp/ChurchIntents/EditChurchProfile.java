package com.example.churchapp.ChurchIntents;

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

import com.example.churchapp.Confirmations.DeleteConfirmation;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class EditChurchProfile extends AppCompatActivity
{
    //GUI
    EditText et_name;
    EditText et_number;
    EditText et_address;
    EditText et_city;
    EditText et_statement;
    EditText et_password;
    Spinner sp_denomination;
    TextView tv_fieldsError;
    Button btn_update;
    Button btn_delete;
    Button btn_createEvent;
    Button btn_churchHome;
    //DATABASE
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent churchHomeIntent;
    Intent createEventIntent;
    Intent deleteConfirmationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_church_profile);

        //GUI
        et_name = findViewById(R.id.et_editChurchProfile_name);
        et_number = findViewById(R.id.et_editChurchProfile_number);
        et_address = findViewById(R.id.et_editChurchProfile_address);
        et_city = findViewById(R.id.et_editChurchProfile_city);
        et_statement = findViewById(R.id.et_editChurchProfile_statement);
        et_password = findViewById(R.id.et_editChurchProfile_password);
        sp_denomination = findViewById(R.id.sp_editChurchProfile_denomination);
        tv_fieldsError = findViewById(R.id.tv_editChurchProfile_fieldsError);
        btn_update = findViewById(R.id.btn_editChurchProfile_update);
        btn_delete = findViewById(R.id.btn_editChurchProfile_delete);
        btn_createEvent = findViewById(R.id.btn_editChurchProfile_createEvent);
        btn_churchHome = findViewById(R.id.btn_editChurchProfile_churchHome);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        churchHomeIntent = new Intent(EditChurchProfile.this, ChurchHome.class);
        createEventIntent = new Intent(EditChurchProfile.this, CreateEvent.class);
        deleteConfirmationIntent = new Intent(EditChurchProfile.this, DeleteConfirmation.class);

        //Fill in text boxes with current info
        fillInTextBoxes();

        //SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.denominations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_denomination.setAdapter(adapter);
        //Set spinner selection equal to the church's info
        sp_denomination.setSelection(adapter.getPosition(Session.getChurch().getDenomination()));

        //FUNCTIONS
        updateButtonClick();
        deleteButtonClick();
        createEventButtonClick();
        churchHomeButtonClick();
        denominationSelect();
    }

    /**========================================UPDATE BUTTON PRESS========================================*/
    private void updateButtonClick()
    {
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Update Account Button Click");

                String name = et_name.getText().toString();
                String number = et_number.getText().toString();
                String address = et_address.getText().toString();
                String city = et_city.getText().toString();
                String statement = et_statement.getText().toString();
                String password = et_password.getText().toString();
                String denomination = sp_denomination.getSelectedItem().toString();

                if(name.equals("") || number.equals("") || address.equals("") || city.equals("") || statement.equals("") || password.equals(""))
                {
                    tv_fieldsError.setVisibility(View.VISIBLE);
                }
                else
                {
                    tv_fieldsError.setVisibility(View.INVISIBLE);
                    //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                    Church church = new Church(Session.getChurch().getEmail(), password, name, denomination, statement, address, city, number);
                    churchesDb.updateChurch(church);
                    Session.login(church);
                    startActivity(churchHomeIntent);
                    Log.v("Button Press", "Update Account Button Click - Moving to ChurchHome");
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

    /**========================================DELETE BUTTON PRESS========================================*/
    private void deleteButtonClick()
    {
        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Delete Account Button Click - Moving to DeleteConfirmation");

                /**==============================.PUTEXTRA() THE INFORMATION NEEDED FOR DELETING IN THE DELETE CONFIRMATION INTENT==============================*/
                /**==============================.PUTEXTRA() THE INFORMATION NEEDED FOR DELETING IN THE DELETE CONFIRMATION INTENT==============================*/
                /**==============================.PUTEXTRA() THE INFORMATION NEEDED FOR DELETING IN THE DELETE CONFIRMATION INTENT==============================*/

                startActivity(deleteConfirmationIntent);
            }
        });
    }

    /**========================================CREATE EVENT BUTTON PRESS========================================*/
    private void createEventButtonClick()
    {
        btn_createEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Create Event Button Click - Moving to CreateEvent");
                startActivity(createEventIntent);
            }
        });
    }

    /**========================================CHURCH HOME BUTTON PRESS========================================*/
    private void churchHomeButtonClick()
    {
        btn_churchHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Church Home Button Click - Moving to ChurchHome");
                startActivity(churchHomeIntent);
            }
        });
    }

    /**========================================FILL IN TEXT BOXES========================================*/
    private void fillInTextBoxes()
    {
        et_name.setText(Session.getChurch().getName());
        et_number.setText(Session.getChurch().getNumber());
        et_address.setText(Session.getChurch().getStreetAddress());
        et_city.setText(Session.getChurch().getCity());
        et_statement.setText(Session.getChurch().getStatementOfFaith());
        et_password.setText(Session.getChurch().getPassword());
    }
}