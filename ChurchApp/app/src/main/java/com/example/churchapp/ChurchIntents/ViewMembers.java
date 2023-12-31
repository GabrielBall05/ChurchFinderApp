package com.example.churchapp.ChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.churchapp.Adapters.MyMembersAdapter;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.User;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ViewMembers extends AppCompatActivity
{
    //GUI
    Button btn_back;
    ListView lv_members;
    TextView tv_noResults;

    //DATABASE
    UsersTableHelper usersDb;

    //INTENTS
    Intent memberDetailsIntent;
    Intent churchHomeIntent;

    //ADAPTER
    MyMembersAdapter adapter;

    //ARRAYLIST
    ArrayList<User> listOfMyMembers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_members);

        //GUI
        btn_back = findViewById(R.id.btn_viewMembers_back);
        lv_members = findViewById(R.id.lv_viewMembers_members);
        tv_noResults = findViewById(R.id.tv_viewMembers_noResults);

        //DATABASE
        usersDb = new UsersTableHelper(this);

        //INTENTS
        memberDetailsIntent = new Intent(ViewMembers.this, MemberDetails.class);
        churchHomeIntent = new Intent(ViewMembers.this, ChurchHome.class);

        //ARRAYLIST
        listOfMyMembers = new ArrayList<User>();
        listOfMyMembers = usersDb.getAllUsersAttendingChurch(Session.getChurch().getEmail());

        //FUNCTIONS
        fillListView();
        backButtonClick();
        listViewItemSelect();
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new MyMembersAdapter(this, listOfMyMembers);
        lv_members.setAdapter(adapter);

        ifNoResultsShow();
    }

    /**========================================LIST VIEW ITEM SELECTED========================================*/
    private void listViewItemSelect()
    {
        lv_members.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("LIST VIEW ITEM CLICK", "List View Item Clicked - Moving to MemberDetails");

                memberDetailsIntent.putExtra("myMember", listOfMyMembers.get(i)); //Put extra the member
                startActivity(memberDetailsIntent);
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
                Log.v("BUTTON CLICK", "Back Button Clicked - Moving to ChurchHome");
                startActivity(churchHomeIntent);
            }
        });
    }

    private void ifNoResultsShow()
    {
        if (listOfMyMembers.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
    }
}