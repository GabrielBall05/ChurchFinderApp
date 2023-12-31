package com.example.churchapp.ChurchIntents;

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
import com.example.churchapp.Other.MasterConfirmation;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ChurchHome extends AppCompatActivity
{
    //GUI
    ListView lv_myEvents;
    Button btn_viewMembers;
    Button btn_createEvent;
    Button btn_editProfile;
    TextView tv_noResults;

    //DATABASE
    UsersTableHelper usersDb;
    EventsTableHelper eventsDb;

    //INTENTS
    Intent editEventIntent;
    Intent viewMembersIntent;
    Intent createEventIntent;
    Intent editProfileIntent;
    Intent masterConfirmationIntent;

    //ADAPTER
    MyEventsAdapter adapter;

    //ARRAYLIST
    ArrayList<Event> listOfMyEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_home);

        //GUI
        lv_myEvents = findViewById(R.id.lv_churchHome_myEvents);
        btn_createEvent = findViewById(R.id.btn_churchHome_createEvent);
        btn_editProfile = findViewById(R.id.btn_churchHome_editProfile);
        btn_viewMembers = findViewById(R.id.btn_churchHome_viewMembers);
        tv_noResults = findViewById(R.id.tv_churchHome_noResults);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        eventsDb = new EventsTableHelper(this);

        //INTENTS
        editEventIntent = new Intent(ChurchHome.this, EditEvent.class);
        viewMembersIntent = new Intent(ChurchHome.this, ViewMembers.class);
        createEventIntent = new Intent(ChurchHome.this, CreateEvent.class);
        editProfileIntent = new Intent(ChurchHome.this, EditChurchProfile.class);
        masterConfirmationIntent = new Intent(ChurchHome.this, MasterConfirmation.class);

        //ARRAYLIST
        listOfMyEvents = new ArrayList<Event>();
        if (eventsDb.doesChurchHaveEvents(Session.getChurch().getEmail()))
        {
            //Get all the church's events
            listOfMyEvents = eventsDb.getAllEventsByChurchEmail(Session.getChurch().getEmail());
            fillListView(); //Update list
        }
        else
        {
            ifNoResultsShow();
        }

        //FUNCTIONS
        viewMembersButtonClick();
        createEventButtonClick();
        editProfileButtonClick();
        listViewItemClick();
        listViewItemLongClick();
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new MyEventsAdapter(this, listOfMyEvents);
        lv_myEvents.setAdapter(adapter);

        ifNoResultsShow();
    }

    /**========================================LIST VIEW ITEM CLICK========================================*/
    private void listViewItemClick()
    {
        lv_myEvents.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("LIST VIEW ITEM CLICK", "List View Item Click - Moving to EditEvent");
                editEventIntent.putExtra("myEvent", listOfMyEvents.get(i)); //Put extra the clicked event
                startActivity(editEventIntent);
            }
        });
    }

    /**========================================LIST VIEW ITEM LONG CLICK========================================*/
    private void listViewItemLongClick()
    {
        lv_myEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("LIST VIEW ITEM LONG CLICK", "List View Item Long Click - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "churchHomeIntent"); //Put extra the name of this intent
                masterConfirmationIntent.putExtra("eventToDelete", listOfMyEvents.get(i)); //Put extra the event
                startActivity(masterConfirmationIntent);
                return false;
            }
        });
    }

    /**========================================VIEW MEMBERS BUTTON CLICK========================================*/
    private void viewMembersButtonClick()
    {
        btn_viewMembers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "View Members Button Clicked - Moving to ViewMembers");
                startActivity(viewMembersIntent);
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

    /**========================================EDIT PROFILE BUTTON CLICK========================================*/
    private void editProfileButtonClick()
    {
        btn_editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Edit Church Profile Button Clicked - Moving to EditChurchProfile");
                startActivity(editProfileIntent);
            }
        });
    }

    private void ifNoResultsShow()
    {
        if (listOfMyEvents.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
    }
}