package com.example.churchapp.UserNoChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.Other.MasterConfirmation;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Other.MainActivity;
import com.example.churchapp.Models.User;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class EditUserProfile extends AppCompatActivity
{
    //GUI
    EditText et_fname;
    EditText et_lname;
    EditText et_city;
    EditText et_password;
    Spinner sp_denomination;
    TextView tv_fieldsError;
    Button btn_update;
    Button btn_delete;
    Button btn_bookmarks;
    Button btn_home;
    Button btn_signOut;

    //DATABASE
    UsersTableHelper usersDb;

    //INTENTS
    Intent userNoChurchHomeIntent;
    Intent bookmarksIntent;
    Intent masterConfirmationIntent;
    Intent mainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        //GUI
        et_fname = findViewById(R.id.et_editUserProfile_fname);
        et_lname = findViewById(R.id.et_editUserProfile_lname);
        et_city = findViewById(R.id.et_editUserProfile_city);
        et_password = findViewById(R.id.et_editUserProfile_password);
        sp_denomination = findViewById(R.id.sp_editUserProfile_denomination);
        tv_fieldsError = findViewById(R.id.tv_editUserProfile_fieldsError);
        btn_update = findViewById(R.id.btn_editUserProfile_update);
        btn_delete = findViewById(R.id.btn_editUserProfile_delete);
        btn_bookmarks = findViewById(R.id.btn_editUserProfile_bookmarks);
        btn_home = findViewById(R.id.btn_editUserProfile_home);
        btn_signOut = findViewById(R.id.btn_editUserProfile_signOut);

        //DATABASE
        usersDb = new UsersTableHelper(this);

        //INTENTS
        userNoChurchHomeIntent = new Intent(EditUserProfile.this, UserNoChurchHome.class);
        bookmarksIntent = new Intent(EditUserProfile.this, BookmarkedChurches.class);
        masterConfirmationIntent = new Intent(EditUserProfile.this, MasterConfirmation.class);
        mainActivityIntent = new Intent(EditUserProfile.this, MainActivity.class);

        //Fill text boxes with current info
        fillInTextBoxes();

        //SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.denominations, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_denomination.setAdapter(adapter);
        //Set spinner selection equal to the user's denomination
        sp_denomination.setSelection(adapter.getPosition(Session.getUser().getDenomination()));

        //FUNCTIONS
        updateButtonClick();
        deleteButtonClick();
        bookmarksButtonClick();
        homeButtonClick();
        denominationSelect();
        signOutButtonClick();
    }

    /**========================================FILL TEXT BOXES========================================*/
    private void fillInTextBoxes()
    {
        et_fname.setText(Session.getUser().getFirstName());
        et_lname.setText(Session.getUser().getLastName());
        et_city.setText(Session.getUser().getCity());
        et_password.setText(Session.getUser().getPassword());
    }

    /**========================================DENOMINATION SELECT========================================*/
    private void denominationSelect()
    {
        sp_denomination.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id)
            {
                String text = parent.getItemAtPosition(i).toString();
                Toast.makeText(parent.getContext(), text, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    /**========================================UPDATE BUTTON CLICK========================================*/
    private void updateButtonClick()
    {
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Update Account Button Clicked");

                String fname = et_fname.getText().toString();
                String lname = et_lname.getText().toString();
                String city = et_city.getText().toString();
                String password = et_password.getText().toString();
                String denomination = sp_denomination.getSelectedItem().toString();

                if (fname.equals("") || lname.equals("") || city.equals("") || password.equals(""))
                {
                    tv_fieldsError.setVisibility(View.VISIBLE);
                }
                else
                {
                    tv_fieldsError.setVisibility(View.INVISIBLE);
                    //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                    User user = new User(Session.getUser().getEmail(), password, fname, lname, Session.getUser().getEmailOfChurchAttending(), denomination, city);
                    usersDb.updateUser(user); //Update user in database
                    Session.login(user); //Log the user in so Session... works properly
                    startActivity(userNoChurchHomeIntent);
                    Log.v("UPDATED ACCOUNT", "Updated Profile - Moving to UserNoChurchHome");
                }
            }
        });
    }

    /**========================================DELETE BUTTON CLICK========================================*/
    private void deleteButtonClick()
    {
        btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Delete User Account Button Clicked - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "editUserProfileIntent"); //Put extra the name of this intent
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
                Log.v("BUTTON CLICK", "Bookmarks Button Clicked - Moving to BookmarkedChurches");
                startActivity(bookmarksIntent);
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
                Log.v("BUTTON CLICK", "Home Button Clicked - Moving to UserNoChurchHome");
                startActivity(userNoChurchHomeIntent);
            }
        });
    }

    /**========================================SIGN OUT BUTTON CLICk========================================*/
    private void signOutButtonClick()
    {
        btn_signOut.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Signing Out - Moving to MainActivity");
                startActivity(mainActivityIntent);
            }
        });
    }
}