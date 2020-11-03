package com.dipuj2ee.owing.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.model.CustomerInfoModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {



    private EditText useremail,userpasswordid;
    private Button login, signup;
    FirebaseAuth auth;
    FirebaseUser user;
    private Intent HomeActivity;
    private ProgressBar loginProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      /*  ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);*/

        login= findViewById(R.id.btn_login);
        signup= findViewById(R.id.btn_signup);
        loginProgress= findViewById(R.id.login_progress);
        useremail= findViewById(R.id.emailedittextid);
        userpasswordid = findViewById(R.id.edtPassword);
        signup.setOnClickListener(this);
        login.setOnClickListener(this);
        loginProgress.setVisibility(View.INVISIBLE);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();





    }

    @Override
    protected void onStart() {
        super.onStart();
        if(user != null) {
            //user is already login  so we need to redirect him to home page
            updateUI();

        }
    }



    private void updateUI() {
        Intent intent = new Intent(LoginActivity.this, CustomerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                netWorkCheck();
                break;

            case R.id.btn_signup:
                Intent intent =new Intent(LoginActivity.this,SignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;


        }


    }


public void signIn(String Uemail,String Upass){
    loginProgress.setVisibility(View.VISIBLE);
    login.setVisibility(View.INVISIBLE);
    auth.signInWithEmailAndPassword(Uemail,Upass)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){
                        login.setVisibility(View.VISIBLE);
                        loginProgress.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this,"Congratulation ",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this,CustomerActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        clear();

                    }else{
                        login.setVisibility(View.VISIBLE);
                        loginProgress.setVisibility(View.INVISIBLE);
                        Toast.makeText(LoginActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();


                    }
                }
            });



}

    // check mobile net work status and then call checkvalidity method ;
    public void netWorkCheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){

            checkValidity();
        }
        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(" NO NetWork")
                    .setMessage("Enable Mobile Network")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.cancel();
                }
            });
            final AlertDialog alertDialog = builder.create();
            alertDialog.show();

        }

    }


    private void checkValidity(){
        String uemail = useremail.getText().toString();
        String upass = userpasswordid.getText().toString();

        int pass = upass.length();


        if(TextUtils.isEmpty(uemail)){
            useremail.setError("Please Type valid Email Address");
            useremail.requestFocus();
        }
        else if(TextUtils.isEmpty(upass)){
            userpasswordid.setError("Pleases Type Password");
            userpasswordid.requestFocus();
        }
        else if(pass < 6){
            userpasswordid.setError("Pleases Type Password at least 6 character");
            userpasswordid.requestFocus();

        }

        else{
            signIn(uemail,upass);
        }


    }

    private void clear() {
        useremail.setText("");
        userpasswordid.setText("");
    }

}
