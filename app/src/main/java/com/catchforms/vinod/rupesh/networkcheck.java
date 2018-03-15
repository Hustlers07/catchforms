package com.catchforms.vinod.rupesh;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Rupesh gupta on 4/28/2017.
 */


public class networkcheck {

static  int intialstatus=0;

    public static boolean checkInternetConnection(Context context){
        ConnectivityManager connectivityManager= (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
//        ConnectivityManager connectivityManager =context.(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();

        return (networkInfo!=null && networkInfo.isConnected());
    }

    public  static void normaldialog(final Context context){
        final AlertDialog.Builder builder= new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Check Internet connection and press retry");
        builder.setNeutralButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                if(intialstatus!=1) {
                    Activity activity = (Activity) context;
                    activity.finish();
                    activity.startActivity(((Activity) context).getIntent());
                }
                else if(intialstatus==1)
                {
                    try{
                        MainActivity3.wv.reload();
                    }
                    catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    intialstatus=0;
                }
            }
        });
        AlertDialog ad= builder.create();
        try{
            ad.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
