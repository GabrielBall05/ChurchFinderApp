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

public class MyEventsAdapter extends BaseAdapter
{
    ArrayList<Event> listOfMyEvents;
    Context context;

    public MyEventsAdapter(Context c, ArrayList<Event> ls)
    {
        context = c;
        listOfMyEvents = ls;
    }

    @Override
    public int getCount()
    {
        return listOfMyEvents.size();
    }

    @Override
    public Object getItem(int i)
    {
        return listOfMyEvents.get(i);
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
            view = mInflater.inflate(R.layout.my_events_custom_cell, null);
        }

        TextView tv_name = view.findViewById(R.id.tv_myEventsCC_name);
        TextView tv_dateTime = view.findViewById(R.id.tv_myEventsCC_dateTime);
        Event event = listOfMyEvents.get(i);
        tv_name.setText(event.getEventName());
        tv_dateTime.setText(event.getDate() + " @" + event.getTime());

        return view;
    }
}