package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.Other.MasterConfirmation;
import com.example.churchapp.Database.BookmarksTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Bookmark;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class ChurchDetails extends AppCompatActivity
{
    //GUI
    TextView tv_name;
    TextView tv_denomination;
    TextView tv_email;
    TextView tv_number;
    TextView tv_addressCity;
    TextView tv_statement;
    Button btn_becomeMember;
    Button btn_bookmark;
    Button btn_back;
    Button btn_viewEvents;

    //DATABASE
    UsersTableHelper usersDb;
    BookmarksTableHelper bookmarksDb;

    //INTENTS
    Intent masterConfirmationIntent;
    Intent churchFinderIntent;
    Intent bookmarksIntent;
    Intent churchEventsIntent;

    //EXTRA
    Church church;
    String cameFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_details);

        //GUI
        tv_name = findViewById(R.id.tv_churchDetails_name);
        tv_denomination = findViewById(R.id.tv_churchDetails_denomination);
        tv_email = findViewById(R.id.tv_churchDetails_email);
        tv_number = findViewById(R.id.tv_churchDetails_number);
        tv_addressCity = findViewById(R.id.tv_churchDetails_addressCity);
        tv_statement = findViewById(R.id.tv_churchDetails_statement);
        btn_becomeMember = findViewById(R.id.btn_churchDetails_becomeMember);
        btn_bookmark = findViewById(R.id.btn_churchDetails_bookmark);
        btn_back = findViewById(R.id.btn_churchDetails_back);
        btn_viewEvents = findViewById(R.id.btn_churchDetails_viewEvents);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        masterConfirmationIntent = new Intent(ChurchDetails.this, MasterConfirmation.class);
        churchFinderIntent = new Intent(ChurchDetails.this, ChurchFinder.class);
        bookmarksIntent = new Intent(ChurchDetails.this, MyBookmarks.class);
        churchEventsIntent = new Intent(ChurchDetails.this, ChurchEvents.class);

        //EXTRA
        Intent origin = getIntent();
        church = (Church) origin.getSerializableExtra("thisChurch"); //Get the church passed to me
        cameFrom = origin.getStringExtra("cameFrom");

        //FUNCTIONS
        fillTextBoxes();
        becomeMemberButtonClick();
        bookmarkButtonClick();
        backButtonClick();
        viewEventsButtonClick();
    }

    /**========================================FILL TEXT BOXES========================================*/
    private void fillTextBoxes()
    {
        tv_name.setText(church.getName() + "");
        tv_denomination.setText(church.getName() + " is a " + church.getDenomination() + " church");
        tv_addressCity.setText("Located at " + church.getStreetAddress() + " in " + church.getCity());
        tv_email.setText("Email them at " + church.getEmail());
        tv_number.setText("Or call/text them at " + church.getNumber());
        tv_statement.setText("Their Statement of Faith is: '" + church.getStatementOfFaith() + "'");

        //Correct the bookmark button text
        if(bookmarksDb.doesBookmarkExist(Session.getUser().getEmail(), church.getEmail()))
        {
            btn_bookmark.setText("Remove\nBookmark");
        }
        else
        {
            btn_bookmark.setText("Bookmark\nChurch");
        }

        //Disable become member button if user is already member of this church
        if (Session.getUser().getEmailOfChurchAttending().equals(church.getEmail()))
        {
            btn_becomeMember.setText("Already\nMember");
            btn_becomeMember.setEnabled(false);
        }
    }

    /**========================================BECOME MEMBER BUTTON CLICK========================================*/
    private void becomeMemberButtonClick()
    {
        btn_becomeMember.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Become Member Button Clicked - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "churchDetailsIntent"); //Put extra the name of this intent
                masterConfirmationIntent.putExtra("thisChurch", church); //Put extra the church the user is looking at
                startActivity(masterConfirmationIntent);
            }
        });
    }

    /**========================================BOOKMARK BUTTON CLICK========================================*/
    private void bookmarkButtonClick()
    {
        btn_bookmark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(bookmarksDb.doesBookmarkExist(Session.getUser().getEmail(), church.getEmail()))
                {
                    Log.v("BUTTON CLICK", "Un/Bookmark Button Clicked - UN-BOOKMARKING");
                    bookmarksDb.deleteBookmark(Session.getUser().getEmail(), church.getEmail()); //Delete the bookmark from the database
                    Toast.makeText(ChurchDetails.this, "Deleted Bookmark", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Log.v("BUTTON CLICK", "Un/Bookmark Button Clicked - BOOKMARKING");
                    Bookmark bookmark = new Bookmark(Session.getUser().getEmail(), church.getEmail());
                    bookmarksDb.createBookmark(bookmark); //Create a bookmark in the database with the emails of the user and the church
                    Toast.makeText(ChurchDetails.this, "Created Bookmark", Toast.LENGTH_SHORT).show();
                }

                //Correct the bookmark button text
                if(bookmarksDb.doesBookmarkExist(Session.getUser().getEmail(), church.getEmail()))
                {
                    btn_bookmark.setText("Remove\nBookmark");
                }
                else
                {
                    btn_bookmark.setText("Bookmark\nChurch");
                }
            }
        });
    }

    /**========================================VIEW EVENTS BUTTON CLICK========================================*/
    private void viewEventsButtonClick()
    {
        btn_viewEvents.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "View Events Button Click - Moving to ChurchEvents");
                churchEventsIntent.putExtra("cameFrom", "churchDetailsIntent");
                churchEventsIntent.putExtra("thisChurch", church);
                startActivity(churchEventsIntent);
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
                if (cameFrom.equals("churchFinderIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Click - Moving to ChurchFinder");
                    startActivity(churchFinderIntent);
                }
                else if (cameFrom.equals("masterConfirmationIntent") && Session.getOriginPage().equals("churchFinderIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Click - Moving to ChurchFinder");
                    startActivity(churchFinderIntent);
                }
                else if (cameFrom.equals("churchEventsIntent") && Session.getOriginPage().equals("churchFinderIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Click - Moving to ChurchFinder");
                    startActivity(churchFinderIntent);
                }
                else if (cameFrom.equals("myBookmarksIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Click - Moving to MyBookmarks");
                    startActivity(bookmarksIntent);
                }
                else if (cameFrom.equals("churchEventsIntent") && Session.getOriginPage().equals("myBookmarksIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Click - Moving to MyBookmarks");
                    startActivity(bookmarksIntent);
                }
            }
        });
    }
}