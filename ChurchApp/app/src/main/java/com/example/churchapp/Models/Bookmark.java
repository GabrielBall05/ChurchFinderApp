package com.example.churchapp.Models;

import java.io.Serializable;

public class Bookmark implements Serializable
{
    private String emailOfUser;
    private String emailOfChurch;

    public Bookmark(String c, String u)
    {
        //ORDER: emailOfChurch, emailOfUser
        emailOfChurch = c;
        emailOfUser = u;
    }

    public String getEmailOfUser()
    {
        return emailOfUser;
    }

    public String getEmailOfChurch()
    {
        return emailOfChurch;
    }
}
