package org.aguapoints.aguapointsapp;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/*
 * Created by edgartrujillo on 7/12/16.
 */

    public class GridViewAdapter extends ArrayAdapter<ImageItem> {

        private Context context;
        private int layoutResourceId;
        private ArrayList<ImageItem> data = new ArrayList<ImageItem>();

        public GridViewAdapter(Context context, int layoutResourceId, ArrayList<ImageItem> data) {
            super(context, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.data = data;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ViewHolder holder;

            if (row == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new ViewHolder();
                holder.imageTitle = (TextView) row.findViewById(R.id.text);
                holder.image = (ImageView) row.findViewById(R.id.image);
                row.setTag(holder);
            } else {
                holder = (ViewHolder) row.getTag();
            }


            ImageItem item = data.get(position);
            holder.imageTitle.setText(item.getTitle());
            holder.image.setImageBitmap(item.getImage());
            return row;
        }

        static class ViewHolder {
            TextView imageTitle;
            ImageView image;
        }
    }