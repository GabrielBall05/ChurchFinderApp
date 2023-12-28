package com.example.churchapp.ChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

        //EXTRA
        Intent origin = getIntent();
        event = (Event) origin.getSerializableExtra("myEvent");

        //ARRAYLISTS
        listOfParticipants = new ArrayList<EventParticipant>();
        listOfParticipants = participantsDb.getParticipantsOfEvent(event.getEventId());
        listOfUsers = new ArrayList<User>();
        for (int i = 0; i < listOfParticipants.size(); i++)
        {
            User user = usersDb.getUserByEmail(listOfParticipants.get(i).getEmailOfParticipant());
            listOfUsers.add(user);
        }

        //FUNCTIONS
        fillText();
        fillListView();
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

    /**========================================BACK BUTTON CLICK========================================*/
    private void backButtonClick()
    {
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Back Button Click - Moving to EditEvent");
                editEventIntent.putExtra("myEvent", event);
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