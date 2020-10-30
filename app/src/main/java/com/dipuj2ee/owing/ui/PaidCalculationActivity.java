package com.dipuj2ee.owing.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.db.SQLiteDBHandeler;
import com.dipuj2ee.owing.model.BalanceModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PaidCalculationActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView totalbalance;
    private EditText singelbalance;
    private CardView savebt,cancelbt,plusbt,minusbt;
    private String name,phone,address,userid,cusid,balance,singlblance , balancetk;
    private Double amount,totalamount,total ;


    DatabaseReference db_customer;
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

        Bundle bundle = getIntent().getExtras();
        name = bundle.getString("name");
        phone = bundle.getString("phone");
        address = bundle.getString("address");
        userid = bundle.getString("userid");
        cusid = bundle.getString("cusid");

        db_customer = FirebaseDatabase.getInstance().getReference("Balance").child(cusid);
        mAuth = FirebaseAuth.getInstance();
        fUser =mAuth.getCurrentUser();
    }

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
            case R.id.cancelcardbuttonid:
                singelbalance.setText("");
                totalbalance.setText("");
                break;

        }

    }


    private void savebalancetk() {
        balancetk = totalbalance.getText().toString().trim();

        if (balancetk.equals("")) {
            Toast.makeText(PaidCalculationActivity.this, "Please Add amount TK", Toast.LENGTH_SHORT).show();

        } else {

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date date = new Date();
            formatter.format(date);
            String trdate = String.valueOf(formatter.format(date));
            totalamount = Double.parseDouble(balancetk);
            BalanceModel balanceModel = new BalanceModel(userid, cusid, 0.0, totalamount, trdate);
            sqLiteBD.addBalanceInfo(balanceModel);
            Toast.makeText(PaidCalculationActivity.this, "Save Successful", Toast.LENGTH_SHORT).show();


        }
    }
}