package com.catchforms.vinod.rupesh;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {

    String setting[]={"Language","Logout"};
    Integer image[]={R.drawable.ic_translate_black_24dp,R.drawable.ic_vpn_key_black_24dp};
    String language[]={"English", "Hindi", "Punjabi","Marathi"};

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ListView lv=(ListView)findViewById(R.id.setting);
        lv.setAdapter(new customSetting(this,setting,image));

        View v=getLayoutInflater().inflate(R.layout.languagedialogue,null);

        listView=(ListView)v.findViewById(R.id.dialog);
//        ArrayAdapter ad= new ArrayAdapter(getApplicationContext(),R.layout.dialogue,R.id.ggg,language);
//        listView.setAdapter(ad);



        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0: {
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCALE_SETTINGS), 0);
                        break;
                    }
                    case 1 :{
                        LogOut();
                        break;
                    }
                }
            }
        });

    }

    private void LogOut() {
        SharedPreferences sp=getSharedPreferences("CacheData",MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.clear();
        ed.commit();

        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
    }

    public void customDialog(){
        AlertDialog.Builder build= new AlertDialog.Builder(this);
        build.setView(R.layout.languagedialogue);
        build.create().show();
    }

}
