package com.example.churchapp.Models;

import java.io.Serializable;

public class Event implements Serializable
{
    //Order: eventId, churchHostingEmail, churchName, eventName, address, date, time, desc
    private int eventId;
    private String churchHostingEmail;
    private String churchName;
    private String eventName;
    private String address;
    private String date;
    private String time;
    private String desc;

    public Event(int id, String e, String cn, String en, String a, String d, String t, String de)
    {
        eventId = id;
        churchHostingEmail = e;
        churchName = cn;
        eventName = en;
        address = a;
        date = d;
        time = t;
        desc = d;
    }

    public int getEventId()
    {
        return eventId;
    }

    public String getChurchHostingEmail()
    {
        return churchHostingEmail;
    }

    public String getChurchName()
    {
        return churchName;
    }

    public String getEventName()
    {
        return eventName;
    }

    public String getAddress()
    {
        return address;
    }

    public String getDate()
    {
        return date;
    }

    public String getTime()
    {
        return time;
    }

    public String getDesc()
    {
        return desc;
    }
}