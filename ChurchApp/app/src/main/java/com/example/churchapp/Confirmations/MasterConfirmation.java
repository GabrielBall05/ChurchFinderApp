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
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;
import com.example.churchapp.UserNoChurchIntents.ChurchDetails;
import com.example.churchapp.UserWithChurchIntents.UserWithChurchHome;

public class MasterConfirmation extends AppCompatActivity
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
    Intent churchDetailsIntent;
    Intent userWithChurchHome;

    //OTHER
    String cameFrom;
    Event eventToDelete;
    Church church;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_confirmation);

        //GUI
        tv_areYouSure = findViewById(R.id.tv_masterConfirmation_areYouSure);
        btn_yes = findViewById(R.id.btn_masterConfirmation_yes);
        btn_no = findViewById(R.id.btn_masterConfirmation_no);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);
        usersDb = new UsersTableHelper(this);
        eventsDb = new EventsTableHelper(this);
        eventPartsDb = new EventParticipantsTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        mainActivityIntent = new Intent(MasterConfirmation.this, MainActivity.class);
        churchHomeIntent = new Intent(MasterConfirmation.this, ChurchHome.class);
        editChurchProfileIntent = new Intent(MasterConfirmation.this, EditChurchProfile.class);
        churchDetailsIntent = new Intent(MasterConfirmation.this, ChurchDetails.class);
        userWithChurchHome = new Intent(MasterConfirmation.this, UserWithChurchHome.class);

        Intent origin = getIntent();
        cameFrom = origin.getStringExtra("cameFrom");
        Log.v("CAME FROM: ", "Came from: " + cameFrom);

        if (cameFrom.equals("churchHomeIntent"))
        {
            eventToDelete = (Event) origin.getSerializableExtra("eventToDelete");
        }
        else if (cameFrom.equals("churchDetailsIntent"))
        {
            church = (Church) origin.getSerializableExtra("thisChurch");
        }

        //FUNCTIONS
        fillTextView();
        yesButtonClick();
        noButtonClick();
    }

    /**========================================FILL TEXT VIEW========================================*/
    private void fillTextView()
    {
        if (cameFrom.equals("churchHomeIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to delete this event?");
        }
        else if (cameFrom.equals("editChurchProfileIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to delete your account?");
        }
        else if (cameFrom.equals("churchDetailsIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to become a member of " + church.getName() + "?");
        }
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
                else if (cameFrom.equals("churchDetailsIntent"))
                {
                    Log.v("Button Press", "Becoming Member of " + church.getName() + " - Moving to UserWithChurchHome");
                    usersDb.becomeMember(Session.getUser().getEmail(), church.getEmail());
                    startActivity(userWithChurchHome);
                }
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
                    Log.v("Button Press", "Not Deleting - Moving back to EditChurchProfile");
                    startActivity(editChurchProfileIntent);
                }
                else if (cameFrom.equals("churchDetailsIntent"))
                {
                    Log.v("Button Press", "Not Becoming Member - Moving back to ChurchDetails");
                    churchDetailsIntent.putExtra("thisChurch", church);
                    startActivity(churchDetailsIntent);
                }
            }
        });
    }
}