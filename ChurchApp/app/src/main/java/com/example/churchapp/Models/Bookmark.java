package com.example.churchapp.Models;

import java.io.Serializable;

public class Bookmark implements Serializable
{
    private String emailOfUser; //References usersf.email
    private String emailOfChurch; //References churches.email

    public Bookmark(String u, String c)
    {
        //ORDER: emailOfUser, emailOfChurch
        emailOfUser = u;
        emailOfChurch = c;
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
