package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.churchapp.Adapters.ListOfBookmarksAdapter;
import com.example.churchapp.Database.BookmarksTableHelper;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Models.Bookmark;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class MyBookmarks extends AppCompatActivity
{
    //GUI
    EditText et_search;
    TextView tv_noResults;
    ListView lv_churches;
    ImageView btn_churchFinder;
    ImageView btn_userHome;
    ImageView btn_myChurch;
    ImageView btn_editProfile;

    //DATABASE
    BookmarksTableHelper bookmarksDb;
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent churchDetailsIntent;
    Intent churchFinderIntent;
    Intent userHomeIntent;
    Intent myChurchIntent;
    Intent editProfileIntent;

    //ARRAYLISTS
    ArrayList<Bookmark> listOfBookmarks;
    ArrayList<Church> listOfChurches;

    //ADAPTER
    ListOfBookmarksAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bookmarks);

        //GUI
        et_search = findViewById(R.id.et_myBookmarks_search);
        tv_noResults = findViewById(R.id.tv_myBookmarks_noResults);
        lv_churches = findViewById(R.id.lv_myBookmarks_churches);
        btn_churchFinder = findViewById(R.id.btn_myBookmarks_churchFinder);
        btn_userHome = findViewById(R.id.btn_myBookmarks_userHome);
        btn_myChurch = findViewById(R.id.btn_myBookmarks_myChurch);
        btn_editProfile = findViewById(R.id.btn_myBookmarks_editProfile);

        //DATABASE
        bookmarksDb = new BookmarksTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        churchDetailsIntent = new Intent(MyBookmarks.this, ChurchDetails.class);
        churchFinderIntent = new Intent(MyBookmarks.this, ChurchFinder.class);
        userHomeIntent = new Intent(MyBookmarks.this, UserHome.class);
        myChurchIntent = new Intent(MyBookmarks.this, MyChurch.class);
        editProfileIntent = new Intent(MyBookmarks.this, EditUserProfile.class);

        //ARRAYLISTS
        listOfBookmarks = new ArrayList<Bookmark>();
        //Have to first get all bookmarks that the user has
        listOfBookmarks = bookmarksDb.getAllBookmarksUnderUser(Session.getUser().getEmail());
        listOfChurches = new ArrayList<Church>();
        //Then have to cycle through bookmarks to make a list of churches to show the info of the churches
        for(int i = 0; i < listOfBookmarks.size(); i++)
        {
            Church church = churchesDb.getChurchByEmail(listOfBookmarks.get(i).getEmailOfChurch());
            listOfChurches.add(church);
        }

        //FUNCTIONS
        fillListView();
        searchBarChange();
        listViewItemClick();
        churchFinderButtonClick();
        homeButtonClick();
        myChurchButtonClick();
        editProfileButtonClick();
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new ListOfBookmarksAdapter(this, listOfBookmarks);
        lv_churches.setAdapter(adapter);

        ifNoResultsShow();
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
                listOfBookmarks = new ArrayList<Bookmark>();
                //Get all churches with the name that contains what the user searched
                listOfChurches = churchesDb.getChurchesByNameAlphabetical(text);
                //Cycle through the churches to add to the list of bookmarks if the user has a bookmark of that church
                for (int i = 0; i < listOfChurches.size(); i++)
                {
                    if (bookmarksDb.doesBookmarkExist(Session.getUser().getEmail(), listOfChurches.get(i).getEmail()))
                    {
                        listOfBookmarks.add(bookmarksDb.getBookmarkUnderUserWhereChurchEmailEquals(listOfChurches.get(i).getEmail()));
                    }
                }

                fillListView(); //Update list
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
                Log.v("LIST VIEW ITEM CLICK", "List View Item Clicked - Moving to ChurchDetails");
                churchDetailsIntent.putExtra("thisChurch", listOfChurches.get(i)); //Put extra the clicked church
                churchDetailsIntent.putExtra("cameFrom", "myBookmarksIntent");
                startActivity(churchDetailsIntent);
            }
        });
    }

    /**========================================SHOW NO RESULTS IF THERE AREN'T ANY RESULTS========================================*/
    private void ifNoResultsShow()
    {
        if (listOfBookmarks.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
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

    /**========================================MY CHURCH BUTTON CLICK========================================*/
    private void myChurchButtonClick()
    {
        btn_myChurch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "My Church Button Clicked - Moving to MyChurch");
                startActivity(myChurchIntent);
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
                Log.v("BUTTON CLICK", "Edit (User) Profile Button Clicked - Moving to EditUserProfile");
                startActivity(editProfileIntent);
            }
        });
    }
}