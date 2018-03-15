package com.catchforms.vinod.rupesh;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Rupesh gupta on 3/24/2017.
 */
public class myclass extends ArrayAdapter {

    Activity context;
    String list[];
    public myclass(Activity context, String[] list) {
        super(context, R.layout.textlist,list);
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v= context.getLayoutInflater().inflate(R.layout.textlist,null);
        TextView tv=(TextView)v.findViewById(R.id.ggg);
        tv.setText(list[position]);
        return v;
    }
}
