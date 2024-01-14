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

import com.example.churchapp.Adapters.ListOfBookmarksAdapter;
import com.example.churchapp.Adapters.ParticipantsAdapter;
import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.EventParticipant;
import com.example.churchapp.Models.User;
import com.example.churchapp.Other.MasterConfirmation;
import com.example.churchapp.R;

import java.util.ArrayList;

public class EventParticipantsPage extends AppCompatActivity
{
    //GUI
    TextView tv_title;
    TextView tv_noResults;
    ListView lv_participants;
    Button btn_back;

    //DATABASE
    EventParticipantsTableHelper participantsDb;
    UsersTableHelper usersDb;

    //INTENTS
    Intent editEventIntent;
    Intent masterConfirmationIntent;

    //ADAPTER
    ParticipantsAdapter adapter;

    //ARRAYLISTS
    ArrayList<User> listOfUsers;
    ArrayList<EventParticipant> listOfParticipants;

    //EXTRA
    Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_participants_page);

        //GUI
        tv_title = findViewById(R.id.tv_eventPartsPage_title);
        tv_noResults = findViewById(R.id.tv_eventPartsPage_noResults);
        lv_participants = findViewById(R.id.lv_eventPartsPage_participants);
        btn_back = findViewById(R.id.btn_eventPartsPage_back);

        //DATABASE
        participantsDb = new EventParticipantsTableHelper(this);
        usersDb = new UsersTableHelper(this);

        //INTENTS
        editEventIntent = new Intent(EventParticipantsPage.this, EditEvent.class);
        masterConfirmationIntent = new Intent(EventParticipantsPage.this, MasterConfirmation.class);

        //EXTRA
        Intent origin = getIntent();
        event = (Event) origin.getSerializableExtra("myEvent"); //Get the event passed

        //ARRAYLISTS
        listOfParticipants = new ArrayList<EventParticipant>();
        //Get participants of the event
        listOfParticipants = participantsDb.getParticipantsOfEvent(event.getEventId());
        listOfUsers = new ArrayList<User>();
        //Cycle through participants to make a list of users to display their information
        for (int i = 0; i < listOfParticipants.size(); i++)
        {
            User user = usersDb.getUserByEmail(listOfParticipants.get(i).getEmailOfParticipant());
            listOfUsers.add(user);
        }

        //FUNCTIONS
        fillText();
        fillListView();
        listViewLongClick();
        backButtonClick();
    }

    /**========================================FILL TEXT========================================*/
    private void fillText()
    {
        tv_title.setText("All members signed up for '" + event.getEventName() + "' on " + event.getDate() + " at " + event.getTime() + ":");
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new ParticipantsAdapter(this, listOfUsers);
        lv_participants.setAdapter(adapter);

        ifNoResultsShow();
    }

    /**========================================LIST VIEW LONG CLICK (REMOVE PARTICIPANT)========================================*/
    private void listViewLongClick()
    {
        lv_participants.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("LIST VIEW LONG CLICK", "Remove Event Participant Action - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "eventParticipantsIntent");
                masterConfirmationIntent.putExtra("thisParticipant", listOfParticipants.get(i));
                masterConfirmationIntent.putExtra("thisUser", listOfUsers.get(i));
                masterConfirmationIntent.putExtra("thisEvent", event);
                startActivity(masterConfirmationIntent);

                return false;
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
                Log.v("BUTTON CLICK", "Back Button Clicked - Moving to EditEvent");
                editEventIntent.putExtra("myEvent", event); //Put extra the event
                startActivity(editEventIntent);
            }
        });
    }

    private void ifNoResultsShow()
    {
        if (listOfUsers.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
    }
}