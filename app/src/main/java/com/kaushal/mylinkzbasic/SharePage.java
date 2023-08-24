package com.kaushal.mylinkzbasic;



import static com.kaushal.mylinkzbasic.Dashboard.UserEmail;
import static com.kaushal.mylinkzbasic.Dashboard.UserName;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import androidmads.library.qrgenearator.QRGEncoder;

public class SharePage extends AppCompatActivity {

    // variables for imageview, edittext,
    // button, bitmap and qrencoder.
    private ImageView qrCodeIV;
    Button btn_scan;
    TextView fabtv1,fabtv2,fabtv3;
    FloatingActionButton fab, fab1, fab2,fab3;
    Animation fabOpen, fabClose;
    boolean isOpen = false;
    private static String sText;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_page);

        qrCodeIV = findViewById(R.id.idIVQrcode);
        btn_scan = findViewById(R.id.btn_scan);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fabtv1=(TextView)findViewById(R.id.fabtv1);
        fabtv2=(TextView)findViewById(R.id.fabtv2);
        fabtv3=(TextView)findViewById(R.id.fabtv3);
        fabOpen = AnimationUtils.loadAnimation
                (this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation
                (this,R.anim.fab_close);

        sText = "MyLinkZ,"+UserName+","+UserEmail;
        db = openOrCreateDatabase("mylinkz_db",MODE_PRIVATE,null);

        String sq_detail = "select detail from user_details where item='avatar' AND user='"+UserEmail+"'";
        Cursor cursord = db.rawQuery(sq_detail,null);
        if(cursord.moveToFirst())
        {
            sText = sText + ',' + cursord.getString(0);
        }

        String sq_detail2 = "select detail from user_details where item='bio' AND user='"+UserEmail+"'";
        Cursor cursord2 = db.rawQuery(sq_detail2,null);
        if(cursord2.moveToFirst())
        {
            sText = sText + ',' + cursord2.getString(0);
        }

        String sq = "select * from user_links where user='"+ UserEmail+"'";
        //String sq2="select detail from user_details where item='Image'";
        //Cursor cursor2 = db.rawQuery(sq2,null);
        Cursor cursor = db.rawQuery(sq, null);
        if (cursor.moveToFirst()) {
            do {
                String temp;
                temp = cursor.getString(0) + "^" + cursor.getString(1);
                sText = sText + "," + temp;
            } while (cursor.moveToNext());
        }

  /*      if(cursor2.moveToFirst()){
            String temp="Image^"+cursor2.getString(0);
            sText=sText+","+temp;
        }*/

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE, 350, 350);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            qrCodeIV.setImageBitmap(bitmap);
            //InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            //manager.hideSoftInputFromWindow(dataEdt.getApplicationWindowToken(),0);
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }


      btn_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
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
                Intent i = new Intent(SharePage.this,Dashboard.class);
                startActivity(i);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(SharePage.this,friend.class);
                startActivity(i);
            }
        });
    }

    private void scanCode()
    {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            //Alert box to conform if user wants to add QR data to database
            AlertDialog.Builder builder = new AlertDialog.Builder(SharePage.this);
            builder.setTitle("New Friend");
            //calling username_return function to split data got from qr
            String[] uname = username_return(result.getContents());
            if(uname[0].equals("MyLinkZ")) {
            //  String temp = "Are you sure you want to add data of "+uname+" in your Friend List?";
            builder.setMessage("Are you sure you want to add data of " + uname[1]+"("+uname[2]+")" + " in your Friend List?");
            builder.setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try {
                                dialogInterface.dismiss();
                                //create friends table and enter data in it
                                String cq = "create table if not exists user_friends(fname varchar,femail varchar,fdetails varchar,user varchar)";
                                db.execSQL(cq);

                                String iq = "insert into user_friends values('" + uname[1] + "','" + uname[2] + "','" + result.getContents().toString() + "','" + UserEmail + "')";
                                db.execSQL(iq);

                                Intent intent = new Intent(SharePage.this,friend.class);
                                startActivity(intent);
                            }catch (Exception e){
                                Log.i("E",e.toString());}
                        }
                    })


                    .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).show();
        } else{
                builder.setMessage("Invalid QR! Not generated by this app");

                builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
            }
    }
    });

    //to split the string
    private String[] username_return(String res)
    {
        String name="";
        String[] temp = res.split(",");
        return temp;
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
            fab.setImageResource(R.drawable.ic_close);
            fab1.startAnimation(fabOpen);
            fab2.startAnimation(fabOpen);
            fab3.startAnimation(fabOpen);
            fabtv1.startAnimation(fabOpen);
            fabtv2.startAnimation(fabOpen);
            fabtv3.startAnimation(fabOpen);
            fab1.setClickable(true);
            fab2.setClickable(true);
            fab3.setClickable(true);
            isOpen=true;
        }
    }
}
