package com.catchforms.vinod.rupesh;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ScrollingActivity extends AppCompatActivity {

    TextView data;
    ProgressBar progressBar;
    networkcheck nc;
    static String stringresponse;
    static int loading=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("title"));
        setSupportActionBar(toolbar);

        nc= new networkcheck();
        data=(TextView)findViewById(R.id.displaydata);
        Typeface font=Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
        data.setTypeface(font);
        progressBar=(ProgressBar)findViewById(R.id.dataprogress);


        callvolley(information.functionname,information.data);


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Loading", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                startActivity(new Intent(getApplicationContext(),applyonline.class));
//            }
//        });

        startRepeatingTask();
    }
    Handler handler= new Handler();

    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run() {

            if(!networkcheck.checkInternetConnection(getApplicationContext())){
                nc.normaldialog(ScrollingActivity.this);
            }

//            else if(data.getText()!=null) {
//
//                progressBar.setVisibility(View.INVISIBLE);
//                stopRepeatingTask();
//            }
            handler.postDelayed(mHandlerTask, 5000);
        }
    };

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        handler.removeCallbacks(mHandlerTask);
    }

    public void callvolley(String functionname,String datafile) {

        String url="http://catchforms.in/vinod/catchforms/datagetter.php?databasename="+functionname+"&file="+datafile+".txt";
        final RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());

        final StringRequest stringRequest= new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading=1;

                Integer i=loading;
                Log.d("load",i.toString());
                progressBar.setVisibility(View.INVISIBLE);
                stopRepeatingTask();
                data.setText(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("data","Volley Error");
            }
        });

        requestQueue.add(stringRequest);
    }
}

