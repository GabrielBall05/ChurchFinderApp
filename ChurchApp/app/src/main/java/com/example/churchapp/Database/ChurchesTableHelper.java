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

    public static void create(SQLiteDatabase _db)
    {
        //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
        final String query = "CREATE TABLE " + DatabaseVariables.CHURCHES_TABLE + " (email TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL, name TEXT NOT NULL, denomination TEXT NOT NULL, statementOfFaith TEXT NOT NULL, streetAddress TEXT NOT NULL, city TEXT NOT NULL, number TEXT NOT NULL);";
        _db.execSQL(query);
        Log.d("DATABASE", "Created churches table");
    }

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

    /**========================================GET CHURCHES BY DENOMINATION========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getChurchesByDenomination(String d)
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE denomination = '" + d + "';";

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

    /**========================================GET CHURCHES BY NAME========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getChurchesByName(String n)
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE name LIKE '%" + n + "%';";

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

    /**========================================GET CHURCHES BY CITY========================================*/
    @SuppressLint("Range")
    public ArrayList<Church> getChurchesByCity(String c)
    {
        ArrayList<Church> listOfChurches = new ArrayList<Church>();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.CHURCHES_TABLE + " WHERE city = '" + c + "';";

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
}
