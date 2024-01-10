package com.example.churchapp.Other;

import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.User;

public class Session
{
    public static String USER_TYPE = "USER";
    public static String CHURCH_TYPE = "CHURCH";
    private static String sessionType;
    private static User user;
    private static Church church;
    private static String originPage;

    public static void login(User u) //Log the User in
    {
        sessionType = USER_TYPE;
        user = u;
    }

    public static void login(Church c) //Log the church in
    {
        sessionType = CHURCH_TYPE;
        church = c;
    }

    public static User getUser()
    {
        return user;
    }

    public static Church getChurch()
    {
        return church;
    }

    public static String getSessionType()
    {
        return sessionType;
    }
    public static String getOriginPage()
    {
        return originPage;
    }
    public static void setOriginPage(String s)
    {
        originPage = s;
    }
}
