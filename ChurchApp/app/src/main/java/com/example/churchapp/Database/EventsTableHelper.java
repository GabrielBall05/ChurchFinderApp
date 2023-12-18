package com.example.churchapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class EventsTableHelper
{
    Database ctx;

    public EventsTableHelper(Context c)
    {
        ctx = new Database(c);
    }

    public static void create(SQLiteDatabase _db)
    {
        //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, desc
        final String query = "CREATE TABLE " + DatabaseVariables.EVENTS_TABLE + " (eventId INTEGER PRIMARY KEY AUTOINCREMENT, churchHostingEmail TEXT NOT NULL, churchName TEXT NOT NULL, eventName TEXT NOT NULL, address TEXT NOT NULL, date TEXT NOT NULL, time TEXT NOT NULL, desc TEXT NOT NULL, FOREIGN KEY (churchHostingEmail) REFERENCES " + DatabaseVariables.CHURCHES_TABLE + " (email) ON DELETE CASCADE);";
        _db.execSQL(query);
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.EVENTS_TABLE + ";");
    }

    //QUERIES LIKE getEventsByChurch or createEvent, etc
}
