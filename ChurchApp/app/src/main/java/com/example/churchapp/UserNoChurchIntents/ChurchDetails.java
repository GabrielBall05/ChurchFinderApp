package com.example.churchapp.UserNoChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.churchapp.Confirmations.MasterConfirmation;
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
    TextView tv_address;
    TextView tv_city;
    TextView tv_statement;
    Button btn_becomeMember;
    Button btn_bookmark;
    Button btn_showBookmarks;
    Button btn_home;
    Button btn_editProfile;

    //DATABASE
    UsersTableHelper usersDb;
    BookmarksTableHelper bookmarksDb;

    //INTENTS
    Intent masterConfirmationIntent;
    Intent userNoChurchHomeIntent;
    Intent bookmarkedChurchesIntent;
    Intent editProfileIntent;

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
        tv_address = findViewById(R.id.tv_churchDetails_address);
        tv_city = findViewById(R.id.tv_churchDetails_city);
        tv_statement = findViewById(R.id.tv_churchDetails_statement);
        btn_becomeMember = findViewById(R.id.btn_churchDetails_becomeMember);
        btn_bookmark = findViewById(R.id.btn_churchDetails_bookmark);
        btn_showBookmarks = findViewById(R.id.btn_churchDetails_bookmarks);
        btn_home = findViewById(R.id.btn_churchDetails_home);
        btn_editProfile = findViewById(R.id.btn_churchDetails_editProfile);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        masterConfirmationIntent = new Intent(ChurchDetails.this, MasterConfirmation.class);
        userNoChurchHomeIntent = new Intent(ChurchDetails.this, UserNoChurchHome.class);
        bookmarkedChurchesIntent = new Intent(ChurchDetails.this, BookmarkedChurches.class);
        editProfileIntent = new Intent(ChurchDetails.this, EditUserProfile.class);

        //EXTRA
        Intent origin = getIntent();
        church = (Church) origin.getSerializableExtra("thisChurch");
        cameFrom = origin.getStringExtra("cameFrom");

        //FUNCTIONS
        fillTextBoxes();
        becomeMemberButtonClick();
        bookmarkButtonClick();
        showBookmarksButtonClick();
        homeButtonClick();
        editProfileButtonClick();
    }

    /**========================================FILL TEXT BOXES========================================*/
    private void fillTextBoxes()
    {
        tv_name.setText("Name: " + church.getName());
        tv_denomination.setText("Denomination: " + church.getDenomination());
        tv_email.setText("Email: " + church.getEmail());
        tv_address.setText("Address: " + church.getStreetAddress());
        tv_city.setText("City: " + church.getCity());
        tv_statement.setText("Statement of Faith: " + church.getStatementOfFaith());

        //Correct the bookmark button text
        if(bookmarksDb.doesBookmarkExist(church.getEmail(), Session.getUser().getEmail()))
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
                Log.v("BUTTON PRESS", "Become Member button press - are you sure? - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "churchDetailsIntent");
                masterConfirmationIntent.putExtra("thisChurch", church);
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
                if(bookmarksDb.doesBookmarkExist(church.getEmail(), Session.getUser().getEmail()))
                {
                    Log.v("BUTTON PRESS - UN-BOOKMARKING", "Un/Bookmark button press - UN-BOOKMARKING");
                    bookmarksDb.deleteBookmark(church.getEmail(), Session.getUser().getEmail());
                }
                else
                {
                    Log.v("BUTTON PRESS - BOOKMARKING", "Un/Bookmark button press - BOOKMARKING");
                    Bookmark bookmark = new Bookmark(church.getEmail(), Session.getUser().getEmail());
                    bookmarksDb.createBookmark(bookmark);
                }

                //Correct the bookmark button text
                if(bookmarksDb.doesBookmarkExist(church.getEmail(), Session.getUser().getEmail()))
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

    /**========================================SHOW BOOKMARKS BUTTON CLICK========================================*/
    private void showBookmarksButtonClick()
    {
        btn_showBookmarks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON PRESS", "Show Bookmarks Button Click - Moving to BookmarkedChurches");
                startActivity(bookmarkedChurchesIntent);
            }
        });
    }

    /**========================================HOME BUTTON CLICK========================================*/
    private void homeButtonClick()
    {
        btn_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON PRESS", "Home Button Click - Moving to UserNoChurchHome");
                startActivity(userNoChurchHomeIntent);
            }
        });
    }

    /**========================================EDIT PROFILE (USER) BUTTON CLICK========================================*/
    private void editProfileButtonClick()
    {
        btn_editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON PRESS", "Edit Profile (User) Button Click - Moving to EditUserProfile");
                startActivity(editProfileIntent);
            }
        });
    }
}