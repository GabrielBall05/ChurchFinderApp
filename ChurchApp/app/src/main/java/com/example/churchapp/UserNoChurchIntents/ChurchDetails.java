package com.example.churchapp.UserNoChurchIntents;

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
    TextView tv_address;
    TextView tv_city;
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
        tv_address = findViewById(R.id.tv_churchDetails_address);
        tv_city = findViewById(R.id.tv_churchDetails_city);
        tv_statement = findViewById(R.id.tv_churchDetails_statement);
        btn_becomeMember = findViewById(R.id.btn_churchDetails_becomeMember);
        btn_bookmark = findViewById(R.id.btn_churchDetails_bookmark);
        btn_back = findViewById(R.id.btn_churchDetails_back);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        masterConfirmationIntent = new Intent(ChurchDetails.this, MasterConfirmation.class);
        userNoChurchHomeIntent = new Intent(ChurchDetails.this, UserNoChurchHome.class);
        bookmarkedChurchesIntent = new Intent(ChurchDetails.this, BookmarkedChurches.class);

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
        tv_name.setText("Name: " + church.getName());
        tv_denomination.setText("Denomination: " + church.getDenomination());
        tv_email.setText("Email: " + church.getEmail());
        tv_number.setText("Number: " + church.getNumber());
        tv_address.setText("Address: " + church.getStreetAddress());
        tv_city.setText("City: " + church.getCity());
        tv_statement.setText("Statement of Faith: " + church.getStatementOfFaith());

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
                    Log.v("BUTTON CLICK", "Un/Bookmark Bookmark Clicked - UN-BOOKMARKING");
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
                    Log.d("BUTTON CLICK", "Back Button Click - Moving to UserNoChurchHome");
                    startActivity(userNoChurchHomeIntent);
                }
                else if (cameFrom.equals("bookmarkedChurchesIntent"))
                {
                    Log.d("BUTTON CLICK", "Back Button Click - Moving to BookmarkedChurches");
                    startActivity(bookmarkedChurchesIntent);
                }
            }
        });
    }
}