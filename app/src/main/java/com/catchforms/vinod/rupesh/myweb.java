package com.catchforms.vinod.rupesh;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Rupesh gupta on 3/26/2017.
 */
public class myweb extends ArrayAdapter {
   Activity context;
    ArrayList website;
    ArrayList yojna_url;
    ProgressBar pb;
    public myweb(Activity context,ArrayList website, ArrayList yojna_url) {
        super(context, R.layout.current_web,website);
        this.context = context;
        this.website = website;
        this.yojna_url=yojna_url;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v= context.getLayoutInflater().inflate(R.layout.current_web,null);

        LinearLayout linearLayout = (LinearLayout)v.findViewById(R.id.yojna_view);
        TextView yojna =(TextView)v.findViewById(R.id.yojna);

        yojna.setText(website.get(position).toString());



        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity3.class);
                intent.putExtra("url",yojna_url.get(position).toString());
                context.startActivity(intent);
            }
        });
        return v;
    }

    class load extends WebViewClient{
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        pb.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
          pb.setVisibility(View.INVISIBLE);
        }
    }
}
