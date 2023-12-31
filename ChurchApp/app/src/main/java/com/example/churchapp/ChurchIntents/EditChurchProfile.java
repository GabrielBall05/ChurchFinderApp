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

import com.example.churchapp.Other.MasterConfirmation;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Other.MainActivity;
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
    Button btn_signOut;

    //DATABASE
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent churchHomeIntent;
    Intent createEventIntent;
    Intent masterConfirmationIntent;
    Intent mainActivityIntent;

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
        btn_signOut = findViewById(R.id.btn_editChurchProfile_signOut);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        churchHomeIntent = new Intent(EditChurchProfile.this, ChurchHome.class);
        createEventIntent = new Intent(EditChurchProfile.this, CreateEvent.class);
        masterConfirmationIntent = new Intent(EditChurchProfile.this, MasterConfirmation.class);
        mainActivityIntent = new Intent(EditChurchProfile.this, MainActivity.class);

        //FILL TEXT BOXES WITH CURRENT INFO
        fillInTextBoxes();

        //SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.denominations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_denomination.setAdapter(adapter);
        //Set spinner selection equal to the church's denomination
        sp_denomination.setSelection(adapter.getPosition(Session.getChurch().getDenomination()));

        //FUNCTIONS
        updateButtonClick();
        deleteButtonClick();
        createEventButtonClick();
        churchHomeButtonClick();
        denominationSelect();
        signOutButtonClick();
    }

    /**========================================UPDATE BUTTON CLICK========================================*/
    private void updateButtonClick()
    {
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Update (Church) Account Button Clicked");

                String name = et_name.getText().toString();
                String number = et_number.getText().toString();
                String address = et_address.getText().toString();
                String city = et_city.getText().toString();
                String statement = et_statement.getText().toString();
                String password = et_password.getText().toString();
                String denomination = sp_denomination.getSelectedItem().toString();

                //A field is empty
                if(name.equals("") || number.equals("") || address.equals("") || city.equals("") || statement.equals("") || password.equals(""))
                {
                    tv_fieldsError.setVisibility(View.VISIBLE); //Tell them
                }
                else
                {
                    tv_fieldsError.setVisibility(View.INVISIBLE);
                    //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                    Church church = new Church(Session.getChurch().getEmail(), password, name, denomination, statement, address, city, number);
                    churchesDb.updateChurch(church); //Update the church in the database
                    Session.login(church); //Log the church in again to update the church's information in Session
                    startActivity(churchHomeIntent);
                    Log.v("UPDATED ACCOUNT", "Updated Profile - Moving to ChurchHome");
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

    /**========================================DELETE BUTTON CLICK========================================*/
    private void deleteButtonClick()
    {
        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Delete Church Account Button Clicked - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "editChurchProfileIntent"); //Put extra the name of this intent
                startActivity(masterConfirmationIntent);
            }
        });
    }

    /**========================================CREATE EVENT BUTTON CLICK========================================*/
    private void createEventButtonClick()
    {
        btn_createEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Create Event Button Clicked - Moving to CreateEvent");
                startActivity(createEventIntent);
            }
        });
    }

    /**========================================CHURCH HOME BUTTON CLICK========================================*/
    private void churchHomeButtonClick()
    {
        btn_churchHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Church Home Button Clicked - Moving to ChurchHome");
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

    /**========================================SIGN OUT BUTTON CLICK========================================*/
    private void signOutButtonClick()
    {
        btn_signOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Signing Out - Moving to MainActivity");
                startActivity(mainActivityIntent);
            }
        });
    }
}