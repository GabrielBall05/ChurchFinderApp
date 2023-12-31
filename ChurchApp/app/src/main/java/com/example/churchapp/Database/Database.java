package com.example.churchapp.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper
{
    public Database(Context ctx)
    {
        super(ctx, DatabaseVariables.DB_NAME, null, DatabaseVariables.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase)
    {
        ChurchesTableHelper.create(sqLiteDatabase);
        UsersTableHelper.create(sqLiteDatabase);
        EventsTableHelper.create(sqLiteDatabase);
        EventParticipantsTableHelper.create(sqLiteDatabase);
        BookmarksTableHelper.create(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        ChurchesTableHelper.clean(sqLiteDatabase);
        UsersTableHelper.clean(sqLiteDatabase);
        EventsTableHelper.clean(sqLiteDatabase);
        EventParticipantsTableHelper.clean(sqLiteDatabase);
        BookmarksTableHelper.clean(sqLiteDatabase);

        //Recall onCreate to remake database
        onCreate(sqLiteDatabase);
    }
}
