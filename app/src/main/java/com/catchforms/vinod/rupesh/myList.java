package com.catchforms.vinod.rupesh;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by root on 3/18/17.
 */

public class myList extends BaseExpandableListAdapter {

    Activity context;
    List<String> label;
    Map<String,List<String>> topic;
    Integer image[];

    public myList(Activity context, List<String> label, Map<String, List<String>> topic,Integer image[]) {
        this.context = context;
        this.label = label;
        this.topic = topic;
        this.image = image;
    }



    @Override
    public int getGroupCount() {
        return label.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return topic.get(label.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return label.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return topic.get(label.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        convertView=context.getLayoutInflater().inflate(R.layout.textparent,null);
        TextView textparent=(TextView)convertView.findViewById(R.id.textparent);
        CircleImageView cv=(CircleImageView)convertView.findViewById(R.id.parent);
        cv.setImageResource(image[groupPosition]);
        textparent.setText(label.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        convertView=context.getLayoutInflater().inflate(R.layout.textchild,null);
        TextView textchild=(TextView)convertView.findViewById(R.id.textchild);
        textchild.setText(topic.get(label.get(groupPosition)).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
