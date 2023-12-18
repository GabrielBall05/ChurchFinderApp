package com.example.churchapp.Models;

import java.io.Serializable;

public class User implements Serializable
{
    //ORDER: email, password, fname, lname, emailOfChurchAttending, denomination, city
    private String email;
    private String password;
    private String fname;
    private String lname;
    private String emailOfChurchAttending;
    private String denomination;
    private String city;

    //Constructor with email of church attending
    public User(String e, String p, String f, String l, String eoc, String d, String c)
    {
        email = e;
        password = p;
        fname = f;
        lname = l;
        emailOfChurchAttending = eoc;
        denomination = d;
        city = c;
    }

    //Constructor without email of church attending
    public User(String e, String p, String f, String l, String d, String c)
    {
        email = e;
        password = p;
        fname = f;
        lname = l;
        denomination = d;
        city = c;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public String getFname()
    {
        return fname;
    }

    public String getLname()
    {
        return lname;
    }

    public String getEmailOfChurchAttending()
    {
        return emailOfChurchAttending;
    }

    public String getDenomination()
    {
        return denomination;
    }

    public String getCity()
    {
        return city;
    }

    public void setEmailOfChurchAttending(String e)
    {
        emailOfChurchAttending = e;
    }
}


