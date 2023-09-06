package com.dipuj2ee.owing.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;
import com.dipuj2ee.owing.model.BalanceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaidCalculationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView totalbalance,previousBalance;
    private EditText singelbalance;
    private CardView savebt, cancelbt, plusbt, minusbt;
    private String name, phone, address, userid, cusid, balance, singlblance, balancetk;
    private Double amount, totalamount, total,preDrBalance,preCrBalance;


    DatabaseReference db_balance, db_balance_info;
    FirebaseAuth mAuth;
    FirebaseUser fUser;

    SQLiteDBHandeler sqLiteBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_calculation);

        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Calculator");

        totalbalance =findViewById(R.id.totalbalanceid);
        previousBalance =findViewById(R.id.previusduetextid);
        singelbalance =findViewById(R.id.singelbalanceid);
        savebt = findViewById(R.id.savecardbuttonid);
        cancelbt = findViewById(R.id.cancelcardbuttonid);
        plusbt = findViewById(R.id.plusbuttonid);
        minusbt = findViewById(R.id.minusbuttonid);

        plusbt.setOnClickListener(this);
        minusbt.setOnClickListener(this);
        savebt.setOnClickListener(this);
        cancelbt.setOnClickListener(this);

        sqLiteBD = new SQLiteDBHandeler(this);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        address = bundle.getString("address");
        userid = bundle.getString("userid");
        cusid = bundle.getString("cusid");

        db_balance = FirebaseDatabase.getInstance().getReference("Balance").child(fUser.getUid()).child(cusid);
        db_balance_info = FirebaseDatabase.getInstance().getReference("BalanceInfo").child(fUser.getUid()).child(cusid);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getbalance();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id .plusbuttonid:
                String balance= String.valueOf( totalbalance.getText());
                String singlblance= String.valueOf( singelbalance.getText());
                if(singlblance.equals("")) {
                    Toast.makeText(this, "টাকার পরিমান লিখুন", Toast.LENGTH_SHORT).show();
                }else {
                    amount= Double.parseDouble(singlblance);
                    if(balance.equals("")){
                        totalbalance.setText(String.valueOf(amount));
                        singelbalance.setText("");
                    }else{
                        totalamount= Double.parseDouble(balance);
                        total = totalamount+amount;
                        totalbalance.setText(String.valueOf(total));
                        singelbalance.setText("");
                    }
                }
                break;
            case R.id .minusbuttonid:
                balance= String.valueOf( totalbalance.getText());
                singlblance= String.valueOf( singelbalance.getText());
                if(singlblance.equals("")) {
                    Toast.makeText(this, "টাকার পরিমান লিখুন", Toast.LENGTH_SHORT).show();
                }else {
                    amount= Double.parseDouble(singlblance);
                    totalamount= Double.parseDouble(balance);
                    total = totalamount-amount;
                    totalbalance.setText(String.valueOf(total));
                    singelbalance.setText("");
                }

                break;

            case R.id.savecardbuttonid:
                savebalancetk();
                updatenetcrbalence();
            case R.id.cancelcardbuttonid:
                singelbalance.setText("");
                totalbalance.setText("");
                break;

        }

    }
    private void savebalancetk() {
        balancetk = totalbalance.getText().toString().trim();
        String key = db_balance_info.push().getKey();
        if (balancetk.equals("")) {
            Toast.makeText(PaidCalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();

        } else {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date();
            formatter.format(date);
            String trdate = formatter.format(date);
            totalamount = Double.parseDouble(balancetk);
            BalanceModel balanceModel = new BalanceModel(userid, cusid, 0.0, totalamount, trdate);
            sqLiteBD.addBalanceInfo(balanceModel);
            // db_balance.setValue(balanceModel);
            db_balance_info.child(key).setValue(balanceModel);
            //Toast.makeText(PaidCalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();


        }
    }
    private void updatenetcrbalence() {
        balancetk = totalbalance.getText().toString().trim();
        String key = db_balance_info.push().getKey();
        if (balancetk.equals("")) {
            Toast.makeText(PaidCalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();

        } else {

            Cursor c = sqLiteBD.getnetbalence(cusid);
            if (c.moveToFirst()) {
/*                Double predrbalence = c.getDouble(3);
                Double precrbalence = c.getDouble(4);*/
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = new Date();
                formatter.format(date);
                String trdate = formatter.format(date);
                totalamount = Double.parseDouble(balancetk);
                Double netcrbalence = preCrBalance + totalamount;
                BalanceModel balanceModel = new BalanceModel(userid, cusid, preDrBalance, netcrbalence, trdate);
                sqLiteBD.updateNetBalance(balanceModel, cusid);
                db_balance.setValue(balanceModel);
                getbalance();
                //db_balance_info.child(key).setValue(balanceModel);
                //Toast.makeText(PaidCalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
                Toast.makeText(PaidCalculationActivity.this, "update Successful", Toast.LENGTH_SHORT).show();

            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = new Date();
                formatter.format(date);
                String trdate = formatter.format(date);
                totalamount = Double.parseDouble(balancetk);
                BalanceModel balanceModel = new BalanceModel(userid, cusid, 0.0, totalamount, trdate);
                sqLiteBD.addnetBalance(balanceModel);
                db_balance.setValue(balanceModel);
                getbalance();
                // db_balance_info.child(key).setValue(balanceModel);
                //Toast.makeText(PaidCalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
                Toast.makeText(PaidCalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();

            }


        }


    }
    public void getbalance(){

        db_balance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){
                    System.out.println(dataSnapshot.getValue());
                    BalanceModel balanceModel = dataSnapshot.getValue(BalanceModel.class);
                    assert balanceModel != null;
                    System.out.println(balanceModel.getCrBalance().toString());
                    double nettotalbalance =0.0;
                    preDrBalance = balanceModel.getDrBalance();
                    preCrBalance=balanceModel.getCrBalance();
                    nettotalbalance =  preDrBalance - preCrBalance;
                    String netduebalance = String.valueOf(nettotalbalance);
                    previousBalance.setText(netduebalance);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}