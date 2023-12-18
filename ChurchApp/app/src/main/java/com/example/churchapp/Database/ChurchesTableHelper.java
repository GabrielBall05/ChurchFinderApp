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
        //QUERY TO CREATE TABLE
        //_db.execSQL(query);
    }

    public static void clean(SQLiteDatabase _db)
    {
        //QUERY TO DROP TABLE
        //_db.execSQL(query);
    }

    //QUERIES LIKE createChurch, etc
}
