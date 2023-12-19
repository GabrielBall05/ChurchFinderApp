package com.example.churchapp.Models;

import java.io.Serializable;

public class Bookmark implements Serializable
{
    private String emailOfUser;
    private String emailOfChurch;

    public Bookmark(String u, String c)
    {
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
