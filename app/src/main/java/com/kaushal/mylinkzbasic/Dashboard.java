package com.kaushal.mylinkzbasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class Dashboard extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    TextView tvUserName,tvUserEmail,fabtv1,fabtv2,fabtv3,tvDesc;
    ImageView UserProPic;
    Button btnSignout;
    FloatingActionButton fab, fab1, fab2,fab3,fabadd;
    Animation fabOpen, fabClose;
    static String UserName,UserEmail;
    boolean isOpen = false;
    SQLiteDatabase db;
    LinearLayout linear_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        UserProPic=(ImageView)findViewById(R.id.avtar);
        tvUserEmail=(TextView)findViewById(R.id.tvUserEmail);
        tvUserName=(TextView)findViewById(R.id.tvUserName);
        btnSignout=(Button)findViewById(R.id.btn_signout);
        tvDesc=(TextView)findViewById(R.id.tv_desc);
        linear_layout=(LinearLayout)findViewById(R.id.linear_layout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fabadd = (FloatingActionButton) findViewById(R.id.fabadd);
        fabtv1=(TextView)findViewById(R.id.fabtv1);
        fabtv2=(TextView)findViewById(R.id.fabtv2);
        fabtv3=(TextView)findViewById(R.id.fabtv3);
        fabOpen = AnimationUtils.loadAnimation
                (this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation
                (this,R.anim.fab_close);

        db = openOrCreateDatabase("mylinkz_db",MODE_PRIVATE,null);
        String sq = "select detail from user_details where item='Name' AND user='"+UserEmail.toString()+"'";
        Cursor cursor = db.rawQuery(sq,null);
        if(cursor.moveToFirst())
        {
            UserName=cursor.getString(0);
        }

        String sq2 = "select detail from user_details where item='avatar' AND user='"+UserEmail.toString()+"'";
        Cursor cursor2 = db.rawQuery(sq2,null);
        if(cursor2.moveToFirst())
        {
            if(cursor2.getString(0).equals("avtar_m1")){UserProPic.setImageResource(R.drawable.avtar_m1);}
            else if(cursor2.getString(0).equals("avtar_m2")){UserProPic.setImageResource(R.drawable.avtar_m2);}
            else if(cursor2.getString(0).equals("avtar_m3")){UserProPic.setImageResource(R.drawable.avtar_m3);}
            else if(cursor2.getString(0).equals("avtar_f1")){UserProPic.setImageResource(R.drawable.avtar_f1);}
            else if(cursor2.getString(0).equals("avtar_f2")){UserProPic.setImageResource(R.drawable.avtar_f2);}
            else if(cursor2.getString(0).equals("avtar_f3")){UserProPic.setImageResource(R.drawable.avtar_f3);}
            else if(cursor2.getString(0).equals("ic")){UserProPic.setImageResource(R.mipmap.ic_launcher);}
        }

        try {
            String sq3 = "select detail from user_details where item='bio' AND user='" + UserEmail.toString() + "'";
            Cursor cursor3 = db.rawQuery(sq3, null);

            if (cursor3.moveToFirst()) {
                tvDesc.setText(cursor3.getString(0));
            }
        }catch (Exception e){tvDesc.setText("");}

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null){
            UserName=acct.getDisplayName();
            UserEmail=acct.getEmail();
            Picasso.get().load(acct.getPhotoUrl()).error(R.mipmap.ic_launcher).into(UserProPic);
        }

        tvUserEmail.setText(UserEmail);
        tvUserName.setText(UserName);

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this,SharePage.class);
                startActivity(i);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(Dashboard.this,friend.class);
                startActivity(i);
            }
        });

        fabadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this,AddLink.class);
                startActivity(i);
            }
        });

        viewdata();

    }

    void signOut(){
        gsc.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                finish();
                startActivity(new Intent(Dashboard.this,MainActivity.class));
            }
        });
    }


    private void animateFab(){
        if (isOpen){
            fab1.startAnimation(fabClose);
            fab2.startAnimation(fabClose);
            fab3.startAnimation(fabClose);
            fabtv1.startAnimation(fabClose);
            fabtv2.startAnimation(fabClose);
            fabtv3.startAnimation(fabClose);
            fab1.setClickable(false);
            fab2.setClickable(false);
            fab3.setClickable(false);
            fabtv1.setClickable(false);
            fabtv2.setClickable(false);
            fabtv3.setClickable(false);
            fab.setImageResource(R.drawable.ic_menu);
            isOpen=false;
        }else {
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            fabtv1.startAnimation(fabOpen);
            fabtv2.startAnimation(fabOpen);
            fabtv3.startAnimation(fabOpen);
            fab.setImageResource(R.drawable.ic_close);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isOpen=true;
        }
    }

    public void viewdata() {
        try {
            View childview;
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String sq = "select * from user_links where user='" + Dashboard.UserEmail + "'";
            Cursor cursor = db.rawQuery(sq, null);

            if (cursor.moveToFirst()) {
                do {
                    childview = inflater.inflate(R.layout.buttonwithoutdelete, null);
                    linear_layout.addView(childview);

                    Button bd_btn_link = (Button) childview.findViewById(R.id.btn_link);
                    bd_btn_link.setText(cursor.getString(0));
                    String cursor2 = cursor.getString(1);

                    bd_btn_link.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Uri uri = Uri.parse(cursor2.toString());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });
                } while (cursor.moveToNext());
            }
        }catch (Exception e){}
    }
}