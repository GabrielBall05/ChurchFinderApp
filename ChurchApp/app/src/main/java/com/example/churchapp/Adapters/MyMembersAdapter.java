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

public class MyMembersAdapter extends BaseAdapter
{
    Context context;
    ArrayList<User> listOfMembers;

    public MyMembersAdapter(Context c, ArrayList<User> ls)
    {
        context = c;
        listOfMembers = ls;
    }

    @Override
    public int getCount()
    {
        return listOfMembers.size();
    }

    @Override
    public Object getItem(int i)
    {
        return listOfMembers.get(i);
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
            view = mInflater.inflate(R.layout.my_members_custom_cell, null);
        }

        //GUI
        TextView tv_fnameLname = view.findViewById(R.id.tv_myMembersCC_fnameLname);

        User user = listOfMembers.get(i);
        tv_fnameLname.setText(user.getFirstName() + " " + user.getLastName());

        return view;
    }
}
