package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.churchapp.Adapters.ParticipantsAdapter;
import com.example.churchapp.ChurchIntents.EventParticipantsPage;
import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.EventParticipant;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsOfEvent extends AppCompatActivity
{
    //GUI
    TextView tv_title;
    TextView tv_noResults;
    Button btn_back;
    ListView lv_parts;

    //DATABASE
    EventParticipantsTableHelper participantsDb;
    UsersTableHelper usersDb;

    //INTENTS
    Intent eventDetailsIntent;

    //ARRAYLISTS
    ArrayList<User> listOfUsers;
    ArrayList<EventParticipant> listOfParticipants;

    //ADAPTER
    ParticipantsAdapter adapter;

    //EXTRA
    Event event;
    Church church;
    String lastCameFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants_of_event);

        //GUI
        tv_title = findViewById(R.id.tv_partsOfEvent_title);
        tv_noResults = findViewById(R.id.tv_partsOfEvent_noResults);
        btn_back = findViewById(R.id.btn_partsOfEvent_back);
        lv_parts = findViewById(R.id.lv_partsOfEvent_parts);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        participantsDb = new EventParticipantsTableHelper(this);

        //INTENTS
        eventDetailsIntent = new Intent(ParticipantsOfEvent.this, EventDetails.class);

        //EXTRA
        Intent origin = getIntent();
        event = (Event) origin.getSerializableExtra("thisEvent");
        church = (Church) origin.getSerializableExtra("thisChurch");
        lastCameFrom = origin.getStringExtra("lastCameFrom");

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
        lv_parts.setAdapter(adapter);

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
                Log.v("BUTTON CLICK", "Back Button Clicked - Moving to EventDetails");
                eventDetailsIntent.putExtra("cameFrom", lastCameFrom);
                eventDetailsIntent.putExtra("thisChurch", church);
                eventDetailsIntent.putExtra("thisEvent", event);
                startActivity(eventDetailsIntent);
            }
        });
    }

    /**========================================IF NO RESULTS SHOW========================================*/
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