package com.catchforms.vinod.rupesh;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class generaldata extends AppCompatActivity {
    ExpandableListView expandable;
    List<String> list;
    Map<String,List<String>> set;
    Integer image[]={R.drawable.gaslogo,R.drawable.electricconnection,R.drawable.water} ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generaldata);
        getSupportActionBar().setTitle("General");
        ActionBar ab=getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        data();
        expandable=(ExpandableListView)findViewById(R.id.expandablelistview);
        expandable.setAdapter(new myList(this,list,set,image));
        expandable.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {


                switch (groupPosition) {
                    case 0: {
                        switch (childPosition) {
                            case 0: {
                                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                                i.putExtra("url", "https://www.bankbazaar.com/gas-connection/indane-gas.html");
                                startActivity(i);
                                break;
                            }
                            case 1: {
                                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                                i.putExtra("url", "https://www.bankbazaar.com/gas-connection/bharat-gas.html");
                                startActivity(i);

                                break;
                            }
                            case 2: {
                                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                                i.putExtra("url", "https://www.bankbazaar.com/gas-connection/hp-gas.html");
                                startActivity(i);
                                break;
                            }
                        }
                        break;
                    }
                    case 1: {
                        switch (childPosition) {
                            case 0: {
                                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                                i.putExtra("url", "http://www.bsesdelhi.com/HTML/FAQ.html");
                                startActivity(i);
                                break;
                            }
                        }
                        break;
                    }
                    case 2: {
                        switch (childPosition) {
                            case 0: {
                                Intent i = new Intent(getApplicationContext(), MainActivity3.class);
                                i.putExtra("url", "http://www.delhi.gov.in/wps/wcm/connect/doit_djb/DJB/Home/Customer+Section/Water/");
                                startActivity(i);
                                break;
                            }
                        }
                        break;
                    }
                }
                return true;
            }
        });
    }

    public void data() {
        list=new ArrayList<>();
        set=new HashMap<>();
        for(String bb:getResources().getStringArray(R.array.general_data)){
            list.add(bb);
        }
List<String> gas=new ArrayList<>();
        List<String> electricity=new ArrayList<>();
        List<String> water=new ArrayList<>();


        for(String g:getResources().getStringArray(R.array.gas)){
            gas.add(g);
        }
        for(String e:getResources().getStringArray(R.array.elec)){
            electricity.add(e);
        }
        for(String w:getResources().getStringArray(R.array.water)){
            water.add(w);
        }
        set.put(list.get(0),gas );
        set.put(list.get(1),electricity);
        set.put(list.get(2),water);
    }
}
