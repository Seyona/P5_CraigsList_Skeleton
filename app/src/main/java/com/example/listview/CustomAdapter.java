package com.example.listview;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by David & Jack on 4/5/16.
 */
public class CustomAdapter extends ArrayAdapter<BikeData> {
    SharedPreferences prefs;

    public CustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public CustomAdapter(Context context, int resource, List<BikeData> bikes) {
        super(context,resource,bikes);
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.listview_row_layout, null);
        }

        BikeData b = getItem(position);

        if (b != null) {
            ImageView img   = (ImageView)v.findViewById(R.id.imageView1);
            TextView  model = (TextView) v.findViewById(R.id.Model);
            TextView  price = (TextView) v.findViewById(R.id.Price);
            TextView  description = (TextView) v.findViewById(R.id.Description);

            if (model != null) {
                model.setText(b.getModel());
            }

            if (price != null) {
                price.setText("" + b.getPrice());
            }

            if (description != null) {
                description.setText(b.getDescription());
            }

            if (img != null) {
                DownloadImageTask task = new DownloadImageTask(b.getPicture(),img);
                task.execute(prefs.getString("listPref","")) ;
            }
        }

        return v;
    }


}
