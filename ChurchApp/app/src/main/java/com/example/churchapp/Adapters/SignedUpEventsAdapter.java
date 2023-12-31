package com.example.churchapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.churchapp.Models.Event;
import com.example.churchapp.R;

import java.util.ArrayList;

public class SignedUpEventsAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Event> listOfEvents;

    public SignedUpEventsAdapter(Context c, ArrayList<Event> ls)
    {
        context = c;
        listOfEvents = ls;
    }

    @Override
    public int getCount()
    {
        return listOfEvents.size();
    }

    @Override
    public Object getItem(int i)
    {
        return listOfEvents.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent)
    {
        if (view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.signed_up_events_custom_cell, null);
        }

        //GUI
        TextView tv_name = view.findViewById(R.id.tv_signedUpEventsCC_name);
        TextView tv_dateTime = view.findViewById(R.id.tv_signedUpEventsCC_dateTime);

        //Get the event
        Event event = listOfEvents.get(i);
        //Fill in text views with name, date, and time
        tv_name.setText(event.getEventName());
        tv_dateTime.setText(event.getDate() + " @" + event.getTime());

        return view;
    }
}
