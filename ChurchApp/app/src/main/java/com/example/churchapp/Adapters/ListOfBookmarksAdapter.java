package com.example.churchapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.churchapp.Database.ChurchesTableHelper;
import com.example.churchapp.Models.Bookmark;
import com.example.churchapp.Models.Church;
import com.example.churchapp.R;

import java.util.ArrayList;

public class ListOfBookmarksAdapter extends BaseAdapter
{
    Context context;
    ArrayList<Bookmark> listOfBookmarks;
    ArrayList<String> listOfNames;
    ArrayList<String> listOfDenominations;

    public ListOfBookmarksAdapter(Context c, ArrayList<Bookmark> ls, ArrayList<String> names, ArrayList<String> denoms)
    {
        context = c;
        listOfBookmarks = ls;
        listOfNames = names;
        listOfDenominations = denoms;
    }

    @Override
    public int getCount()
    {
        return listOfBookmarks.size();
    }

    @Override
    public Object getItem(int i)
    {
        return listOfBookmarks.get(i);
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
            view = mInflater.inflate(R.layout.my_bookmarks_custom_cell, null);
        }

        TextView tv_name = view.findViewById(R.id.tv_myBookmarksCC_name);
        TextView tv_denomination = view.findViewById(R.id.tv_myBookmarksCC_denomination);
        Bookmark bookmark = listOfBookmarks.get(i);

        tv_name.setText(listOfNames.get(i));
        tv_denomination.setText(listOfDenominations.get(i));

        return view;
    }
}
