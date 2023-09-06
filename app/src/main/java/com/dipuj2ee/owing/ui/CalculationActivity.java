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

public class CalculationActivity extends AppCompatActivity implements View.OnClickListener {
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

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Calculator");
        setContentView(R.layout.activity_calculation);
        totalBalance = findViewById(R.id.totalbalanceid);
        previousBalance = findViewById(R.id.previusduetextid);
        singleBalance = findViewById(R.id.singelbalanceid);
        CardView savebt = findViewById(R.id.savecardbuttonid);
        CardView cancelbt = findViewById(R.id.cancelcardbuttonid);
        CardView plusbt = findViewById(R.id.plusbuttonid);
        CardView minusbt = findViewById(R.id.minusbuttonid);

        plusbt.setOnClickListener(this);
        minusbt.setOnClickListener(this);
        savebt.setOnClickListener(this);
        cancelbt.setOnClickListener(this);

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

    //this mathod use for back to previus page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
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
                String singlblance = String.valueOf(singleBalance.getText());
                if (singlblance.equals("")) {
                    Toast.makeText(this, "টাকার পরিমান লিখুন", Toast.LENGTH_SHORT).show();
                } else {
                    amount = Double.parseDouble(singlblance);
                    if (balance.equals("")) {
                        totalBalance.setText(String.valueOf(amount));
                        singleBalance.setText("");
                    } else {
                        totalAmount = Double.parseDouble(balance);
                        total = totalAmount + amount;
                        totalBalance.setText(String.valueOf(total));
                        singleBalance.setText("");
                    }
                }
                break;
            case R.id.minusbuttonid:
                balance = String.valueOf(totalBalance.getText());
                singlblance = String.valueOf(singleBalance.getText());
                if (singlblance.equals("")) {
                    Toast.makeText(this, "টাকার পরিমান লিখুন", Toast.LENGTH_SHORT).show();
                } else {
                    amount = Double.parseDouble(singlblance);
                    totalAmount = Double.parseDouble(balance);
                    total = totalAmount - amount;
                    totalBalance.setText(String.valueOf(total));
                    singleBalance.setText("");
                }

                break;

            case R.id.savecardbuttonid:

                if (!hasPreviousDue) {
                    saveDueAmount();
                } else {
                    updateDueAmount();
                }
            case R.id.cancelcardbuttonid:
                singleBalance.setText("");
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
                    double nettotalbalance = 0.0;
                    preDrBalance = balanceModel.getDrBalance();
                    preCrBalance = balanceModel.getCrBalance();
                    nettotalbalance = preDrBalance - preCrBalance;
                    String netduebalance = String.valueOf(nettotalbalance);
                    previousBalance.setText(netduebalance);

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
            Toast.makeText(CalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();
        } else {
            Date date = new Date();
            String trdate = SimpleDateFormat.getDateTimeInstance().format(date);
            totalAmount = Double.parseDouble(balanceTk);
            BalanceModel balanceModel = new BalanceModel(userid, cusId, totalAmount, 0.0, trdate);
            db_balance.setValue(balanceModel);
            assert key != null;
            db_balance_info.child(key).setValue(balanceModel);
            getPreviousDueBalance();
            Toast.makeText(CalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateDueAmount() {

        balanceTk = totalBalance.getText().toString().trim();
        String key = db_balance_info.push().getKey();
        if (balanceTk.equals("")) {
            Toast.makeText(CalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();
        } else {
            Date date = new Date();
            String trdate = SimpleDateFormat.getDateTimeInstance().format(date);
            //ystem.out.println("local date formet" +trdate);
            totalAmount = Double.parseDouble(balanceTk);
            Double netdrbalence = preDrBalance + totalAmount;
            BalanceModel balanceModel = new BalanceModel(userid, cusId, netdrbalence, preCrBalance, trdate);
            BalanceModel balanceinfoModel = new BalanceModel(userid, cusId, totalAmount, 0.0, trdate);
            db_balance.setValue(balanceModel);
            assert key != null;
            db_balance_info.child(key).setValue(balanceinfoModel);
            getPreviousDueBalance();
            Toast.makeText(CalculationActivity.this, "update Successful", Toast.LENGTH_SHORT).show();

        }
    }
/*    public void  rowcount(){
        Cursor c = sqLiteBD.getDebitTransection(cusid);
        int rowcount = c.getCount();
        if(rowcount > 100){
            Log.d("mas",String.valueOf(rowcount));
            delatetOneRaw();
            savebalancetk();

        }else{
            savebalancetk();
        }

    }
    public void delatetOneRaw(){
        Cursor c = sqLiteBD.getDebitTransection(cusid);

        if(c.moveToFirst()){
            int rawid = Integer.parseInt(c.getString(0));
            sqLiteBD.deleteFirstRaw(rawid);
            Log.d("mas", "Deleted rew id is :" + rawid);
        }
    }
    private void savebalancetk() {
        balancetk = totalbalance.getText().toString().trim();
        String key = db_balance_info.push().getKey();
        if(balancetk.equals("")){

            Toast.makeText(CalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();

        }else {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date();
            formatter.format(date);
            String trdate = formatter.format(date);
            totalamount = Double.parseDouble(balancetk);
            BalanceModel balanceModel = new BalanceModel(userid, cusid, totalamount, 0.0, trdate);
            sqLiteBD.addBalanceInfo(balanceModel);
           // db_balance.setValue(balanceModel);
           // getbalance();
            assert key != null;
            db_balance_info.child(key).setValue(balanceModel);
            //Toast.makeText(CalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();
        }
    }
    private void updatenetdrbalence() {
        balancetk = totalbalance.getText().toString().trim();
        String key = db_balance_info.push().getKey();
        if (balancetk.equals("")) {
            Toast.makeText(CalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();

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
                Double netdrbalence = preDrBalance + totalamount;
                BalanceModel balanceModel = new BalanceModel(userid, cusid, netdrbalence, preCrBalance, trdate);
                //sqLiteBD.addnetBalance(balanceModel);
                sqLiteBD.updateNetBalance(balanceModel, cusid);
                db_balance.setValue(balanceModel);
                getPreviousDueBalance();
                // db_balance_info.child(key).setValue(balanceModel);
                Toast.makeText(CalculationActivity.this, "update Successful", Toast.LENGTH_SHORT).show();

            } else {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                Date date = new Date();
                formatter.format(date);
                String trdate = formatter.format(date);
                totalamount = Double.parseDouble(balancetk);
                BalanceModel balanceModel = new BalanceModel(userid, cusid, totalamount, 0.0, trdate);
                sqLiteBD.addnetBalance(balanceModel);
                db_balance.setValue(balanceModel);
                getPreviousDueBalance();
                //db_balance_info.child(key).setValue(balanceModel);
                Toast.makeText(CalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();

            }


        }


    }*/
}