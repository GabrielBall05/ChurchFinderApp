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

import com.example.churchapp.ChurchIntents.EditChurchProfile;
import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.User;
import com.example.churchapp.R;
import com.example.churchapp.UserIntents.EditUserProfile;

public class ChangePassword extends AppCompatActivity
{
    //GUI
    EditText et_oldPass;
    EditText et_newPass;
    EditText et_confirmPass;
    Button btn_update;
    Button btn_back;
    TextView tv_error;

    //INTENTS
    Intent editUserProfileIntent;
    Intent editChurchProfileIntent;

    //DATABASE
    UsersTableHelper usersDb;
    ChurchesTableHelper churchesDb;

    //EXTRA
    String cameFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        //GUI
        et_oldPass = findViewById(R.id.et_changePassword_oldPassword);
        et_newPass = findViewById(R.id.et_changePassword_newPassword);
        et_confirmPass = findViewById(R.id.et_changePassword_confirmPassword);
        btn_update = findViewById(R.id.btn_changePassword_update);
        btn_back = findViewById(R.id.btn_changePassword_back);
        tv_error = findViewById(R.id.tv_changePassword_error);
        tv_error.setVisibility(View.INVISIBLE);

        //INTENTS
        editChurchProfileIntent = new Intent(ChangePassword.this, EditChurchProfile.class);
        editUserProfileIntent = new Intent(ChangePassword.this, EditUserProfile.class);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        churchesDb = new ChurchesTableHelper(this);

        //EXTRA
        Intent origin = getIntent();
        cameFrom = origin.getStringExtra("cameFrom");

        //FUNCTIONS
        backButtonClick();
        updateButtonClick();
    }

    /**========================================UPDATE BUTTON CLICK========================================*/
    private void updateButtonClick()
    {
        btn_update.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON CLICK", "Update Button Click");
                tv_error.setVisibility(View.INVISIBLE);

                String oldPass = et_oldPass.getText().toString();
                String newPass = et_newPass.getText().toString();
                String confirmPass = et_confirmPass.getText().toString();

                if (oldPass.equals("") || newPass.equals("") || confirmPass.equals(""))
                {
                    tv_error.setText("*Please fill out all fields*");
                    tv_error.setVisibility(View.VISIBLE);
                }
                else
                {
                    tv_error.setVisibility(View.INVISIBLE);

                    if(cameFrom.equals("editChurchProfileIntent"))
                    {
                        if (oldPass.equals(Session.getChurch().getPassword()))
                        {
                            //check confirm
                            if (newPass.equals(confirmPass))
                            {

                                Church church = new Church(Session.getChurch().getEmail(), newPass, Session.getChurch().getName(), Session.getChurch().getDenomination(), Session.getChurch().getStatementOfFaith(), Session.getChurch().getStreetAddress(), Session.getChurch().getCity(), Session.getChurch().getNumber());
                                churchesDb.updateChurch(church);
                                Session.login(church);
                                Log.v("BUTTON CLICK", "Update Button Click - Moving to EditChurchProfile");
                                Toast.makeText(ChangePassword.this, "Updated Password", Toast.LENGTH_SHORT).show();
                                startActivity(editChurchProfileIntent);
                            }
                            else
                            {
                                tv_error.setText("*New & Confirm Passwords do not match*");
                                tv_error.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            tv_error.setText("*Old Password not correct*");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    }
                    else if (cameFrom.equals("editUserProfileIntent"))
                    {
                        if (oldPass.equals(Session.getUser().getPassword()))
                        {
                            //check confirm
                            if (newPass.equals(confirmPass))
                            {
                                User user = new User(Session.getUser().getEmail(), newPass, Session.getUser().getFirstName(), Session.getUser().getLastName(), Session.getUser().getEmailOfChurchAttending(), Session.getUser().getDenomination(), Session.getUser().getCity());
                                usersDb.updateUser(user);
                                Session.login(user);
                                Log.v("BUTTON CLICK", "Update Button Click - Moving to EditUserProfile");
                                Toast.makeText(ChangePassword.this, "Updated Password", Toast.LENGTH_SHORT).show();
                                startActivity(editUserProfileIntent);
                            }
                            else
                            {
                                tv_error.setText("*New * Confirm Passwords do not match*");
                                tv_error.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            tv_error.setText("*Old Password not correct*");
                            tv_error.setVisibility(View.VISIBLE);
                        }
                    }
                }
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
                if(cameFrom.equals("editChurchProfileIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Click - Moving to EditChurchProfile");
                    startActivity(editChurchProfileIntent);
                }
                else if (cameFrom.equals("editUserProfileIntent"))
                {
                    Log.v("BUTTON CLICK", "Back Button Click - Moving to EditUserProfile");
                    startActivity(editUserProfileIntent);
                }
            }
        });
    }
}