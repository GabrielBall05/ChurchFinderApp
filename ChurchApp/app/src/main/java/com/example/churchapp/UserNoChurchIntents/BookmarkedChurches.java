package com.example.churchapp.UserNoChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.churchapp.Adapters.ListOfBookmarksAdapter;
import com.example.churchapp.Adapters.ListOfChurchesAdapter;
import com.example.churchapp.Database.BookmarksTableHelper;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Models.Bookmark;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class BookmarkedChurches extends AppCompatActivity
{
    //GUI
    EditText et_search;
    TextView tv_noResults;
    ListView lv_churches;
    Button btn_home;
    Button btn_editProfile;

    //DATABASE
    BookmarksTableHelper bookmarksDb;
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent churchDetailsIntent;
    Intent editProfileIntent;
    Intent userNoChurchHomeIntent;

    //ARRAYLIST
    ArrayList<Bookmark> listOfBookmarks;

    //ADAPTER
    ListOfBookmarksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarked_churches);

        //GUI
        et_search = findViewById(R.id.et_bookmarkedChurches_search);
        tv_noResults = findViewById(R.id.tv_bookmarkedChurches_noResults);
        lv_churches = findViewById(R.id.lv_bookmarkedChurches_churches);
        btn_home = findViewById(R.id.btn_bookmarkedChurches_home);
        btn_editProfile = findViewById(R.id.btn_bookmarkedChurches_editProfile);

        //DATABASE
        bookmarksDb = new BookmarksTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        churchDetailsIntent = new Intent(BookmarkedChurches.this, ChurchDetails.class);
        editProfileIntent = new Intent(BookmarkedChurches.this, EditUserProfile.class);
        userNoChurchHomeIntent = new Intent(BookmarkedChurches.this, UserNoChurchHome.class);

        //ARRAYLIST
        listOfBookmarks = new ArrayList<Bookmark>();
        listOfBookmarks = bookmarksDb.getAllBookmarksUnderUser(Session.getUser().getEmail());

        //FUNCTIONS
        fillListView();
    }

    private void fillListView()
    {
        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> denominations = new ArrayList<String>();
        for (int i = 0; i < listOfBookmarks.size(); i++)
        {
            Log.d("CHURCH EMAIL OF BOOKMARK", listOfBookmarks.get(i).getEmailOfChurch());
            Church church = churchesDb.getChurchByEmail(listOfBookmarks.get(i).getEmailOfChurch());
            Log.d("CHURCH NAME", church.getName());
            names.add(church.getName());
            denominations.add(church.getDenomination());
        }

        adapter = new ListOfBookmarksAdapter(this, listOfBookmarks, names, denominations);
        lv_churches.setAdapter(adapter);
    }
}