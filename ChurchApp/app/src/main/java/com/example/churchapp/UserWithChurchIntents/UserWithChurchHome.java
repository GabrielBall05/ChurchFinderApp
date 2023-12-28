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

import com.example.churchapp.Adapters.ListOfChurchesAdapter;
import com.example.churchapp.Adapters.SignedUpEventsAdapter;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.EventParticipant;
import com.example.churchapp.Models.User;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class UserWithChurchHome extends AppCompatActivity
{
    //GUI
    ListView lv_events;
    TextView tv_noResults;
    Button btn_myChurchEvents;
    Button btn_myChurch;
    Button btn_editProfile;

    //DATABASE
    EventsTableHelper eventsDb;
    EventParticipantsTableHelper participantsDb;
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent myChurchIntent;
    Intent editUserProfile2Intent;
    Intent myChurchEventsIntent;
    Intent eventDetailsIntent;

    //ARRAYLIST
    ArrayList<Event> listOfEvents;
    ArrayList<Integer> listOfEventIds;

    //ADAPTER
    SignedUpEventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_with_church_home);

        //GUI
        lv_events = findViewById(R.id.lv_userHome_events);
        tv_noResults = findViewById(R.id.tv_userHome_noResults);
        btn_myChurchEvents = findViewById(R.id.btn_userHome_allEvents);
        btn_myChurch = findViewById(R.id.btn_userHome_myChurch);
        btn_editProfile = findViewById(R.id.btn_userHome_editProfile);

        //DATABASE
        eventsDb = new EventsTableHelper(this);
        participantsDb = new EventParticipantsTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        myChurchIntent = new Intent(UserWithChurchHome.this, MyChurch.class);
        editUserProfile2Intent = new Intent(UserWithChurchHome.this, EditUserProfile2.class);
        myChurchEventsIntent = new Intent(UserWithChurchHome.this, MyChurchEvents.class);
        eventDetailsIntent = new Intent(UserWithChurchHome.this, EventDetails.class);

        //ARRAYLIST
        listOfEventIds = new ArrayList<Integer>();
        listOfEventIds = participantsDb.getEventIdsThatUserSignedUpFor(Session.getUser().getEmail());
        listOfEvents = new ArrayList<Event>();
        for (int i = 0; i < listOfEventIds.size(); i++)
        {
            Event event = eventsDb.getEventById(listOfEventIds.get(i));
            listOfEvents.add(event);
        }

        //FUNCTIONS
        fillButtonText();
        fillListView();
        listViewClick();
        myChurchEventsClick();
        myChurchClick();
        editProfileClick();
    }

    /**========================================FILL BUTTON TEXT========================================*/
    private void fillButtonText()
    {
        Church church = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
        btn_myChurchEvents.setText("See all " + church.getName() + "'s Events");
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new SignedUpEventsAdapter(this, listOfEvents);
        lv_events.setAdapter(adapter);

        ifNoResultsShow();
    }

    /**========================================LIST VIEW ITEM CLICK========================================*/
    private void listViewClick()
    {
        lv_events.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("List View Item Select", "List View item selected - Moving to EventDetails");
                eventDetailsIntent.putExtra("thisEvent", listOfEvents.get(i));
                eventDetailsIntent.putExtra("cameFrom", "userWithChurchHomeIntent");
                startActivity(eventDetailsIntent);
            }
        });
    }

    /**========================================MY CHURCH EVENTS CLICK========================================*/
    private void myChurchEventsClick()
    {
        btn_myChurchEvents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "My Church Events Button Press - Moving to MyChurchEvents");
                startActivity(myChurchEventsIntent);
            }
        });
    }

    /**========================================MY CHURCH CLICK========================================*/
    public void myChurchClick()
    {
        btn_myChurch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "My Church Button Press - Moving to MyChurch");
                startActivity(myChurchIntent);
            }
        });
    }

    /**========================================EDIT PROFILE CLICK========================================*/
    private void editProfileClick()
    {
        btn_editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Edit Profile (User) Button Press - Moving to EditUserProfile2");
                startActivity(editUserProfile2Intent);
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