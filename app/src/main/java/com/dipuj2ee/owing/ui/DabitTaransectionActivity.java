package com.dipuj2ee.owing.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.adapter.DrTransectionAdapter;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;
import com.dipuj2ee.owing.model.BalanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    DatabaseReference db_balance,db_balance_info;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dabit_taransection);

        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("TRANSACTION DETAILS");

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
        db_balance = FirebaseDatabase.getInstance().getReference("Balance").child(userid).child(cusid);
        db_balance_info = FirebaseDatabase.getInstance().getReference("BalanceInfo").child(userid).child(cusid);

       // getDrTransection();
        getTransection();
       // getbalance();
        getnetbalance();


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
    private void getTransection(){
        db_balance_info.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                balanceModelList.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()){
                    BalanceModel balanceModel= data.getValue(BalanceModel.class);
                    assert balanceModel != null;
                    System.out.println(balanceModel.getCusid());
                    balanceModelList.add(balanceModel);
                }

                listView.setAdapter(drTransectionAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


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
    public void getnetbalance(){

        db_balance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){
                    System.out.println(dataSnapshot.getValue());
                    BalanceModel balanceModel = dataSnapshot.getValue(BalanceModel.class);
                    assert balanceModel != null;
                    String totalDrBalance = String.valueOf(balanceModel.getDrBalance());
                    String totalCrBalance = String.valueOf(balanceModel.getCrBalance());
                    debittotaltaka.setText(totalDrBalance);
                    credittotaltaka.setText(totalCrBalance);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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