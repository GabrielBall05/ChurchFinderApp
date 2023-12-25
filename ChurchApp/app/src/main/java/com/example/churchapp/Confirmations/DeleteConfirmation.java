package com.example.churchapp.Confirmations;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.churchapp.ChurchIntents.ChurchHome;
import com.example.churchapp.ChurchIntents.EditChurchProfile;
import com.example.churchapp.Database.BookmarksTableHelper;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.MainActivity;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class DeleteConfirmation extends AppCompatActivity
{
    //GUI
    TextView tv_areYouSure;
    Button btn_yes;
    Button btn_no;

    //DATABASE
    ChurchesTableHelper churchesDb;
    UsersTableHelper usersDb;
    EventsTableHelper eventsDb;
    EventParticipantsTableHelper eventPartsDb;
    BookmarksTableHelper bookmarksDb;

    //INTENTS
    Intent mainActivityIntent;
    Intent churchHomeIntent;
    Intent editChurchProfileIntent;

    //OTHER
    String cameFrom;
    Event eventToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_confirmation);

        //GUI
        tv_areYouSure = findViewById(R.id.tv_deleteConfirmation_areYouSure);
        btn_yes = findViewById(R.id.btn_deleteConfirmation_yes);
        btn_no = findViewById(R.id.btn_deleteConfirmation_no);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);
        usersDb = new UsersTableHelper(this);
        eventsDb = new EventsTableHelper(this);
        eventPartsDb = new EventParticipantsTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        mainActivityIntent = new Intent(DeleteConfirmation.this, MainActivity.class);
        churchHomeIntent = new Intent(DeleteConfirmation.this, ChurchHome.class);
        editChurchProfileIntent = new Intent(DeleteConfirmation.this, EditChurchProfile.class);

        Intent origin = getIntent();
        cameFrom = origin.getStringExtra("cameFrom");

        if (cameFrom.equals("churchHomeIntent"))
        {
            eventToDelete = (Event) origin.getSerializableExtra("eventToDelete");
        }

        //FUNCTIONS
        fillTextView();
        yesButtonClick();
        noButtonClick();
    }

    /**========================================FILL TEXT VIEW========================================*/
    private void fillTextView()
    {

    }

    /**========================================YES BUTTON PRESS========================================*/
    private void yesButtonClick()
    {
        btn_yes.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "'YES' Button Click");

                //DELETE IT
                if (cameFrom.equals("churchHomeIntent"))
                {
                    Log.v("Button Press", "Deleting Event - Moving to ChurchHome");
                    eventsDb.deleteEvent(eventToDelete.getEventId());
                    startActivity(churchHomeIntent);
                }
                else if (cameFrom.equals("editChurchProfileIntent"))
                {
                    Log.v("Button Press", "Deleting Church - Moving to MainActivity");
                    eventsDb.deleteChurchEvents(Session.getChurch().getEmail());
                    churchesDb.deleteChurch(Session.getChurch().getEmail());
                    startActivity(mainActivityIntent);
                }


                //Log.v("Button Press", "Deleting  - Moving to ");
                //Log.v("Button Press", "Deleting  - Moving to ");
            }
        });
    }

    /**========================================NO BUTTON PRESS========================================*/
    private void noButtonClick()
    {
        btn_no.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "'NO' Button Click");

                //GO BACK
                if (cameFrom.equals("churchHomeIntent"))
                {
                    Log.v("Button Press", "Not Deleting - Moving back to ChurchHome");
                    startActivity(churchHomeIntent);
                }
                else if (cameFrom.equals("editChurchProfileIntent"))
                {
                    Log.v("Button Press", "Not Deleting - Moving back to ");
                    startActivity(editChurchProfileIntent);
                }

                //Log.v("Button Press", "Not Deleting - Moving back to ");
                //Log.v("Button Press", "Not Deleting - Moving back to ");
            }
        });
    }
}