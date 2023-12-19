package com.example.churchapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.churchapp.ChurchIntents.ChurchHome;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.User;
import com.example.churchapp.Other.CreateAccount;
import com.example.churchapp.Other.CreateChurch;
import com.example.churchapp.Other.Session;
import com.example.churchapp.UserNoChurchIntents.UserNoChurchHome;
import com.example.churchapp.UserWithChurchIntents.UserWithChurchHome;

public class MainActivity extends AppCompatActivity
{
    //GUI
    EditText et_email;
    EditText et_password;
    Button btn_login;
    Button btn_createAccount;
    Button btn_createChurch;
    TextView tv_fieldsError;
    TextView tv_loginError;

    //Database
    ChurchesTableHelper churchesDb;
    UsersTableHelper usersDb;

    //Intents
    Intent userNoChurchHome;
    Intent userWithChurchHome;
    Intent churchHome;
    Intent createAccountIntent;
    Intent createChurchIntent;

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
        tv_fieldsError = findViewById(R.id.tv_loginPage_fieldsError);
        tv_loginError = findViewById(R.id.tv_loginPage_loginError);

        //Database
        churchesDb = new ChurchesTableHelper(this);
        usersDb = new UsersTableHelper(this);

        //Intents
        userNoChurchHome = new Intent(MainActivity.this, UserNoChurchHome.class);
        userWithChurchHome = new Intent(MainActivity.this, UserWithChurchHome.class);
        churchHome = new Intent(MainActivity.this, ChurchHome.class);
        createAccountIntent = new Intent(MainActivity.this, CreateAccount.class);
        createChurchIntent = new Intent(MainActivity.this, CreateChurch.class);


        //FUNCTIONS
        loginButtonClick();
        createAccountButtonClick();
        createChurchButtonClick();
    }

    private void loginButtonClick()
    {
        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("BUTTON PRESS", "Login Button Press (Main Activity)");

                String email = et_email.getText().toString();
                String password = et_password.getText().toString();

                //Check to make sure both fields are filled
                if (email.equals("") || password.equals(""))
                {
                    tv_fieldsError.setVisibility(View.VISIBLE); //Show error
                }
                else
                {
                    tv_fieldsError.setVisibility(View.INVISIBLE);

                    //Fields are filled, move on to getting account type
                    String accountType = getEmailAccountType(email);

                    if (accountType == null) //Account doesn't exist
                    {
                        tv_loginError.setVisibility(View.VISIBLE); //Tell user
                    }
                    else if (accountType.equals(Session.USER_TYPE)) //IF USER
                    {
                        User user = usersDb.getUserByEmail(email); //Get the user
                        if (user.isValidLogin(password)) //Check login validity
                        {
                            Session.login(user);
                            //Determine if user is a member of a church or not
                            if (usersDb.hasChurchFindByUserEmail(user.getEmail())) //Has church
                            {
                                Log.d("BUTTON PRESS", "Login Button - Moving to UserWithChurchHome");
                                startActivity(userWithChurchHome);
                            }
                            else //Doesn't have church
                            {
                                Log.d("BUTTON PRESS", "Login Button - Moving to UserNoChurchHome");
                                startActivity(userNoChurchHome);
                            }
                        }
                    }
                    else if (accountType.equals(Session.CHURCH_TYPE)) //IF CHURCH
                    {
                        Church church = churchesDb.getChurchByEmail(email); //Get the church
                        if (church.isValidLogin(password)) //Check login validity
                        {
                            Session.login(church);
                            Log.d("BUTTON PRESS", "Login Button - Moving to Church Home");
                            startActivity(churchHome);
                        }

                    }
                }
            }
        });
    }

    public String getEmailAccountType(String email)
    {
        if (usersDb.getUserByEmail(email) != null)
        {
            return Session.USER_TYPE;
        }
        else if (churchesDb.getChurchByEmail(email) != null)
        {
            return Session.CHURCH_TYPE;
        }

        return null;
    }

    private void createAccountButtonClick()
    {
        btn_createAccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("BUTTON PRESS", "Create Account Button Press (Main Activity) - Moving to CreateAccount");
                startActivity(createAccountIntent);
            }
        });
    }

    private void createChurchButtonClick()
    {
        btn_createChurch.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Log.d("BUTTON PRESS", "Create Church Button Press (Main Activity) - Moving to CreateChurch");
                startActivity(createChurchIntent);
            }
        });
    }
}