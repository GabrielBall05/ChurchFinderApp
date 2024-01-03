package com.example.churchapp.Other;

public class SearchParams
{
    public static String SEARCHING_BY = "all";
    public static String NAME = "";
    public static String CITY = Session.getUser().getCity();
    public static String DENOMINATION = Session.getUser().getDenomination();

    //Getters
    public static String getSearchingBy()
    {
        return SEARCHING_BY;
    }

    public static String getName()
    {
        return NAME;
    }

    public static String getCity()
    {
        return CITY;
    }

    public static String getDenomination()
    {
        return DENOMINATION;
    }

    //Setters
    public static void setSearchingBy(String s)
    {
        SEARCHING_BY = s;
    }

    public static void setName(String n)
    {
        NAME = n;
    }

    public static void setCity(String c)
    {
        CITY = c;
    }

    public static void setDenomination(String d)
    {
        DENOMINATION = d;
    }
}
