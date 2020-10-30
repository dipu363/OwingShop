package com.dipuj2ee.owing.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.model.CustomerInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<CustomerInfoModel> searchQueries = null;
    private ArrayList<CustomerInfoModel> arraylist;

    public CustomerListAdapter(Context context, List<CustomerInfoModel> searchQueries) {
        mContext = context;
        this.searchQueries = searchQueries;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CustomerInfoModel>();
        this.arraylist.addAll(searchQueries);
    }

    public class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return searchQueries.size();
    }

    @Override
    public CustomerInfoModel getItem(int position) {
        return searchQueries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.customerlist_sample_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(searchQueries.get(position).getName());
        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
     /*   searchQueries.clear();
        if (charText.length() == 0) {
            searchQueries.addAll(arraylist);
        } else {
            for (CustomerInfoModel wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    searchQueries.add(wp);
                }
            }
        }*/
        //notifyDataSetChanged();
    }

}

