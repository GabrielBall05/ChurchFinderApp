package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.churchapp.Adapters.SignedUpEventsAdapter;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class UserHome extends AppCompatActivity
{
    //GUI
    ListView lv_events;
    TextView tv_noResults;
    Button btn_myChurchEvents;
    ImageView btn_bookmarks;
    ImageView btn_churchFinder;
    ImageView btn_myChurch;
    ImageView btn_editProfile;

    //DATABASE
    EventsTableHelper eventsDb;
    EventParticipantsTableHelper participantsDb;
    ChurchesTableHelper churchesDb;
    UsersTableHelper usersDb;

    //INTENTS
    Intent myChurchIntent;
    Intent editUserProfileIntent;
    Intent churchEventsIntent;
    Intent eventDetailsIntent;
    Intent myBookmarksIntent;
    Intent churchFinderIntent;

    //ARRAYLISTS
    ArrayList<Event> listOfEvents;
    ArrayList<Integer> listOfEventIds;

    //ADAPTER
    SignedUpEventsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        //GUI
        lv_events = findViewById(R.id.lv_userHome_events);
        tv_noResults = findViewById(R.id.tv_userHome_noResults);
        btn_myChurchEvents = findViewById(R.id.btn_userHome_allEvents);
        btn_bookmarks = findViewById(R.id.btn_userHome_myBookmarks);
        btn_churchFinder = findViewById(R.id.btn_userHome_churchFinder);
        btn_myChurch = findViewById(R.id.btn_userHome_myChurch);
        btn_editProfile = findViewById(R.id.btn_userHome_editProfile);

        //DATABASE
        eventsDb = new EventsTableHelper(this);
        participantsDb = new EventParticipantsTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);
        usersDb = new UsersTableHelper(this);

        //INTENTS
        myBookmarksIntent = new Intent(UserHome.this, MyBookmarks.class);
        churchFinderIntent = new Intent(UserHome.this, ChurchFinder.class);
        myChurchIntent = new Intent(UserHome.this, MyChurch.class);
        editUserProfileIntent = new Intent(UserHome.this, EditUserProfile.class);
        churchEventsIntent = new Intent(UserHome.this, ChurchEvents.class);
        eventDetailsIntent = new Intent(UserHome.this, EventDetails.class);

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
        bookmarksClick();
        churchFinderClick();
        myChurchClick();
        editProfileClick();
    }

    /**========================================FILL BUTTON TEXT========================================*/
    private void fillButtonText()
    {
        if (usersDb.doesUserHaveChurch(Session.getUser().getEmail()))
        {
            Church church = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
            btn_myChurchEvents.setText("See all " + church.getName() + "'s Events");
        }
        else
        {
            btn_myChurchEvents.setVisibility(View.INVISIBLE);
        }
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
                Log.v("LIST VIEW ITEM CLICK", "List View Item Clicked - Moving to EventDetails");
                Session.setOriginPage("userHomeIntent");
                eventDetailsIntent.putExtra("thisEvent", listOfEvents.get(i)); //Put extra the clicked event
                eventDetailsIntent.putExtra("cameFrom", "userHomeIntent"); //Put extra the name of this intent
                startActivity(eventDetailsIntent);
            }
        });
    }

    /**========================================SHOW NO RESULTS IF THERE AREN'T ANY RESULTS========================================*/
    private void ifNoResultsShow()
    {
        if (listOfEvents.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE); //Show "no results"
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
    }

    /**========================================MY CHURCH EVENTS CLICK========================================*/
    private void myChurchEventsClick()
    {
        btn_myChurchEvents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "My Church Events Button Clicked - Moving to ChurchEvents");
                Session.setOriginPage("userHomeIntent");
                churchEventsIntent.putExtra("cameFrom", "userHomeIntent");
                Church churchToPass = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
                churchEventsIntent.putExtra("thisChurch", churchToPass);
                startActivity(churchEventsIntent);
            }
        });
    }

    /**========================================BOOKMARKS CLICK========================================*/
    private void bookmarksClick()
    {
        btn_bookmarks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Bookmarks Button Clicked - Moving to MyBookmarks");
                startActivity(myBookmarksIntent);
            }
        });
    }

    /**========================================CHURCH FINDER CLICK========================================*/
    private void churchFinderClick()
    {
        btn_churchFinder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Church Finder Button Clicked - Moving to ChurchFinder");
                startActivity(churchFinderIntent);
            }
        });
    }

    /**========================================MY CHURCH CLICK========================================*/
    private void myChurchClick()
    {
        btn_myChurch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "My Church Button Clicked - Moving to MyChurch");
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
                Log.v("BUTTON CLICK", "Edit Profile (User) Button Clicked - Moving to EditUserProfile");
                startActivity(editUserProfileIntent);
            }
        });
    }
}