package com.example.churchapp.ChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;

public class ChurchHome extends AppCompatActivity
{
    //GUI
    ListView lv_myEvents;
    Button btn_viewMembers;
    Button btn_createEvent;
    Button btn_editProfile;

    //DATABASE
    UsersTableHelper usersDb;
    EventsTableHelper eventsDb;

    //INTENTS
    Intent editEventIntent;
    Intent viewMembersIntent;
    Intent createEventIntent;
    Intent editProfileIntent;
    Intent deleteConfirmationIntent;

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

        //DATABASE
        usersDb = new UsersTableHelper(this);
        eventsDb = new EventsTableHelper(this);

        //INTENTS
        editEventIntent = new Intent(ChurchHome.this, EditEvent.class);
        viewMembersIntent = new Intent(ChurchHome.this, ViewMembers.class);
        createEventIntent = new Intent(ChurchHome.this, CreateEvent.class);
        editProfileIntent = new Intent(ChurchHome.this, EditChurchProfile.class);

        //FUNCTIONS
        viewMembersButtonClick();
        createEventButtonClick();
        editProfileButtonClick();
        listViewItemClick();
        listViewItemLongClick();
    }

    private void listViewItemClick()
    {
        lv_myEvents.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.v("List View Click", "List view item click in ChurchHome (lv_myEvents) - Moving to EditEvent");
                                                            /**==============================MAKE A NEW EVENT GIVEN THE INFO AT int position AND .putExtra() IT TO EDITEVENT INTENT==============================*/
                startActivity(editEventIntent);
            }
        });
    }

    private void listViewItemLongClick()
    {
        lv_myEvents.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                Log.v("List View Long Click", "List view long click in ChurchHome (lv_myEvents) - Moving to DeleteConfirmation");
                                                            /**==============================MAKE A NEW EVENT GIVEN THE INFO AT int position AND .putExtra() IT TO DELETECONFIRMATION INTENT==============================*/
                startActivity(deleteConfirmationIntent);

                return false;
            }
        });
    }

    private void viewMembersButtonClick()
    {
        btn_viewMembers.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "View Members Button Click - Moving to ViewMembers");
                startActivity(viewMembersIntent);
            }
        });
    }

    private void createEventButtonClick()
    {
        btn_createEvent.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Create Event Button Click - Moving to CreateEvent");
                startActivity(createEventIntent);
            }
        });
    }

    private void editProfileButtonClick()
    {
        btn_editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Edit Church Profile Button Click - Moving to EditChurchProfile");
                startActivity(editProfileIntent);
            }
        });
    }
}