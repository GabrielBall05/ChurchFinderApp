package com.example.churchapp.UserIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.Adapters.ListOfChurchesAdapter;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Other.SearchParams;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ChurchFinder extends AppCompatActivity
{
    //GUI
    EditText et_search;
    Spinner sp_filter;
    Spinner sp_denominations;
    ListView lv_churches;
    TextView tv_noResults;
    ImageView btn_bookmarks;
    ImageView btn_userHome;
    ImageView btn_myChurch;
    ImageView btn_editProfile;

    //DATABASE
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent churchDetailsIntent;
    Intent bookmarksIntent;
    Intent userHomeIntent;
    Intent myChurchIntent;
    Intent editUserProfileIntent;

    //STRINGS
    String searchByName = "Search by name";
    String searchByDenomination = "Search by denomination";
    String searchByCity = "Search by city";
    String searchByAll = "All churches";

    //ADAPTER
    ListOfChurchesAdapter adapter;
    ArrayAdapter<CharSequence> denominationsAdapter;
    ArrayAdapter<CharSequence> filterAdapter;

    //ARRAYLIST
    ArrayList<Church> listOfChurches;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_finder);

        //GUI
        et_search = findViewById(R.id.et_churchFinder_search);
        sp_filter = findViewById(R.id.sp_churchFinder_filter);
        sp_denominations = findViewById(R.id.sp_churchFinder_denominations);
        lv_churches = findViewById(R.id.lv_churchFinder_churches);
        btn_bookmarks = findViewById(R.id.btn_churchFinder_myBookmarks);
        btn_userHome = findViewById(R.id.btn_churchFinder_userHome);
        btn_myChurch = findViewById(R.id.btn_churchFinder_myChurch);
        btn_editProfile = findViewById(R.id.btn_churchFinder_editProfile);
        tv_noResults = findViewById(R.id.tv_churchFinder_noResults);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        churchDetailsIntent = new Intent(ChurchFinder.this, ChurchDetails.class);
        bookmarksIntent = new Intent(ChurchFinder.this, MyBookmarks.class);
        editUserProfileIntent = new Intent(ChurchFinder.this, EditUserProfile.class);
        userHomeIntent = new Intent(ChurchFinder.this, UserHome.class);
        myChurchIntent = new Intent(ChurchFinder.this, MyChurch.class);

        //FILTER SPINNER
        filterAdapter = ArrayAdapter.createFromResource(this, R.array.filter, android.R.layout.simple_spinner_item);
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_filter.setAdapter(filterAdapter);

        //DENOMINATIONS SPINNER
        denominationsAdapter = ArrayAdapter.createFromResource(this, R.array.denominations, android.R.layout.simple_spinner_item);
        denominationsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_denominations.setAdapter(denominationsAdapter);

        //ARRAYLIST
        listOfChurches = new ArrayList<Church>();
        listOfChurches = churchesDb.getAllChurchesAlphabetical();

        //FUNCTIONS
        fillListView();
        searchBarChange();
        listViewItemSelect();
        filterSelect();
        denominationSelect();
        bookmarksButtonClick();
        userHomeClick();
        myChurchClick();
        editProfileButtonClick();
        getSearchParamsAndFill();
    }

    /**========================================GET SEARCH PARAMETERS AND FILL EVERYTHING========================================*/
    private void getSearchParamsAndFill()
    {
        if (SearchParams.getSearchingBy().equals("name"))
        {
            sp_filter.setSelection(filterAdapter.getPosition(searchByName));
            searchByName(SearchParams.getName());
        }
        else if (SearchParams.getSearchingBy().equals("city"))
        {
            sp_filter.setSelection(filterAdapter.getPosition(searchByCity));
            searchByCity(SearchParams.getCity());
        }
        else if (SearchParams.getSearchingBy().equals("denomination"))
        {
            sp_filter.setSelection(filterAdapter.getPosition(searchByDenomination));
            searchByDenomination(SearchParams.getDenomination());
        }
        else if (SearchParams.getSearchingBy().equals("all"))
        {
            sp_filter.setSelection(filterAdapter.getPosition(searchByAll));
            searchByAll();
        }
    }

    /**========================================SEARCH BAR CHANGE========================================*/
    private void searchBarChange()
    {
        et_search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) //Whenever the search bar's text gets changed by the user
            {
                String text = s.toString();
                listOfChurches = new ArrayList<Church>();

                if (SearchParams.getSearchingBy().equals("name")) //If user is searching by name of church
                {
                    SearchParams.setName(text);
                    listOfChurches = churchesDb.getChurchesByNameAlphabetical(text); //Get by name
                }
                else if (SearchParams.getSearchingBy().equals("city")) //If user is searching by church's city
                {
                    SearchParams.setCity(text);
                    listOfChurches = churchesDb.getChurchesByCityAlphabetical(text); //Get by city
                }

                fillListView(); //Update list
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**========================================LIST VIEW ITEM CLICK========================================*/
    private void listViewItemSelect()
    {
        lv_churches.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id)
            {
                Log.v("LIST VIEW ITEM CLICK", "List View Item Clicked - Moving to ChurchDetails");
                Session.setOriginPage("churchFinderIntent");
                churchDetailsIntent.putExtra("thisChurch", listOfChurches.get(i)); //Put extra the clicked church
                churchDetailsIntent.putExtra("cameFrom", "churchFinderIntent");
                startActivity(churchDetailsIntent);
            }
        });
    }

    /**========================================FILTER SELECTED========================================*/
    private void filterSelect()
    {
        sp_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id)
            {
                String text = parent.getItemAtPosition(i).toString();

                if (text.equals(searchByDenomination)) //IF THE USER WANTS TO SEARCH BY DENOMINATION
                {
                    if(SearchParams.getSearchingBy().equals("denomination"))
                    {
                        searchByDenomination(SearchParams.getDenomination());
                    }
                    else
                    {
                        searchByDenomination(Session.getUser().getDenomination());
                    }
                }
                else if (text.equals(searchByName)) //IF THE USER WANTS TO SEARCH BY NAME
                {
                    if (SearchParams.getSearchingBy().equals("name"))
                    {
                        searchByName(SearchParams.getName());
                    }
                    else
                    {
                        searchByName("");
                    }
                }
                else if (text.equals(searchByCity)) //IF THE USER WANTS TO SEARCH BY CITY
                {
                    if (SearchParams.getSearchingBy().equals("city"))
                    {
                        searchByCity(SearchParams.getCity());
                    }
                    else
                    {
                        searchByCity(Session.getUser().getCity());
                    }
                }
                else if (text.equals(searchByAll)) //IF THE USER WANTS TO SEE ALL CHURCHES
                {
                    searchByAll();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**========================================SEARCH BY DENOMINATION========================================*/
    private void searchByDenomination(String d)
    {
        //Show denomination drop down, hide search, reset search text
        sp_denominations.setVisibility(View.VISIBLE);
        et_search.setVisibility(View.INVISIBLE);
        et_search.setText("");

        //Set searchingBy in SearchParams
        SearchParams.setSearchingBy("denomination");

        //Start by showing the churches of the user's denomination
        sp_denominations.setSelection(denominationsAdapter.getPosition(d));
        listOfChurches = new ArrayList<Church>();
        listOfChurches = churchesDb.getChurchesByDenominationAlphabeticalByName(sp_denominations.getSelectedItem().toString());
        fillListView(); //Update list
    }

    /**========================================SEARCH BY NAME========================================*/
    private void searchByName(String n)
    {
        //Hide denomination drop down, show search, reset search text, set search hint
        sp_denominations.setVisibility(View.INVISIBLE);
        et_search.setVisibility(View.VISIBLE);
        et_search.setHint("Search by name");
        et_search.setText(n);

        //Set searchingBy in SearchParams
        SearchParams.setSearchingBy("name");

        //Just show all churches, when the user starts typing, it'll update
        listOfChurches = new ArrayList<Church>();
        listOfChurches = churchesDb.getChurchesByNameAlphabetical(n);
        fillListView(); //Update list
    }

    /**========================================SEARCH BY CITY========================================*/
    private void searchByCity(String c)
    {
        //Hide denomination drop down, show search, set search text to user's current city, set search hint
        sp_denominations.setVisibility(View.INVISIBLE);
        et_search.setVisibility(View.VISIBLE);
        et_search.setHint("Search by city");
        et_search.setText(c);

        //Set searchingBy in SearchParams
        SearchParams.setSearchingBy("city");

        listOfChurches = new ArrayList<Church>();
        listOfChurches = churchesDb.getChurchesByCityAlphabetical(c);
        fillListView(); //Update list
    }

    /**========================================SHOW ALL (SEARCH BY ALL)========================================*/
    private void searchByAll()
    {
        //Hide denomination drop down, hide search, reset search text
        sp_denominations.setVisibility(View.INVISIBLE);
        et_search.setVisibility(View.INVISIBLE);
        et_search.setText("");

        //Set searchingBy in SearchParams
        SearchParams.setSearchingBy("all");

        //Get all churches to show
        listOfChurches = new ArrayList<Church>();
        listOfChurches = churchesDb.getAllChurchesAlphabetical();
        fillListView(); //Update list
    }

    /**========================================DENOMINATION SELECTED========================================*/
    private void denominationSelect()
    {
        sp_denominations.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id)
            {
                String text = parent.getItemAtPosition(i).toString();

                if (SearchParams.getSearchingBy().equals("denomination")) //If the user is searching by denomination
                {
                    SearchParams.setDenomination(text);
                    //Get the churches given the selected denomination
                    listOfChurches = new ArrayList<Church>();
                    listOfChurches = churchesDb.getChurchesByDenominationAlphabeticalByName(text);
                    fillListView(); //Update list
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**========================================FILL LIST VIEW========================================*/
    private void fillListView()
    {
        adapter = new ListOfChurchesAdapter(this, listOfChurches);
        lv_churches.setAdapter(adapter);

        ifNoResultsShow();
    }

    /**========================================SHOW NO RESULTS IF THERE AREN'T ANY RESULTS========================================*/
    private void ifNoResultsShow()
    {
        if (listOfChurches.size() == 0)
        {
            tv_noResults.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_noResults.setVisibility(View.INVISIBLE);
        }
    }

    /**========================================BOOKMARKS BUTTON CLICK========================================*/
    private void bookmarksButtonClick()
    {
        btn_bookmarks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Bookmarked Churches Button Clicked - Moving to MyBookmarks");
                startActivity(bookmarksIntent);
            }
        });
    }

    /**========================================USER HOME BUTTON CLICK========================================*/
    private void userHomeClick()
    {
        btn_userHome.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "User Home Button Clicked - Moving to UserHome");
                startActivity(userHomeIntent);
            }
        });
    }

    /**========================================MY CHURCH BUTTON CLICK========================================*/
    private void myChurchClick()
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

    /**========================================EDIT PROFILE BUTTON CLICK========================================*/
    private void editProfileButtonClick()
    {
        btn_editProfile.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Edit Profile (User) Button Clicked - Moving to EditUserProfile");
                startActivity(editUserProfileIntent);
            }
        });
    }
}