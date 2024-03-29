package com.example.churchapp.Other;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.churchapp.ChurchIntents.ChurchHome;
import com.example.churchapp.Database.BookmarksTableHelper;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.EventParticipantsTableHelper;
import com.example.churchapp.Database.EventsTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;
import com.example.churchapp.UserIntents.ChurchFinder;
import com.example.churchapp.UserIntents.UserHome;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    //GUI
    EditText et_email;
    EditText et_password;
    Button btn_login;
    Button btn_createAccount;
    Button btn_createChurch;
    TextView tv_loginError;

    //DATABASE
    ChurchesTableHelper churchesDb;
    UsersTableHelper usersDb;
    BookmarksTableHelper bookmarksDb;
    EventsTableHelper eventsDb;
    EventParticipantsTableHelper participantsDb;

    //INTENTS
    Intent userHomeIntent;
    Intent churchHomeIntent;
    Intent createAccountIntent;
    Intent createChurchIntent;

    //ARRAYLISTS FOR TESTING
    ArrayList<User> listOfUsers;
    ArrayList<Church> listOfChurches;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //GUI
        et_email = findViewById(R.id.et_loginPage_email);
        et_password = findViewById(R.id.et_loginPage_password);
        btn_login = findViewById(R.id.btn_loginPage_login);
        btn_createAccount = findViewById(R.id.btn_loginPage_createAccount);
        btn_createChurch = findViewById(R.id.btn_loginPage_createChurch);
        tv_loginError = findViewById(R.id.tv_loginPage_loginError);

        //DATABASE
        churchesDb = new ChurchesTableHelper(this);
        usersDb = new UsersTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);
        eventsDb = new EventsTableHelper(this);
        participantsDb = new EventParticipantsTableHelper(this);

        //INTENTS
        userHomeIntent = new Intent(MainActivity.this, UserHome.class);
        churchHomeIntent = new Intent(MainActivity.this, ChurchHome.class);
        createAccountIntent = new Intent(MainActivity.this, CreateAccount.class);
        createChurchIntent = new Intent(MainActivity.this, CreateChurch.class);

        //INITIALIZE DATABASE TABLES WITH DUMMY DATA
        usersDb.dummyUsers();
        churchesDb.dummyChurches();
        eventsDb.dummyEvents();
        //Can't really do participants, would be difficult since eventId is auto-incrementing


//        //===TESTING===
//        listOfUsers = new ArrayList<User>();
//        listOfChurches = new ArrayList<Church>();
//        listOfUsers = usersDb.getAllUsers();
//        listOfChurches = churchesDb.getAllChurches();
//        logAllUsersAndChurches();
//        //===TESTING===


        //FUNCTIONS
        loginButtonClick();
        createAccountButtonClick();
        createChurchButtonClick();
    }

    /**========================================LOGIN BUTTON CLICK========================================*/
    private void loginButtonClick()
    {
        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.v("BUTTON CLICK", "Login Button Clicked");

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();


                if (email.equals("") || password.equals("")) //Check to make sure both fields are filled
                {
                    tv_loginError.setVisibility(View.VISIBLE); //Show error
                }
                else
                {
                    tv_loginError.setVisibility(View.INVISIBLE);

                    //Fields are filled, move on to getting account type
                    String accountType = getEmailAccountType(email);

                    if (accountType == null) //Account doesn't exist
                    {
                        tv_loginError.setVisibility(View.VISIBLE); //Tell user
                    }
                    else if (accountType.equals(Session.USER_TYPE)) //If user
                    {
                        tv_loginError.setVisibility(View.INVISIBLE);

                        User user = usersDb.getUserByEmail(email); //Get the user from the database
                        if (user.isValidLogin(password)) //Check login validity
                        {
                            Session.login(user); //Log the user in
                            Log.v("LOGGING IN", "Logged in User - Moving to UserHome");
                            Toast.makeText(MainActivity.this, "Logged in", Toast.LENGTH_SHORT).show();
                            startActivity(userHomeIntent);
                        }
                        else
                        {
                            tv_loginError.setVisibility(View.VISIBLE);
                        }
                    }
                    else if (accountType.equals(Session.CHURCH_TYPE)) //If church
                    {
                        tv_loginError.setVisibility(View.INVISIBLE);

                        Church church = churchesDb.getChurchByEmail(email); //Get the church from the database
                        if (church.isValidLogin(password)) //Check login validity
                        {
                            Session.login(church); //Log in church
                            Log.v("LOGGING IN", "Logged in Church - Moving to ChurchHome");
                            startActivity(churchHomeIntent);
                        }
                        else
                        {
                            tv_loginError.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        });
    }

    /**========================================GET THE ACCOUNT TYPE ASSOCIATED WITH THIS EMAIL========================================*/
    public String getEmailAccountType(String email)
    {
        if (usersDb.getUserByEmail(email) != null) //The email exists in the users table
        {
            return Session.USER_TYPE; //It is a user
        }
        else if (churchesDb.getChurchByEmail(email) != null) //The email exists in the churches table
        {
            return Session.CHURCH_TYPE; //It is a church
        }

        return null; //Not in either table
    }

    /**========================================CREATE ACCOUNT BUTTON CLICK========================================*/
    private void createAccountButtonClick()
    {
        btn_createAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.v("BUTTON CLICK", "Create Account Button Clicked - Moving to CreateAccount");
                startActivity(createAccountIntent);
            }
        });
    }

    /**========================================CREATE CHURCH BUTTON CLICK========================================*/
    private void createChurchButtonClick()
    {
        btn_createChurch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.v("BUTTON CLICK", "Create Church Button Clicked - Moving to CreateChurch");
                startActivity(createChurchIntent);
            }
        });
    }



    /**========================================LOG ALL USERS AND CHURCHES FOR TESTING (EMAILS & PASSWORDS)========================================*/
    private void logAllUsersAndChurches()
    {
        Log.i("USERS", "==========ALL USERS==========");
        for (int i = 0; i < listOfUsers.size(); i++)
        {
            //Log.i("User:", "Email: " + listOfUsers.get(i).getEmail() + " - Password: " + listOfUsers.get(i).getPassword());
            Log.i("User:", "Email: " + listOfUsers.get(i).getEmail() + " - Password: " + listOfUsers.get(i).getPassword() + " - Church Attending: " + listOfUsers.get(i).getEmailOfChurchAttending());
        }
        Log.i("CHURCHES", "==========ALL CHURCHES==========");
        for (int i = 0; i < listOfChurches.size(); i++)
        {
            Log.i("Church:", "Email: " + listOfChurches.get(i).getEmail() + " - Password: " + listOfChurches.get(i).getPassword() + " - Name: " + listOfChurches.get(i).getName());
        }
    }
}