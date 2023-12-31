package com.example.churchapp.UserWithChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Models.Bookmark;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.EventParticipant;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class EventDetails extends AppCompatActivity
{
    //GUI
    TextView tv_churchName;
    TextView tv_eventName;
    TextView tv_address;
    TextView tv_date;
    TextView tv_time;
    TextView tv_desc;
    Button btn_sign;
    Button btn_back;
    Button btn_home;

    //DATABASE
    EventParticipantsTableHelper participantsDb;

    //INTENTS
    Intent userWithChurchHomeIntent;
    Intent myChurchEventsIntent;

    //EXTRA
    String cameFrom;
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //GUI
        tv_churchName = findViewById(R.id.tv_eventDetails_churchName);
        tv_eventName = findViewById(R.id.tv_eventDetails_eventName);
        tv_address = findViewById(R.id.tv_eventDetails_address);
        tv_date = findViewById(R.id.tv_eventDetails_date);
        tv_time = findViewById(R.id.tv_eventDetails_time);
        tv_desc = findViewById(R.id.tv_eventDetails_desc);
        btn_sign = findViewById(R.id.btn_eventDetails_sign);
        btn_back = findViewById(R.id.btn_eventDetails_back);
        btn_home = findViewById(R.id.btn_eventDetails_home);

        //DATABASE
        participantsDb = new  EventParticipantsTableHelper(this);

        //INTENTS
        userWithChurchHomeIntent = new Intent(EventDetails.this, UserWithChurchHome.class);
        myChurchEventsIntent = new Intent(EventDetails.this, MyChurchEvents.class);

        //EXTRA
        Intent origin = getIntent();
        cameFrom = origin.getStringExtra("cameFrom"); //Get the name of the previous intent
        event = (Event) origin.getSerializableExtra("thisEvent"); //Get the event passed to me

        //FUNCTIONS
        fillTextBoxes();
        signButtonClick();
        backButtonClick();
        homeButtonClick();
    }

    /**========================================FILL TEXT BOXES========================================*/
    private void fillTextBoxes()
    {
        tv_churchName.setText("Church Name: " + event.getChurchName());
        tv_eventName.setText("Event Name: " + event.getEventName());
        tv_address.setText("Address: " + event.getAddress());
        tv_date.setText("Date: " + event.getDate());
        tv_time.setText("Time: " + event.getTime());
        tv_desc.setText("Description: " + event.getDescription());

        //Correct the bookmark button text
        //Checks if the user is signed up for the event passed to me
        if(participantsDb.isUserSignedUpForEvent(event.getEventId(), Session.getUser().getEmail()))
        {
            btn_sign.setText("Leave\nEvent");
        }
        else
        {
            btn_sign.setText("Sign\nUp");
        }
    }

    /**========================================SIGN BUTTON CLICK========================================*/
    private void signButtonClick()
    {
        btn_sign.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(participantsDb.isUserSignedUpForEvent(event.getEventId(), Session.getUser().getEmail()))
                {
                    Log.v("BUTTON CLICK", "LEAVING EVENT");
                    EventParticipant p = new EventParticipant(event.getEventId(), Session.getUser().getEmail());
                    participantsDb.deleteEventParticipant(p); //Remove user from event
                }
                else
                {
                    Log.v("BUTTON CLICK", "SIGNING UP FOR EVENT");
                    EventParticipant p = new EventParticipant(event.getEventId(), Session.getUser().getEmail());
                    participantsDb.createEventParticipant(p); //Add user as participant to this event
                }

                //Correct the bookmark button text
                if(participantsDb.isUserSignedUpForEvent(event.getEventId(), Session.getUser().getEmail()))
                {
                    btn_sign.setText("Leave\nEvent");
                }
                else
                {
                    btn_sign.setText("Sign\nUp");
                }
            }
        });
    }

    /**========================================HOME BUTTON CLICK========================================*/
    private void homeButtonClick()
    {
        btn_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Home Button Clicked - Moving to UserWithChurchHome");
                startActivity(userWithChurchHomeIntent);
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
                if (cameFrom.equals("myChurchEventsIntent")) //If came from MyChurchEvents, go back there
                {
                    Log.v("BUTTON CLICK", "Back Button Clicked - Moving to MyChurchEvents");
                    startActivity(myChurchEventsIntent);
                }
                else if (cameFrom.equals("userWithChurchHomeIntent")) //If came from UserWithChurchHome, go back there
                {
                    Log.v("BUTTON CLICK", "Back Button Clicked - Moving to UserWithChurchHome");
                    startActivity(userWithChurchHomeIntent);
                }

            }
        });
    }
}