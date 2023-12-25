package com.example.churchapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.churchapp.Models.Church;
import com.example.churchapp.Models.Event;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ListOfChurchesAdapter extends BaseAdapter
{
    ArrayList<Church> listOfChurches;
    Context context;

    public ListOfChurchesAdapter(Context c, ArrayList<Church> ls)
    {
        context = c;
        listOfChurches = ls;
    }

    @Override
    public int getCount()
    {
        return listOfChurches.size();
    }

    @Override
    public Object getItem(int i)
    {
        return listOfChurches.get(i);
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
            view = mInflater.inflate(R.layout.filtered_churches_custom_cell, null);
        }

        TextView tv_name = view.findViewById(R.id.tv_filteredChurchesCC_name);
        TextView tv_denomination = view.findViewById(R.id.tv_filteredChurchesCC_denomination);
        Church church = listOfChurches.get(i);
        tv_name.setText(church.getName());
        tv_denomination.setText(church.getDenomination());

        return view;
    }
}
