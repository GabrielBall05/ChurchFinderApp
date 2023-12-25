package com.example.churchapp.UserNoChurchIntents;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.churchapp.Confirmations.MasterConfirmation;
import com.example.churchapp.Database.BookmarksTableHelper;
import com.example.churchapp.Database.UsersTableHelper;
import com.example.churchapp.Models.Bookmark;
import com.example.churchapp.Models.Church;
import com.example.churchapp.Other.Session;
import com.example.churchapp.R;

public class ChurchDetails extends AppCompatActivity
{
    //GUI
    TextView tv_name;
    TextView tv_denomination;
    TextView tv_email;
    TextView tv_address;
    TextView tv_city;
    TextView tv_statement;
    Button btn_becomeMember;
    Button btn_bookmark;
    Button btn_back;

    //DATABASE
    UsersTableHelper usersDb;
    BookmarksTableHelper bookmarksDb;

    //INTENTS
    Intent masterConfirmationIntent;
    Intent userNoChurchHomeIntent;

    //THIS CHURCH
    Church church;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_church_details);

        //GUI
        tv_name = findViewById(R.id.tv_churchDetails_name);
        tv_denomination = findViewById(R.id.tv_churchDetails_denomination);
        tv_email = findViewById(R.id.tv_churchDetails_email);
        tv_address = findViewById(R.id.tv_churchDetails_address);
        tv_city = findViewById(R.id.tv_churchDetails_city);
        tv_statement = findViewById(R.id.tv_churchDetails_statement);
        btn_becomeMember = findViewById(R.id.btn_churchDetails_becomeMember);
        btn_bookmark = findViewById(R.id.btn_churchDetails_bookmark);
        btn_back = findViewById(R.id.btn_churchDetails_back);

        //DATABASE
        usersDb = new UsersTableHelper(this);
        bookmarksDb = new BookmarksTableHelper(this);

        //INTENTS
        masterConfirmationIntent = new Intent(ChurchDetails.this, MasterConfirmation.class);
        userNoChurchHomeIntent = new Intent(ChurchDetails.this, UserNoChurchHome.class);

        //EXTRA
        Intent cameFrom = getIntent();
        church = (Church) cameFrom.getSerializableExtra("thisChurch");

        //FUNCTIONS
        fillTextBoxes();
        becomeMemberButtonClick();
        bookmarkButtonClick();
        backButtonClick();
    }

    /**========================================FILL TEXT BOXES========================================*/
    private void fillTextBoxes()
    {
        tv_name.setText(tv_name.getText() + church.getName());
        tv_denomination.setText(tv_denomination.getText() + church.getDenomination());
        tv_email.setText(tv_email.getText() + church.getEmail());
        tv_address.setText(tv_address.getText() + church.getStreetAddress());
        tv_city.setText(tv_city.getText() + church.getCity());
        tv_statement.setText(tv_statement.getText() + church.getStatementOfFaith());

        //Correct the bookmark button text
        if(bookmarksDb.doesBookmarkExist(church.getEmail(), Session.getUser().getEmail()))
        {
            btn_bookmark.setText("Remove\nBookmark");
        }
        else
        {
            btn_bookmark.setText("Bookmark\nChurch");
        }
    }

    /**========================================BECOME MEMBER BUTTON CLICK========================================*/
    private void becomeMemberButtonClick()
    {
        btn_becomeMember.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.v("BUTTON PRESS", "Become Member button press - are you sure? - Moving to MasterConfirmation");
                masterConfirmationIntent.putExtra("cameFrom", "churchDetailsIntent");
                masterConfirmationIntent.putExtra("thisChurch", church);
                startActivity(masterConfirmationIntent);
            }
        });
    }

    /**========================================BOOKMARK BUTTON CLICK========================================*/
    private void bookmarkButtonClick()
    {
        btn_bookmark.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(bookmarksDb.doesBookmarkExist(church.getEmail(), Session.getUser().getEmail()))
                {
                    Log.v("BUTTON PRESS - UN-BOOKMARKING", "Un/Bookmark button press - UN-BOOKMARKING");
                    bookmarksDb.deleteBookmark(church.getEmail(), Session.getUser().getEmail());
                }
                else
                {
                    Log.v("BUTTON PRESS - BOOKMARKING", "Un/Bookmark button press - BOOKMARKING");
                    Bookmark bookmark = new Bookmark(church.getEmail(), Session.getUser().getEmail());
                    bookmarksDb.createBookmark(bookmark);
                }

                //Correct the bookmark button text
                if(bookmarksDb.doesBookmarkExist(church.getEmail(), Session.getUser().getEmail()))
                {
                    btn_bookmark.setText("Remove\nBookmark");
                }
                else
                {
                    btn_bookmark.setText("Bookmark\nChurch");
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
                Log.v("BUTTON PRESS", "Back button click - moving back to UserNoChurchHome");
                startActivity(userNoChurchHomeIntent);
            }
        });
    }
}