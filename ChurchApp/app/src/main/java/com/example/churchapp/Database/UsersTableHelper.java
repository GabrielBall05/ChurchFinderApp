package com.example.churchapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.churchapp.Models.User;

public class UsersTableHelper
{
    Database ctx;

    public UsersTableHelper(Context c)
    {
        ctx = new Database(c);
    }

    public static void create(SQLiteDatabase _db)
    {
        //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
        final String query = "CREATE TABLE " + DatabaseVariables.USERS_TABLE + " (email TEXT PRIMARY KEY NOT NULL, password TEXT NOT NULL, firstname TEXT NOT NULL, lastname TEXT NOT NULL, emailOfChurchAttending TEXT, denomination TEXT NOT NULL, city TEXT NOT NULL, FOREIGN KEY (emailOfChurchAttending) REFERENCES " + DatabaseVariables.CHURCHES_TABLE + " (email));";
        _db.execSQL(query);
        Log.d("DATABASE", "Created users table");
    }

    public static void clean(SQLiteDatabase _db)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + DatabaseVariables.USERS_TABLE + ";");
    }

    /**========================================CREATE USER========================================*/
    public void createUser(User u)
    {
        //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES ('" + u.getEmail() + "', '" + u.getPassword() + "', '" + u.getFirstName() + "', '" + u.getLastName() + "', '" + u.getEmailOfChurchAttending() + "', '" + u.getDenomination() + "', '" + u.getCity() + "');";
        db.execSQL(query);
        db.close();
    }

    /**========================================UPDATE USER========================================*/
    public void updateUser(User u)
    {
        //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "UPDATE " + DatabaseVariables.USERS_TABLE + " SET password = '" + u.getPassword() + "', firstname = '" + u.getFirstName() + "', lastname = '" + u.getLastName() + "', emailOfChurchAttending = '" + u.getEmailOfChurchAttending() + "', denomination = '" + u.getDenomination() + "', city = '" + u.getCity() + "' WHERE email = '" + u.getEmail() + "';";
        db.execSQL(query);
        db.close();
    }

    /**========================================DELETE USER========================================*/
    public void deleteUser(String e)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        db.execSQL("DELETE FROM " + DatabaseVariables.USERS_TABLE + " WHERE email = '" + e + "';");
    }

    /**========================================GET USER BY EMAIL========================================*/
    @SuppressLint("Range")
    public User getUserByEmail(String e)
    {
        SQLiteDatabase db = ctx.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.USERS_TABLE + " WHERE email = '" + e + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
            String lastname = cursor.getString(cursor.getColumnIndex("lastname"));
            String emailOfChurchAttending = cursor.getString(cursor.getColumnIndex("emailOfChurchAttending"));
            String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
            String city = cursor.getString(cursor.getColumnIndex("city"));

            User userToPass = new User(email, password, firstname, lastname, emailOfChurchAttending, denomination, city);
            db.close();
            return userToPass;
        }
        db.close();
        return null;
    }

    /**========================================SEE IF USER HAS CHURCH (GIVEN USER'S EMAIL)========================================*/
    @SuppressLint("Range")
    public boolean hasChurchFindByUserEmail(String e)
    {
        SQLiteDatabase db = ctx.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.USERS_TABLE + " WHERE email = '" + e + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst())
        {
            String emailOfChurchAttending = cursor.getString(cursor.getColumnIndex("emailOfChurchAttending"));
            if (!emailOfChurchAttending.equals("")) //If it doesn't work, try using .isEmpty()
            {
                return true;
            }
        }
        return false;
    }
}
