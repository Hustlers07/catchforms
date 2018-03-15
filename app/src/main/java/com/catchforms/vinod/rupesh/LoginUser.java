package com.catchforms.vinod.rupesh;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;


public class LoginUser extends AppCompatActivity {


    CallbackManager callbackManager;

    int RC_SIGN_IN=1;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;
    Profile profile;

    SharedPreferences sp;
    SharedPreferences.Editor ed;
    static boolean storagepermission=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_user);


        initialise();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // .enableAutoManage(this /* FragmentActivity */, (GoogleApiClient.OnConnectionFailedListener) MainActivity.this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void initialise() {
        storagepermission=isStoragePermissionGranted();
        sp=getSharedPreferences("Data",MODE_PRIVATE);
        ed= sp.edit();
        callbackManager = CallbackManager.Factory.create();

        Button cafelogin = (Button)findViewById(R.id.cafe);
        Button google_sign =(Button)findViewById(R.id.signgoogle_1);
        LoginButton facebookloginButton = (LoginButton)findViewById(R.id.signfacebook_1);
        facebookloginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        google_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
                Toast.makeText(getApplicationContext(),"Please wait",Toast.LENGTH_SHORT).show();
            }
        });


        facebookloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogin();
                Toast.makeText(getApplicationContext(),"Please wait",Toast.LENGTH_SHORT).show();
            }
        });

        cafelogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                Toast.makeText(getApplicationContext(),"Cafe Center",Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void fblogin() {

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code


                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        try {
                            String  fbfirstname = object.getString("first_name");
                            String fbsurname = object.getString("last_name");
                            String  fbemail = object.getString("email");
                            String  fbid = object.getString("id");
                            String profile_picture = "https://graph.facebook.com/"+fbid+ "/picture?height=400&width=400";
                            profilepicture(profile_picture);
                            mydatastorage(fbfirstname,fbsurname,fbemail,fbid,1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();

                ed.putString("logged_as","Personal User");
                ed.commit();
                Toast.makeText(getApplicationContext(),"Please wait",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(getApplicationContext(),"Facebook Login Cancelled",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(getApplicationContext(),"Facebook Login Error",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            Log.d("hello", "handleSignInResult:" + result.isSuccess());
            Toast.makeText(getApplicationContext(),"Google Login Successfull",Toast.LENGTH_SHORT).show();

            acct = result.getSignInAccount();
            try{
                profilepicture(acct.getPhotoUrl().toString());
            }catch (Exception e){
                e.printStackTrace();
            }
            mydatastorage(acct.getGivenName(),acct.getFamilyName(),acct.getEmail(),acct.getId(),2);
            // Signed in successfully, show authenticated UI.
            /// TextView mStatusTextView=(TextView)findViewById(R.id.textnew);
            //mStatusTextView.setText(acct.getDisplayName());
            // updateUI(true);
            ed.putString("logged_as","Personal User");
            ed.commit();
            startActivity(new Intent(getApplicationContext(),firstscreen.class));

        } else {
            Toast.makeText(getApplicationContext(),"Google Login Unsuccessfull, Retry",Toast.LENGTH_SHORT).show();
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }



    public void mydatastorage(String firstName,String surName, String email, String id, int p3) {

//        personinfo.setPersonName(firstName);
//        personinfo.setPersonEmail(email);
//        personinfo.setPersonId(id);
//
////        Log.d("fbid",id);
////        Log.d("fbname",displayName);
//        Log.d("fbemail",email);
//

        //       profilepicture(image);

        switch (p3)
        {
            case 1: {
                ed.putString("name",firstName);
                ed.putString("surname",surName);
                ed.putString("personEmail",email);
                ed.putString("personId",id);
                ed.putString("loggedwith","fb");
                ed.putInt("log",1);
                ed.commit();

                personinfo.setLoggedinwith("fb");

                break;
            }
            case 2 :{
                ed.putString("name",firstName);
                ed.putString("surname",surName);
                ed.putString("personEmail",email);
                ed.putString("personId",id);
                ed.putString("loggedwith","gmail");
                ed.putInt("log",1);
                ed.commit();

                personinfo.setLoggedinwith("gmail");

                break;
            }
        }

    }
//

    public void profilepicture(String image_url) {
        LoginUser.DownloadTask downloadtask = new DownloadTask();
        downloadtask.execute(image_url);
    }

    class DownloadTask extends AsyncTask<String,Integer,String> {

        ProgressDialog progressdialog;

        @Override
        protected void onPreExecute() {

//            progressdialog = new ProgressDialog(getApplicationContext());
//            progressdialog.setTitle("Processing...");
//            progressdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progressdialog.setMax(100);
//            progressdialog.setProgress(0);
//            progressdialog.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
//            progressdialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
//            progressdialog.hide();
            Toast.makeText(getApplicationContext(),"Login Successful",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),firstscreen.class));
        }

        @Override
        protected String doInBackground(String... params) {
            String path = params[0];
            int file_length=0;
            if (storagepermission){


                Log.d("isrunning","yes");

                try {
                    URL url = new URL(path);
                    URLConnection urlconnection = url.openConnection();
                    urlconnection.connect();
                    file_length = urlconnection.getContentLength();


                    String folder_path= Environment.getExternalStorageDirectory().getAbsolutePath().toString();

                    File profilepic_folder = new File(folder_path+"/Catchuser");

                    if (!profilepic_folder.exists()){
                        profilepic_folder.mkdirs();
                    }

                    File input_file = new File(profilepic_folder,"profilepic.jpg");
                    InputStream inputstream = new BufferedInputStream(url.openStream(),8192);
                    byte[] data = new byte[1024];

                    int total =0;
                    int count =0;

                    OutputStream outputstream = new FileOutputStream(profilepic_folder.getAbsolutePath().toString()+"/profile.jpg");
                    while((count=inputstream.read(data))!=-1){
                        total=total+count;
                        outputstream.write(data,0,count);
//                               int progress = (int) total*100/file_length;
//
//                              publishProgress(progress);
                    }

                    inputstream.close();
                    outputstream.close();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else{
//                Toast.makeText(getApplicationContext(),"Permission is not Granted",Toast.LENGTH_SHORT).show();
            }

            return "Progress Complete...";
        }
    }


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.d("permission","Permission is granted");
                return true;
            } else {

                Log.d("permission","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.d("permission","Permission is granted");
            return true;
        }
    }

    public void logout(SharedPreferences sp){

        String folder_path= Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        File profilepic_folder = new File(folder_path+"/Catchuser/profile.jpg");
        profilepic_folder.delete();

        final SharedPreferences.Editor ed= sp.edit();
        if(sp.getString("loggedwith","Invalid").compareTo("fb")==0){
            LoginManager.getInstance().logOut();
            ed.clear();
            ed.putInt("log",2);
            ed.commit();

        }
        else if(sp.getString("loggedwith","Invalid").compareTo("gmail")==0){
            ed.clear();
            ed.putInt("log",2);
            ed.commit();
        }
    }

}
