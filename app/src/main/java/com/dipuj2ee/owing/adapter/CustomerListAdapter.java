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
import java.util.Locale;

public class CustomerListAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<CustomerInfoModel> searchList =null;
    private List<CustomerInfoModel> customers = null;
    SQLiteDBHandeler dbnetBlance;

    public CustomerListAdapter(Context context, List<CustomerInfoModel> customerInfo, ArrayList<CustomerInfoModel> cusSearch) {
        mContext = context;
        this.customers = customerInfo;
        this.searchList = cusSearch;
        inflater = LayoutInflater.from(mContext);



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
        holder.cusname.setText(customers.get(position).getName());
        holder.cusaddress.setText(customers.get(position).getAddress());
        holder.cusphone.setText(customers.get(position).getPhone());


        return view;
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public CustomerInfoModel getItem(int position) {
        return customers.get(position);
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
    public void filter(String charText) {

       String queryText = charText.toLowerCase(Locale.getDefault());

        System.out.println("searchlist"+searchList.size());
        if (queryText.length() == 0) {
            customers.clear();
            customers.addAll(searchList);
            System.out.println(customers.size());

        } else {
            customers.clear();
            for (CustomerInfoModel customer : searchList) {
                if (customer.getName().toLowerCase(Locale.getDefault()).contains(queryText)) {
                    customers.add(customer);
                }
                System.out.println(customers.size());
            }

        }
        notifyDataSetChanged();

    }


}

