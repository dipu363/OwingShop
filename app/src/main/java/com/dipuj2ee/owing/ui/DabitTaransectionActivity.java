package com.dipuj2ee.owing.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.adapter.DrTransectionAdapter;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;
import com.dipuj2ee.owing.model.BalanceModel;

import java.util.ArrayList;
import java.util.List;

public class DabitTaransectionActivity extends AppCompatActivity {

    private TextView debittotaltaka ,credittotaltaka;
    private String name,phone,address,userid,cusid;
    private ListView listView;
    private DrTransectionAdapter drTransectionAdapter;
    private List<BalanceModel> balanceModelList;
    private List<BalanceModel> balanceModelList2;
    SQLiteDBHandeler sqLiteDBHandeler;
    private List<Double> drbalancelist;
    private List<Double> crbalancelist;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabit_taransection);

        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Debit Transaction");

        debittotaltaka = findViewById(R.id.totaldebittakaid);
        credittotaltaka = findViewById(R.id.totalcredittakaid);
        listView = findViewById(R.id.drlistid);
        sqLiteDBHandeler = new SQLiteDBHandeler(this);
        balanceModelList = new ArrayList<>();
        balanceModelList2 = new ArrayList<>();
        drTransectionAdapter = new DrTransectionAdapter(this,balanceModelList);

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        address = bundle.getString("address");
        userid = bundle.getString("userid");
        cusid = bundle.getString("cusid");

        getDrTransection();
        getbalance();


    }


    public void getDrTransection(){

        Cursor c =sqLiteDBHandeler.getDebitTransection(cusid);

        if(c.moveToFirst()){


            do {
                BalanceModel balanceModel = new BalanceModel(c.getString(1),c.getString(2),c.getDouble(3),c.getDouble(4),c.getString(5));
                balanceModelList.add(balanceModel);
            }
            while (c.moveToNext());
        }

        listView.setAdapter(drTransectionAdapter);





    }
    public void getbalance(){
        Cursor c =sqLiteDBHandeler.getindividualcusbalance(cusid);
        drbalancelist = new ArrayList<Double>();
        crbalancelist = new ArrayList<Double>();
        double drtotalbal =0.0;
        double crtotalbal =0.0;

        if (c.moveToFirst()){



            do {
                drbalancelist.add(
                        c.getDouble(3));
            } while (c.moveToNext());

            for(double drbal :drbalancelist){
                drtotalbal +=drbal;
            }
        }



        if(c.moveToFirst()){

            do {
                crbalancelist.add(
                        c.getDouble(4));
            }
            while (c.moveToNext());

            for(double crbal :crbalancelist){
                crtotalbal +=crbal;
            }


        }

        String netdrbalance = String.valueOf(drtotalbal);
        String netcrbalance = String.valueOf(crtotalbal);
        debittotaltaka.setText(netdrbalance);
        credittotaltaka.setText(netcrbalance);




    }

    //this mathod use for back to previus page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}