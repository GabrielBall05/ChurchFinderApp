package com.example.churchapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.churchapp.Models.Bookmark;
import com.example.churchapp.Models.Church;

import java.util.ArrayList;

public class BookmarksTableHelper
{
    Database ctx;

    public BookmarksTableHelper(Context c)
    {
        ctx = new Database(c);
    }

    public static void create(SQLiteDatabase _db)
    {
        //Order: emailOfUser, emailOfChurch
        final String query = "CREATE TABLE " + DatabaseVariables.BOOKMARKS_TABLE + " (emailOfUser TEXT NOT NULL, emailOfChurch TEXT NOT NULL, FOREIGN KEY (emailOfUser) REFERENCES " + DatabaseVariables.USERS_TABLE + " (email) ON DELETE CASCADE, FOREIGN KEY (emailOfChurch) REFERENCES " + DatabaseVariables.CHURCHES_TABLE + " (email) ON DELETE CASCADE);";
        _db.execSQL(query);
        Log.d("DATABASE", "Created bookmarks table");
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.BOOKMARKS_TABLE + ";");
    }

    /**========================================CREATE BOOKMARK========================================*/
    public void createBookmark(Bookmark b)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "INSERT INTO " + DatabaseVariables.BOOKMARKS_TABLE + " VALUES ('" + b.getEmailOfUser() + "', '" + b.getEmailOfChurch() + "');";
        db.execSQL(query);
        db.close();
    }

    /**========================================DELETE BOOKMARK========================================*/
    public void deleteBookmark(String churchEmail, String userEmail)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseVariables.BOOKMARKS_TABLE + " WHERE emailOfChurch = '" + churchEmail + "' AND emailOfUser = '" + userEmail + "';");
        db.close();
    }

    /**========================================DELETE ALL BOOKMARKS========================================*/
    public void deleteAllBookmarks()
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseVariables.BOOKMARKS_TABLE + ";");
        db.close();
    }

    /**========================================GET ALL BOOKMARKS BY USER'S EMAIL========================================*/
    @SuppressLint("Range")
    public ArrayList<Bookmark> getAllBookmarksUnderUser(String e)
    {
        ArrayList<Bookmark> listOfBookmarks = new ArrayList<Bookmark>();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.BOOKMARKS_TABLE + " WHERE emailOfUser = '" + e + "';";

        SQLiteDatabase db = ctx.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //Order: emailOfUser, emailOfChurch
                String emailOfUser = cursor.getString(cursor.getColumnIndex("emailOfUser"));
                String emailOfChurch = cursor.getString(cursor.getColumnIndex("emailOfChurch"));

                listOfBookmarks.add(new Bookmark(emailOfUser, emailOfChurch));
            }
            while (cursor.moveToNext());
        }

        db.close();
        return listOfBookmarks;
    }

    /**========================================DOES BOOKMARK EXIST========================================*/
    public boolean doesBookmarkExist(String churchEmail, String userEmail) //Given church's email and user's email
    {
        SQLiteDatabase db = ctx.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.BOOKMARKS_TABLE + " WHERE emailOfChurch = '" + churchEmail + "' AND emailOfUser = '" + userEmail + "';";
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

    /**========================================DELETE USER BOOKMARKS (CALLED WHEN USER DELETES ACCOUNT)========================================*/
    public void deleteUserBookmarks(String userEmail)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "DELETE FROM " + DatabaseVariables.BOOKMARKS_TABLE + " WHERE emailOfUser = '" + userEmail + "';";
        db.execSQL(query);
        db.close();
    }

    /**========================================DELETE CHURCH BOOKMARKS (CALLED WHEN CHURCH DELETES ACCOUNT)========================================*/
    public void deleteChurchBookmarks(String churchEmail)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "DELETE FROM " + DatabaseVariables.BOOKMARKS_TABLE + " WHERE emailOfChurch = '" + churchEmail + "';";
        db.execSQL(query);
        db.close();
    }

}