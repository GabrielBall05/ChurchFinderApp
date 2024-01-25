package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.churchapp.Adapters.MyMembersAdapter;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ChurchMembers extends AppCompatActivity
{
    //GUI
    TextView tv_title;
    TextView tv_noResults;
    Button btn_back;
    ListView lv_members;

    //DATABASE
    UsersTableHelper usersDb;

    //INTENTS
    Intent churchDetailsIntent;

    //ARRAYLIST
    ArrayList<User> listOfMembers;

    //ADAPTER
    MyMembersAdapter adapter;

    //EXTRA
    Church church;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_members);

        //GUI
        tv_title = findViewById(R.id.tv_churchMembers_title);
        tv_noResults = findViewById(R.id.tv_churchMembers_noResults);
        btn_back = findViewById(R.id.btn_churchMembers_back);
        lv_members = findViewById(R.id.lv_churchMembers_members);

        //DATABASE
        usersDb = new UsersTableHelper(this);

        //INTENTS
        churchDetailsIntent = new Intent(ChurchMembers.this, ChurchDetails.class);

        //EXTRA
        Intent origin = getIntent();
        church = (Church) origin.getSerializableExtra("thisChurch");

        //ARRAYLIST
        listOfMembers = new ArrayList<User>();
        listOfMembers = usersDb.getAllUsersAttendingChurch(church.getEmail());

        //FUNCTIONS
        fillTitleText();
        fillListView();
        backButtonClick();
    }

    /**========================================FILL TITLE TEXT========================================*/
    private void fillTitleText()
    {
        tv_title.setText("Showing Members of " + church.getName());
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new MyMembersAdapter(this, listOfMembers);
        lv_members.setAdapter(adapter);

        ifNoResultsShow();
    }

    /**========================================BACK BUTTON CLICK========================================*/
    private void backButtonClick()
    {
        btn_back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Back Button Click - Moving to ChurchDetails");
                churchDetailsIntent.putExtra("cameFrom", "churchMembersIntent");
                churchDetailsIntent.putExtra("thisChurch", church);
                startActivity(churchDetailsIntent);
            }
        });
    }

    /**========================================IF NO RESULTS SHOW========================================*/
    private void ifNoResultsShow()
    {
        if (listOfMembers.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
    }
}