package com.dipuj2ee.owing.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.model.CustomerInfoModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CustomerInfoActivity extends AppCompatActivity {


    private EditText name,address,phone;
    private Button savebtn;
    DatabaseReference db_customer;
    FirebaseAuth mAuth;
    FirebaseUser fUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.cusnameid);
        address=findViewById(R.id.cusaddressid);
        phone = findViewById(R.id.cuscontactid);
        savebtn = findViewById(R.id.cusbtnid);

        db_customer = FirebaseDatabase.getInstance().getReference("CustomerInfo");
        mAuth = FirebaseAuth.getInstance();
        fUser =mAuth.getCurrentUser();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveCustomerInfo();
                clear();
                Toast.makeText(CustomerInfoActivity.this, "Save Successful", Toast.LENGTH_LONG).show();


            }
        });

    }



    private void saveCustomerInfo() {
        String cname = name.getText().toString().trim();
        String caddress = address.getText().toString().trim();
        String cphone = phone.getText().toString().trim();
        String userid = fUser.getUid();

        if(TextUtils.isEmpty(cname)){
            name.setError("Please type Name");
            name.requestFocus();

        } if(TextUtils.isEmpty(caddress)){
            address.setError("Please type Address");
            address.requestFocus();

        } if(TextUtils.isEmpty(cphone)){
            phone.setError("Please type Valid Phone Number");
            phone.requestFocus();

        } else{

           String key=  db_customer.push().getKey();
            CustomerInfoModel cusmodel = new CustomerInfoModel(key,cname,caddress,cphone,userid);
            db_customer.child(userid).child(key).setValue(cusmodel);


        }


    }

    private void clear() {
        name.setText("");
        address.setText("");
        phone.setText("");

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
