package com.example.churchapp.ChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class CreateEvent extends AppCompatActivity
{
    //GUI
    EditText et_name;
    EditText et_address;
    EditText et_date;
    EditText et_time;
    EditText et_desc;
    TextView tv_fieldsError;
    Button btn_create;
    ImageView btn_churchHome;
    ImageView btn_editProfile;

    //DATABASE
    EventsTableHelper eventsDb;

    //INTENTS
    Intent churchHomeIntent;
    Intent editProfileIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        //GUI
        et_name = findViewById(R.id.et_createEvent_name);
        et_address = findViewById(R.id.et_createEvent_address);
        et_date = findViewById(R.id.et_createEvent_date);
        et_time = findViewById(R.id.et_createEvent_time);
        et_desc = findViewById(R.id.et_createEvent_desc);
        tv_fieldsError = findViewById(R.id.tv_createEvent_fieldsError);
        btn_create = findViewById(R.id.btn_createEvent_create);
        btn_churchHome = findViewById(R.id.btn_createEvent_home);
        btn_editProfile = findViewById(R.id.btn_createEvent_editProfile);

        //DATABASE
        eventsDb = new EventsTableHelper(this);

        //INTENTS
        churchHomeIntent = new Intent(CreateEvent.this, ChurchHome.class);
        editProfileIntent = new Intent(CreateEvent.this, EditChurchProfile.class);

        //FUNCTION
        createButtonClick();
        homeButtonClick();
        editProfileButtonClick();
    }

    /**========================================CREATE EVENT BUTTON CLICK========================================*/
    private void createButtonClick()
    {
        btn_create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Create Event Button Clicked");

                String eventName = et_name.getText().toString();
                String address = et_address.getText().toString();
                String date = et_date.getText().toString();
                String time = et_time.getText().toString();
                String description = et_desc.getText().toString();

                //A field is empty
                if (eventName.equals("") || address.equals("") || date.equals("") || time.equals("") || description.equals(""))
                {
                    tv_fieldsError.setVisibility(View.VISIBLE); //Tell them
                }
                else
                {
                    tv_fieldsError.setVisibility(View.INVISIBLE);

                    //Order: churchHostingEmail, churchName, eventName, address, date, time, description
                    Event e = new Event(Session.getChurch().getEmail(), Session.getChurch().getName(), eventName, address, date, time, description);
                    eventsDb.createEvent(e); //Create the event in the database
                    Toast.makeText(CreateEvent.this, "Event Created", Toast.LENGTH_SHORT).show();
                    Log.v("CREATED EVENT", "Event Created - Moving to ChurchHome");
                    startActivity(churchHomeIntent);
                }
            }
        });
    }

    /**========================================CHURCH HOME BUTTON CLICK========================================*/
    private void homeButtonClick()
    {
        btn_churchHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Home Button Clicked - Moving to ChurchHome");
                startActivity(churchHomeIntent);
            }
        });
    }

    /**========================================EDIT PROFILE BUTTON CLICK========================================*/
    private void editProfileButtonClick()
    {
        btn_editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Edit Profile Button Clicked - Moving to EditChurchProfile");
                startActivity(editProfileIntent);
            }
        });
    }
}