package com.example.churchapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.churchapp.Models.User;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ParticipantsAdapter extends BaseAdapter
{
    Context context;
    ArrayList<User> listOfParticipants;

    public ParticipantsAdapter(Context c, ArrayList<User> ls)
    {
        context = c;
        listOfParticipants = ls;
    }

    @Override
    public int getCount()
    {
        return listOfParticipants.size();
    }

    @Override
    public Object getItem(int i)
    {
        return listOfParticipants.get(i);
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
            view = mInflater.inflate(R.layout.participants_custom_cell, null);
        }

        //GUI
        TextView tv_name = view.findViewById(R.id.tv_participantsCC_name);

        //Get the user
        User user = listOfParticipants.get(i);
        //Set text view with first name and last name
        tv_name.setText(user.getFirstName() + " " + user.getLastName());

        return view;
    }
}
