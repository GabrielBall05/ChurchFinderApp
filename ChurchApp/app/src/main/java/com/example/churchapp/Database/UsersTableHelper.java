package com.example.churchapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class UsersTableHelper
{
    Database ctx;

    public UsersTableHelper(Context c)
    {
        ctx = new Database(c);
    }

    public static void create(SQLiteDatabase _db)
    {
        //ORDER: email, password, fname, lname, emailOfChurchAttending, denomination, city
        final String query = "CREATE TABLE " + DatabaseVariables.USERS_TABLE + " (email TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL, firstname TEXT NOT NULL, lastname TEXT NOT NULL, emailOfChurchAttending TEXT, denomination TEXT NOT NULL, city TEXT NOT NULL, FOREIGN KEY (emailOfChurchAttending) REFERENCES " + DatabaseVariables.CHURCHES_TABLE + " (email));";
        _db.execSQL(query);
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.USERS_TABLE + ";");
    }

    //QUERIES LIKE getUserByEmail or createUser, etc
}
