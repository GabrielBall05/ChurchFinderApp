package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    //DATABASE
    UsersTableHelper usersDb;
    BookmarksTableHelper bookmarksDb;

    //INTENTS
    Intent masterConfirmationIntent;
    Intent userNoChurchHomeIntent;
    Intent bookmarkedChurchesIntent;

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

        //DATABASE
        usersDb = new UsersTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        masterConfirmationIntent = new Intent(ChurchDetails.this, MasterConfirmation.class);
        userNoChurchHomeIntent = new Intent(ChurchDetails.this, ChurchFinder.class);
        bookmarkedChurchesIntent = new Intent(ChurchDetails.this, MyBookmarks.class);

        //EXTRA
        Intent origin = getIntent();
        church = (Church) origin.getSerializableExtra("thisChurch"); //Get the church passed to me
        cameFrom = origin.getStringExtra("cameFrom");

        //FUNCTIONS
        fillTextBoxes();
        becomeMemberButtonClick();
        bookmarkButtonClick();
        backButtonClick();
    }

    /**========================================FILL TEXT BOXES========================================*/
    private void fillTextBoxes()
    {
        tv_name.setText(church.getName());
        tv_denomination.setText(church.getName() + " is a " + church.getDenomination() + " church");
        tv_addressCity.setText("Located at " + church.getStreetAddress() + " in " + church.getCity());
        tv_email.setText("Email them at " + church.getEmail());
        tv_number.setText("Or call/text them at " + church.getNumber());
        tv_statement.setText("Their Statement of Faith is: '" + church.getStatementOfFaith() + "'");

        //Correct the bookmark button text
        //Checks if the user has a bookmark of this church
        if(bookmarksDb.doesBookmarkExist(Session.getUser().getEmail(), church.getEmail()))
        {
            btn_bookmark.setText("Remove\nBookmark");
        }
        else
        {
            btn_bookmark.setText("Bookmark\nChurch");
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
                }
                else
                {
                    Log.v("BUTTON CLICK", "Un/Bookmark Button Clicked - BOOKMARKING");
                    Bookmark bookmark = new Bookmark(Session.getUser().getEmail(), church.getEmail());
                    bookmarksDb.createBookmark(bookmark); //Create a bookmark in the database with the emails of the user and the church
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

    /**========================================BACK BUTTON CLICK========================================*/
    private void backButtonClick()
    {
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (cameFrom.equals("userNoChurchHomeIntent"))
                {
                    Log.d("BUTTON CLICK", "Back Button Click - Moving to ChurchFinder");
                    startActivity(userNoChurchHomeIntent);
                }
                else if (cameFrom.equals("bookmarkedChurchesIntent"))
                {
                    Log.d("BUTTON CLICK", "Back Button Click - Moving to MyBookmarks");
                    startActivity(bookmarkedChurchesIntent);
                }
            }
        });
    }
}