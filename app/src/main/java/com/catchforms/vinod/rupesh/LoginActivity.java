package com.catchforms.vinod.rupesh;

import android.*;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.session.MediaSession;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mobile_login;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    LoginButton loginButton;
    CallbackManager callbackManager;

    static boolean storagepermission=false;
    int RC_SIGN_IN=1;
    GoogleApiClient mGoogleApiClient;
    GoogleSignInAccount acct;
    Profile profile;
    TextView forgotpassword;

    SharedPreferences sp;
    SharedPreferences.Editor ed;
    String image_path;
    static String gmailpicture_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


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
        mobile_login = (AutoCompleteTextView)findViewById(R.id.mobilelogin);
        populateAutoComplete();
       forgotpassword = (TextView)findViewById(R.id.forgotpass);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        sp=getSharedPreferences("Data",MODE_PRIVATE);
        ed= sp.edit();
        storagepermission=isStoragePermissionGranted();
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.signfacebook);
        // loginButton.setReadPermissions("email");
        loginButton.setReadPermissions(Arrays.asList(
                "public_profile", "email"));

        loginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                fblogin();
            }
        });

        Button signInButton = (Button) findViewById(R.id.signgoogle);
        signInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        forgotpassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpass();
            }
        });


    }

    String profile_picture;
    String fbemail;
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
                            if (!object.getString("email").isEmpty()) {
                                fbemail = object.getString("email");
                            }else {
                                fbemail ="none";
                            }
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
                Intent intent =new Intent(getApplicationContext(),register_form.class);
                 startActivity(intent);
                finish();
         //       Toast.makeText(getApplicationContext(),"Facebook Login Success",Toast.LENGTH_SHORT).show();
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


    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mobile_login, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mobile_login.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mobile_login.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mobile_login.setError(getString(R.string.error_field_required));
            focusView = mobile_login;
            cancel = true;
        } else if (!isMobileNoValid(email)) {
            mobile_login.setError(getString(R.string.error_invalid_email));
            focusView = mobile_login;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
            callvolley();
        }
    }
    String s;
    static String contact;
    private void callvolley() {
        contact = mobile_login.getText().toString();
        if (contact.contains("+91")) {
            contact = contact.replace("+91", "");
        }
        String url="http://catchforms.in/vinod/catchforms/user_data/authentication.php?contact="+contact+"&password="+mPasswordView.getText().toString();

        Log.d("Login_Activity",url);
        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());

        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    s=response.getString("Logged_id");

                    Log.d("Logged_id",s);

                    if(!s.contentEquals("402"))
                    {
                        showProgress(false);

                        ed.putString("personId",s);
                        ed.putInt("log",1);
                        ed.putString("logged_as","Cafe Center");
                        ed.putString("loggedwith",response.getString("Logged_with"));
                        ed.commit();
                        Toast.makeText(getApplicationContext(),"Log in Successfull",Toast.LENGTH_SHORT).show();
                        checkProfileImage();

                    }
                    else{
                        finish();
                        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        Toast.makeText(getApplicationContext(),"Invalid Details",Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e){

                    Log.d("datatotal","exception");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("datatotal111","volleyerror");
            }
        }


        );
        queue.add(jsonObjectRequest);

    }
    private void checkProfileImage() {
        Intent intent = new Intent(getApplicationContext(), profile.class);

        image_path= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Catchuser/profile.jpg";

        File profilepic_image = new File(image_path);

        if (profilepic_image.exists()) {

            startActivity(intent);

        }else {
            String Logged_id =sp.getString("personId","not present");


            if(sp.getString("loggedwith","Invalid").compareTo("fb")==0){

                try{
                    String profile_picture = "https://graph.facebook.com/"+Logged_id+ "/picture?height=400&width=400";
                    profilepicture(profile_picture);
                }catch (Exception e) {
                    e.printStackTrace();
                    startActivity(intent);
                }
                startActivity(intent);
            }
            else if(sp.getString("loggedwith","Invalid").compareTo("gmail")==0){
                try{
                    String profile_picture ="http://picasaweb.google.com/data/entry/api/user/"+Logged_id+"?alt=json";
                    gmailProfilepic_url(profile_picture);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.d("datatotal1111","exception");
                    startActivity(intent);
                }
                startActivity(intent);
            }
        }
    }


    private void gmailProfilepic_url(String profile_picture) {

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());


        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, profile_picture, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                   //Log.d("datatotal",response.toString());
                    JSONObject jsonObject = response.getJSONObject("entry");
                    //Log.d("datatotal1",jsonObject.toString());
                    JSONObject jsonObject1 = jsonObject.getJSONObject("gphoto$thumbnail");
                   // Log.d("datatotal11",jsonObject1.toString());
                    gmailpicture_url= jsonObject1.getString("$t");
                    try {
                        profilepicture(gmailpicture_url);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    Log.d("datatotal111",gmailpicture_url);

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

//    public void callvolley(){
//        String url="http://telpherpoint.com/vinod/catchforms/user_data/authentication.php?contact="+mobile_login.getText().toString()+"&password="+mPasswordView.getText().toString();
//        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
//
//        JsonObjectRequest jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    String s=response.getString("status");
////                    response.op
//                    Log.d("datatotal",s);
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
//
//        );
//        queue.add(jsonObjectRequest);
//    }

    private boolean isMobileNoValid(String mobile_number) {
        //TODO: Replace this with your own logic
        return mobile_number.length()>9;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mobile_login.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
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
         //   Log.d("hello", "handleSignInResult:" + result.isSuccess());
           // Toast.makeText(getApplicationContext(),"Google Login Successfull, Continue",Toast.LENGTH_SHORT).show();

            acct = result.getSignInAccount();
            try{
//                Log.d("profile111",acct.getPhotoUrl().toString());
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
            startActivity(new Intent(getApplicationContext(),register_form.class));

        } else {
            Toast.makeText(getApplicationContext(),"Google Login Unsuccessfull, Retry",Toast.LENGTH_SHORT).show();
            // Signed out, show unauthenticated UI.
            // updateUI(false);
        }
    }



    public void mydatastorage(String firstName,String surName, String email, String id, int p3) {

//        personinfo.setPersonName(displayName);
//        personinfo.setPersonEmail(email);
//        personinfo.setPersonId(id);

//        Log.d("fbid",id);
//        Log.d("fbname",displayName);
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
       DownloadTask downloadtask = new DownloadTask();
        downloadtask.execute(image_url);
    }

    class DownloadTask extends AsyncTask<String,Integer,String>{

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
     //       Toast.makeText(getApplicationContext(),"Completed",Toast.LENGTH_SHORT).show();
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
        else {
            ed.clear();
            ed.putInt("log",2);
            ed.commit();
        }
    }


    AlertDialog.Builder forgot_pass;
    Button dialog_submit;
    ImageButton close;
    TextView contactNo;
    AlertDialog show;

    private void forgotpass(){
        View view = getLayoutInflater().inflate(R.layout.forgotpassmobile,null);
        forgot_pass = new AlertDialog.Builder(this);
        forgot_pass.setView(view);
        contactNo = (EditText)view.findViewById(R.id.forgot_mobile);
        close = (ImageButton)view.findViewById(R.id.forgotpass_dialog_cancel);
        dialog_submit = (Button)view.findViewById(R.id.submit_contact);

        dialog_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(contactNo.getText().toString().isEmpty() || contactNo.getText().toString().length()<10){
                    if(contactNo.getText().toString().isEmpty()){
                        contactNo.setError("Required");
                    }
                    else {
                        contactNo.setError("Invalid No.");
                    }
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),OTP.class);
                    intent.putExtra("contact",contactNo.getText().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });
        try{
            show=forgot_pass.create();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                }
            });
            show.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

