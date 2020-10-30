package com.dipuj2ee.owing.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dipuj2ee.owing.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Email_Verify_Activity extends AppCompatActivity {

    private TextView emailverify;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private Button buttonok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email__verify_);


        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        emailverify= findViewById(R.id.emailverificationid);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        buttonok=findViewById(R.id.btnemailverifyokid);


        if(user.isEmailVerified()){
            Bundle bundle = getIntent().getExtras();
            String email = bundle.getString("email");
            String password = bundle.getString("pass");
            userLogin(email,password);

        }  else {
            emailverify.setText("Email is not verified (click to verify email)");
            buttonok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Intent intent = new Intent(Email_Verify_Activity.this,LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(),"send a Email to your Email id please verify your Email",Toast.LENGTH_LONG).show();

                        }
                    });
                }
            });

        }


    }



    private void userLogin(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(Email_Verify_Activity.this,CustomerActivity.class));

                        }else{
                            Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_LONG).show();


                        }
                    }
                });



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