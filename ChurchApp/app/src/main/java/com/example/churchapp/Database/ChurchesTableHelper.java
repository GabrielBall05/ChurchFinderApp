package com.example.churchapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.User;

import java.util.ArrayList;

public class ChurchesTableHelper
{
    Database ctx;

    public ChurchesTableHelper(Context c)
    {
        ctx = new Database(c);
    }

    /**========================================CREATE TABLE========================================*/
    public static void create(SQLiteDatabase _db)
    {
        //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
        final String query = "CREATE TABLE " + DatabaseVariables.CHURCHES_TABLE + " (email TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL, name TEXT NOT NULL, denomination TEXT NOT NULL, statementOfFaith TEXT NOT NULL, streetAddress TEXT NOT NULL, city TEXT NOT NULL, number TEXT NOT NULL);";
        _db.execSQL(query);
        Log.d("DATABASE", "Created Churches Table");
    }

    /**========================================DROP TABLE========================================*/
    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.CHURCHES_TABLE + ";");
    }

    /**========================================CREATE CHURCH========================================*/
    public void createChurch(Church c)
    {
        //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES ('" + c.getEmail() + "', '" + c.getPassword() + "', '" + c.getName() + "', '" + c.getDenomination() + "', '" + c.getStatementOfFaith() + "', '" + c.getStreetAddress() + "', '" + c.getCity() + "', '" + c.getNumber() + "');";
        db.execSQL(query);
        db.close();
    }

    /**========================================UPDATE CHURCH========================================*/
    public void updateChurch(Church c)
    {
        //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "UPDATE " + DatabaseVariables.CHURCHES_TABLE + " SET password = '" + c.getPassword() + "', name = '" + c.getName() + "', denomination = '" + c.getDenomination() + "', statementOfFaith = '" + c.getStatementOfFaith() + "', streetAddress = '" + c.getStreetAddress() + "', city = '" + c.getCity() + "', number = '" + c.getNumber() + "' WHERE email = '" + c.getEmail() + "';";
        db.execSQL(query);
        db.close();
    }

    /**========================================DELETE CHURCH========================================*/
    public void deleteChurch(String e)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE email = '" + e + "';");
        db.close();
    }

    /**========================================GET CHURCH BY EMAIL========================================*/
    @SuppressLint("Range")
    public Church getChurchByEmail(String e)
    {
        SQLiteDatabase db = ctx.getReadableDatabase();
        String selectQuery = "SELECT * FROM " +  DatabaseVariables.CHURCHES_TABLE + " WHERE email = '" + e + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
            String statement = cursor.getString(cursor.getColumnIndex("statementOfFaith"));
            String streetAddress = cursor.getString(cursor.getColumnIndex("streetAddress"));
            String city = cursor.getString(cursor.getColumnIndex("city"));
            String number = cursor.getString(cursor.getColumnIndex("number"));

            Church churchToPass = new Church(email, password, name, denomination, statement, streetAddress, city, number);
            db.close();
            return churchToPass;
        }
        db.close();
        return null;
    }

    /**========================================GET CHURCHES BY DENOMINATION (ALPHABETICAL)========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getChurchesByDenominationAlphabeticalByName(String d)
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE denomination = '" + d + "' ORDER BY name ASC;";
        SQLiteDatabase db = ctx.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
                String statement = cursor.getString(cursor.getColumnIndex("statementOfFaith"));
                String streetAddress = cursor.getString(cursor.getColumnIndex("streetAddress"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String number = cursor.getString(cursor.getColumnIndex("number"));

                listOfChurches.add(new Church(email, password, name, denomination, statement, streetAddress, city, number));
            }
            while (cursor.moveToNext());
        }

        db.close();
        return listOfChurches;
    }

    /**========================================GET CHURCHES BY NAME (ALPHABETICAL)========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getChurchesByNameAlphabetical(String n)
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE name LIKE '%" + n + "%' ORDER BY name ASC;";
        SQLiteDatabase db = ctx.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
                String statement = cursor.getString(cursor.getColumnIndex("statementOfFaith"));
                String streetAddress = cursor.getString(cursor.getColumnIndex("streetAddress"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String number = cursor.getString(cursor.getColumnIndex("number"));

                listOfChurches.add(new Church(email, password, name, denomination, statement, streetAddress, city, number));
            }
            while (cursor.moveToNext());
        }

        db.close();
        return listOfChurches;
    }

    /**========================================GET CHURCHES BY CITY (ALPHABETICAL)========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getChurchesByCityAlphabetical(String c)
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE city LIKE '%" + c + "%' ORDER BY name ASC;";
        SQLiteDatabase db = ctx.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
                String statement = cursor.getString(cursor.getColumnIndex("statementOfFaith"));
                String streetAddress = cursor.getString(cursor.getColumnIndex("streetAddress"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String number = cursor.getString(cursor.getColumnIndex("number"));

                listOfChurches.add(new Church(email, password, name, denomination, statement, streetAddress, city, number));
            }
            while (cursor.moveToNext());
        }

        db.close();
        return listOfChurches;
    }

    /**========================================GET ALL CHURCHES (ALPHABETICAL)========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getAllChurchesAlphabetical()
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();
        String query = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + " ORDER BY name ASC;";
        SQLiteDatabase db = ctx.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
                String statement = cursor.getString(cursor.getColumnIndex("statementOfFaith"));
                String streetAddress = cursor.getString(cursor.getColumnIndex("streetAddress"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String number = cursor.getString(cursor.getColumnIndex("number"));

                listOfChurches.add(new Church(email, password, name, denomination, statement, streetAddress, city, number));
            }
            while (cursor.moveToNext());
        }

        db.close();
        return listOfChurches;
    }

    /**========================================GET ALL CHURCHES========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getAllChurches()
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + ";";
        SQLiteDatabase db = ctx.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
                String statement = cursor.getString(cursor.getColumnIndex("statementOfFaith"));
                String streetAddress = cursor.getString(cursor.getColumnIndex("streetAddress"));
                String city = cursor.getString(cursor.getColumnIndex("city"));
                String number = cursor.getString(cursor.getColumnIndex("number"));

                listOfChurches.add(new Church(email, password, name, denomination, statement, streetAddress, city, number));
            }
            while (cursor.moveToNext());
        }

        db.close();
        return listOfChurches;
    }

    /**========================================DOES EMAIL EXIST========================================*/
    public boolean doesEmailExist(String e)
    {
        SQLiteDatabase db = ctx.getReadableDatabase();
        String selectQuery = "SELECT email FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE email = '" + e + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() == 0) //Email doesn't exist
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**========================================INITIALIZE CHURCHES TABLE========================================*/
    public void dummyChurches()
    {
        if (getAllChurches().size() == 0)
        {
            SQLiteDatabase db = ctx.getWritableDatabase();
            //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('bridgepoint@gmail.com', 'password', 'BridgePoint Church', 'Baptist', 'We belive Jesus is God', '9875 Lewis Ave', 'Temperance', '123-456-7890');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('gracelutheran@gmail.com', 'password', 'Grace Lutheran Church', 'Lutheran', 'We believe in the Bible', '630 N Monroe St', 'Monroe', '987-654-3210');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('resonatebaptist@gmail.com', 'password', 'Resonate Baptist Church', 'Baptist', 'We belive Jesus is God', '51 Penn St', 'Toledo', '579-834-5347');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('blessrefuge@gmail.com', 'password', 'Bless Refuge Baptist Church', 'Baptist', 'We belive Jesus is God', '44 W Sage Street', 'Sandusky', '787-235-8690');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('trinitylutheran@gmail.com', 'password', 'Trinity Lutheran Church', 'Lutheran', 'We believe in the Bible', '501 Scott St', 'Denver', '125-412-5235');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('hosannalutheran@gmail.com', 'password', 'Hosanna Lutheran Church', 'Lutheran', 'We believe in the Bible', '76 South Jackson Ave', 'Chicago', '859-923-2346');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('thecalvarylutheran@gmail.com', 'password', 'The Calvary Lutheran Church', 'Lutheran', 'We believe in the Bible', '5 Hickory Drive', 'Boston', '983-873-4553');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('villagebrook@gmail.com', 'password', 'Village Brook Church', 'Pentecostal', 'We believe in the Bible', '7472 Clay Street', 'Nashville', '142-445-6349');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('exaltpeace@gmail.com', 'password', 'Exalt Peace Pentecostal Church', 'Pentecostal', 'We believe in the Bible', '9690 SE Windfall Drive', 'San Diego', '565-756-4781');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('revolutionofconnection@gmail.com', 'password', 'Revolution of Connection Pentecostal Church', 'Pentecostal', 'We believe in the Bible', '53 Lakewood Dr', 'Kansas City', '275-583-5474');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('harvestbible@gmail.com', 'password', 'Harvest Bible Church', 'Methodist', 'We believe in the Bible', '7029 Sunnyslope Street', 'Temperance', '973-852-2357');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('encounterriverkingdom@gmail.com', 'password', 'Encounter River Kingdom Church', 'Methodist', 'We believe in the Bible', '81 North Shipley St', 'Monroe', '984-919-1361');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('triumphchurch@gmail.com', 'password', 'Triumph Church', 'Evangelical', 'We believe in the Bible', '48 South New Saddle Road', 'Toledo', '683-324-4580');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('kingofkings@gmail.com', 'password', 'King of Kings Evangelical Church', 'Evangelical', 'We believe in the Bible', '613 3rd Drive', 'Sandusky', '856-842-1241');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('saintmarycatholic@gmail.com', 'password', 'St Marys Catholic Church', 'Catholic', 'We believe in the Bible', '7819 N. Oxford St', 'Denver', '834-2453-4458');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('saintjohncatholic@gmail.com', 'password', 'St Johns Catholic Church', 'Catholic', 'We believe in the Bible', '316 Walt Whitman Ave', 'Chicago', '849-8411-2435');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('saintpaulcatholic@gmail.com', 'password', 'St Pauls Catholic Church', 'Catholic', 'We believe in the Bible', '7360 Columbia Ave', 'Boston', '775-124-9128');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('exaltvineyard@gmail.com', 'password', 'Exalt Vineyard Church', 'Non Denominational', 'We believe in the Bible', '74 Willow St', 'Nashville', '423-422-1985');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('newcovenant@gmail.com', 'password', 'New Covenant Church', 'Non Denominational', 'We believe in the Bible', '36 NE. Birchpond Street', 'San Diego', '857-442-9890');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('riverwoodrefuge@gmail.com', 'password', 'Riverwood Refuge Church', 'Non Denominational', 'We believe in the Bible', '936 Maiden St', 'Kansas City', '735-748-8981');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('creeksoflife@gmail.com', 'password', 'Creeks of Life Church', 'Anglican', 'We believe in the Bible', '7958 Boston St', 'Temperance', '245-682-5829');");
            db.execSQL("INSERT INTO " + DatabaseVariables.CHURCHES_TABLE + " VALUES('pentecostlife@gmail.com', 'password', 'Pentecost Life Anglican Church', 'Anglican', 'We believe in the Bible', '28 South Green Lake Ave', 'Monroe', '275-583-5474');");
            db.close();
        }
    }
}
