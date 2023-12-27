package com.example.churchapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.churchapp.Models.Event;
import com.example.churchapp.Models.EventParticipant;

import java.util.ArrayList;

public class EventParticipantsTableHelper
{
    Database ctx;

    public EventParticipantsTableHelper(Context c)
    {
        ctx = new Database(c);
    }

    public static void create(SQLiteDatabase _db)
    {
        //ORDER: eventId (foreign key) referencing events.eventId, emailOfParticipant (foreign key) referencing users.email
        final String query = "CREATE TABLE " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " (eventId INTEGER, emailOfParticipant TEXT NOT NULL, FOREIGN KEY (eventId) REFERENCES " + DatabaseVariables.EVENTS_TABLE + " (eventId) ON DELETE CASCADE, FOREIGN KEY (emailOfParticipant) REFERENCES " + DatabaseVariables.USERS_TABLE + " (email) ON DELETE CASCADE);";
        _db.execSQL(query);
        Log.d("DATABASE", "Created event participants table");
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + ";");
    }

    /**========================================CREATE EVENT PARTICIPANT========================================*/
    public void createEventParticipant(EventParticipant p)
    {
        //ORDER: eventId, emailOfParticipant
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "INSERT INTO " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " VALUES ('" + p.getEventId() + "', '" + p.getEmailOfParticipant() + "');";
        db.execSQL(query);
        db.close();
    }

    /**========================================DELETE EVENT PARTICIPANT========================================*/
    public void deleteEventParticipant(EventParticipant p)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "DELETE FROM " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " WHERE eventId = '" + p.getEventId() + "' AND emailOfParticipant = '" + p.getEmailOfParticipant() + "';";
        db.execSQL(query);
        db.close();
    }

    /**========================================GET EVENTID'S THAT A USER IS SIGNED UP FOR========================================*/
    @SuppressLint("Range")
    public ArrayList<Integer> getEventIdsThatUserSignedUpFor(String e)
    {
        ArrayList<Integer> listOfEventIds = new ArrayList<Integer>();
        SQLiteDatabase db = ctx.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " WHERE emailOfParticipant = '" + e + "';";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                listOfEventIds.add(cursor.getInt(cursor.getColumnIndex("eventId")));
            }
            while (cursor.moveToNext());
        }
        db.close();
        return listOfEventIds;
    }

    /**========================================GET PARTICIPANTS OF EVENT========================================*/
    @SuppressLint("Range")
    public ArrayList<EventParticipant> getParticipantsOfEvent(int id)
    {
        ArrayList<EventParticipant> listOfParticipants = new ArrayList<EventParticipant>();
        SQLiteDatabase db = ctx.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " WHERE eventId = '" + id + "';";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                int eventID = cursor.getInt(cursor.getColumnIndex("eventId"));
                String email = cursor.getString(cursor.getColumnIndex("emailOfParticipant"));

                listOfParticipants.add(new EventParticipant(eventID, email));
            }
            while(cursor.moveToNext());
        }
        db.close();
        return listOfParticipants;
    }

    /**========================================IS USER SIGNED UP FOR EVENT========================================*/
    public boolean isUserSignedUpForEvent(int id, String e)
    {
        SQLiteDatabase db = ctx.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " WHERE eventId = '" + id + "' AND emailOfParticipant = '" + e + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**========================================DUMMY PARTICIPANTS========================================*/
}
