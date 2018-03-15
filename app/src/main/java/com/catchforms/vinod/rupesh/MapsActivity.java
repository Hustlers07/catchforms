package com.catchforms.vinod.rupesh;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.provider.*;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    networkcheck nc;
    LocationManager locationManager;
    //    LocationListener locationlistner;
    //    Marker current_location;
    static double latitude;
    static double longitude;
    Location location;
    RatingBar rate;
    TextView tvName,more;
    HashMap <Integer,String> hash_map_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    //    mMap = mapFragment.getMap();


        networkcheck.checkInternetConnection(getApplicationContext());
        nc = new networkcheck();

        hash_map_id = new HashMap<Integer, String>();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                return;
            }
        }
        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            try{
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            }catch (Exception e){}
        } else {
            Toast.makeText(getApplicationContext(), "GPS Required", Toast.LENGTH_LONG).show();
            enablegps();
        }

//        locationlistner = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                try {
////                    if(current_location != null){
////                        current_location.remove();
////                    }
//
//                    latitude = location.getLatitude();
//                    longitude = location.getLongitude();
//       //             LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
////                    setLonglang.setCurrent(latLng);
////                    Log.d("Longitude", String.valueOf(location.getLatitude()));
////                    current_location = googleMap.addMarker(new MarkerOptions().position(latLng).title("Me"));
//                }
//                catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//                Log.d("Latitude", "disabled");
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//                Log.d("Latitude", "enable");
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//                Log.d("Latitude", "status");
//            }
//        };
//
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationlistner);
//

        startRepeatingTask();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
    }

    private void enablegps() {
        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;

        if (mMap != null) {
            mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                @Override
                public View getInfoContents(final Marker marker) {

                    View v = getLayoutInflater().inflate(R.layout.map_snippet, null);

                    rate=(RatingBar)v.findViewById(R.id.MyRating);
                    tvName = (TextView) v.findViewById(R.id.textt);
                    tvName.setText(marker.getSnippet().replace('_',' '));
                    try{
                        rate.setRating(marker.getAlpha());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {

                           int position1= Integer.parseInt(marker.getId().toString().replace("m",""));
                         //   Toast.makeText(getApplicationContext(),hash_map_id.get(position1),Toast.LENGTH_SHORT).show();
                            profile.transferred = 1;
                           Intent intent = new Intent(getApplicationContext(),profile.class);
                            intent.putExtra("person_id",hash_map_id.get(position1));
                            startActivity(intent);

                        }
                    });

                    return v;
                }
            });
        }

//        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
//        setLonglang lo= new setLonglang();
//
//        // Add a marker in Sydney and move the camera
        // LatLng delhi = new LatLng(28.7041, 77.1025 );

//        try {
//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lo.getCurrent(), 14f));
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        try {
//            mMap.addMarker(new MarkerOptions().position(lo.getCurrent()).title("Me"));
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }


//        if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }

        try {

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 10.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        callVolley(mMap);

    }
    public void callVolley(final GoogleMap mMap){

        String url="http://catchforms.in/vinod/catchforms/user_data/locate.php?type="+getIntent().getStringExtra("title");
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        final JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
//                    Log.d("response1111",response.toString());

                    for (int i=0;i<response.length();i++){
                        final JSONObject jsonObject = response.getJSONObject(i);
                        double latitude = jsonObject.getDouble("Latitude");
                        double longitude = jsonObject.getDouble("Longitude");
                        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)).title(jsonObject.getString("Center_name")).snippet(jsonObject.getString("Center_address")).alpha(Float.parseFloat(jsonObject.getString("Rating"))));
                        hash_map_id.put(i,jsonObject.getString("Logged_id"));


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
        queue.add(jsonArrayRequest);

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



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
                nc.normaldialog(MapsActivity.this);


            }
            handler.postDelayed(mHandlerTask,3000);
        }
    };


}
