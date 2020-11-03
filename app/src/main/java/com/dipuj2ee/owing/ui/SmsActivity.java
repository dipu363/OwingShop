package com.dipuj2ee.owing.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dipuj2ee.owing.R;

public class SmsActivity extends AppCompatActivity {
     EditText editTextNumber,editTextMessage;

     ImageButton sendButton,addbtn;
     private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        ActivityCompat.requestPermissions(SmsActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);

        editTextMessage = findViewById(R.id.editTextMessage);
        editTextNumber = findViewById(R.id.editTextNumber);
        sendButton = findViewById(R.id.sendButtonid);

        Bundle bundle = getIntent().getExtras();
        phoneNumber = bundle.getString("phone");
        editTextNumber.setText(phoneNumber);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendSMS();
                clear();
            }
        });

    }

    public void sendSMS(){

        String message = editTextMessage.getText().toString();
        String number = editTextNumber.getText().toString();

       /* Intent intent=new Intent(getApplicationContext(),SmsActivity.class);
        intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pi=PendingIntent.getActivity(getApplicationContext(), 0, intent,0);*/

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(number, null, message, null,null);



        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }
    public void clear(){

        editTextNumber.setText("");
        editTextMessage.setText("");
    }
}