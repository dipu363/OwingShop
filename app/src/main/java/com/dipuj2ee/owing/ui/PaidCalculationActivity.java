package com.dipuj2ee.owing.ui;

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

    private TextView totalBalance, previousBalance;
    private EditText singleBalance;
    private String userid;
    private String cusId;
    private String balanceTk;
    private Double totalAmount;
    private Double preDrBalance;
    private Double preCrBalance;
    private boolean hasPreviousDue;


    DatabaseReference db_balance, db_balance_info;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    SQLiteDBHandeler sqLiteBD;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_calculation);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Calculator");

        totalBalance = findViewById(R.id.totalbalanceid);
        previousBalance = findViewById(R.id.previusduetextid);
        singleBalance = findViewById(R.id.singelbalanceid);
        CardView btnSave = findViewById(R.id.savecardbuttonid);
        CardView btnCancel = findViewById(R.id.cancelcardbuttonid);
        CardView btnPlus = findViewById(R.id.plusbuttonid);
        CardView btnMinus = findViewById(R.id.minusbuttonid);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        sqLiteBD = new SQLiteDBHandeler(this);
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("name");
        String phone = bundle.getString("phone");
        String address = bundle.getString("address");
        userid = bundle.getString("userid");
        cusId = bundle.getString("cusid");

        db_balance = FirebaseDatabase.getInstance().getReference("Balance").child(fUser.getUid()).child(cusId);
        db_balance_info = FirebaseDatabase.getInstance().getReference("BalanceInfo").child(fUser.getUid()).child(cusId);

    }

    @Override
    protected void onStart() {
        super.onStart();
        getPreviousDueBalance();
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
        Double amount;
        Double total;
        switch (v.getId()) {
            case R.id.plusbuttonid:
                String balance = String.valueOf(totalBalance.getText());
                String singleBalance = String.valueOf(this.singleBalance.getText());
                if (singleBalance.equals("")) {
                    Toast.makeText(this, "টাকার পরিমান লিখুন", Toast.LENGTH_SHORT).show();
                } else {
                    amount = Double.parseDouble(singleBalance);

                    if (balance.equals("")) {
                        totalBalance.setText(String.valueOf(amount));
                        this.singleBalance.setText("");
                    } else {
                        totalAmount = Double.parseDouble(balance);
                        total = totalAmount + amount;
                        totalBalance.setText(String.valueOf(total));
                        this.singleBalance.setText("");
                    }
                }
                break;
            case R.id.minusbuttonid:
                balance = String.valueOf(totalBalance.getText());
                singleBalance = String.valueOf(this.singleBalance.getText());
                if (singleBalance.equals("")) {
                    Toast.makeText(this, "টাকার পরিমান লিখুন", Toast.LENGTH_SHORT).show();
                } else {
                    amount = Double.parseDouble(singleBalance);
                    totalAmount = Double.parseDouble(balance);
                    total = totalAmount - amount;
                    totalBalance.setText(String.valueOf(total));
                    this.singleBalance.setText("");
                }

                break;

            case R.id.savecardbuttonid:
                if (!hasPreviousDue) {
                    saveDueAmount();
                } else {
                    updateDueAmount();
                }
            case R.id.cancelcardbuttonid:
                this.singleBalance.setText("");
                totalBalance.setText("");
                break;

        }

    }

    private void getPreviousDueBalance() {

        db_balance.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {
                    hasPreviousDue = true;
                    System.out.println(dataSnapshot.getValue());
                    BalanceModel balanceModel = dataSnapshot.getValue(BalanceModel.class);
                    assert balanceModel != null;
                    System.out.println(balanceModel.getCrBalance().toString());
                    double counterbalance = 0.0;
                    preDrBalance = balanceModel.getDrBalance();
                    preCrBalance = balanceModel.getCrBalance();
                    counterbalance = preDrBalance - preCrBalance;
                    String netBalance = String.valueOf(counterbalance);
                    previousBalance.setText(netBalance);

                } else {
                    hasPreviousDue = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void saveDueAmount() {

        balanceTk = totalBalance.getText().toString().trim();
        String key = db_balance_info.push().getKey();
        if (balanceTk.equals("")) {
            Toast.makeText(PaidCalculationActivity.this, "Please add amount TK", Toast.LENGTH_SHORT).show();
        } else {
            Date date = new Date();
            String transactionDate = SimpleDateFormat.getDateTimeInstance().format(date);
            totalAmount = Double.parseDouble(balanceTk);
            BalanceModel balanceModel = new BalanceModel(userid, cusId, 0.0, totalAmount, transactionDate);
            db_balance.setValue(balanceModel);
            assert key != null;
            db_balance_info.child(key).setValue(balanceModel);
            getPreviousDueBalance();
            Toast.makeText(PaidCalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDueAmount() {

        balanceTk = totalBalance.getText().toString().trim();
        String key = db_balance_info.push().getKey();
        if (balanceTk.equals("")) {
            Toast.makeText(PaidCalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();
        } else {
            Date date = new Date();
            String transactionDate = SimpleDateFormat.getDateTimeInstance().format(date);
            totalAmount = Double.parseDouble(balanceTk);
            Double netCrBalance = preCrBalance + totalAmount;

            BalanceModel balanceModel = new BalanceModel(userid, cusId, preDrBalance, netCrBalance, transactionDate);
            BalanceModel balanceInfoModel = new BalanceModel(userid, cusId, 0.0, totalAmount, transactionDate);
            db_balance.setValue(balanceModel);
            assert key != null;
            db_balance_info.child(key).setValue(balanceInfoModel);
            getPreviousDueBalance();
            Toast.makeText(PaidCalculationActivity.this, "update Successful", Toast.LENGTH_SHORT).show();

        }
    }
/*    private void savebalancetk() {
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
*//*                Double predrbalence = c.getDouble(3);
                Double precrbalence = c.getDouble(4);*//*
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

    }*/
}