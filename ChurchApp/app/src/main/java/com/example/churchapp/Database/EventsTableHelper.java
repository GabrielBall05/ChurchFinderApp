package com.example.churchapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;

import java.util.ArrayList;

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
        Log.d("DATABASE", "Created events table");
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.EVENTS_TABLE + ";");
    }

    /**========================================CREATE EVENT========================================*/
    public void createEvent(Event e)
    {
        //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, desc
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, desc) VALUES ('" + e.getChurchHostingEmail() + "', '" + e.getChurchName() + "', '" + e.getEventName() + "', '" + e.getAddress() + "', '" + e.getDate() + "', '" + e.getTime() + "', '" + e.getDesc() + "');";
        db.execSQL(query);
        db.close();
    }

    /**========================================UPDATE EVENT========================================*/
    public void updateEvent(Event e)
    {
        //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, desc
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "UPDATE " + DatabaseVariables.EVENTS_TABLE + " SET churchName = '" + e.getChurchName() + "', eventName = '" + e.getEventName() + "', address = '" + e.getAddress() + "', date = '" + e.getDate() + "', time = '" + e.getTime() + "', desc = '" + e.getDesc() + "' WHERE email = '" + e.getEventId() + "';";
        db.execSQL(query);
        db.close();
    }

    /**========================================DELETE EVENT========================================*/
    public void deleteEvent(int id)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseVariables.EVENTS_TABLE + " WHERE eventId = '" + id + "';");
        db.close();
    }

    /**========================================GET ALL EVENTS BY CHURCH'S EMAIL========================================*/
    @SuppressLint("Range")
    public ArrayList<Event> getAllEventsByChurchEmail(String e)
    {
        ArrayList<Event> listOfEvents = new ArrayList<Event>();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.EVENTS_TABLE + " WHERE churchHostingEmail = '" + e + "';";

        SQLiteDatabase db = ctx.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, desc
                int eventId = cursor.getInt(cursor.getColumnIndex("eventId"));
                String churchHostingEmail = cursor.getString(cursor.getColumnIndex("churchHostingEmail"));
                String churchName = cursor.getString(cursor.getColumnIndex("churchName"));
                String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String desc = cursor.getString(cursor.getColumnIndex("desc"));

                listOfEvents.add(new Event(eventId, churchHostingEmail, churchName, eventName, address, date, time, desc));
            }
            while (cursor.moveToNext());
        }

        db.close();
        return listOfEvents;
    }
}
