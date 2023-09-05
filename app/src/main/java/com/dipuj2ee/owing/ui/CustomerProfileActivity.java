package com.dipuj2ee.owing.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;
import com.dipuj2ee.owing.model.BalanceModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomerProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView cname, caddress, cphone, balancetext;
    private CardView cardadd, cardpaid, carddrtrid, cardbill, cardsms;
    private String name, phone, address, userid, cusid, netduebalance;
    SQLiteDBHandeler sqLiteDBHandeler;
    Double netDebit, netCredit;

    private List<Double> drbalancelist;
    private List<Double> crbalancelist;
    DatabaseReference databaseReference;
    BalanceModel balanceModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(" CUSTOMER PROFILE");

        setContentView(R.layout.activity_customer_profile);

        cname= findViewById(R.id.nameid);
        caddress= findViewById(R.id.addressid);
        cphone = findViewById(R.id.phoneid);
        cardadd = findViewById(R.id.card_add_due_id);
        cardpaid = findViewById(R.id.card_paid_due_id);
        carddrtrid = findViewById(R.id.drtr_cardid);
        cardbill = findViewById(R.id.card_bill_id);
        cardsms = findViewById(R.id.card_sms_id);



        balancetext=findViewById(R.id.balance_amountid);
        cardadd.setOnClickListener(this);
        cardpaid.setOnClickListener(this);
        carddrtrid.setOnClickListener(this);
        cardbill.setOnClickListener(this);
        cardsms.setOnClickListener(this);
        sqLiteDBHandeler = new SQLiteDBHandeler(this);
        balanceModel = new BalanceModel();




        Bundle bundle = getIntent().getExtras();
         name = bundle.getString("name");
         phone = bundle.getString("phone");
         address = bundle.getString("address");
         userid = bundle.getString("userid");
         cusid = bundle.getString("cusid");
        databaseReference = FirebaseDatabase.getInstance().getReference("Balance").child(userid).child(cusid);



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
            case R.id.card_sms_id:
                Intent intent_sms = new Intent(CustomerProfileActivity.this,SmsActivity.class);
                intent_sms.putExtra("phone", phone);
                intent_sms.putExtra("netBalance", netduebalance);
                startActivity(intent_sms);
                break;

        }

    }

    public void getbalance(){

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){
                    System.out.println(dataSnapshot.getValue());
                    balanceModel = dataSnapshot.getValue(BalanceModel.class);
                    assert balanceModel != null;
                    System.out.println(balanceModel.getCrBalance().toString());
                    double nettotalbalance =0.0;
                    nettotalbalance = balanceModel.getDrBalance() - balanceModel.getCrBalance();
                    netduebalance = String.valueOf(nettotalbalance);
                    balancetext.setText(netduebalance);

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
