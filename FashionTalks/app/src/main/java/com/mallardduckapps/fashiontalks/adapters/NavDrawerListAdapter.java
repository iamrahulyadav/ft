package com.mallardduckapps.fashiontalks.adapters;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.R;

/**
 * Created by oguzemreozcan on 13/02/15.
 */

public class NavDrawerListAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final String[] names;
    private final Integer[] imageId;
    public NavDrawerListAdapter(Activity context,
                      String[] names, Integer[] imageId) {
        super(context, R.layout.navdrawer_row, names);
        this.context = context;
        this.names = names;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.navdrawer_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.text);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
        txtTitle.setText(names[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
