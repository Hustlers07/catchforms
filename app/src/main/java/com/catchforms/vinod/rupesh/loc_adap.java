package com.catchforms.vinod.rupesh;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Rupesh gupta on 4/29/2017.
 */

public class loc_adap extends ArrayAdapter {
    Activity context;
    String list[];
    public loc_adap(@NonNull Activity context, @NonNull String list[]) {
        super(context, R.layout.textlist , list);
        this.context=context;
        this.list=list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=context.getLayoutInflater().inflate(R.layout.loc_adap,null);
        TextView tv=(TextView)v.findViewById(R.id.loc_adap);
        tv.setText(list[position]);
        return v;
    }
}
