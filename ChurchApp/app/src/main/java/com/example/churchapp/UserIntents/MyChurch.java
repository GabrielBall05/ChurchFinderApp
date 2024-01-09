package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Other.MasterConfirmation;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class MyChurch extends AppCompatActivity
{
    //GUI
    TextView tv_name;
    TextView tv_denomination;
    TextView tv_addressCity;
    TextView tv_email;
    TextView tv_number;
    TextView tv_statement;
    Button btn_leave;
    ImageView btn_bookmarks;
    ImageView btn_churchFinder;
    ImageView btn_userHome;
    ImageView btn_editProfile;
    TextView tv_notMember;

    //DATABASE
    ChurchesTableHelper churchesDb;
    UsersTableHelper usersDb;

    //INTENTS
    Intent editUserProfileIntent;
    Intent userHomeIntent;
    Intent bookmarksIntent;
    Intent churchFinderIntent;
    Intent masterConfirmationIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_church);

        //GUI
        tv_name = findViewById(R.id.tv_myChurch_name);
        tv_denomination = findViewById(R.id.tv_myChurch_denomation);
        tv_addressCity = findViewById(R.id.tv_myChurch_addressCity);
        tv_email = findViewById(R.id.tv_myChurch_email);
        tv_number = findViewById(R.id.tv_myChurch_number);
        tv_statement = findViewById(R.id.tv_myChurch_statement);
        btn_leave = findViewById(R.id.btn_myChurch_leave);
        btn_bookmarks = findViewById(R.id.btn_myChurch_myBookmarks);
        btn_churchFinder = findViewById(R.id.btn_myChurch_churchFinder);
        btn_userHome = findViewById(R.id.btn_myChurch_userHome);
        btn_editProfile = findViewById(R.id.btn_myChurch_editProfile);
        tv_notMember = findViewById(R.id.tv_myChurch_notMember);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);
        usersDb = new UsersTableHelper(this);

        //INTENTS
        editUserProfileIntent = new Intent(MyChurch.this, EditUserProfile.class);
        userHomeIntent = new Intent(MyChurch.this, UserHome.class);
        churchFinderIntent = new Intent(MyChurch.this, ChurchFinder.class);
        bookmarksIntent = new Intent(MyChurch.this, MyBookmarks.class);
        masterConfirmationIntent = new Intent(MyChurch.this, MasterConfirmation.class);

        //FUNCTIONS
        fillText();
        leaveButtonClick();
        bookmarksButtonClick();
        churchFinderButtonClick();
        homeButtonClick();
        editProfileButtonClick();
    }

    /**========================================FILL TEXT========================================*/
    private void fillText()
    {
        if (usersDb.doesUserHaveChurch(Session.getUser().getEmail()))
        {
            //Get the church that the user attends to fill in the text boxes
            Church church = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
            tv_name.setText("You attend " + church.getName());
            tv_denomination.setText(church.getName() + " is a " + church.getDenomination() + " church");
            tv_addressCity.setText("Located at " + church.getStreetAddress() + " in " + church.getCity());
            tv_email.setText("Email them at " + church.getEmail());
            tv_number.setText("Or call/text them at " + church.getNumber());
            tv_statement.setText("Their statement of Faith is: " + church.getStatementOfFaith());
            btn_leave.setText("Leave " + church.getName());
            tv_notMember.setVisibility(View.INVISIBLE);
        }
        else
        {
            tv_notMember.setVisibility(View.VISIBLE);
            tv_name.setVisibility(View.INVISIBLE);
            tv_denomination.setVisibility(View.INVISIBLE);
            tv_addressCity.setVisibility(View.INVISIBLE);
            tv_email.setVisibility(View.INVISIBLE);
            tv_number.setVisibility(View.INVISIBLE);
            tv_statement.setVisibility(View.INVISIBLE);
            btn_leave.setVisibility(View.INVISIBLE);
        }
    }

    /**========================================LEAVE BUTTON CLICK========================================*/
    private void leaveButtonClick()
    {
        btn_leave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Leave Church Button Clicked - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "myChurchIntent"); //Put extra the name of this intent
                Church church = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
                masterConfirmationIntent.putExtra("myChurch", church); //Put extra the church that the user attends
                startActivity(masterConfirmationIntent);
            }
        });
    }

    /**========================================BOOKMARKS BUTTON CLICK========================================*/
    private void bookmarksButtonClick()
    {
        btn_bookmarks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Bookmarks Button Clicked - Moving to MyBookmarks");
                startActivity(bookmarksIntent);
            }
        });
    }

    /**========================================CHURCH FINDER BUTTON CLICK========================================*/
    private void churchFinderButtonClick()
    {
        btn_churchFinder.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Church Finder Button Clicked - Moving to ChurchFinder");
                startActivity(churchFinderIntent);
            }
        });
    }

    /**========================================HOME BUTTON CLICK========================================*/
    private void homeButtonClick()
    {
        btn_userHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Home Button Clicked - Moving to UserHome");
                startActivity(userHomeIntent);
            }
        });
    }

    /**========================================EDIT PROFILE BUTTON CLICK========================================*/
    private void editProfileButtonClick()
    {
        btn_editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Edit Profile Button Clicked - Moving to EditUserProfile2");
                startActivity(editUserProfileIntent);
            }
        });
    }
}