package com.catchforms.vinod.rupesh;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class News extends AppCompatActivity {

    ListView news_list;
    //make news view and set data;
    networkcheck nc;
    static ArrayList title,description,news_url;
    ProgressBar news_progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        nc= new networkcheck();
        networkcheck.intialstatus=1;
        title = new ArrayList();
        description = new ArrayList();
        news_url = new ArrayList();
        news_progress =(ProgressBar)findViewById(R.id.news_progress);
        news_progress.setVisibility(View.VISIBLE);

        callTimesOFIndiavolley();

        news_list=(ListView)findViewById(R.id.news_list);

        ActionBar ab= getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        startRepeatingTask();
    }
    private void callTimesOFIndiavolley() {
        String url="https://newsapi.org/v1/articles?source=the-times-of-india&sortBy=latest&apiKey=ed72f99f60a6472f84e594401e2a514b";

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        final JsonObjectRequest jsonobject= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("ok")){
                        JSONObject news_response;
                        JSONArray jsonArray = response.getJSONArray("articles");
                        for(int i=0;i<jsonArray.length();i++){
                            news_response= jsonArray.getJSONObject(i);
                            Log.d("response111",news_response.toString());
                            title.add(news_response.getString("title"));
                            description.add(news_response.getString("description"));
                            news_url.add(news_response.getString("url"));
                        }
                        try{
                            the_Hindu();
                        }catch (Exception e){
                            news_list.setAdapter(new news_setter(News.this,title,description,news_url));
                            news_progress.setVisibility(View.INVISIBLE);
                        }
                    }

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
        queue.add(jsonobject);
    }

    private void the_Hindu() {
        String url="https://newsapi.org/v1/articles?source=the-hindu&sortBy=top&language=en&apiKey=ed72f99f60a6472f84e594401e2a514b";

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        final JsonObjectRequest jsonobject= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    if (response.getString("status").equals("ok")){
                        JSONObject news_response;
                        JSONArray jsonArray = response.getJSONArray("articles");
                        for(int i=0;i<jsonArray.length();i++){
                            news_response= jsonArray.getJSONObject(i);
                            Log.d("response111",news_response.toString());
                            title.add(news_response.getString("title"));
                            description.add(news_response.getString("description"));
                            news_url.add(news_response.getString("url"));
                        }

                        news_list.setAdapter(new news_setter(News.this,title,description,news_url));
                        news_progress.setVisibility(View.INVISIBLE);
                    }

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
        queue.add(jsonobject);

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
                nc.normaldialog(News.this);

            }
            handler.postDelayed(mHandlerTask,3000);
        }
    };


}
