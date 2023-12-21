package com.example.churchapp.ChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.churchapp.Models.User;
import com.example.churchapp.R;

public class MemberDetails extends AppCompatActivity
{
    //GUI
    TextView tv_fname;
    TextView tv_lname;
    TextView tv_denomination;
    TextView tv_city;
    Button btn_back;

    //INTENTS
    Intent viewMembersIntent;

    //OTHER
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);

        //GUI
        tv_fname = findViewById(R.id.tv_memberDetails_fname);
        tv_lname = findViewById(R.id.tv_memberDetails_lname);
        tv_denomination = findViewById(R.id.tv_memberDetails_denomination);
        tv_city = findViewById(R.id.tv_memberDetails_city);
        btn_back = findViewById(R.id.btn_memberDetails_back);

        //INTENTS
        viewMembersIntent = new Intent(MemberDetails.this, ViewMembers.class);

        //GET EXTRA STUFF
        Intent cameFrom = getIntent();
        user = (User) cameFrom.getSerializableExtra("myMember");

        //FUNCTIONS
        fillTextViews();
        backButtonClick();
    }

    /**========================================FILL TEXT VIEWS========================================*/
    private void fillTextViews()
    {
        tv_fname.setText("First Name: " + user.getFirstName());
        tv_lname.setText("Last Name: " + user.getLastName());
        tv_denomination.setText("Denomination: " + user.getDenomination());
        tv_city.setText("City: " + user.getCity());
    }

    /**========================================BACK BUTTON CLICK========================================*/
    private void backButtonClick()
    {
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("Button Press", "Back Button Click (from MemberDetails) - Moving to ViewMembers");
                startActivity(viewMembersIntent);
            }
        });
    }
}