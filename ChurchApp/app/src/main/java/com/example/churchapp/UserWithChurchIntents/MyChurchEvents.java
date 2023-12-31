package com.example.churchapp.UserWithChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.churchapp.Adapters.MyEventsAdapter;
import com.example.churchapp.Adapters.SignedUpEventsAdapter;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class MyChurchEvents extends AppCompatActivity
{
    //GUI
    TextView tv_title;
    TextView tv_noResults;
    Button btn_back;
    ListView lv_events;

    //DATABASE
    EventsTableHelper eventsDb;
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent userWithChurchHomeIntent;
    Intent eventDetailsIntent;

    //ARRAYLIST
    ArrayList<Event> listOfEvents;

    //ADAPTER
    MyEventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_church_events);

        //GUI
        tv_title = findViewById(R.id.tv_myChurchEvents_title);
        tv_noResults = findViewById(R.id.tv_myChurchEvents_noResults);
        btn_back = findViewById(R.id.btn_myChurchEvents_back);
        lv_events = findViewById(R.id.lv_myChurchEvents_events);

        //DATABASE
        eventsDb = new EventsTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        userWithChurchHomeIntent = new Intent(MyChurchEvents.this, UserWithChurchHome.class);
        eventDetailsIntent = new Intent(MyChurchEvents.this, EventDetails.class);

        //ARRAYLIST
        listOfEvents = new ArrayList<Event>();
        if (eventsDb.doesChurchHaveEvents(Session.getUser().getEmailOfChurchAttending())) //If the user's church has events
        {
            //Get all the events from the user's church
            listOfEvents = eventsDb.getAllEventsByChurchEmail(Session.getUser().getEmailOfChurchAttending());
            fillListView();
        }
        else
        {
            tv_noResults.setVisibility(View.VISIBLE); //Show no results
        }

        //FUNCTIONS
        fillTitleText();
        listViewItemClick();
        backButtonClick();
    }

    /**========================================FILL TITLE TEXT========================================*/
    private void fillTitleText()
    {
        Church church = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
        tv_title.setText("All " + church.getName() + "'s events");
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new MyEventsAdapter(this, listOfEvents);
        lv_events.setAdapter(adapter);

        ifNoResultsShow(); //Check if there are any results every time the list view is updated
    }

    /**========================================LIST VIEW ITEM CLICK========================================*/
    private void listViewItemClick()
    {
        lv_events.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("LIST VIEW ITEM CLICK", "List View Item Clicked - Moving to EventDetails");
                eventDetailsIntent.putExtra("thisEvent", listOfEvents.get(i)); //Put extra the clicked event
                eventDetailsIntent.putExtra("cameFrom", "myChurchEventsIntent"); //Put extra the name of this intent
                startActivity(eventDetailsIntent);
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
                Log.v("BUTTON CLICK", "Back Button Clicked - Moving to UserWithChurchHome");
                startActivity(userWithChurchHomeIntent);
            }
        });
    }

    /**========================================SHOW NO RESULTS IF THERE AREN'T ANY RESULTS========================================*/
    private void ifNoResultsShow()
    {
        if (listOfEvents.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
    }
}