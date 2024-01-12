package com.example.churchapp.ChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.EventParticipant;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class EditEvent extends AppCompatActivity
{
    //GUI
    EditText et_name;
    EditText et_address;
    EditText et_date;
    EditText et_time;
    EditText et_desc;
    TextView tv_fieldsError;
    Button btn_update;
    Button btn_back;
    Button btn_viewParticipants;

    //INTENTS
    Intent churchHomeIntent;
    Intent participantsIntent;

    //DATABASE
    EventsTableHelper eventsDb;

    //EXTRA
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        //GUI
        et_name = findViewById(R.id.et_editEvent_name);
        et_address = findViewById(R.id.et_editEvent_address);
        et_date = findViewById(R.id.et_editEvent_date);
        et_time = findViewById(R.id.et_editEvent_time);
        et_desc = findViewById(R.id.et_editEvent_desc);
        tv_fieldsError = findViewById(R.id.tv_editEvent_fieldsError);
        btn_update = findViewById(R.id.btn_editEvent_update);
        btn_viewParticipants = findViewById(R.id.btn_editEvent_viewParticipants);
        btn_back = findViewById(R.id.btn_editEvent_back);

        //INTENTS
        churchHomeIntent = new Intent(EditEvent.this, ChurchHome.class);
        participantsIntent = new Intent(EditEvent.this, EventParticipantsPage.class);

        //DATABASE
        eventsDb = new EventsTableHelper(this);

        //GET EXTRA STUFF
        Intent cameFrom = getIntent();
        event = (Event) cameFrom.getSerializableExtra("myEvent"); //Get the event passed

        //FUNCTIONS
        fillTextBoxes();
        updateButtonClick();
        backButtonClick();
        viewParticipantsButtonClick();
    }

    /**========================================FILL TEXT BOXES========================================*/
    private void fillTextBoxes()
    {
        et_name.setText(event.getEventName());
        et_address.setText(event.getAddress());
        et_date.setText(event.getDate());
        et_time.setText(event.getTime());
        et_desc.setText(event.getDescription());
    }

    /**========================================UPDATE BUTTON CLICK========================================*/
    private void updateButtonClick()
    {
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Update Button Clicked");

                String name = et_name.getText().toString();
                String address = et_address.getText().toString();
                String date = et_date.getText().toString();
                String time = et_time.getText().toString();
                String desc = et_desc.getText().toString();

                //A field is empty
                if (name.equals("") || address.equals("") || date.equals("") || time.equals("") || desc.equals(""))
                {
                    tv_fieldsError.setVisibility(View.VISIBLE); //Tell them
                }
                else
                {
                    tv_fieldsError.setVisibility(View.INVISIBLE);
                    //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
                    eventsDb.updateEvent(new Event(event.getEventId(), Session.getChurch().getEmail(), Session.getChurch().getName(), name, address, date, time, desc));
                    Toast.makeText(EditEvent.this, "Event Updated", Toast.LENGTH_SHORT).show();
                    Log.v("UPDATED EVENT", "Updated Event - Moving to ChurchHome");
                    startActivity(churchHomeIntent);
                }
            }
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
                Log.v("BUTTON CLICK", "Back Button Clicked - Moving to ChurchHome");
                startActivity(churchHomeIntent);
            }
        });
    }

    /**========================================VIEW PARTICIPANTS BUTTON CLICK========================================*/
    private void viewParticipantsButtonClick()
    {
        btn_viewParticipants.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "View Participants Button Clicked - Moving to EventParticipantsPage");
                participantsIntent.putExtra("myEvent", event); //Put extra the event
                startActivity(participantsIntent);
            }
        });
    }
}