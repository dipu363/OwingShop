package com.dipuj2ee.owing.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SmsActivity extends AppCompatActivity {



    public Context context;
    EditText editTextNumber, editTextMessage;
    UserModel userModel;
    Button sendButton;
    DatabaseReference dbUserinfo;
    FirebaseUser currentUser;
    FirebaseAuth auth;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("SEND MASSAGE");
        ActivityCompat.requestPermissions(SmsActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        editTextMessage = findViewById(R.id.editTextMessage);
        editTextNumber = findViewById(R.id.editTextNumber);
        sendButton = findViewById(R.id.sendButtonid);
        userModel = new UserModel();
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        dbUserinfo = FirebaseDatabase.getInstance().getReference("UsersInfo");
        setSms();
        bundle = getIntent().getExtras();
        String phoneNumber = bundle.getString("phone");
        editTextNumber.setText(phoneNumber);


        sendButton.setOnClickListener(view -> {

            sendSMS();
            clear();
        });

    }

    public void sendSMS(){

        String message = editTextMessage.getText().toString();
        String number = editTextNumber.getText().toString();

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null, null);


        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }


    private void setSms() {
        dbUserinfo.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.getChildrenCount());
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    userModel = ds.getValue(UserModel.class);
                    assert userModel != null;
                    if (userModel.getId().equals(currentUser.getUid())) {
                        break;
                    }

                    System.out.println(userModel.getUname());
                }
                String netBalance = bundle.getString("netBalance");
                String name = userModel.getUname();
                String phone = userModel.getPhone();
                String sms = name + "\n" + phone + "\nআপনার বাকি আছে" + netBalance + "টাকা\nধন্যবাদ";
                editTextMessage.setText(sms);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void clear() {

        editTextNumber.setText("");
        editTextMessage.setText("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}