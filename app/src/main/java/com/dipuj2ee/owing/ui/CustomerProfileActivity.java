package com.dipuj2ee.owing.ui;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;

import java.util.ArrayList;
import java.util.List;

public class CustomerProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView cname,caddress,cphone ,balancetext;
    private CardView cardadd ,cardpaid, carddrtrid,cardcrtrid;
    private String name,phone,address,userid,cusid;
    SQLiteDBHandeler sqLiteDBHandeler;

    private List<Double> drbalancelist;
    private List<Double> crbalancelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Profile");

        setContentView(R.layout.activity_customer_profile);

        cname= findViewById(R.id.nameid);
        caddress= findViewById(R.id.addressid);
        cphone = findViewById(R.id.phoneid);
        cardadd = findViewById(R.id.card_add_due_id);
        cardpaid = findViewById(R.id.card_paid_due_id);
        carddrtrid = findViewById(R.id.drtr_cardid);


        balancetext=findViewById(R.id.balance_amountid);
        cardadd.setOnClickListener(this);
        cardpaid.setOnClickListener(this);
        carddrtrid.setOnClickListener(this);
        cardcrtrid.setOnClickListener(this);
        sqLiteDBHandeler = new SQLiteDBHandeler(this);



        Bundle bundle = getIntent().getExtras();
         name = bundle.getString("name");
         phone = bundle.getString("phone");
         address = bundle.getString("address");
         userid = bundle.getString("userid");
         cusid = bundle.getString("cusid");



        cname.setText(name);
        caddress.setText(address);
        cphone.setText(phone);

        //getbalance();



    }

    @Override
    protected void onStart() {
        super.onStart();

        getbalance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_add_due_id:
                Intent intent_add = new Intent(CustomerProfileActivity.this,CalculationActivity.class);
                intent_add.putExtra("name",name);
                intent_add.putExtra("phone",phone);
                intent_add.putExtra("address",address);
                intent_add.putExtra("userid",userid);
                intent_add.putExtra("cusid",cusid);
                startActivity(intent_add);
                break;
            case R.id.card_paid_due_id:
                Intent intent_paid = new Intent(CustomerProfileActivity.this,PaidCalculationActivity.class);
                intent_paid.putExtra("name",name);
                intent_paid.putExtra("phone",phone);
                intent_paid.putExtra("address",address);
                intent_paid.putExtra("userid",userid);
                intent_paid.putExtra("cusid",cusid);
                startActivity(intent_paid);
                break;
            case R.id.drtr_cardid:
                Intent intent_drtr = new Intent(CustomerProfileActivity.this,DabitTaransectionActivity.class);
                intent_drtr.putExtra("name",name);
                intent_drtr.putExtra("phone",phone);
                intent_drtr.putExtra("address",address);
                intent_drtr.putExtra("userid",userid);
                intent_drtr.putExtra("cusid",cusid);
                startActivity(intent_drtr);


                break;

        }

    }

    public void getbalance(){
        Cursor c =sqLiteDBHandeler.getindividualcusbalance(cusid);
         drbalancelist = new ArrayList<Double>();
         crbalancelist = new ArrayList<Double>();
        double drtotalbal =0.0;
        double crtotalbal =0.0;
        double nettotalbalance =0.0;


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

        nettotalbalance = drtotalbal-crtotalbal;

        String netbalance = String.valueOf(nettotalbalance);
        balancetext.setText(netbalance);



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