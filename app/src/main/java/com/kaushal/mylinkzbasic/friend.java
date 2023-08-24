package com.kaushal.mylinkzbasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kaushal.mylinkzbasic.databinding.ActivityFriendBinding;
import com.kaushal.mylinkzbasic.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class friend extends AppCompatActivity {

    TextView fabtv1,fabtv2,fabtv3;
    FloatingActionButton fab, fab1, fab2,fab3;
    Animation fabOpen, fabClose;
    boolean isOpen = false;
    ActivityFriendBinding binding;
    LinearLayout ll;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab3);
        fabtv1 = (TextView) findViewById(R.id.fabtv1);
        fabtv2 = (TextView) findViewById(R.id.fabtv2);
        fabtv3 = (TextView) findViewById(R.id.fabtv3);
        fabOpen = AnimationUtils.loadAnimation
                (this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation
                (this, R.anim.fab_close);
        ll = (LinearLayout) findViewById(R.id.ll);

        ArrayList<String> names = new ArrayList<>();
        ArrayList<String> emails = new ArrayList<>();
        ArrayList<String> clips = new ArrayList<>();
        ArrayList<String> ProPic = new ArrayList<>();

        try {
            db = openOrCreateDatabase("mylinkz_db", MODE_PRIVATE, null);
            String sq = "select fdetails from user_friends where user='" + Dashboard.UserEmail + "'";
            Cursor cursor = db.rawQuery(sq, null);

            if (cursor.moveToFirst()) {
                do {
                    String temp = cursor.getString(0);
                    String tempclip = "";
                    Log.i("Temp", temp);
                    String[] div = temp.split(",");

               /* String[] div2 = div[1].split("^");
                names[count] = div2[1].toString();

                div2 = div[2].split("^");
                emails[count] = div2[1].toString();*/
                    names.add(div[1].toString());
                    emails.add(div[2].toString());
                    ProPic.add(div[3].toString());

                    for (Integer i = 5; i < div.length; i++) {
                        if (div[i].contains("Instagram")) {
                            tempclip = tempclip + "Instagram,";
                        } else if (div[i].contains("Facebook")) {
                            tempclip = tempclip + "Facebook,";
                        } else if (div[i].contains("Github")) {
                            tempclip = tempclip + "Github,";
                        } else if (div[i].contains("Linkedin")) {
                            tempclip = tempclip + "Linkedin,";
                        }
                    }
                    clips.add(tempclip.toString());
                    Log.i("Clips", clips.get(0));
                } while (cursor.moveToNext());

                Log.i("Name", names.toString());
                Log.i("Emails", emails.toString());
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"No Friends Added to the list",Toast.LENGTH_SHORT).show();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(friend.this, Dashboard.class);
                startActivity(i);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(friend.this, SharePage.class);
                startActivity(i);
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        View childview;
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (Integer i = 0; i < names.size(); i++) {
            childview = inflater.inflate(R.layout.lst_item, null);
            ll.addView(childview);

            TextView tvUname = (TextView) childview.findViewById(R.id.tvUName);
            TextView tvUEmail = (TextView) childview.findViewById(R.id.tvUEmail);
            ImageView friendpropic = (ImageView) childview.findViewById(R.id.friendProPic);
            ImageView c0 = (ImageView) childview.findViewById(R.id.ic1);
            ImageView c1 = (ImageView) childview.findViewById(R.id.ic2);
            ImageView c2 = (ImageView) childview.findViewById(R.id.ic3);
            ImageView c3 = (ImageView) childview.findViewById(R.id.ic4);
            FloatingActionButton fab_next = (FloatingActionButton) childview.findViewById(R.id.fab_next);

            tvUname.setText(names.get(i));
            tvUEmail.setText(emails.get(i));

            Integer finalI = i;
            fab_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(friend.this,frienddetail.class);
                    intent.putExtra("Email",emails.get(finalI).toString());
                    startActivity(intent);
                }
            });

            if(ProPic.get(i).equals("avtar_m1"))
            {
                friendpropic.setImageResource(R.drawable.avtar_m1);
            }
            else if(ProPic.get(i).equals("avtar_m2")){friendpropic.setImageResource(R.drawable.avtar_m2);}
            else if(ProPic.get(i).equals("avtar_m3")){friendpropic.setImageResource(R.drawable.avtar_m3);}
            else if(ProPic.get(i).equals("avtar_f1")){friendpropic.setImageResource(R.drawable.avtar_f1);}
            else if(ProPic.get(i).equals("avtar_f2")){friendpropic.setImageResource(R.drawable.avtar_f2);}
            else if(ProPic.get(i).equals("avtar_f3")){friendpropic.setImageResource(R.drawable.avtar_f3);}
            else if(ProPic.get(i).equals("ic")){friendpropic.setImageResource(R.mipmap.ic_launcher);}

            String[] temp = clips.get(i).split(",");
            for (Integer j = 0; j < temp.length; j++) {
                if (j == 0) {
                    if (temp[j].equals("Instagram")) {
                        c0.setImageResource(R.drawable.ic_instagram);
                    } else if (temp[j].equals("Facebook")) {
                        c0.setImageResource(R.drawable.ic_facebook);
                    } else if (temp[j].equals("Linkedin")) {
                        c0.setImageResource(R.drawable.ic_linkedin);
                    } else if (temp[j].equals("Github")) {
                        c0.setImageResource(R.drawable.ic_github);
                    }
                } else if (j == 1) {
                    if (temp[j].equals("Instagram")) {
                        c1.setImageResource(R.drawable.ic_instagram);
                    } else if (temp[j].equals("Facebook")) {
                        c1.setImageResource(R.drawable.ic_facebook);
                    } else if (temp[j].equals("Linkedin")) {
                        c1.setImageResource(R.drawable.ic_linkedin);
                    } else if (temp[j].equals("Github")) {
                        c1.setImageResource(R.drawable.ic_github);
                    }
                } else if (j == 2) {
                    if (temp[j].equals("Instagram")) {
                        c2.setImageResource(R.drawable.ic_instagram);
                    } else if (temp[j].equals("Facebook")) {
                        c2.setImageResource(R.drawable.ic_facebook);
                    } else if (temp[j].equals("Linkedin")) {
                        c2.setImageResource(R.drawable.ic_linkedin);
                    } else if (temp[j].equals("Github")) {
                        c2.setImageResource(R.drawable.ic_github);
                    }
                } else if (j == 3) {
                    if (temp[j].equals("Instagram")) {
                        c3.setImageResource(R.drawable.ic_instagram);
                    } else if (temp[j].equals("Facebook")) {
                        c3.setImageResource(R.drawable.ic_facebook);
                    } else if (temp[j].equals("Linkedin")) {
                        c3.setImageResource(R.drawable.ic_linkedin);
                    } else if (temp[j].equals("Github")) {
                        c3.setImageResource(R.drawable.ic_github);
                    }
                }
            }
        }
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