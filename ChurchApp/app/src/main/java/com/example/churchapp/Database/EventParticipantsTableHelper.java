package com.example.churchapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.EVENT_PARTICIPANTS_TABLE + ";");
    }

    //QUERIES
}
