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
        cameFrom = origin.getStringExtra("cameFrom");
        event = (Event) origin.getSerializableExtra("thisEvent");

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
                    Log.v("Button Press", "LEAVING EVENT");
                    EventParticipant p = new EventParticipant(event.getEventId(), Session.getUser().getEmail());
                    participantsDb.deleteEventParticipant(p);
                }
                else
                {
                    Log.v("Button Press", "SIGNING UP FOR EVENT");
                    EventParticipant p = new EventParticipant(event.getEventId(), Session.getUser().getEmail());
                    participantsDb.createEventParticipant(p);
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
                Log.v("Button Press", "Home Button Press - Moving back to UserWithChurchHome");
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
                if (cameFrom.equals("myChurchEventsIntent"))
                {
                    Log.v("Button Press", "Back Button Press - Moving back to MyChurchEvents");
                    startActivity(myChurchEventsIntent);
                }
                else if (cameFrom.equals("userWithChurchHomeIntent"))
                {
                    Log.v("Button Press", "Back Button Press - Moving back to UserWithChurchHome");
                    startActivity(userWithChurchHomeIntent);
                }

            }
        });
    }
}