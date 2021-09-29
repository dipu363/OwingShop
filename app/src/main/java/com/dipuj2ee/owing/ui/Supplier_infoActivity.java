package com.dipuj2ee.owing.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.model.SupplierModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Supplier_infoActivity extends AppCompatActivity {

    DatabaseReference db_supplier;
    FirebaseAuth mAuth;
    FirebaseUser fUser;
    private EditText name, srname, phone;
    private Button savebtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_info);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("ADD SUPPLIER");

        name = findViewById(R.id.supnameid);
        srname = findViewById(R.id.srnameid);
        phone = findViewById(R.id.supcontactid);
        savebtn = findViewById(R.id.suplierbtnid);

        db_supplier = FirebaseDatabase.getInstance().getReference("SupplierInfo");
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCustomerInfo();
                clear();
                Toast.makeText(Supplier_infoActivity.this, "Save Successful", Toast.LENGTH_LONG).show();


            }
        });


    }


    private void saveCustomerInfo() {
        String sname = name.getText().toString().trim();
        String supsrname = srname.getText().toString().trim();
        String sphone = phone.getText().toString().trim();
        String userid = fUser.getUid();

        if (TextUtils.isEmpty(sname)) {
            name.setError("Please type Name");
            name.requestFocus();

        }
        if (TextUtils.isEmpty(supsrname)) {
            srname.setError("Please type Address");
            srname.requestFocus();

        }
        if (TextUtils.isEmpty(sphone)) {
            phone.setError("Please type Valid Phone Number");
            phone.requestFocus();

        } else {

            String key = db_supplier.push().getKey();
            SupplierModel supmodel = new SupplierModel(key, userid, sname, supsrname, sphone);
            db_supplier.child(userid).child(key).setValue(supmodel);


        }


    }

    private void clear() {
        name.setText("");
        srname.setText("");
        phone.setText("");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}