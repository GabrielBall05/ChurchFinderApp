package com.example.churchapp.Models;

import java.io.Serializable;

public class Church implements Serializable
{
    //ORDER: email, password, name, denomination, statementOfFaith, streetAddress, city
    private String email;
    private String password;
    private String name;
    private String denomination;
    private String statementOfFaith;
    private String streetAddress;
    private String city;

    public Church(String e, String p, String n, String d, String s, String a, String c)
    {
        email = e;
        password = p;
        name = n;
        denomination = d;
        statementOfFaith = s;
        streetAddress = a;
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
}
