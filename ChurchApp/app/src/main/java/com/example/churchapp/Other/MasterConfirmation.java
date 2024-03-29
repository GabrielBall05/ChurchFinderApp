package com.example.churchapp.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.ChurchIntents.ChurchHome;
import com.example.churchapp.ChurchIntents.EditChurchProfile;
import com.example.churchapp.ChurchIntents.EventParticipantsPage;
import com.example.churchapp.Database.BookmarksTableHelper;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.EventParticipant;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;
import com.example.churchapp.UserIntents.ChurchDetails;
import com.example.churchapp.UserIntents.EditUserProfile;
import com.example.churchapp.UserIntents.ChurchFinder;
import com.example.churchapp.UserIntents.MyChurch;
import com.example.churchapp.UserIntents.UserHome;

import java.util.ArrayList;

public class MasterConfirmation extends AppCompatActivity
{
    //GUI
    TextView tv_areYouSure;
    Button btn_yes;
    Button btn_no;
    EditText et_pass;
    TextView tv_incorrect;

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
    Intent eventParticipantsIntent;

    //EXTRA
    Intent origin;
    String cameFrom;
    String deleteOrSignOut;
    Event eventToDelete;
    Church church;
    EventParticipant participantToDelete;
    Event eventToPassBack;
    User userToShowInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_confirmation);

        //GUI
        tv_areYouSure = findViewById(R.id.tv_masterConfirmation_areYouSure);
        btn_yes = findViewById(R.id.btn_masterConfirmation_yes);
        btn_no = findViewById(R.id.btn_masterConfirmation_no);
        et_pass = findViewById(R.id.et_masterConfirmation_password);
        tv_incorrect = findViewById(R.id.tv_masterConfirmation_incorrect);

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
        eventParticipantsIntent = new Intent(MasterConfirmation.this, EventParticipantsPage.class);

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
        else if (cameFrom.equals("eventParticipantsIntent"))
        {
            participantToDelete = (EventParticipant) origin.getSerializableExtra("thisParticipant");
            eventToPassBack = (Event) origin.getSerializableExtra("thisEvent");
            userToShowInfo = (User) origin.getSerializableExtra("thisUser");
        }

        //FUNCTIONS
        fillTextView();
        yesButtonClick();
        noButtonClick();
    }

    /**========================================FILL TEXT VIEW========================================*/
    private void fillTextView()
    {
        tv_incorrect.setVisibility(View.INVISIBLE);
        et_pass.setVisibility(View.INVISIBLE);

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
                et_pass.setVisibility(View.VISIBLE);
                tv_incorrect.setVisibility(View.INVISIBLE);
            }
            else if (deleteOrSignOut.equals("signOut"))
            {
                tv_areYouSure.setText("Are you sure you want to sign out?");
            }
        }
        else if (cameFrom.equals("churchDetailsIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to become a member of " + church.getName() + "? You can leave at any time in the 'My Church' page.");
        }
        else if (cameFrom.equals("myChurchIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to leave " + church.getName() + "? You can become a member again anytime.");
        }
        else if (cameFrom.equals("eventParticipantsIntent"))
        {
            tv_areYouSure.setText("Are you sure you want to remove " + userToShowInfo.getFirstName() + " " + userToShowInfo.getLastName() + " from this event?");
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
                    Toast.makeText(MasterConfirmation.this, "Event Deleted", Toast.LENGTH_SHORT).show();
                    startActivity(churchHomeIntent);
                }
                else if (cameFrom.equals("editChurchProfileIntent")  && deleteOrSignOut.equals("delete"))
                {
                    if (et_pass.getText().toString().equals(Session.getChurch().getPassword()))
                    {
                        tv_incorrect.setVisibility(View.INVISIBLE);

                        Log.v("DELETING", "Deleting Church - Moving to MainActivity");
                        ArrayList<Event> ls = eventsDb.getAllEventsByChurchEmail(Session.getChurch().getEmail());
                        if (eventsDb.doesChurchHaveEvents(Session.getChurch().getEmail()))
                        {
                            for (int i = 0; i < ls.size(); i++) //Deletes all participants under every event this church has
                            {
                                participantsDb.removeAllParticipantsFromEvent(ls.get(i).getEventId());
                            }
                        }
                        eventsDb.deleteChurchEvents(Session.getChurch().getEmail()); //Delete the church's events
                        bookmarksDb.deleteChurchBookmarks(Session.getChurch().getEmail()); //Delete bookmarks (that any user has) of the church
                        usersDb.removeAllUsersFromChurch(Session.getChurch().getEmail());
                        churchesDb.deleteChurch(Session.getChurch().getEmail()); //Delete the church
                        Toast.makeText(MasterConfirmation.this, "Your Account Has Been Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(mainActivityIntent);
                    }
                    else
                    {
                        tv_incorrect.setVisibility(View.VISIBLE);
                    }
                }
                else if (cameFrom.equals("editChurchProfileIntent")  && deleteOrSignOut.equals("signOut"))
                {
                    Log.v("SIGNING OUT", "Signing Out - Moving to MainActivity");
                    Toast.makeText(MasterConfirmation.this, "Signed Out", Toast.LENGTH_SHORT).show();
                    startActivity(mainActivityIntent);
                }
                else if (cameFrom.equals("editUserProfileIntent") && deleteOrSignOut.equals("delete"))
                {
                    if (et_pass.getText().toString().equals(Session.getUser().getPassword()))
                    {
                        tv_incorrect.setVisibility(View.INVISIBLE);

                        Log.v("DELETING", "Deleting User - Moving to MainActivity");
                        bookmarksDb.deleteUserBookmarks(Session.getUser().getEmail()); //Delete bookmarks that the user has
                        usersDb.deleteUser(Session.getUser().getEmail()); //Delete the user
                        participantsDb.removeUserFromAllEvents(Session.getUser().getEmail());
                        Toast.makeText(MasterConfirmation.this, "Your Account Has Been Deleted", Toast.LENGTH_SHORT).show();
                        startActivity(mainActivityIntent);
                    }
                    else
                    {
                        tv_incorrect.setVisibility(View.VISIBLE);
                    }
                }
                else if (cameFrom.equals("editUserProfileIntent") && deleteOrSignOut.equals("signOut"))
                {
                    Log.v("SIGNING OUT", "Signing Out - Moving to MainActivity");
                    Toast.makeText(MasterConfirmation.this, "Signed Out", Toast.LENGTH_SHORT).show();
                    startActivity(mainActivityIntent);
                }
                else if (cameFrom.equals("churchDetailsIntent"))
                {
                    Log.v("BECOMING MEMBER", "Becoming Member of " + church.getName() + " - Moving to UserHome");
                    usersDb.becomeMemberOrLeaveChurch(Session.getUser().getEmail(), church.getEmail()); //Become member of church (update emailOfChurchAttending column)
                    //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                    User user = new User(Session.getUser().getEmail(), Session.getUser().getPassword(), Session.getUser().getFirstName(), Session.getUser().getLastName(), church.getEmail(), Session.getUser().getDenomination(), Session.getUser().getCity());
                    Session.login(user); //Log the user in again to update the user's information in Session
                    Toast.makeText(MasterConfirmation.this, "Became Member", Toast.LENGTH_SHORT).show();
                    startActivity(myChurchIntent);
                }
                else if (cameFrom.equals("myChurchIntent"))
                {
                    Log.v("LEAVING CHURCH", "Leaving " + church.getName() + " - Moving to ChurchFinder");
                    usersDb.becomeMemberOrLeaveChurch(Session.getUser().getEmail(), ""); //Leave the church (pass "")
                    //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                    User user = new User(Session.getUser().getEmail(), Session.getUser().getPassword(), Session.getUser().getFirstName(), Session.getUser().getLastName(), "", Session.getUser().getDenomination(), Session.getUser().getCity());
                    Session.login(user);  //Log the user in again to update the user's information in Session
                    Toast.makeText(MasterConfirmation.this, "Left Church", Toast.LENGTH_SHORT).show();
                    startActivity(churchFinderIntent);
                }
                else if (cameFrom.equals("eventParticipantsIntent"))
                {
                    Log.v("REMOVING PARTICIPANT", "Removing " + userToShowInfo.getFirstName() + " " + userToShowInfo.getLastName() + " from event - Moving to EventParticipantsPage");
                    EventParticipant part = new EventParticipant(eventToPassBack.getEventId(), participantToDelete.getEmailOfParticipant());
                    participantsDb.deleteEventParticipant(part);
                    eventParticipantsIntent.putExtra("myEvent", eventToPassBack);
                    Toast.makeText(MasterConfirmation.this, "Removed Participant From Event", Toast.LENGTH_SHORT).show();
                    startActivity(eventParticipantsIntent);
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
                    Toast.makeText(MasterConfirmation.this, "NOT Deleting Event", Toast.LENGTH_SHORT).show();
                    startActivity(churchHomeIntent);
                }
                else if (cameFrom.equals("editChurchProfileIntent"))
                {
                    Log.v("NO", "Not Deleting - Moving back to EditChurchProfile");
                    Toast.makeText(MasterConfirmation.this, "Selected 'NO'", Toast.LENGTH_SHORT).show();
                    startActivity(editChurchProfileIntent);
                }
                else if (cameFrom.equals("editUserProfileIntent"))
                {
                    Log.v("NO", "Not Deleting/Signing Out - Moving back to EditUserProfile");
                    Toast.makeText(MasterConfirmation.this, "Selected 'NO'", Toast.LENGTH_SHORT).show();
                    startActivity(editUserProfileIntent);
                }
                else if (cameFrom.equals("churchDetailsIntent"))
                {
                    Log.v("NO", "Not Becoming Member - Moving back to ChurchDetails");
                    churchDetailsIntent.putExtra("thisChurch", church);
                    churchDetailsIntent.putExtra("cameFrom", "masterConfirmationIntent");
                    Toast.makeText(MasterConfirmation.this, "NOT Becoming Member", Toast.LENGTH_SHORT).show();
                    startActivity(churchDetailsIntent);
                }
                else if (cameFrom.equals("myChurchIntent"))
                {
                    Log.v("NO", "Staying Member - Moving back to MyChurch");
                    Toast.makeText(MasterConfirmation.this, "Staying Member", Toast.LENGTH_SHORT).show();
                    startActivity(myChurchIntent);
                }
                else if (cameFrom.equals("eventParticipantsIntent"))
                {
                    Log.v("NO", "Participant not being removed - Moving back to EventParticipantsPage");
                    eventParticipantsIntent.putExtra("myEvent", eventToPassBack);
                    Toast.makeText(MasterConfirmation.this, "NOT Removing Participant", Toast.LENGTH_SHORT).show();
                    startActivity(eventParticipantsIntent);
                }
            }
        });
    }
}