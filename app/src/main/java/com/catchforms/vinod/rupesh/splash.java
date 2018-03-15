package com.catchforms.vinod.rupesh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class splash extends AppCompatActivity {
    ProgressBar pb;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
//        splash=(TextView)findViewById(R.id.text_splash);
//        Typeface font=Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
//        splash.setTypeface(font);

//        pb = (ProgressBar) findViewById(R.id.progress);
//
        sp=getSharedPreferences("Data",MODE_PRIVATE);
        editor = sp.edit();
        t1.start();

    }

    Thread t1 = new Thread() {
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

//            SharedPreferences sharedPreferences=getSharedPreferences("my",MODE_PRIVATE);
//
//            int logstate=sharedPreferences.getInt("status",2);
             Intent i = new Intent(getApplicationContext(),firstscreen.class);
                startActivity(i);
            finish();
            if(sp.getInt("app_state",2)==1)
            {
                 Intent intent = new Intent(getApplicationContext(), firstscreen.class);
                startActivity(intent);
                finish();
            }
            else{
               Intent intent = new Intent(getApplicationContext(), slideview.class);
                editor.putInt("app_state",1);
                editor.commit();
               startActivity(intent);
                finish();
            }


        }
    };
}