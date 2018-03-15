   package com.catchforms.vinod.rupesh;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton applybutton;
    ExpandableListView expandableListView;
    List<String> label;
    Map<String, List<String>> topic;
    List<String> driving=new ArrayList<>();


    Integer image[]={R.drawable.aadhar,R.drawable.voter,R.drawable.pancard,R.drawable.ration,R.drawable.driving} ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forms");
        ab.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);

        filldata();

        applybutton = (FloatingActionButton) findViewById(R.id.apply);

        applybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), applyonline.class);
                startActivity(intent);
            }
        });


        expandableListView = (ExpandableListView) findViewById(R.id.expandablelist);

        expandableListView.setAdapter(new myList(this, label, topic,image));

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                Toast.makeText(getApplicationContext(),topic.get(label.get(groupPosition)).get(childPosition),Toast.LENGTH_SHORT).show();

                switch (groupPosition) {
                    case 0: {

                        Intent loaddata = new Intent(getApplicationContext(), ScrollingActivity.class);
                        new information().aadhar(childPosition);
                        loaddata.putExtra("data",information.data );
                        loaddata.putExtra("title", information.title);
                        startActivity(loaddata);
                        break;
                    }

                    case 1: {

                        Intent loaddata = new Intent(getApplicationContext(), ScrollingActivity.class);
                        new information().voterid(childPosition);
                        loaddata.putExtra("data",information.data );
                        loaddata.putExtra("title", information.title);
                        startActivity(loaddata);
                        break;
                    }

                    case 2: {

                        Intent loaddata = new Intent(getApplicationContext(), ScrollingActivity.class);
                        new information().pan(childPosition);
                        loaddata.putExtra("data",information.data );
                        loaddata.putExtra("title", information.title);
                        startActivity(loaddata);
                        break;
                    }

                    case 3: {

                        Intent loaddata = new Intent(getApplicationContext(), ScrollingActivity.class);
                        new information().ration(childPosition);
                        loaddata.putExtra("data",information.data );
                        loaddata.putExtra("title", information.title);
                        startActivity(loaddata);
                        break;
                    }
                    case 4:{
                        Intent loaddata=new Intent(getApplicationContext(),ScrollingActivity.class);
                        new information().licence(childPosition);
                        loaddata.putExtra("data",information.data);
                        loaddata.putExtra("title",information.title);
                        startActivity(loaddata);
                        break;

                    }

                }

                return true;
            }
        });

    }

    public void filldata() {
        label = new ArrayList<>();
        topic = new HashMap<>();
        for (String b : getResources().getStringArray(R.array.list)) {
            label.add(b);
        }


        List<String> aadharcard = new ArrayList<>();
        List<String> votercard = new ArrayList<>();
        List<String> pancard = new ArrayList<>();
        List<String> rationcard = new ArrayList<>();
        List<String> driving=new ArrayList<>();

        for (String a : getResources().getStringArray(R.array.aadhar)) {
            aadharcard.add(a);
        }

        for (String v : getResources().getStringArray(R.array.Voter)) {
            votercard.add(v);
        }
            for (String p : getResources().getStringArray(R.array.pan)) {
                pancard.add(p);
            }

            for (String ra : getResources().getStringArray(R.array.Ration)) {
                rationcard.add(ra);
            }
            for(String dl:getResources().getStringArray((R.array.Driving))){
                driving.add(dl);
            }


            topic.put(label.get(0), aadharcard);
            topic.put(label.get(1), votercard);
            topic.put(label.get(2), pancard);
            topic.put(label.get(3), rationcard);
            topic.put(label.get(4),driving);


      }

    }

