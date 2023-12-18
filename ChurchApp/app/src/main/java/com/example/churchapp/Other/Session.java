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
    private static boolean loggedIn;

    public static void login(User u)
    {
        sessionType = USER_TYPE;
        loggedIn = true;
        user = u;
    }

    public static void login(Church c)
    {
        sessionType = CHURCH_TYPE;
        loggedIn = true;
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
}
