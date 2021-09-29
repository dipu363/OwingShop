package com.dipuj2ee.owing.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.adapter.CustomerListAdapter;
import com.dipuj2ee.owing.model.CustomerInfoModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    // Declare Variables
    private ListView listView;
    FloatingActionButton flbtn;
    private CustomerListAdapter cuslistadapter;
    private SearchView editsearch;
    private List<CustomerInfoModel> customerlist;

    DatabaseReference cusInfo;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private String tag;
    public int numofCus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("CUSTOMERS");

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        editsearch = findViewById(R.id.customersearchid);
        editsearch.setOnQueryTextListener(this);

        listView = findViewById(R.id.cuslistviewid);
        flbtn = findViewById(R.id.fab);

        flbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentadd = new Intent(CustomerActivity.this, CustomerInfoActivity.class);
                startActivity(intentadd);
            }
        });
        customerlist = new ArrayList<>();
        cuslistadapter = new CustomerListAdapter(this, customerlist);
        getcustomerlist();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = customerlist.get(position).getName();
                String phone = customerlist.get(position).getPhone();
                String userid = customerlist.get(position).getUserid();
                String address = customerlist.get(position).getAddress();
                String cusid = customerlist.get(position).getId();

                Intent intent = new Intent(CustomerActivity.this,CustomerProfileActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("phone",phone);
                intent.putExtra("address",address);
                intent.putExtra("userid",userid);
                intent.putExtra("cusid",cusid);
                intent.putExtra("numofCus",numofCus);
                startActivity(intent);
                Toast.makeText(CustomerActivity.this, "Customer  "+ name + "  Is Clicked", Toast.LENGTH_SHORT).show();


            }
        });


    }



    public void getcustomerlist(){
        String  userid = mUser.getUid();
        cusInfo = FirebaseDatabase.getInstance().getReference("CustomerInfo").child(userid);
        //Toast.makeText(this, "getcustomerlist is called", Toast.LENGTH_SHORT).show();
        cusInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customerlist.clear();
                // numofCus = (int) dataSnapshot.getChildrenCount();
                    for(DataSnapshot listdata:dataSnapshot.getChildren()){
                           CustomerInfoModel customerNames = listdata.getValue(CustomerInfoModel.class);
                           // Binds all strings into an array
                           customerlist.add(customerNames);
                    }



                listView.setAdapter(cuslistadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();

        inflater.inflate(R.menu.example_menu_item,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addcusid:
                Intent intentadd = new Intent(CustomerActivity.this,CustomerInfoActivity.class);
                startActivity(intentadd);
                break;


            case R.id.menulogoutid:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(CustomerActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                finish();
                break;

            }
        return super.onOptionsItemSelected(item);

    }


    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        cuslistadapter.filter(newText);
        return true;
    }
}
