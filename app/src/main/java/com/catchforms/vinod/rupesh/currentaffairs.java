package com.catchforms.vinod.rupesh;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class currentaffairs extends AppCompatActivity {
    ListView lv1;
    networkcheck nc;
    ProgressBar yojna_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkcheck.checkInternetConnection(getApplicationContext());
        setContentView(R.layout.activity_currentaffairs);

        nc= new networkcheck();
        networkcheck.intialstatus=1;

        yojna_progress =(ProgressBar)findViewById(R.id.yojna_progress);
        yojna_progress.setVisibility(View.VISIBLE);

        callyojnavolley();

        lv1=(ListView)findViewById(R.id.current_listview);

        ActionBar ab= getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        startRepeatingTask();
    }


    private void callyojnavolley() {
        String url="http://catchforms.in/vinod/catchforms/user_data/yojna.php";

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        JsonArrayRequest jsonarray= new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList website = new ArrayList();
                final ArrayList yojna_url = new ArrayList();
                try {
                    int length = response.length();
                    for(int i=0; i<length;i++){
                        JSONObject jsonobject = response.getJSONObject(i);
                        website.add(jsonobject.getString("Title"));
                        yojna_url.add(jsonobject.get("Url"));
                    }
                    lv1.setAdapter(new myweb(currentaffairs.this,website,yojna_url));
                    yojna_progress.setVisibility(View.INVISIBLE);

                }catch(Exception e){

                    Log.d("datatotal","exception");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }


        );
        queue.add(jsonarray);
    }

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

//    void stopRepeatingTask()
//    {
//        handler.removeCallbacks(mHandlerTask);
//    }

    Handler handler=new Handler();

    Runnable mHandlerTask =new Runnable() {
        @Override
        public void run() {
            if(!networkcheck.checkInternetConnection(getApplicationContext()))
            {
                nc.normaldialog(currentaffairs.this);

            }
            handler.postDelayed(mHandlerTask,3000);
        }
    };


}
