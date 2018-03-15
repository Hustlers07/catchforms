package com.catchforms.vinod.rupesh;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by root on 7/4/17.
 */

class news_setter extends ArrayAdapter {
    Activity context;
    ArrayList title;
    ArrayList description;
    ArrayList news_url;

    public news_setter(Activity context, ArrayList title, ArrayList description, ArrayList news_url) {
        super(context, R.layout.news_display,title);
        this.context = context;
        this.title = title;
        this.description = description;
        this.news_url = news_url;
    }

    LinearLayout news_display;
    TextView news_title,news_description;
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = context.getLayoutInflater().inflate(R.layout.news_display,null);
        news_title =(TextView)v.findViewById(R.id.news_title);
        news_description =(TextView)v.findViewById(R.id.news_description);
        news_display =(LinearLayout)v.findViewById(R.id.news_layout);
        news_title.setText(title.get(position).toString());
        news_description.setText(title.get(position).toString());
        news_display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity3.class);
                intent.putExtra("url",news_url.get(position).toString());
                context.startActivity(intent);
            }
        });

        return v;
    }


}
