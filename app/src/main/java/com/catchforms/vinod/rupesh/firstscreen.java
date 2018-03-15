package com.catchforms.vinod.rupesh;

import android.*;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.toolbox.Volley;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;

import java.io.File;
import java.io.FileInputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class firstscreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ToggleButton toggleButton;
    View view;
    int backpress;
    LocationManager locationManager;
    LocationListener locationlistner;
    TextView t1,t2,t3,t4,t5,t6,t7,t8,tv1,tv2,tv3;
    SharedPreferences sp;
    CircleImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_firstscreen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sp=getSharedPreferences("Data",MODE_PRIVATE);

    //    ImageView imageView= (ImageView)findViewById(R.id.img_profile);

        //        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);

        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sp.getInt("log",2)==1&&sp.getString("logged_as","null").contentEquals("Cafe Center")) {
                    startActivity(new Intent(getApplicationContext(), profile.class));
                }
                else {
                    startActivity(new Intent(getApplicationContext(),LoginUser.class));
                }
            }
        });
        tv1=(TextView)header.findViewById(R.id.user_text);
        tv2=(TextView)header.findViewById(R.id.user_email);
        tv1.setText(sp.getString("name","Login")+" "+sp.getString("surname"," "));
        tv2.setText(sp.getString("logged_as",""));
        img =(CircleImageView)header.findViewById(R.id.img_profile);

        Menu menu = navigationView.getMenu();
        MenuItem login = menu.findItem(R.id.logincheck);
        if(sp.getInt("log",2) !=1){

            login.setTitle(getResources().getString(R.string.login));
        }
        else {
            login.setTitle(getResources().getString(R.string.logout));
        }


        String image_path= Environment.getExternalStorageDirectory().getAbsolutePath().toString()+"/Catchuser/profile.jpg";

        File profilepic_image = new File(image_path);

        if (profilepic_image.exists()){

            try {
                Bitmap b = BitmapFactory.decodeStream(new FileInputStream(image_path));
                img.setImageBitmap(b);
                Log.d("profLoad","yes");
                //        imageView.setImageDrawable(Drawable.createFromPath(image_path));
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        t1=(TextView)findViewById(R.id.home_text);

        Typeface font=Typeface.createFromAsset(getAssets(),"fonts/verdana.ttf");
        t1.setTypeface(font);

        t2=(TextView)findViewById(R.id.yojna_text);
        t2.setTypeface(font);

        t3=(TextView)findViewById(R.id.forms_text);
        t3.setTypeface(font);

        t4=(TextView)findViewById(R.id.locate_text);
        t4.setTypeface(font);

        t5=(TextView)findViewById(R.id.hello_text);
        t5.setTypeface(font);

        t6=(TextView)findViewById(R.id.drive_text);
        t6.setTypeface(font);

        t7=(TextView)findViewById(R.id.text_apply);
        t7.setTypeface(font);
        tv3=(TextView)findViewById(R.id.current_news_text);
        tv3.setTypeface(font);



    }

    Intent i;
    public void display(View v){
        switch (v.getId()){
            case R.id.home  :{
                i =new Intent(getApplicationContext(),MainActivity3.class);
                i.putExtra("url","https://sites.google.com/site/catchformsindia/");
                startActivity(i);
                break;
            }

            case R.id.IDforms :{
                i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                break;
            }
            case R.id.locator:{
                i=new Intent(getApplicationContext(),Locate.class);
                startActivity(i);
                break;
            }
            case R.id.current_affair:{
                i=new Intent(getApplicationContext(),currentaffairs.class);
                startActivity(i);
                break;
            }
            case R.id.setting :{
                i=new Intent(getApplicationContext(),Settings.class);
                startActivity(i);
                break;
            }
            case R.id.general :{
                i=new Intent(getApplicationContext(),generaldata.class);
                startActivity(i);
                break;
            }
            case R.id.applyonline :{
                i=new Intent(getApplicationContext(),applyonline.class);
                startActivity(i);
                break;
            }
            case R.id.drive:{
                 i = new Intent(getApplicationContext(), MainActivity3.class);
                i.putExtra("url", "https://accounts.google.com/ServiceLogin?service=wise&passive=true&continue=http%3A%2F%2Fdrive.google.com%2F%3Futm_source%3Den_US&utm_medium=button&utm_campaign=web&utm_content=gotodrive&usp=gtd&ltmpl=drive&urp=https%3A%2F%2Fwww.google.co.in%2F");
                startActivity(i);
                break;
            }
            case R.id.current_news :{
                i=new Intent(getApplicationContext(),News.class);
                startActivity(i);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
  //          super.onBackPressed();
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

            if(backpress==1) {
                Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
            }
            else if (backpress==2) {
                this.finish();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
   //     getMenuInflater().inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId())
        {
            case R.id.profile :
            {
             //   Toast.makeText(getApplicationContext(),"Profile",Toast.LENGTH_SHORT).show();
             //   Intent i =new Intent(getApplicationContext(),profile.class)
                // startActivity(i);
               // return  true;
            }
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            String logged_as=sp.getString("logged_as","none");
            if(logged_as.contentEquals("Cafe Center")){
                startActivity(new Intent(getApplicationContext(), profile.class));
            }
            else{
                Toast.makeText(getApplicationContext(),"Not Registered as Cafe Center",Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_rateus) {

        } else if (id == R.id.language) {
            startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS), 0);

        }
        else if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT,"Link");
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        } else if (id == R.id.feedback) {
            composeEmail("Feedback");
        }
        else if (id == R.id.logincheck){
            if(sp.getInt("log",2) !=1){
                Intent i = new Intent(firstscreen.this, LoginUser.class);
                startActivity(i);
                item.setTitle("Logout");
            }
            else {

                item.setTitle("Login");
                new LoginActivity().logout(sp);
                tv1.setText("Login");
                tv2.setText("");
                img.setImageResource(R.drawable.ic_person_black_24dp);
                Toast.makeText(getApplicationContext(),"Logged Out Successfully",Toast.LENGTH_SHORT).show();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void composeEmail(String subject) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto: catchforms7@gmail.com")); // only email apps should handle this
        //intent.putExtra(Intent.EXTRA_EMAIL,addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

//    public void mydisplay(View view){
//        Log.d("pp","pressed");
//        toggleButton=(ToggleButton)view.findViewById(R.id.login);
//
//        if(toggleButton.isChecked()){
//            // to logout and clear the data in shared prefrence
//            Toast.makeText(this,"Logged out Successfully",Toast.LENGTH_SHORT).show();
//        }
//        else {
//            // login page
//            try {
//                Intent i =new Intent(this, LoginActivity.class);
//                startActivity(i);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//        }
//    }
}
