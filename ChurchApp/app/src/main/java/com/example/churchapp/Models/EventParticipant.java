package com.example.churchapp.Models;

import java.io.Serializable;

public class EventParticipant implements Serializable
{
    private int eventId; //References events.eventId
    private String emailOfParticipant; //References users.email

    public EventParticipant(int id, String e)
    {
        //ORDER: eventId, emailOfParticipant
        eventId = id;
        emailOfParticipant = e;
    }

    public int getEventId()
    {
        return eventId;
    }

    public String getEmailOfParticipant()
    {
        return emailOfParticipant;
    }
}
