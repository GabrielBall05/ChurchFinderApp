package com.example.churchapp.UserWithChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.churchapp.Confirmations.MasterConfirmation;
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
    Button btn_home;
    Button btn_editProfile;

    //DATABASE
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent editUserProfile2Intent;
    Intent userWithChurchHomeIntent;
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
        btn_home = findViewById(R.id.btn_myChurch_home);
        btn_editProfile = findViewById(R.id.btn_myChurch_editProfile);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        editUserProfile2Intent = new Intent(MyChurch.this, EditUserProfile2.class);
        userWithChurchHomeIntent = new Intent(MyChurch.this, UserWithChurchHome.class);
        masterConfirmationIntent = new Intent(MyChurch.this, MasterConfirmation.class);

        //FUNCTIONS
        fillText();
        leaveButtonClick();
        homeButtonClick();
        editProfileButtonClick();
    }

    /**========================================FILL TEXT========================================*/
    private void fillText()
    {
        Church church = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
        tv_name.setText("You attend " + church.getName());
        tv_denomination.setText(church.getName() + " is a " + church.getDenomination() + " church");
        tv_addressCity.setText("Located at " + church.getStreetAddress() + " in " + church.getCity());
        tv_email.setText("Email them at " + church.getEmail());
        tv_number.setText("Or call/text them at " + church.getNumber());
        tv_statement.setText("Their statement of Faith is: " + church.getStatementOfFaith());
        btn_leave.setText("Leave " + church.getName());
    }

    /**========================================LEAVE BUTTON CLICK========================================*/
    private void leaveButtonClick()
    {
        btn_leave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Leave Church Button Press - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "myChurchIntent");
                Church church = churchesDb.getChurchByEmail(Session.getUser().getEmailOfChurchAttending());
                masterConfirmationIntent.putExtra("myChurch", church);
                startActivity(masterConfirmationIntent);
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
                Log.v("Button Press", "Home Button Press - Moving to UserWithChurchHome");
                startActivity(userWithChurchHomeIntent);
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
                Log.v("Button Press", "Edit Profile Button Press - Moving to EditUserProfile2");
                startActivity(editUserProfile2Intent);
            }
        });
    }
}