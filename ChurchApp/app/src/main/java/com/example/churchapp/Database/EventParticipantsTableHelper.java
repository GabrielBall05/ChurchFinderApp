package com.example.churchapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.churchapp.Models.EventParticipant;

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
        final String query = "CREATE TABLE " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " (eventId INTEGER PRIMARY KEY, emailOfParticipant TEXT NOT NULL, FOREIGN KEY (eventId) REFERENCES " + DatabaseVariables.EVENTS_TABLE + " (eventId) ON DELETE CASCADE, FOREIGN KEY (emailOfParticipant) REFERENCES " + DatabaseVariables.USERS_TABLE + " (email) ON DELETE CASCADE);";
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
    public void deleteEventParticipant(int id)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + " WHERE eventId = '" + id + "';");
        db.close();
    }
}
