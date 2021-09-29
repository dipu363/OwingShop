package com.dipuj2ee.owing.ui;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.adapter.SupplierListAdapter;
import com.dipuj2ee.owing.model.SupplierModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class SupplierListActivity extends AppCompatActivity {

    public int numofCus;
    FloatingActionButton flbtn;
    DatabaseReference cusInfo;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private ListView listView;
    private SupplierListAdapter suplistadapter;
    private SearchView editsearch;
    private List<SupplierModel> supplierlist;
    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_list);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("SUPPLIERS");


/*        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        editsearch = findViewById(R.id.suppliersearchid);
        editsearch.setOnQueryTextListener(this);

        listView = findViewById(R.id.suplistviewid);
        flbtn = findViewById(R.id.addsupplierfabid);

        flbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentadd = new Intent(SupplierListActivity.this,Supplier_infoActivity.class);
                startActivity(intentadd);
            }
        });
        supplierlist = new ArrayList<>();
        suplistadapter = new CustomerListAdapter(this,supplierlist);
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
        });*/


    }

/*    public void getallsupplier(){
        String  userid = mUser.getUid();
        cusInfo = FirebaseDatabase.getInstance().getReference("SupplierInfo").child(userid);
        //Toast.makeText(this, "getcustomerlist is called", Toast.LENGTH_SHORT).show();
        cusInfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                supplierlist.clear();
                numofCus = (int) dataSnapshot.getChildrenCount();
                for(DataSnapshot listdata:dataSnapshot.getChildren()){
                    Sup customerNames = listdata.getValue(CustomerInfoModel.class);
                    // Binds all strings into an array
                    supplierlist.add(customerNames);
                }



                listView.setAdapter(cuslistadapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }*/
/*
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }*/
}