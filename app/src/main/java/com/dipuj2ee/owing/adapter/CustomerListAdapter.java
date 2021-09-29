package com.dipuj2ee.owing.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;
import com.dipuj2ee.owing.model.CustomerInfoModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<CustomerInfoModel> searchQueries = null;
    private final ArrayList<CustomerInfoModel> arraylist;
    SQLiteDBHandeler dbnetBlance;

    public CustomerListAdapter(Context context, List<CustomerInfoModel> searchQueries) {
        mContext = context;
        this.searchQueries = searchQueries;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CustomerInfoModel>();
        this.arraylist.addAll(searchQueries);
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        double drtotalbal = 0.0;
        double crtotalbal = 0.0;
        double nettotalbalance = 0.0;
        dbnetBlance = new SQLiteDBHandeler(mContext);
        Cursor c = dbnetBlance.getnetbalence(searchQueries.get(position).getId());

        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.customerlist_sample_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.cusname = view.findViewById(R.id.cusnameid);
            holder.cusaddress = view.findViewById(R.id.customeraddressid);
            holder.cusphone = view.findViewById(R.id.cusphoneid);
            holder.cusduebalence = view.findViewById(R.id.cusnetbalenceid);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.cusname.setText(searchQueries.get(position).getName());
        holder.cusaddress.setText(searchQueries.get(position).getAddress());
        holder.cusphone.setText(searchQueries.get(position).getPhone());
        if (c.moveToFirst()) {
            drtotalbal = c.getDouble(3);
            crtotalbal = c.getDouble(4);

        }

        nettotalbalance = drtotalbal - crtotalbal;
        String netduebalance = String.valueOf(nettotalbalance);
        holder.cusduebalence.setText(netduebalance);
        return view;
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

    public class ViewHolder {
        TextView cusname;
        TextView cusaddress;
        TextView cusphone;
        TextView cusduebalence;
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

