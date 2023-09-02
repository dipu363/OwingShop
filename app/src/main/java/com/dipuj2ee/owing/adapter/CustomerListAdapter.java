package com.dipuj2ee.owing.adapter;

import android.content.Context;
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

public class CustomerListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private final ArrayList<CustomerInfoModel> arraylist = new ArrayList<>();
    private List<CustomerInfoModel> customerlist = null;
    SQLiteDBHandeler dbnetBlance;


    public CustomerListAdapter(Context context, List<CustomerInfoModel> searchQueries) {
        mContext = context;
        this.customerlist = searchQueries;
        inflater = LayoutInflater.from(mContext);
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
        holder.cusname.setText(customerlist.get(position).getName());
        holder.cusaddress.setText(customerlist.get(position).getAddress());
        holder.cusphone.setText(customerlist.get(position).getPhone());


        return view;
    }

    @Override
    public int getCount() {
        return customerlist.size();
    }

    @Override
    public CustomerInfoModel getItem(int position) {
        return customerlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView cusname;
        TextView cusaddress;
        TextView cusphone;
        TextView cusduebalence;
    }

    // Filter Class
   /* public void filter(String charText) {

       String queryText = charText.toLowerCase(Locale.getDefault());

arraylist.size();
        if (queryText.length() == 0) {
            customerlist.clear();
            customerlist.addAll(arraylist);
            System.out.println(customerlist.size());

        } else {
            customerlist.clear();
            for (CustomerInfoModel customer : arraylist) {
                if (customer.getName().toLowerCase(Locale.getDefault()).contains(queryText)) {
                    customerlist.add(customer);
                }
                System.out.println(customerlist.size());
            }

        }
        notifyDataSetChanged();

    }*/


}

