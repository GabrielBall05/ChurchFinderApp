package com.example.churchapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.churchapp.Models.User;

import java.util.ArrayList;

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

    /**========================================UPDATE USER'S CHURCH ATTENDING (GIVEN USER'S EMAIL & CHURCH EMAIL or "")========================================*/
    public void becomeMemberOrLeaveChurch(String userEmail, String churchEmail)
    {
        SQLiteDatabase db = ctx.getWritableDatabase();
        String query = "UPDATE " + DatabaseVariables.USERS_TABLE + " SET emailOfChurchAttending = '" + churchEmail + "' WHERE email = '" + userEmail + "';";
        db.execSQL(query);
        db.close();
    }

    /**========================================SEE IF USER HAS CHURCH (GIVEN USER'S EMAIL)========================================*/
    @SuppressLint("Range")
    public ArrayList<User> getAllUsersAttendingChurch(String e)
    {
        ArrayList<User> listOfUsersAttendingThisChurch = new ArrayList<User>();
        SQLiteDatabase db = ctx.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + DatabaseVariables.USERS_TABLE + " WHERE emailOfChurchAttending = '" + e + "';";
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String lastname = cursor.getString(cursor.getColumnIndex("lastname"));
                String emailOfChurchAttending = cursor.getString(cursor.getColumnIndex("emailOfChurchAttending"));
                String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
                String city = cursor.getString(cursor.getColumnIndex("city"));

                listOfUsersAttendingThisChurch.add(new User(email, password, firstname, lastname, emailOfChurchAttending, denomination, city));
            }
            while (cursor.moveToNext());
        }
        db.close();
        return listOfUsersAttendingThisChurch;
    }

    /**========================================SEE IF USER HAS CHURCH (GIVEN USER'S EMAIL)========================================*/
    @SuppressLint("Range")
    public boolean doesUserHaveChurch(String e)
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
    /**========================================GET ALL USERS========================================*/
    @SuppressLint("Range")
    public ArrayList<User> getAllUsers()
    {
        ArrayList<User> listOfUsers = new ArrayList<User>();

        String selectQuery = "SELECT * FROM " + DatabaseVariables.USERS_TABLE + ";";

        SQLiteDatabase db = ctx.getReadableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst())
        {
            do
            {
                //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String password = cursor.getString(cursor.getColumnIndex("password"));
                String firstname = cursor.getString(cursor.getColumnIndex("firstname"));
                String lastname = cursor.getString(cursor.getColumnIndex("lastname"));
                String emailOfChurchAttending = cursor.getString(cursor.getColumnIndex("emailOfChurchAttending"));
                String denomination = cursor.getString(cursor.getColumnIndex("denomination"));
                String city = cursor.getString(cursor.getColumnIndex("city"));

                listOfUsers.add(new User(email, password, firstname, lastname, emailOfChurchAttending, denomination, city));
            }
            while (cursor.moveToNext());
        }
        db.close();
        return listOfUsers;
    }


    /**========================================DOES EMAIL EXIST========================================*/
    public boolean doesEmailExist(String e)
    {
        SQLiteDatabase db = ctx.getReadableDatabase();

        String selectQuery = "SELECT email FROM " + DatabaseVariables.USERS_TABLE + " WHERE email = '" + e + "';";

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() == 0)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    /**========================================INITIALIZE USERS TABLE========================================*/
    public void dummyUsers()
    {
        if (getAllUsers().size() == 0)
        {
            SQLiteDatabase db = ctx.getWritableDatabase();
            //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('ryandelarosa@gmail.com', 'password', 'Ryan', 'Delarosa', 'exaltvineyard@gmail.com', 'Non Denominational', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('iylamontgomery@gmail.com', 'password', 'Iyla', 'Montgomery', 'gracelutheran@gmail.com', 'Lutheran', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('maximilianoahmed@gmail.com', 'password', 'Maximiliano', 'Ahmed', 'saintmarycatholic@gmail.com', 'Catholic', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('joliewilliamson@gmail.com', 'password', 'Jolie', 'Williamson', 'harvestbible@gmail.com', 'Methodist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('emersonroberts@gmail.com', 'password', 'Emerson', 'Roberts', 'triumphchurch@gmail.com', 'Evangelical', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('paisleypacheco@gmail.com', 'password', 'Paisley', 'Pacheco', 'creeksoflife@gmail.com', 'Anglican', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('erikarmstrong@gmail.com', 'password', 'Erik', 'Armstrong', 'villagebrook@gmail.com', 'Pentecostal', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('presleyenriquez@gmail.com', 'password', 'Presley', 'Enriquez', 'bridgepoint@gmail.com', 'Baptist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('elishaarroyo@gmail.com', 'password', 'Elisha', 'Arroyo', 'exaltvineyard@gmail.com', 'Non Denominational', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('giacaldwell@gmail.com', 'password', 'Gia', 'Caldwell', 'gracelutheran@gmail.com', 'Lutheran', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('kyrawilkins@gmail.com', 'password', 'Kyra', 'Wilkins', 'saintmarycatholic@gmail.com', 'Catholic', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('yusufwright@gmail.com', 'password', 'Yusuf', 'Wright', 'harvestbible@gmail.com', 'Methodist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('lilymullen@gmail.com', 'password', 'Lily', 'Mullen', 'triumphchurch@gmail.com', 'Evangelical', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('shepardfranco@gmail.com', 'password', 'Shepard', 'Franco', 'creeksoflife@gmail.com', 'Anglican', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('charleighyoder@gmail.com', 'password', 'Charleigh', 'Yoder', 'villagebrook@gmail.com', 'Pentecostal', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('johanmoss@gmail.com', 'password', 'Johan', 'Moss', 'bridgepoint@gmail.com', 'Baptist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('biancameza@gmail.com', 'password', 'Bianca', 'Meza', 'exaltvineyard@gmail.com', 'Non Denominational', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('lucianjohnson@gmail.com', 'password', 'Lucian', 'Johnson', 'gracelutheran@gmail.com', 'Lutheran', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('emmahansen@gmail.com', 'password', 'Emma', 'Hansen', 'saintmarycatholic@gmail.com', 'Catholic', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('charliejones@gmail.com', 'password', 'Charlie', 'Jones', 'harvestbible@gmail.com', 'Methodist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('sophiapope@gmail.com', 'password', 'Sophia', 'Pope', 'triumphchurch@gmail.com', 'Evangelical', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('gunnarsharp@gmail.com', 'password', 'Gunnar', 'Sharp', 'creeksoflife@gmail.com', 'Anglican', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('camrynmcbride@gmail.com', 'password', 'Camryn', 'McBride', 'villagebrook@gmail.com', 'Pentecostal', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('denverpacheco@gmail.com', 'password', 'Denver', 'Pacheco', 'bridgepoint@gmail.com', 'Baptist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('parisbernard@gmail.com', 'password', 'Paris', 'Bernard', 'newcovenant@gmail.com', 'Non Denominational', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('jairhancock@gmail.com', 'password', 'Jair', 'Hancock', 'trinitylutheran@gmail.com', 'Lutheran', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('katelynherrera@gmail.com', 'password', 'Katelyn', 'Herrera', 'saintjohncatholic@gmail.com', 'Catholic', 'Chicago');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('riverkoch@gmail.com', 'password', 'River', 'Koch', 'harvestbible@gmail.com', 'Methodist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('milanahaynes@gmail.com', 'password', 'Milana', 'Haynes', 'triumphchurch@gmail.com', 'Evangelical', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('kasonstewart@gmail.com', 'password', 'Kason', 'Stewart', 'creeksoflife@gmail.com', 'Anglican', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('mayablack@gmail.com', 'password', 'Maya', 'Black', 'exaltpeace@gmail.com', 'Pentecostal', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('matteobrewer@gmail.com', 'password', 'Matteo', 'Brewer', 'blessrefuge@gmail.com', 'Baptist', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('theajarvis@gmail.com', 'password', 'Thea', 'Jarvis', 'newcovenant@gmail.com', 'Non Denominational', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('marlonrush@gmail.com', 'password', 'Marlon', 'Rush', 'trinitylutheran@gmail.com', 'Lutheran', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('maleahwheeler@gmail.com', 'password', 'Maleah', 'Wheeler', 'saintjohncatholic@gmail.com', 'Catholic', 'Chicago');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('kennethhoover@gmail.com', 'password', 'Kenneth', 'Hoover', 'harvestbible@gmail.com', 'Methodist', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('virginiastark@gmail.com', 'password', 'Virginia', 'Stark', 'triumphchurch@gmail.com', 'Evangelical', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('kristopherdaniel@gmail.com', 'password', 'Kristopher', 'Daniel', 'creeksoflife@gmail.com', 'Anglican', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('joywinters@gmail.com', 'password', 'Joy', 'Winters', 'exaltpeace@gmail.com', 'Pentecostal', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('deandrelang@gmail.com', 'password', 'Deandre', 'Lang', 'blessrefuge@gmail.com', 'Baptist', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('amirahbranch@gmail.com', 'password', 'Amirah', 'Branch', 'newcovenant@gmail.com', 'Non Denominational', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('keenantorres@gmail.com', 'password', 'Keenan', 'Torres', 'trinitylutheran@gmail.com', 'Lutheran', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('violetpayne@gmail.com', 'password', 'Violet', 'Payne', 'saintjohncatholic@gmail.com', 'Catholic', 'Chicago');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('edwardmeyers@gmail.com', 'password', 'Edward', 'Meyers', 'encounterriverkingdom@gmail.com', 'Methodist', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('leylaklein@gmail.com', 'password', 'Leyla', 'Klein', 'kingofkings@gmail.com', 'Evangelical', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('marcocurry@gmail.com', 'password', 'Marco', 'Curry', 'pentecostlife@gmail.com', 'Anglican', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('alisonsosa@gmail.com', 'password', 'Alison', 'Sosa', 'exaltpeace@gmail.com', 'Pentecostal', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('emirvaughan@gmail.com', 'password', 'Emir', 'Vaughan', 'blessrefuge@gmail.com', 'Baptist', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('nancycalderon@gmail.com', 'password', 'Nancy', 'Calderon', 'riverwoodrefuge@gmail.com', 'Non Denominational', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('oakleyfoley@gmail.com', 'password', 'Oakley', 'Foley', 'hosannalutheran@gmail.com', 'Lutheran', 'Chicago');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('zayleeavalos@gmail.com', 'password', 'Zaylee', 'Avalos', 'saintpaulcatholic@gmail.com', 'Catholic', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('coenrobles@gmail.com', 'password', 'Coen', 'Robles', 'encounterriverkingdom@gmail.com', 'Methodist', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('felicityharrington@gmail.com', 'password', 'Felicity', 'Harrington', 'kingofkings@gmail.com', 'Evangelical', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('omaribrock@gmail.com', 'password', 'Omari', 'Brock', 'pentecostlife@gmail.com', 'Anglican', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('jadawiggins@gmail.com', 'password', 'Jada', 'Wiggins', 'revolutionofconnection@gmail.com', 'Pentecostal', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('azariahhuang@gmail.com', 'password', 'Azariah', 'Huang', 'resonatebaptist@gmail.com', 'Baptist', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('francescabenson@gmail.com', 'password', 'Francesca', 'Benson', 'riverwoodrefuge@gmail.com', 'Non Denominational', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('desmondenriquez@gmail.com', 'password', 'Desmond', 'Enriquez', 'hosannalutheran@gmail.com', 'Lutheran', 'Chicago');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('nelliesantiago@gmail.com', 'password', 'Nellie', 'Santiago', 'saintpaulcatholic@gmail.com', 'Catholic', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('beckhamorr@gmail.com', 'password', 'Beckham', 'Orr', 'encounterriverkingdom@gmail.com', 'Methodist', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('alaiyalynch@gmail.com', 'password', 'Alaiya', 'Lynch', 'kingofkings@gmail.com', 'Evangelical', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('zaneowens@gmail.com', 'password', 'Zane', 'Owens', 'pentecostlife@gmail.com', 'Anglican', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('amayacarson@gmail.com', 'password', 'Amaya', 'Carson', 'revolutionofconnection@gmail.com', 'Pentecostal', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('aresboyer@gmail.com', 'password', 'Ares', 'Boyer', 'resonatebaptist@gmail.com', 'Baptist', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('chayafloyd@gmail.com', 'password', 'Chaya', 'Floyd', 'riverwoodrefuge@gmail.com', 'Non Denominational', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('piercehuff@gmail.com', 'password', 'Pierce', 'Huff', 'thecalvarylutheran@gmail.com', 'Lutheran', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('karsynbarrett@gmail.com', 'password', 'Karsyn', 'Barrett', 'saintpaulcatholic@gmail.com', 'Catholic', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('angelofuller@gmail.com', 'password', 'Angelo', 'Fuller', 'encounterriverkingdom@gmail.com', 'Methodist', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('oakleysmall@gmail.com', 'password', 'Oakley', 'Small', 'kingofkings@gmail.com', 'Evangelical', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('rudyortiz@gmail.com', 'password', 'Rudy', 'Ortiz', 'pentecostlife@gmail.com', 'Anglican', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('annalambert@gmail.com', 'password', 'Anna', 'Lambert', 'revolutionofconnection@gmail.com', 'Pentecostal', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('marioalexander@gmail.com', 'password', 'Mario', 'Alexander', 'resonatebaptist@gmail.com', 'Baptist', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('lylasuarez@gmail.com', 'password', 'Lyla', 'Suarez', 'riverwoodrefuge@gmail.com', 'Non Denominational', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('sorenwagner@gmail.com', 'password', 'Soren', 'Wagner', 'thecalvarylutheran@gmail.com', 'Lutheran', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('maevejenkins@gmail.com', 'password', 'Maeve', 'Jenkins', 'saintpaulcatholic@gmail.com', 'Catholic', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('declanstrickland@gmail.com', 'password', 'Declan', 'Strickland', 'encounterriverkingdom@gmail.com', 'Methodist', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('niafarrell@gmail.com', 'password', 'Nia', 'Farrell', 'kingofkings@gmail.com', 'Evangelical', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('tyfuller@gmail.com', 'password', 'Ty', 'Fuller', 'pentecostlife@gmail.com', 'Anglican', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('oakleyfrye@gmail.com', 'password', 'Oakley', 'Frye', 'revolutionofconnection@gmail.com', 'Pentecostal', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('francoroy@gmail.com', 'password', 'Franco', 'Roy', 'resonatebaptist@gmail.com', 'Baptist', 'Toledo');");
            //====================================================================CUTOFF FOR USERS WHO DON'T HAVE A CHURCH YET (20 USERS CHURCH-LESS)============================================================
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('savannacochran@gmail.com', 'password', 'Savanna', 'Cochran', '', 'Non Denominational', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('dannydonnell@gmail.com', 'password', 'Danny', 'Donnell', '', 'Lutheran', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('bellamyespinosa@gmail.com', 'password', 'Bellamy', 'Espinosa', '', 'Catholic', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('khalidrice@gmail.com', 'password', 'Khalid', 'Rice', '', 'Methodist', 'Chicago');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('adamckinney@gmail.com', 'password', 'Ada', 'McKinney', '', 'Evangelical', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('romeorobles@gmail.com', 'password', 'Romeo', 'Robles', '', 'Anglican', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('felicityhunter@gmail.com', 'password', 'Felicity', 'Hunter', '', 'Pentecostal', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('archerdeleon@gmail.com', 'password', 'Archer', 'Deleon', '', 'Baptist', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('gabriellemcdaniel@gmail.com', 'password', 'Gabrielle', 'McDaniel', '', 'Non Denominational', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('majorfisher@gmail.com', 'password', 'Major', 'Fisher', '', 'Lutheran', 'Monroe');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('aryaburgess@gmail.com', 'password', 'Arya', 'Burgess', '', 'Catholic', 'Toledo');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('koltonrichmond@gmail.com', 'password', 'Kolton', 'Richmond', '', 'Methodist', 'Sandusky');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('whitneywashington@gmail.com', 'password', 'Whitney', 'Washington', '', 'Evangelical', 'Denver');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('juandecker@gmail.com', 'password', 'Juan', 'Decker', '', 'Anglican', 'Chicago');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('aleenamorales@gmail.com', 'password', 'Aleena', 'Morales', '', 'Pentecostal', 'Boston');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('aaronschmidt@gmail.com', 'password', 'Aaron', 'Schmidt', '', 'Baptist', 'Nashville');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('kimberlyblanchard@gmail.com', 'password', 'Kimberly', 'Blanchard', '', 'Non Denominational', 'San Diego');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('adlerbailey@gmail.com', 'password', 'Adler', 'Bailey', '', 'Lutheran', 'Kansas City');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('kennedykeith@gmail.com', 'password', 'Kennedy', 'Keith', '', 'Catholic', 'Temperance');");
            db.execSQL("INSERT INTO " + DatabaseVariables.USERS_TABLE + " VALUES('jaggeranderson@gmail.com', 'password', 'Jagger', 'Anderson', '', 'Methodist', 'Monroe');");
            db.close();
        }
    }
}
