package com.example.churchapp.UserNoChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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

    //ARRAYLISTS
    ArrayList<Bookmark> listOfBookmarks;
    ArrayList<Church> listOfChurches;

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

        //ARRAYLISTS
        listOfBookmarks = new ArrayList<Bookmark>();
        listOfBookmarks = bookmarksDb.getAllBookmarksUnderUser(Session.getUser().getEmail());
        listOfChurches = new ArrayList<Church>();
        for(int i = 0; i < listOfBookmarks.size(); i++)
        {
            Church church = churchesDb.getChurchByEmail(listOfBookmarks.get(i).getEmailOfChurch());
            listOfChurches.add(church);
        }

        //FUNCTIONS
        fillListView();
        searchBarChange();
        listViewItemClick();
        homeButtonClick();
        editProfileButtonClick();
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
//        //String arrays to pass to the adapter because it doesn't like reading from database in there
//        ArrayList<String> names = new ArrayList<String>();
//        ArrayList<String> denominations = new ArrayList<String>();
//        for (int i = 0; i < listOfBookmarks.size(); i++)
//        {
//            Church church = churchesDb.getChurchByEmail(listOfBookmarks.get(i).getEmailOfChurch());
//            names.add(church.getName());
//            denominations.add(church.getDenomination());
//        }
//
//        adapter = new ListOfBookmarksAdapter(this, listOfBookmarks, names, denominations);

        adapter = new ListOfBookmarksAdapter(this, listOfBookmarks);
        lv_churches.setAdapter(adapter);
    }

    /**========================================LIST VIEW ITEM CLICK========================================*/
    private void searchBarChange()
    {
        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String text = s.toString();
                listOfChurches = new ArrayList<Church>();
                listOfChurches = churchesDb.getChurchesByName(text);
                listOfBookmarks = new ArrayList<Bookmark>();
                for(int i = 0; i < listOfChurches.size(); i++)
                {
                    Bookmark bookmark = bookmarksDb.getBookmarkByChurchEmail(listOfChurches.get(i).getEmail());
                    listOfBookmarks.add(bookmark);
                }

                fillListView();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**========================================LIST VIEW ITEM CLICK========================================*/
    private void listViewItemClick()
    {
        lv_churches.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("LIST VIEW ITEM CLICK", "List View Item Click - Moving to ChurchDetails");
                churchDetailsIntent.putExtra("thisChurch", listOfChurches.get(i));
                startActivity(churchDetailsIntent);
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