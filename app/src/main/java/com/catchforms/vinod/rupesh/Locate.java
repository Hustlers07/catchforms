package com.catchforms.vinod.rupesh;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.provider.*;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import static com.catchforms.vinod.rupesh.networkcheck.intialstatus;

public class Locate extends AppCompatActivity {

    ListView lv;

    setLonglang latlong = new setLonglang();
    static boolean mypermission = false;

    Intent map;

    static boolean Locationenabled = false;
    SharedPreferences sp;
    SharedPreferences.Editor ed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locate);
        String list[] =getResources().getStringArray(R.array.mapdata);
        lv = (ListView) findViewById(R.id.list);
        loc_adap my = new loc_adap(this, list);
        lv.setAdapter(my);

        map = new Intent(getApplicationContext(),MapsActivity.class);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        sp=getSharedPreferences("Data",MODE_PRIVATE);
        ed=sp.edit();

        permission();


            onitemclick();


        startRepeatingTask();

    }

    private void permission() {
        if(Build.VERSION.SDK_INT >=23){
            if (ActivityCompat.checkSelfPermission(Locate.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Locate.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    public void onitemclick(){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                permission();
                    switch (i) {
                        case 0: {
                            map.putExtra("title","aadhar");
                            startActivity(map);
                            break;
                        }
                        case 1: {
                            map.putExtra("title","voterid");
                            startActivity(map);
                            break;
                        }
                        case 2: {
                            map.putExtra("title","pan");
                            startActivity(map);
                            break;
                        }
                        case 3: {
                            map.putExtra("title","ration");
                            startActivity(map);
                            break;
                        }

                    }

            }
        });
    }


    void startRepeatingTask() {
        mHandlerTask.run();
    }

    static int initialstatus;
    Handler handler = new Handler();

    Runnable mHandlerTask = new Runnable() {
        @Override
        public void run() {
            if (!checkInternetConnection(getApplicationContext())) {
                normaldialog(Locate.this);
                initialstatus = 1;

            }

            handler.postDelayed(mHandlerTask, 3000);
        }
    };

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
//        ConnectivityManager connectivityManager =context.(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());
    }

    public static void normaldialog(final Context context) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Check Internet connection and press retry");
        builder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if (intialstatus != 1) {
                    Activity activity = (Activity) context;
                    activity.finish();
                    activity.startActivity(((Activity) context).getIntent());
                } else if (intialstatus == 1) {
                    initialstatus = 0;
                }
            }
        });
        AlertDialog ad = builder.create();
        try {
            ad.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
