package com.catchforms.vinod.rupesh;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile extends AppCompatActivity {

    CircleImageView profile;
    TextView name;
    TextView mobile;
    TextView center_name;
    TextView center_address;
    TextView rest;
    RatingBar ratingBar;
    ArrayList arrayList ;
    ArrayAdapter arrayAdapter;
    ListView listView;
    Button edit,delete,submit_rating;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    static int transferred=0;
    static String person_id;
    AlertDialog.Builder build;
    RatingBar dialog_rating;
    ImageButton close;
    LinearLayout call_rating;
    AlertDialog dialog_build;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initialise();

        if (transferred==1){
            person_id = getIntent().getStringExtra("person_id");
        }
        else {
            person_id = sp.getString("personId","notpresent");
        }
        callvolley();
        settingdata();
    }

    private void initialise() {
        profile = (CircleImageView) findViewById(R.id.profilepage_imag);
        sp=getSharedPreferences("Data",MODE_PRIVATE);
        ed=sp.edit();
        getSupportActionBar().hide();
        name=(TextView)findViewById(R.id.profilepage_name);
        mobile=(TextView)findViewById(R.id.mobile_no);
        center_name =(TextView)findViewById(R.id.profile_center_name);
        center_address = (TextView)findViewById(R.id.profile_center_address);
        rest = (TextView)findViewById(R.id.profilepage_rest);
        ratingBar = (RatingBar)findViewById(R.id.MyRating);
        arrayList = new ArrayList();
        arrayAdapter = new ArrayAdapter(this, R.layout.centertypelist,arrayList);
        listView = (ListView)findViewById(R.id.centertype);
        edit = (Button)findViewById(R.id.profile_edit);
        delete =(Button)findViewById(R.id.profile_delete);
        call_rating = (LinearLayout)findViewById(R.id.call_rating);

        if (transferred !=1){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteprofile();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent intent = new Intent(getApplicationContext(), register_form.class);
                intent.putExtra("check","1");
                startActivity(intent);
            }
        });

        }
        else{
            delete.setVisibility(View.INVISIBLE);
            edit.setVisibility(View.INVISIBLE);
            call_rating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ratingDialogBox();
                }
            });
        }
    }


    private void deleteprofile() {
        String url="http://catchforms.in/vinod/catchforms/user_data/deleteaccount.php?userid="+person_id;

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(Integer.parseInt(response.getString("status"))==1){
                        new LoginUser().logout(sp);
                        Toast.makeText(getApplicationContext(),"Deleted Successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), firstscreen.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Deletion Failed",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), firstscreen.class));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Deletion Failed",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }


        );
        queue.add(jsonObjectRequest);

    }

    static String image_path;
    private void settingdata() {


       try{
        image_path  = Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Catchuser/profile.jpg";
       }
       catch (Exception e){
           e.printStackTrace();
       }

        File profilepic_image = new File(image_path);

        if (transferred !=1) {
            if (profilepic_image.exists()) {

                try {
                    Bitmap b = BitmapFactory.decodeStream(new FileInputStream(image_path));
                    profile.setImageBitmap(b);
                    Log.d("profLoad", "yes");
                    //        imageView.setImageDrawable(Drawable.createFromPath(image_path));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void callvolley() {
        String url="http://catchforms.in/vinod/catchforms/user_data/fetchingalldata.php?userid="+person_id;

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        Log.d("loggedid",sp.getString("personId","notpresent"));

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("datatotal",response.toString());
                    name.setText(response.getString("Name"));
                    mobile.setText("+91 "+response.getString("Contact_no"));
                    center_name.setText("Center Name : "+response.getString("Center_name").replace('_',' '));
                    center_address.setText("Center Address : "+response.getString("Center_address").replace('_',' '));
                    if(transferred !=1){
                        ed.putString("name",response.getString("Name"));
                        ed.commit();
                    }
                    if(Integer.parseInt(response.getString("Aadhar_card"))==1)
                    {
                        arrayList.add("Aadhar ");

                    }
                    if(Integer.parseInt(response.getString("Voterid_card"))==1)
                    {
                        arrayList.add("Voter ");
                    }
                    if(Integer.parseInt(response.getString("Pan_card"))==1)
                    {
                        arrayList.add("Pan ");
                    }
                    if(Integer.parseInt(response.getString("Rest_other"))==1)
                    {
                        arrayList.add("Other ");
                    }
                    try{
                        rest.setText("Rest : "+response.getString("Rest").replace('_',' '));
                    }catch (Exception e){
                        e.printStackTrace();
                        rest.setText("Rest : ----");
                    }
                    listView.setAdapter(arrayAdapter);
                    ratingBar.setRating(Float.parseFloat(response.getString("Rating")));

                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("datatotal","exception");
                    startActivity(new Intent(getApplicationContext(),firstscreen.class));
                    Toast.makeText(getApplicationContext(),"Not Register As Cafe Center",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }


        );
        queue.add(jsonObjectRequest);
    }



    private void ratingDialogBox() {

        View alert_view = getLayoutInflater().inflate(R.layout.rating,null);
        dialog_rating = (RatingBar) alert_view.findViewById(R.id.give_rating);
        close =(ImageButton)alert_view.findViewById(R.id.rating_dialog_cancel);

        build = new AlertDialog.Builder(this);
        build.setView(alert_view);
        submit_rating =(Button)alert_view.findViewById(R.id.submit_rating);
        submit_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Rated : "+String.valueOf(dialog_rating.getRating()),Toast.LENGTH_SHORT).show();
                submitrating(String.valueOf(dialog_rating.getRating()));
            }


        });
        try{
           dialog_build=build.create();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_build.dismiss();
                }
            });
            dialog_build.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void submitrating(final String rating) {
        String url="http://catchforms.in/vinod/catchforms/user_data/rating.php?loggedid="+person_id+"&userrating="+rating;

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(Integer.parseInt(response.getString("status"))==401){
                        Toast.makeText(getApplicationContext(),"Rated : "+rating,Toast.LENGTH_SHORT).show();

                        dialog_build.dismiss();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();

                        dialog_build.dismiss();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Deletion Failed",Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal","volleyerror");
            }
        }


        );
        queue.add(jsonObjectRequest);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        transferred =0;
        if (transferred !=1) {
            startActivity(new Intent(getApplicationContext(), firstscreen.class));
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        transferred =0;
    }
}
