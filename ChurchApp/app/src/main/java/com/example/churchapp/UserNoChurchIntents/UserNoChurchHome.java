package com.example.churchapp.UserNoChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.Adapters.ListOfChurchesAdapter;
import com.example.churchapp.Adapters.MyEventsAdapter;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

import java.util.ArrayList;

public class UserNoChurchHome extends AppCompatActivity
{
    //GUI
    EditText et_search;
    Spinner sp_filter;
    Spinner sp_denominations;
    ListView lv_churches;
    Button btn_bookmarks;
    Button btn_editProfile;
    TextView tv_noResults;

    //DATABASE
    ChurchesTableHelper churchesDb;

    //INTENTS
    Intent churchDetailsIntent;
    Intent bookmarksIntent;
    Intent editUserProfileIntent;

    //STRINGS
    String searchByName = "Search by name";
    String searchByDenomination = "Search by denomination";
    String searchByCity = "Search by city";
    String allChurches = "All churches";

    //BOOLEANS
    Boolean searchingByName = false;
    Boolean searchingByCity = false;
    Boolean searchingByDenomination = false;

    //ADAPTER
    ListOfChurchesAdapter adapter;
    ArrayAdapter<CharSequence> denominationsAdapter;

    //ARRAYLIST
    ArrayList<Church> listOfChurches;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_no_church_home);

        //GUI
        et_search = findViewById(R.id.et_userNCHome_search);
        sp_filter = findViewById(R.id.sp_userNCHome_filter);
        sp_denominations = findViewById(R.id.sp_userNCHome_denominations);
        lv_churches = findViewById(R.id.lv_userNCHome_churches);
        btn_bookmarks = findViewById(R.id.btn_userNCHome_bookmarks);
        btn_editProfile = findViewById(R.id.btn_userNCHome_editProfile);
        tv_noResults = findViewById(R.id.tv_userNCHome_noResults);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);

        //INTENTS
        churchDetailsIntent = new Intent(UserNoChurchHome.this, ChurchDetails.class);
        bookmarksIntent = new Intent(UserNoChurchHome.this, BookmarkedChurches.class);
        editUserProfileIntent = new Intent(UserNoChurchHome.this, EditUserProfile.class);

        //FILTER SPINNER
        ArrayAdapter<CharSequence> filterAdapter = ArrayAdapter.createFromResource(this, R.array.filter, android.R.layout.simple_spinner_item);
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
        editProfileButtonClick();
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

                if (searchingByName) //If user is searching by name of church
                {
                    listOfChurches = churchesDb.getChurchesByNameAlphabetical(text); //Get by name
                }
                else if (searchingByCity) //If user is searching by church's city
                {
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
                churchDetailsIntent.putExtra("thisChurch", listOfChurches.get(i)); //Put extra the clicked church
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
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

                if (text.equals(searchByDenomination)) //IF THE USER WANTS TO SEARCH BY DENOMINATION
                {
                    //Show denomination drop down, hide search, reset search text
                    sp_denominations.setVisibility(View.VISIBLE);
                    et_search.setVisibility(View.INVISIBLE);
                    et_search.setText("");

                    //Set booleans
                    searchingByName = false;
                    searchingByDenomination = true;
                    searchingByCity = false;

                    //Start by showing the churches of the user's denomination
                    sp_denominations.setSelection(denominationsAdapter.getPosition(Session.getUser().getDenomination()));
                    listOfChurches = new ArrayList<Church>();
                    listOfChurches = churchesDb.getChurchesByDenominationAlphabeticalByName(sp_denominations.getSelectedItem().toString());
                    fillListView(); //Update list
                }
                else if (text.equals(searchByName)) //IF THE USER WANTS TO SEARCH BY NAME
                {
                    //Hide denomination drop down, show search, reset search text, set search hint
                    sp_denominations.setVisibility(View.INVISIBLE);
                    et_search.setVisibility(View.VISIBLE);
                    et_search.setHint("Search by name");
                    et_search.setText("");

                    //Set booleans
                    searchingByName = true;
                    searchingByDenomination = false;
                    searchingByCity = false;

                    //Just show all churches, when the user starts typing, it'll update
                    listOfChurches = new ArrayList<Church>();
                    listOfChurches = churchesDb.getAllChurchesAlphabetical();
                    fillListView(); //Update list
                }
                else if (text.equals(searchByCity)) //IF THE USER WANTS TO SEARCH BY CITY
                {
                    //Hide denomination drop down, show search, set search text to user's current city, set search hint
                    sp_denominations.setVisibility(View.INVISIBLE);
                    et_search.setVisibility(View.VISIBLE);
                    et_search.setHint("Search by city");
                    et_search.setText(Session.getUser().getCity());

                    //Set booleans
                    searchingByName = false;
                    searchingByDenomination = false;
                    searchingByCity = true;

                    //Just show all churches in the user's city
                    listOfChurches = new ArrayList<Church>();
                    listOfChurches = churchesDb.getChurchesByCityAlphabetical(Session.getUser().getCity());
                    fillListView(); //Update list
                }
                else if (text.equals(allChurches)) //IF THE USER WANTS TO SEE ALL CHURCHES
                {
                    //Hide denomination drop down, hide search, reset search text
                    sp_denominations.setVisibility(View.INVISIBLE);
                    et_search.setVisibility(View.INVISIBLE);
                    et_search.setText("");

                    //Set booleans
                    searchingByName = false;
                    searchingByDenomination = false;
                    searchingByCity = false;

                    //Get all churches to show
                    listOfChurches = new ArrayList<Church>();
                    listOfChurches = churchesDb.getAllChurchesAlphabetical();
                    fillListView(); //Update list
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();

                if (searchingByDenomination) //If the user is searching by denomination
                {
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

    /**========================================BOOKMARKS BUTTON CLICK========================================*/
    private void bookmarksButtonClick()
    {
        btn_bookmarks.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Bookmarked Churches Button Clicked - Moving to BookmarkedChurches");
                startActivity(bookmarksIntent);
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
}