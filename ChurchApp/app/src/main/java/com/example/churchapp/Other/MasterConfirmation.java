package com.example.churchapp.Other;

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
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;
import com.example.churchapp.UserIntents.ChurchDetails;
import com.example.churchapp.UserIntents.EditUserProfile;
import com.example.churchapp.UserIntents.ChurchFinder;
import com.example.churchapp.UserIntents.MyChurch;
import com.example.churchapp.UserIntents.UserHome;

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
    EventParticipantsTableHelper participantsDb;
    BookmarksTableHelper bookmarksDb;

    //INTENTS
    Intent mainActivityIntent;
    Intent churchHomeIntent;
    Intent editChurchProfileIntent;
    Intent editUserProfileIntent;
    Intent churchDetailsIntent;
    Intent userHomeIntent;
    Intent churchFinderIntent;
    Intent myChurchIntent;

    //EXTRA
    Intent origin;
    String cameFrom;
    String deleteOrSignOut;
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
        participantsDb = new EventParticipantsTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        mainActivityIntent = new Intent(MasterConfirmation.this, MainActivity.class);
        churchHomeIntent = new Intent(MasterConfirmation.this, ChurchHome.class);
        editChurchProfileIntent = new Intent(MasterConfirmation.this, EditChurchProfile.class);
        editUserProfileIntent = new Intent(MasterConfirmation.this, EditUserProfile.class);
        churchDetailsIntent = new Intent(MasterConfirmation.this, ChurchDetails.class);
        userHomeIntent = new Intent(MasterConfirmation.this, UserHome.class);
        churchFinderIntent = new Intent(MasterConfirmation.this, ChurchFinder.class);
        myChurchIntent = new Intent(MasterConfirmation.this, MyChurch.class);

        origin = getIntent();
        cameFrom = origin.getStringExtra("cameFrom"); //Get the name of the previous intent

        if (cameFrom.equals("churchHomeIntent")) //If previous intent is ChurchHome, get the event
        {
            eventToDelete = (Event) origin.getSerializableExtra("eventToDelete");
        }
        else if (cameFrom.equals("churchDetailsIntent")) //If previous intent is ChurchDetails, get the church
        {
            church = (Church) origin.getSerializableExtra("thisChurch");
        }
        else if (cameFrom.equals("myChurchIntent")) //If previous intent is MyChurch, get the church
        {
            church = (Church) origin.getSerializableExtra("myChurch");
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
        else if (cameFrom.equals("editChurchProfileIntent") || cameFrom.equals("editUserProfileIntent"))
        {
            deleteOrSignOut = origin.getStringExtra("deleteOrSignOut");
            if (deleteOrSignOut.equals("delete"))
            {
                tv_areYouSure.setText("Are you sure you want to delete your account?");
            }
            else if (deleteOrSignOut.equals("signOut"))
            {
                tv_areYouSure.setText("Are you sure you want to sign out?");
            }
        }
        else if (cameFrom.equals("churchDetailsIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to become a member of " + church.getName() + "? You can leave at any time in the 'My Church' page. Your bookmarks will remain intact");
        }
        else if (cameFrom.equals("myChurchIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to leave " + church.getName() + "? This will also remove you from any events you are signed up for at this church.");
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
                Log.v("BUTTON CLICK", "'YES' Button Clicked in MasterConfirmation");

                //DO IT
                if (cameFrom.equals("churchHomeIntent"))
                {
                    Log.v("DELETING", "Deleting Event - Moving to ChurchHome");
                    participantsDb.removeAllParticipantsFromEvent(eventToDelete.getEventId()); //Remove all participants of that event
                    eventsDb.deleteEvent(eventToDelete.getEventId()); //Delete the event
                    startActivity(churchHomeIntent);
                }
                else if (cameFrom.equals("editChurchProfileIntent"))
                {
                    Log.v("DELETING", "Deleting Church - Moving to MainActivity");
                    eventsDb.deleteChurchEvents(Session.getChurch().getEmail()); //Delete the church's events
                    bookmarksDb.deleteChurchBookmarks(Session.getChurch().getEmail()); //Delete bookmarks (that any user has) of the church
                    churchesDb.deleteChurch(Session.getChurch().getEmail()); //Delete the church
                    startActivity(mainActivityIntent);
                }
                else if (cameFrom.equals("editUserProfileIntent") && deleteOrSignOut.equals("delete"))
                {
                    Log.v("DELETING", "Deleting User - Moving to MainActivity");
                    bookmarksDb.deleteUserBookmarks(Session.getUser().getEmail()); //Delete bookmarks that the user has
                    usersDb.deleteUser(Session.getUser().getEmail()); //Delete the user
                    participantsDb.removeUserFromAllEvents(Session.getUser().getEmail());
                    startActivity(mainActivityIntent);
                }
                else if (cameFrom.equals("editUserProfileIntent") && deleteOrSignOut.equals("signOut"))
                {
                    Log.v("SIGNING OUT", "Signing Out - Moving to MainActivity");
                    startActivity(mainActivityIntent);
                }
                else if (cameFrom.equals("churchDetailsIntent"))
                {
                    Log.v("BECOMING MEMBER", "Becoming Member of " + church.getName() + " - Moving to UserHome");
                    usersDb.becomeMemberOrLeaveChurch(Session.getUser().getEmail(), church.getEmail()); //Become member of church (update emailOfChurchAttending column)
                    //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                    User user = new User(Session.getUser().getEmail(), Session.getUser().getPassword(), Session.getUser().getFirstName(), Session.getUser().getLastName(), church.getEmail(), Session.getUser().getDenomination(), Session.getUser().getCity());
                    Session.login(user); //Log the user in again as to update the user's information in Session
                    startActivity(userHomeIntent);
                }
                else if (cameFrom.equals("myChurchIntent"))
                {
                    Log.v("LEAVING CHURCH", "Leaving " + church.getName() + " & removing user from events - Moving to ChurchFinder");
                    usersDb.becomeMemberOrLeaveChurch(Session.getUser().getEmail(), ""); //Leave the church (pass "")
                    //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                    User user = new User(Session.getUser().getEmail(), Session.getUser().getPassword(), Session.getUser().getFirstName(), Session.getUser().getLastName(), "", Session.getUser().getDenomination(), Session.getUser().getCity());
                    Session.login(user);  //Log the user in again as to update the user's information in Session
                    participantsDb.removeUserFromAllEvents(Session.getUser().getEmail()); //Remove the user from any events
                    startActivity(churchFinderIntent);
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
                Log.v("BUTTON CLICK", "'NO' Button Clicked");

                //GO BACK
                if (cameFrom.equals("churchHomeIntent"))
                {
                    Log.v("NO", "Not Deleting - Moving back to ChurchHome");
                    startActivity(churchHomeIntent);
                }
                else if (cameFrom.equals("editChurchProfileIntent"))
                {
                    Log.v("NO", "Not Deleting - Moving back to EditChurchProfile");
                    startActivity(editChurchProfileIntent);
                }
                else if (cameFrom.equals("editUserProfileIntent"))
                {
                    Log.v("NO", "Not Deleting/Signing Out - Moving back to EditUserProfile");
                    startActivity(editUserProfileIntent);
                }
                else if (cameFrom.equals("churchDetailsIntent"))
                {
                    Log.v("NO", "Not Becoming Member - Moving back to ChurchDetails");
                    churchDetailsIntent.putExtra("thisChurch", church);
                    startActivity(churchDetailsIntent);
                }
                else if (cameFrom.equals("myChurchIntent"))
                {
                    Log.v("NO", "Staying Member - Moving back to MyChurch");
                    startActivity(myChurchIntent);
                }
            }
        });
    }
}