package com.example.churchapp.Models;

import java.io.Serializable;

public class Church implements Serializable
{
    private String email;
    private String password;
    private String name;
    private String denomination;
    private String statementOfFaith;
    private String streetAddress;
    private String city;
    private String number;

    public Church(String e, String p, String n, String d, String s, String a, String c, String num)
    {
        //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city, number
        email = e;
        password = p;
        name = n;
        denomination = d;
        statementOfFaith = s;
        streetAddress = a;
        city = c;
        number = num;
    }

    public String getEmail()
    {
        return email;
    }

    public String getPassword()
    {
        return password;
    }

    public String getName()
    {
        return name;
    }

    public String getDenomination()
    {
        return denomination;
    }

    public String getStatementOfFaith()
    {
        return statementOfFaith;
    }

    public String getStreetAddress()
    {
        return streetAddress;
    }

    public String getCity()
    {
        return city;
    }

    public String getNumber()
    {
        return number;
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
