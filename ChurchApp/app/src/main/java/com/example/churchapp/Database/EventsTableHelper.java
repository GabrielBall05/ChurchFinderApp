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
            db.close();
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

    /**========================================GET ALL EVENTS========================================*/
    @SuppressLint("Range")
    public ArrayList<Event> getAllEvents()
    {
        ArrayList<Event> listOfEvents = new ArrayList<Event>();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.EVENTS_TABLE + ";";

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
        }

        db.close();
        return listOfEvents;
    }

    /**========================================DUMMY EVENTS========================================*/
    public void dummyEvents()
    {
        if (getAllEvents().size() == 0)
        {
            //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, description
            SQLiteDatabase db = ctx.getWritableDatabase();
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('bridgepoint@gmail.com', 'BridgePoint Church', 'Pancake Breakfast', '9875 Lewis Ave', '12-27-28', '8am', 'Pancake Breakfast Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('bridgepoint@gmail.com', 'BridgePoint Church', 'Trivia Night', '9875 Lewis Ave', '01-17-28', '5pm', 'Trivia Night Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('gracelutheran@gmail.com', 'Grace Lutheran Church', 'Christmas Dinner', '630 N Monroe St', '12-22-28', '6pm', 'Christmas Dinner Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('gracelutheran@gmail.com', 'Grace Lutheran Church', 'Ice Skating', '800 Woodward Ave', '02-07-28', '3pm', 'Ice Skating with the church. Everyone is invited.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('resonatebaptist@gmail.com', 'Resonate Baptist Church', 'Escape Room', '58 Penn St', '03-12-28', '4pm', 'Escape Room Fun!');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('resonatebaptist@gmail.com', 'Resonate Baptist Church', 'Thanksgiving Dinner', '58 Penn St', '11-22-28', '7pm', 'Thanksgiving Dinner night hosted by the youth group for as a fundraiser.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('trinitylutheran@gmail.com', 'Trinity Lutheran Church', 'Bowling', '501 Scott St', '05-12-28', '9pm', 'Family Bowling Night');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('hosannalutheran@gmail.com', 'Hosanna Lutheran Church', 'Mystery Dinner', '76 South Jackson Ave', '03-23-28', '6pm', 'Mystery Dinner Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('hosannalutheran@gmail.com', 'Hosanna Lutheran Church', 'Thanksgiving Dinner', '76 South Jackson Ave', '11-22-28', '6pm', 'Thanksgiving Dinner night hosted by the youth group for as a fundraiser.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('thecalvarylutheran@gmail.com', 'The Calvary Lutheran Church', 'Bible Jeopardy', '5 Hickory Drive', '07-12-28', '5pm', 'Bible Jeopardy Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('villagebrook@gmail.com', 'Village Brook Church', 'Bowling', '7472 Clay Street', '10-07-28', '10pm', 'Family Bowling Night');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('exaltpeace@gmail.com', 'Exalt Peace Pentecostal Church', 'Roadside Cleanup', '9690 SE Windfall Drive', '04-24-28', '1pm', 'Roadside Cleanup Volunteer Work');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('revolutionofconnection@gmail.com', 'Revolution of Connection Pentecostal Church', 'Picnic', '53 Lakewood Dr', '07-18-28', '12pm', 'Churchwide Picnic Day with Food Trucks');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('revolutionofconnection@gmail.com', 'Revolution of Connection Pentecostal Church', 'Thanksgiving Dinner', '53 Lakewood Dr', '11-22-28', '6pm', 'Thanksgiving Dinner night hosted by the youth group for as a fundraiser.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('harvestbible@gmail.com', 'Harvest Bible Church', 'Talent Show', '7029 Sunnyslope Street', '01-18-28', '6pm', 'Talent Show Event. Anyone can perform.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('encounterriverkingdom@gmail.com', 'Encounter River Kingdom Church', 'Ice Skating', '81 North Shipley St', '01-29-28', '2pm', 'Ice Skating day for the whole church');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('triumphchurch@gmail.com', 'Triumph Church', 'Christmas Dinner', '48 South New Saddle Road', '12-18-28', '6pm', 'Christmas Dinner Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('triumphchurch@gmail.com', 'Triumph Church', 'Thanksgiving Dinner', '48 South New Saddle Road', '11-22-28', '6pm', 'Thanksgiving Dinner night hosted by the youth group for as a fundraiser.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('kingofkings@gmail.com', 'King of Kings Evangelical Church', 'Mystery Dinner', '613 3rd Drive', '09-18-28', '6pm', 'Mystery Dinner Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('saintmarycatholic@gmail.com', 'St Marys Catholic Church', 'Bible Jeopardy', '7819 N. Oxford St', '04-28-28', '5pm', 'Bible Jeopardy Fundraiser for Youth Group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('saintjohncatholic@gmail.com', 'St Johns Catholic Church', 'Bowling', '316 Walt Whitman Ave', '12-22-28', '10pm', 'Bowling until midnight! Everyone is welcome.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('saintpaulcatholic@gmail.com', 'St Pauls Catholic Church', 'Adopt-a-road', '7360 Columbia Ave', '07-04-28', '2pm', 'Adopt-a-road cleanup day for community service.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('saintpaulcatholic@gmail.com', 'St Pauls Catholic Church', 'Thanksgiving Dinner', '7360 Columbia Ave', '11-22-28', '6pm', 'Thanksgiving Dinner night hosted by the youth group for as a fundraiser.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('exaltvineyard@gmail.com', 'Exalt Vineyard Church', 'Picnic', '74 Willow St', '07-29-28', '11am', 'Picnic day with food trucks. Invite your friends!');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('newcovenant@gmail.com', 'New Covenant Church', 'Talent Show', '36 NE. Birchpond Street', '05-30-24', '7pm', 'Talent show!');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('newcovenant@gmail.com', 'New Covenant Church', 'Thanksgiving Dinner', '36 NE. Birchpond Street', '11-22-28', '7pm', 'Thanksgiving Dinner night hosted by the youth group for as a fundraiser.');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('riverwoodrefuge@gmail.com', 'Riverwood Refuge Church', 'Ice Skating', '936 Maiden St', '03-01-28', '1pm', 'Churchwide ice skating event');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('creeksoflife@gmail.com', 'Creeks of Life Church', 'Bible Jeopardy', '7958 Boston St', '05-30-28', '8pm', 'Bible Jeopardy fundraiser for the youth group');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('pentecostlife@gmail.com', 'Pentecost Life Anglican Church', 'Bowling', '28 South Green Lake Ave', '11-20-28', '9pm', 'Bowling night all night');");
            db.execSQL("INSERT INTO " + DatabaseVariables.EVENTS_TABLE + " (churchHostingEmail, churchName, eventName, address, date, time, description) VALUES ('pentecostlife@gmail.com', 'Pentecost Life Anglican Church', 'Thanksgiving Dinner', '28 South Green Lake Ave', '11-22-28', '6pm', 'Thanksgiving Dinner night hosted by the youth group for as a fundraiser.');");
            db.close();
        }
    }
}
