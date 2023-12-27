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
        //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
        final String query = "CREATE TABLE " + DatabaseVariables.EVENTS_TABLE + " (eventId INTEGER PRIMARY KEY AUTOINCREMENT, churchHostingEmail TEXT NOT NULL, churchName TEXT NOT NULL, eventName TEXT NOT NULL, address TEXT NOT NULL, date TEXT NOT NULL, time TEXT NOT NULL, description TEXT NOT NULL, FOREIGN KEY (churchHostingEmail) REFERENCES " + DatabaseVariables.CHURCHES_TABLE + " (email) ON DELETE CASCADE);";
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
        //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('" + e.getChurchHostingEmail() + "', '" + e.getChurchName() + "', '" + e.getEventName() + "', '" + e.getAddress() + "', '" + e.getDate() + "', '" + e.getTime() + "', '" + e.getDescription() + "');";
        db.execSQL(query);
        db.close();
    }

    /**========================================UPDATE EVENT========================================*/
    public void updateEvent(Event e)
    {
        //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "UPDATE " + DatabaseVariables.EVENTS_TABLE + " SET churchName = '" + e.getChurchName() + "', eventName = '" + e.getEventName() + "', address = '" + e.getAddress() + "', date = '" + e.getDate() + "', time = '" + e.getTime() + "', description = '" + e.getDescription() + "' WHERE eventId = '" + e.getEventId() + "';";
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

    /**========================================GET EVENT BY ID========================================*/
    @SuppressLint("Range")
    public Event getEventById(int id)
    {
        SQLiteDatabase db = ctx.getReadableDatabase();

        String selectQuery = "SELECT * FROM " +  DatabaseVariables.EVENTS_TABLE + " WHERE eventId = '" + id + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
            int eventId = cursor.getInt(cursor.getColumnIndex("eventId"));
            String churchHostingEmail = cursor.getString(cursor.getColumnIndex("churchHostingEmail"));
            String churchName = cursor.getString(cursor.getColumnIndex("churchName"));
            String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
            String address = cursor.getString(cursor.getColumnIndex("address"));
            String date = cursor.getString(cursor.getColumnIndex("date"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String description = cursor.getString(cursor.getColumnIndex("description"));

            Event event = new Event(eventId, churchHostingEmail, churchName, eventName, address, date, time, description);
            db.close();
            return event;
        }
        db.close();
        return null;
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
                //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
                int eventId = cursor.getInt(cursor.getColumnIndex("eventId"));
                String churchHostingEmail = cursor.getString(cursor.getColumnIndex("churchHostingEmail"));
                String churchName = cursor.getString(cursor.getColumnIndex("churchName"));
                String eventName = cursor.getString(cursor.getColumnIndex("eventName"));
                String address = cursor.getString(cursor.getColumnIndex("address"));
                String date = cursor.getString(cursor.getColumnIndex("date"));
                String time = cursor.getString(cursor.getColumnIndex("time"));
                String description = cursor.getString(cursor.getColumnIndex("description"));

                listOfEvents.add(new Event(eventId, churchHostingEmail, churchName, eventName, address, date, time, description));
            }
            while (cursor.moveToNext());

            db.close();
            return listOfEvents;
        }
        else
        {
            return null;
        }
    }

    /**========================================DOES CHURCH HAVE ANY EVENTS========================================*/
    @SuppressLint("Range")
    public boolean doesChurchHaveEvents(String e)
    {
        ArrayList<Event> listOfEvents = new ArrayList<Event>();
        SQLiteDatabase db = ctx.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.EVENTS_TABLE + " WHERE churchHostingEmail = '" + e + "';";
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

    /**========================================DELETE CHURCH'S EVENTS========================================*/
    public void deleteChurchEvents(String e)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "DELETE FROM " + DatabaseVariables.EVENTS_TABLE + " WHERE churchHostingEmail = '" + e + "';";
        db.execSQL(query);
        db.close();
    }

    /**========================================DUMMY EVENTS========================================*/
    public void dummyEvents()
    {
        //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
        SQLiteDatabase db = ctx.getWritableDatabase();
        db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('bridgepoint@gmail.com', 'BridgePoint Church', 'Pancake Breakfast', '9875 Lewis Ave', '12-27-23', '8am', 'Pancake Breakfast Fundraiser for Youth Group');");
        db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('bridgepoint@gmail.com', 'BridgePoint Church', 'Trivia Night', '9875 Lewis Ave', '01-17-24', '5pm', 'Trivia Night Fundraiser for Youth Group');");
        db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('gracelutheran@gmail.com', 'Grace Lutheran Church', 'Christmas Dinner', '630 N Monroe St', '12-22-23', '6pm', 'Christmas Dinner Fundraiser for Youth Group');");
        db.close();
    }
}
