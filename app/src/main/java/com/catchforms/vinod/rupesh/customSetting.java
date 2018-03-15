package com.catchforms.vinod.rupesh;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 3/29/17.
 */
public class customSetting extends ArrayAdapter {
    Activity context;
    String[] setting;
    Integer[] image;

    public customSetting(Activity context, String[] setting, Integer[] image) {
        super(context,R.layout.textparent,setting);
        this.context = context;
        this.setting = setting;
        this.image = image;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v=context.getLayoutInflater().inflate(R.layout.textparent,null);
        TextView tv=(TextView)v.findViewById(R.id.textparent);
        tv.setText(setting[position]);
        CircleImageView cv=(CircleImageView)v.findViewById(R.id.parent);
        cv.setImageResource(image[position]);
        return v;
    }
}
