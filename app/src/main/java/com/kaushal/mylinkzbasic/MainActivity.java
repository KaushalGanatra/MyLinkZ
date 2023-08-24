package com.kaushal.mylinkzbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    Button login,btngooglelogin;
    ImageView avtar;
    EditText edt_email,edt_password;
    TextView tv_incorrect,tvRegister;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        login = (Button) findViewById(R.id.btnRegister);
        edt_email = (EditText) findViewById(R.id.edtName);
        edt_password = (EditText) findViewById(R.id.edtEmail);
        tv_incorrect = (TextView) findViewById(R.id.tv_incorrect);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        btngooglelogin = (Button) findViewById(R.id.btngooglelogin);
        avtar = (ImageView) findViewById(R.id.avtar);

        avtar.setImageResource(R.mipmap.ic_launcher);

        db = openOrCreateDatabase("mylinkz_db",MODE_PRIVATE,null);
        tv_incorrect.setVisibility(View.GONE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String baseSq = "select DISTINCT tbl_name from sqlite_master where tbl_name='user_details'";
                Cursor Basecursor = db.rawQuery(baseSq, null);
                if (Basecursor.moveToFirst()) {

                    String sq = "select detail from user_details where user='" + edt_email.getText().toString() + "' AND item='Password'";
                    Cursor cursor = db.rawQuery(sq, null);
                    if (cursor.moveToFirst()) {
                        if (edt_password.getText().toString().equals(cursor.getString(0))) {
                            Intent i = new Intent(MainActivity.this, Dashboard.class);
                            startActivity(i);

                            Toast toast = Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT);
                            toast.show();

                            Dashboard.UserEmail = edt_email.getText().toString();
                        } else {
                            tv_incorrect.setVisibility(View.VISIBLE);
                        }
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "User with this email dosen't Exist", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "User with this email dosen't Exist", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });

        btngooglelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    Integer counter=0;
    @Override
    public void onBackPressed()
    {
        if(counter==0){
            Toast toast = Toast.makeText(getApplicationContext(),"Do it again to exit from the application",Toast.LENGTH_SHORT);
            toast.show();
            counter++;
        }
        else {
            counter=0;
            finishAffinity();
        }
    }

    void signIn(){
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent,1000);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try{
                task.getResult(ApiException.class);
                NavigateToSecondActivity();}
            catch(ApiException e){
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void NavigateToSecondActivity(){
        finish();
        Intent i = new Intent(MainActivity.this, Dashboard.class);
        startActivity(i);
    }
}