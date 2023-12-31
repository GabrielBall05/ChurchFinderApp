package com.example.churchapp.Models;

import java.io.Serializable;

public class User implements Serializable
{
    //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
    private String email;
    private String password;
    private String firstname;
    private String lastname;
    private String emailOfChurchAttending;
    private String denomination;
    private String city;

    public User(String e, String p, String f, String l, String eoc, String d, String c)
    {
        //ORDER: email, password, firstname, lastname, emailOfChurchAttending, denomination, city
        email = e;
        password = p;
        firstname = f;
        lastname = l;
        emailOfChurchAttending = eoc;
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

    public String getFirstName()
    {
        return firstname;
    }

    public String getLastName()
    {
        return lastname;
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

    public boolean isValidLogin(String enteredPassword)
    {
        if (enteredPassword.equals(password))
        {
            return true;
        }
        return false;
    }
}


