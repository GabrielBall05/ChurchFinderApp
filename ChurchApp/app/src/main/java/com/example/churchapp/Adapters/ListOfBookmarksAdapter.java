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
    //I want an instance of the churches table because it's easier than joining tables in a query and because I can
    ChurchesTableHelper churchesDb;
    Context context;
    ArrayList<Bookmark> listOfBookmarks;

    public ListOfBookmarksAdapter(Context c, ArrayList<Bookmark> ls)
    {
        context = c;
        listOfBookmarks = ls;
        churchesDb = new ChurchesTableHelper(c);
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

        Church church = churchesDb.getChurchByEmail(bookmark.getEmailOfChurch());
        tv_name.setText(church.getName());
        tv_denomination.setText(church.getDenomination());

        return view;
    }
}
