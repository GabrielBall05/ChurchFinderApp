package com.example.churchapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class ChurchesTableHelper
{
    Database db;

    public ChurchesTableHelper(Context ctx)
    {
        db = new Database(ctx);
    }

    public static void create(SQLiteDatabase _db)
    {
        //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city
        final String query = "CREATE TABLE " + DatabaseVariables.CHURCHES_TABLE + " (email TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL, name TEXT NOT NULL, denomination TEXT NOT NULL, statementOfFaith TEXT NOT NULL, streetAddress TEXT NOT NULL, city TEXT NOT NULL);";
        _db.execSQL(query);
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.CHURCHES_TABLE + ";");
    }

    //QUERIES LIKE createChurch, etc
}
