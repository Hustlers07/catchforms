package com.catchforms.vinod.rupesh;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.*;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;

import static com.catchforms.vinod.rupesh.centerinfo.password;

public class register_form extends AppCompatActivity {

    CheckBox aadhar, pan, voter,rest,ration;
    EditText center_name, center_address, rest_other, center_pass;
    Button registation_submit;
    AutoCompleteTextView center_contact;
    Location location;
    LocationManager locationManager;
    SharedPreferences sp;
    SharedPreferences.Editor ed;
    static int check = 0;
    Spinner spinner;
    static String state;
    ImageView pass_visibility;
    static boolean visibility_password=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_form);


        initialise();
        permission();
        final Intent intent = new Intent(this,getLocation.class);
        startService(intent);
        sp=getSharedPreferences("Data",MODE_PRIVATE);
        ed= sp.edit();
        spinner = (Spinner) findViewById(R.id.spin);
        final String a[] = getResources().getStringArray(R.array.spinner);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.textspin, a);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
             state = a[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getApplicationContext(),"State selected",Toast.LENGTH_SHORT).show();
            }
        });


        try {
            if(Integer.parseInt(getIntent().getStringExtra("check"))==1){
                check=1;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        if (check == 1){
            setdata();
        }

        registation_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (center_name.getText().toString().isEmpty() || center_address.getText().toString().isEmpty() || center_contact.getText().toString().isEmpty() || center_pass.getText().toString().isEmpty() || center_contact.getText().toString().length() != 10 || state.contentEquals("State") ) {
                    showError();
                } else {
                    locationDialog();
                }
            }
        });

        pass_visibility.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (visibility_password==false){
                    center_pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass_visibility.setImageResource(R.drawable.ic_visibility_off_black_24dp);
                    visibility_password=true;
                }
                else {
                    center_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass_visibility.setImageResource(R.drawable.ic_remove_red_eye_black_24dp);
                    visibility_password=false;
                }
            }
        });
    }

    private void initialise() {
        aadhar = (CheckBox) findViewById(R.id.checkboxAdhar);
        pan = (CheckBox) findViewById(R.id.checkboxPan);
        voter = (CheckBox) findViewById(R.id.checkboxVoter);
        ration = (CheckBox)findViewById(R.id.checkboxRation);
        rest   = (CheckBox)findViewById(R.id.checkboxRest) ;
        registation_submit = (Button) findViewById(R.id.registation_submit);
        center_name = (EditText) findViewById(R.id.center_name);
        center_address = (EditText) findViewById(R.id.center_address);
        center_contact = (AutoCompleteTextView) findViewById(R.id.center_contact);
        center_pass = (EditText) findViewById(R.id.center_pass);
        rest_other = (EditText) findViewById(R.id.rest_other);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        pass_visibility = (ImageView)findViewById(R.id.password_visibility);
    }

    private void setdata() {
        setdatavolley();
    }
    private void setdatavolley() {
        String url="http://catchforms.in/vinod/catchforms/user_data/fetchingalldata.php?userid="+sp.getString("personId","notpresent");
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        Log.d("loggedid",sp.getString("personId","notpresent"));

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("datatotal",response.toString());
                    center_contact.setText(response.getString("Contact_no"));
                    center_name.setText(response.getString("Center_name").replace('_',' '));
                    center_address.setText(response.getString("Center_address").replace('_',' '));
//                    center_pass.setText(response.getString("User_pass"));
                    ed.putString("name",response.getString("Name"));
                    ed.commit();
                    if(Integer.parseInt(response.getString("Aadhar_card"))==1)
                    {
                        aadhar.setChecked(true);
                    }
                    if(Integer.parseInt(response.getString("Voterid_card"))==1)
                    {
                        voter.setChecked(true);
                    }
                    if(Integer.parseInt(response.getString("Pan_card"))==1)
                    {
                        pan.setChecked(true);
                    }
                    if(Integer.parseInt(response.getString("Rest_other"))==1)
                    {
                        rest.setChecked(true);
                    }
                    try{
                        rest_other.setText(response.getString("Rest"));
                    }catch (Exception e){
                        e.printStackTrace();
                        rest.setText("Rest : ----");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("datatotal","exception");
                    startActivity(new Intent(getApplicationContext(),firstscreen.class));
                    Toast.makeText(getApplicationContext(),"Unable to update data",Toast.LENGTH_SHORT).show();
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




    public void checkbox(View view) {
        switch (view.getId()) {
            case R.id.checkboxAdhar: {
                if (aadhar.isChecked())
                    Toast.makeText(getApplicationContext(), "Aadhar Card Checked", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Aadhar Card Unchecked", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.checkboxPan: {

                if (pan.isChecked())
                    Toast.makeText(getApplicationContext(), "Pan Card Checked", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Pan Card Unchecked", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.checkboxVoter: {
                if (voter.isChecked())
                    Toast.makeText(getApplicationContext(), "Voter Card Checked", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Voter Card Unchecked", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.checkboxRation: {
                if (ration.isChecked())
                    Toast.makeText(getApplicationContext(), "Ration Card Checked", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Ration Card Unchecked", Toast.LENGTH_SHORT).show();
                break;
            }

            case R.id.checkboxRest: {
                if (rest.isChecked()) {
                    Toast.makeText(getApplicationContext(), "Rest Checked", Toast.LENGTH_SHORT).show();
                    rest_other.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Rest Unchecked", Toast.LENGTH_SHORT).show();
                    rest_other.setVisibility(View.INVISIBLE);
                }
                break;
            }

        }
    }

    private void showError() {
        if (center_contact.getText().toString().isEmpty()) {
            center_contact.setError("Required");
        }
        else if (isPasswordValid(center_pass.getText().toString())) {
            center_pass.setError(getString(R.string.error_invalid_password));
        }

        else if (center_name.getText().toString().isEmpty()) {
            center_name.setError("Required");
        }
        else if (center_address.getText().toString().isEmpty()) {
            center_address.setError("Required");
        }
//        else if(center_contact.getText().toString().isEmpty() ){
//            center_contact.setError("Required");
//        }

        else if (center_contact.getText().toString().length() != 10) {
            center_contact.setError("Invalid Number");
        }
        else if(state.contentEquals("State")){
            Toast.makeText(getApplicationContext(),"State Required",Toast.LENGTH_SHORT).show();
        }
         else if (rest_other.getText().toString().isEmpty())
            rest_other.setText("none");
    }
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() <= 4;
    }

    private void locationDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(register_form.this);
        builder.setTitle("Location");
        builder.setMessage("Current Location will be used as Center Location");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getLocation();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void getLocation() {
//        if(Build.VERSION.SDK_INT >=23){
//            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
//                return;
//            }
//        }
//       location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            if(check !=1){
               // submitDetails();
              senddetails_to_OTP_Intent();
            }
            else {
                if (center_name.getText().toString().isEmpty() || center_address.getText().toString().isEmpty() || center_contact.getText().toString().isEmpty() || center_pass.getText().toString().isEmpty() || center_contact.getText().toString().length() != 10 || state.contentEquals("State") ) {
                    showError();
                } else {
                    updateDetails();
                }

            }

        }else{
            Toast.makeText(getApplicationContext(),"GPS Required",Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

        }
     //   getLocation();

//        }catch (Exception e){
//            e.printStackTrace();
//            Toast.makeText(getApplicationContext(),"Unable to Submit",Toast.LENGTH_SHORT).show();
//        }
    }

    private void senddetails_to_OTP_Intent() {
        OTP.registration=true;
        Intent intent = new Intent(getApplicationContext(),OTP.class);
        intent.putExtra("center_pass",center_pass.getText().toString());
        intent.putExtra("contact",center_contact.getText().toString());
        intent.putExtra("state",state);
        intent.putExtra("center_name",center_name.getText().toString().replace(' ','_'));
        intent.putExtra("center_address",center_address.getText().toString().replace(' ','_'));
        intent.putExtra("aadhar",String.valueOf(aadhar.isChecked()));
        intent.putExtra("voter_id",String.valueOf(voter.isChecked()));
        intent.putExtra("pan",String.valueOf(pan.isChecked()));
        intent.putExtra("ration",String.valueOf(ration.isChecked()));
        intent.putExtra("restother",String.valueOf(rest.isChecked()));
        intent.putExtra("rest",rest_other.getText().toString().replace(' ','_'));
        intent.putExtra("latitude",String.valueOf(new locationsetter().getLatitude()));
        intent.putExtra("longitude",String.valueOf(new locationsetter().getLongitude()));
        startActivity(intent);
    }

    private void updateDetails() {
        String url ="http://catchforms.in/vinod/catchforms/user_data/update.php?loggedid="+sp.getString("personId","notpresent")+"&contact="+center_contact.getText().toString()+"&password="+center_pass.getText().toString()+"&state="+state+"&centername="+center_name.getText().toString().replace(' ','_')+"&centeraddress="+center_address.getText().toString().replace(' ','_')+"&aadhar="+String.valueOf(aadhar.isChecked())+"&voterid="+voter.isChecked()+"&pan="+String.valueOf(pan.isChecked())+"&ration="+String.valueOf(ration.isChecked())+"&restother="+String.valueOf(rest.isChecked())+"&rest="+rest_other.getText().toString().replace(' ','_')+"&latitude="+String.valueOf(location.getLatitude())+"&longitude="+String.valueOf(location.getLongitude());

        Log.d("url111",url);
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int s=Integer.parseInt(response.getString("status"));

                    if (s==401){
                        Toast.makeText(getApplicationContext(),"Update Success",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),profile.class));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Update Failed",Toast.LENGTH_SHORT).show();
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
        queue.add(jsonObjectRequest);

    }

    private void permission() {
        if(Build.VERSION.SDK_INT >=23){
            if (ActivityCompat.checkSelfPermission(register_form.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(register_form.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

//
//    public void submitDetails(){
//        String url="http://telpherpoint.com/vinod/catchforms/user_data/register.php?username="+sp.getString("name","Not present")+"&password="+center_pass.getText().toString()+"&email="+sp.getString("personEmail","Not present")+"&loggedid="+sp.getString("personId","notpresent")+"&loggedwith="+sp.getString("loggedwith","Not present")+"&contact="+center_contact.getText().toString()+"&state="+state+"&centername="+center_name.getText().toString().replace(' ','_')+"&centeraddress="+center_address.getText().toString().replace(' ','_')+"&aadhar="+String.valueOf(aadhar.isChecked())+"&voterid="+voter.isChecked()+"&pan="+String.valueOf(pan.isChecked())+"&ration="+String.valueOf(ration.isChecked())+"&restother="+String.valueOf(rest.isChecked())+"&rest="+rest_other.getText().toString().replace(' ','_')+"&latitude="+String.valueOf(location.getLatitude())+"&longitude="+String.valueOf(location.getLongitude());
//        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
//      Log.d("success11",response.getString("status"));
//        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    int s=Integer.parseInt(response.getString("status"));
//
//
//                    Log.d("success111",response.getString("status"));
//                    if (s==401){
//                        ed.putString("logged_as","Cafe Center");
//                        ed.commit();
//                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
//                        Toast.makeText(getApplicationContext(),"Registeration Success",Toast.LENGTH_SHORT).show();
//                    }
//                    else if(s==400){
//                        ed.putString("logged_as","Cafe Center");
//                        ed.commit();
//                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
//                        Toast.makeText(getApplicationContext(),"Registrar Already Exists",Toast.LENGTH_SHORT).show();
//                    }
//                    else if(s==402){
//                        Toast.makeText(getApplicationContext(),"Registeration Failed",Toast.LENGTH_SHORT).show();
//                    }
//                    else if(s==404){
//                        Toast.makeText(getApplicationContext(),"Registerion Failed",Toast.LENGTH_SHORT).show();
//                    }
//                }catch(Exception e){
//
//                    Log.d("datatotal","exception");
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("datatotal","volleyerror");
//            }
//        }
//
//        );
//        queue.add(jsonObjectRequest);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),firstscreen.class));
        finish();
    }
//        private void submitDetails(){
//
//        String url="http://telpherpoint.com/vinod/catchforms/user_data/register.php?username="+sp.getString("name","Not present")+"&password="+center_pass.getText().toString()+"&email="+sp.getString("personEmail","Not present")+"&loggedid="+sp.getString("personId","notpresent")+"&loggedwith="+sp.getString("loggedwith","Not present")+"&contact="+center_contact.getText().toString()+"&state="+state+"&centername="+center_name.getText().toString()+"&centeraddress="+center_address.getText().toString()+"&aadhar="+String.valueOf(aadhar.isChecked())+"&voterid="+voter.isChecked()+"&pan="+String.valueOf(pan.isChecked())+"&ration="+String.valueOf(ration.isChecked())+"&restother="+String.valueOf(rest.isChecked())+"&rest="+rest_other.getText().toString()+"&latitude="+String.valueOf(location.getLatitude())+"&longitude="+String.valueOf(location.getLongitude());
//        Log.d("image_url",url);
//        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
//
//        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("datatotal",response.toString());
//                try {
//                    String report=response.getString("success");
//
//                    int s=Integer.parseInt(report);
//                    Log.d("success11",report);
//                    if (s==401){
//                        ed.putString("logged_as","Cafe Center");
//                        ed.commit();
//                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
//                        Toast.makeText(getApplicationContext(),"Registeration Success",Toast.LENGTH_SHORT).show();
//                    }
//                    else if(s==400){
//                        ed.putString("logged_as","Cafe Center");
//                        ed.commit();
//                        startActivity(new Intent(getApplicationContext(),firstscreen.class));
//                        Toast.makeText(getApplicationContext(),"Registrar Already Exists",Toast.LENGTH_SHORT).show();
//                    }
//                    else if(s==402){
//                        Toast.makeText(getApplicationContext(),"Registerion Failed",Toast.LENGTH_SHORT).show();
//                    }
//                    else if(s==404){
//                        Toast.makeText(getApplicationContext(),"Registerion Failed",Toast.LENGTH_SHORT).show();
//                    }
//                }catch(Exception e){
//                    Toast.makeText(getApplicationContext(),"Registerion Failed",Toast.LENGTH_SHORT).show();
//                    Log.d("datatotal","exception");
//
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getApplicationContext(),"Registerion Failed",Toast.LENGTH_SHORT).show();
//                Log.d("datatotal","volleyerror");
//            }
//        }
//
//
//        );
//        queue.add(jsonObjectRequest);
//    }
}
