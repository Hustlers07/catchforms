package com.catchforms.vinod.rupesh;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by root on 6/24/17.
 */

public class getLocation extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    int value;
    LocationManager locationManager;
    GoogleApiClient googleApiClient;
    Location location;
    LocationRequest request;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "OnCreate", Toast.LENGTH_SHORT).show();
        getLocation_gps();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getApplicationContext(), "OnStartCommand", Toast.LENGTH_SHORT).show();
        googleApiClient.connect();
        return value;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(getApplicationContext(), "OnDestroy", Toast.LENGTH_SHORT).show();
    }

    public void getLocation_gps() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
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
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);


        if(location!=null){
            Log.d("location111","latitude"+location.getLatitude());
            Log.d("location111","longitude"+location.getLongitude());
            new locationsetter().setLatitude(location.getLatitude());
            new locationsetter().setLongitude(location.getLongitude());
            try {
                getaddress(location.getLatitude(),location.getLongitude());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            request = LocationRequest.create();
            request.setInterval(6000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            Log.d("location111","latitude"+location.getLatitude());
            Log.d("location111","longitude"+location.getLongitude());

        }
        //getaddress();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    LocationRequest location_request;
//    public void Start_Location_Updates(){
//        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,location_request,this);
//    }

    String add="";
    public void getaddress(double lat,double lon) throws IOException {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addressList = null;
        try{
            addressList=geocoder.getFromLocation(lat,lon,1);
            Log.d("logg111",String.valueOf(lat));
            Log.d("log1111",String.valueOf(lon));

        }catch (IOException e){

        }
        if(addressList==null || addressList.size()==0){

        }
        else {
            Address address = addressList.get(0);
            ArrayList <String> arr= new ArrayList<>();
            for (int i=0;i<address.getMaxAddressLineIndex();i++){
                arr.add(address.getAddressLine(i));
                add+= address.getAddressLine(i);
            }
            Log.d("address111",add);
        }
    }
}
