package com.dipuj2ee.owing.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.model.BalanceModel;

import java.util.List;

public class DrTransectionAdapter extends BaseAdapter {
    Context context;
    List<BalanceModel> balanceModelList =null;
    LayoutInflater inflater;

    public DrTransectionAdapter(Context context, List<BalanceModel> balancelist) {

        this.context = context;
        this.balanceModelList = balancelist;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return balanceModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return balanceModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final DrTransectionAdapter.ViewHolder holder;
        if (view == null) {
            holder = new DrTransectionAdapter.ViewHolder();
            view = inflater.inflate(R.layout.transection_sample_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.trdate = (TextView) view.findViewById(R.id.transection_date_id);
            holder.drtaka = (TextView) view.findViewById(R.id.drtransection_taka_id);
            holder.crtaka = (TextView) view.findViewById(R.id.crtransection_taka_id);
            view.setTag(holder);
        } else {
            holder = (DrTransectionAdapter.ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.trdate.setText(balanceModelList.get(position).getTrdate());
        holder.drtaka.setText("+ " + balanceModelList.get(position).getDrBalance());
        holder.crtaka.setText("- " + balanceModelList.get(position).getCrBalance());

        return view;
    }

    public class ViewHolder {
        TextView trdate;
        TextView drtaka;
        TextView crtaka;

    }
}
