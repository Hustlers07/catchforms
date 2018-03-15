package com.catchforms.vinod.rupesh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;

import static java.lang.Boolean.TRUE;

public class GoogleLogin extends AppCompatActivity {

    Context context=this;
    Profile profile = Profile.getCurrentProfile();
    Button loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_google_login);

        getSupportActionBar().hide();

        initialsiefbcontrol();

        loginButton =(Button)findViewById(R.id.fblogin);


//        loginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                loginwithfb();
//            }
//        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };



//        profileTracker= new ProfileTracker() {
//            @Override
//            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
//
//                nextActivity(oldProfile);
//            }
        //r  };

        // profileTracker.startTracking();
        accessTokenTracker.startTracking();

    }

    private void initialsiefbcontrol() {

        loginButton =(Button)findViewById(R.id.fblogin);

        callbackManager= CallbackManager.Factory.create();

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                if (profile != null) {
                    Intent i = new Intent(getApplicationContext(), firstscreen.class);
                    LoggedInSuccessfully();

                        startActivity(i);
                        Toast.makeText(getApplicationContext(), "Logged", Toast.LENGTH_SHORT).show();
                    //                   i.putExtra("image", profile.getProfilePictureUri(200, 100));
                    Toast.makeText(getApplicationContext(), "Logged Successfully" + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Loggin Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Logged Successfully" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void LoggedInSuccessfully()
    {
        SharedPreferences sharedPreferences=getSharedPreferences("CacheData",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        String name=profile.getName();
        String id=profile.getId();

        editor.putString("name",name);
        editor.putString("email", id);
        editor.putInt("LOG", 1);
        editor.commit();
        startActivity(new Intent(getApplicationContext(),register_form.class));


    }


//    private void loginwithfb(){
//
//
////        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
////            @Override
////            public void onSuccess(LoginResult loginResult) {
////
////                if (profile != null) {
////                    Intent i = new Intent(getApplicationContext(),firstscreen.class);
////                   boolean c=LoggedInSuccessfully();
////                    if(c==TRUE){
////                        startActivity(i);
////                        Toast.makeText(getApplicationContext(),"Logged",Toast.LENGTH_SHORT).show();
////                    }
////                    //                   i.putExtra("image", profile.getProfilePictureUri(200, 100));
////                    Toast.makeText(getApplicationContext(), "Logged Successfully" + loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();
////                }
////            }
////
////            @Override
////            public void onCancel() {
////                Toast.makeText(getApplicationContext(),"Loggin Cancelled",Toast.LENGTH_SHORT).show();
////            }
////
////            @Override
////            public void onError(FacebookException error) {
////                Toast.makeText(getApplicationContext(),"Logged Successfully"+error.getMessage(),Toast.LENGTH_SHORT).show();
////            }
////        });
//
//
//    }
//
//
////    @Override
////    protected void onResume() {
////        super.onResume();
////        Profile profile= Profile.getCurrentProfile();
////        nextActivity(profile);
////    }
//


    static int backpress;
    @Override
    public void onBackPressed() {
        Thread t1 = new Thread() {
            public void run() {

                try {
                    Thread.sleep(5000);
                    backpress = 0;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        t1.start();
        backpress++;

        if (backpress == 1) {
            Toast.makeText(getApplicationContext(), " Press Back again to Go Back ", Toast.LENGTH_SHORT).show();
        } else if (backpress == 2) {
            this.finish();
        }
    }


}
