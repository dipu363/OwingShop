package com.dipuj2ee.owing.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.dipuj2ee.owing.R;
import com.dipuj2ee.owing.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private EditText uname, uemail,uphone,upassword;
    private Button btncountinu;
    private FirebaseAuth mAuth;
    private TextView doclogintaxt;
    private ProgressBar loadingProgress;
    private DatabaseReference db_users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        actionBar .setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        loadingProgress = findViewById(R.id.regProgressBar);
        uname= findViewById(R.id.edit_name);
        uemail = findViewById(R.id.edt_email);
        uphone = findViewById(R.id.edt_phoneNo);
        upassword = findViewById(R.id.edt_password);
        btncountinu = findViewById(R.id.btnSignUpContinue);
        doclogintaxt = findViewById(R.id.docLogin);
        btncountinu.setOnClickListener(this);
        doclogintaxt.setOnClickListener(this);
        loadingProgress.setVisibility(View.INVISIBLE);
        mAuth = FirebaseAuth.getInstance();
        db_users = FirebaseDatabase.getInstance().getReference("UsersInfo");


    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnSignUpContinue:
                netWorkCheck();
                break;
            case R.id.docLogin:
                Intent intent =new Intent(SignUpActivity.this,LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }


    }
    //create user accountmathod

    private void CreateUserAccount(final String email,final String name,final String phone, final String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        String userid = firebaseUser.getUid();
                                        String key = db_users.push().getKey();
                                        UserModel userModel = new UserModel(userid,name,phone,email,password);
                                        db_users.child(key).setValue(userModel);
                                        emailverify(email,password);

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(SignUpActivity.this, "Email All Ready Used Or Invalid",
                                                Toast.LENGTH_LONG).show();
                                       // startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                                        btncountinu.setVisibility(View.VISIBLE);
                                        loadingProgress.setVisibility(View.INVISIBLE);

                                    }

                                    // ...
                                }
                            });






    }

    public void emailverify(String email,String pass){

        Intent intent = new Intent(SignUpActivity.this,Email_Verify_Activity.class);
        intent.putExtra("email",email);
        intent.putExtra("pass",pass);
        finish();
        startActivity(intent);

    }



/*    public void signIn(String Uemail,String Upass){
        mAuth.signInWithEmailAndPassword(Uemail,Upass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful()){
                            showMessage("congratulation");
                            startActivity(new Intent(SignUpActivity.this,CustomerActivity.class));
                        }
                    }
                });

    }*/
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
    // check validity ui field and call register method ;

    private void checkValidity(){
        final  String name = uname.getText().toString().trim();
        final String email = uemail.getText().toString().trim();
        final String phone = uphone.getText().toString().trim();
        final String password = upassword.getText().toString().trim();

        int passlenth = password.length();
        //checking the validity of the email
        if (TextUtils.isEmpty(name)) {
            uname.setError("Enter your name");
            uname.requestFocus();
        } else if (TextUtils.isEmpty(email)) {

            uemail.setError("Enter a valid email address");
            uemail.requestFocus();

        }

        //checking the validity of the password
        else if (TextUtils.isEmpty(phone)) {
            uphone.setError("Enter your phone No");
            uphone.requestFocus();
        } else if (TextUtils.isEmpty(password) ) {
            upassword.setError("Enter your password");
            upassword.requestFocus();
        }
        else if (passlenth < 6 ) {
            upassword.setError("Password Entered At Least 6 Characters ");
            upassword.requestFocus();
        }
        else {
            btncountinu.setVisibility(View.INVISIBLE);
            loadingProgress.setVisibility(View.VISIBLE);
            CreateUserAccount(email,name,phone,password);
            clear();


        }


    }



    // simple method to show toast message
    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }


          //user login mathod
    public void clear(){
        uname.setText("");
        uemail.setText("");
        uphone.setText("");
        upassword.setText("");
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
