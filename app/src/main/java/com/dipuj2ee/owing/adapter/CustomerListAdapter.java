package com.dipuj2ee.owing.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;
import com.dipuj2ee.owing.model.BalanceModel;
import com.dipuj2ee.owing.model.CustomerInfoModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.customerlist_sample_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.cusname = view.findViewById(R.id.cusnameid);
            holder.cusaddress = view.findViewById(R.id.customeraddressid);
            holder.cusphone = view.findViewById(R.id.cusphoneid);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.cusname.setText(searchQueries.get(position).getName());
        holder.cusaddress.setText(searchQueries.get(position).getAddress());
        holder.cusphone.setText(searchQueries.get(position).getPhone());


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

