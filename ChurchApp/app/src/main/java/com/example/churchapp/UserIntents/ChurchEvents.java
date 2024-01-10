package com.example.churchapp.UserIntents;

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
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ChurchEvents extends AppCompatActivity
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
    Intent userHomeIntent;
    Intent eventDetailsIntent;
    Intent churchDetailsIntent;

    //ARRAYLIST
    ArrayList<Event> listOfEvents;

    //ADAPTER
    MyEventsAdapter adapter;

    //EXTRA
    String cameFrom;
    Church church;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_events);

        //GUI
        tv_title = findViewById(R.id.tv_churchEvents_title);
        tv_noResults = findViewById(R.id.tv_churchEvents_noResults);
        btn_back = findViewById(R.id.btn_churchEvents_back);
        lv_events = findViewById(R.id.lv_churchEvents_events);

        //DATABASE
        eventsDb = new EventsTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        userHomeIntent = new Intent(ChurchEvents.this, UserHome.class);
        eventDetailsIntent = new Intent(ChurchEvents.this, EventDetails.class);
        churchDetailsIntent = new Intent(ChurchEvents.this, ChurchDetails.class);

        //EXTRA
        Intent origin = getIntent();
        cameFrom = origin.getStringExtra("cameFrom");
        church = (Church) origin.getSerializableExtra("thisChurch");

        //ARRAYLIST
        listOfEvents = new ArrayList<Event>();
        church = (Church) origin.getSerializableExtra("thisChurch");
        if (eventsDb.doesChurchHaveEvents(church.getEmail()))
        {
            tv_noResults.setVisibility(View.INVISIBLE);
            listOfEvents = eventsDb.getAllEventsByChurchEmail(church.getEmail());
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
        tv_title.setText("All " + churchesDb.getChurchByEmail(church.getEmail()).getName() + "'s events");
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
                eventDetailsIntent.putExtra("cameFrom", "churchEventsIntent"); //Put extra the name of this intent
                eventDetailsIntent.putExtra("thisChurch", church);
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
                Log.v("BUTTON CLICK", "Back Button Clicked");

                if (Session.getOriginPage().equals("userHomeIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Clicked - Moving to UserHome");
                    startActivity(userHomeIntent);
                }
                else
                {
                    Log.v("BUTTON CLICK", "Back Button Clicked - Moving to ChurchDetails");
                    churchDetailsIntent.putExtra("cameFrom", "churchEventsIntent");
                    churchDetailsIntent.putExtra("thisChurch", church);
                    startActivity(churchDetailsIntent);
                }
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