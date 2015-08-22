package com.mallardduckapps.fashiontalks.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mallardduckapps.fashiontalks.FashionTalksApp;
import com.mallardduckapps.fashiontalks.R;
import com.mallardduckapps.fashiontalks.objects.Country;
import com.mallardduckapps.fashiontalks.utils.FTUtils;

import java.util.ArrayList;

/**
 * Created by oguzemreozcan on 22/08/15.
 */
public class CountryAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<Country> data;
    private LayoutInflater inflater = null;
    private final String TAG = "CountryAdapter";
    Resources res;
    String font;
    FashionTalksApp app;

    public CountryAdapter(Activity act, ArrayList<Country> countryList) {

        this.activity = act;
        data = countryList;
        font = activity.getString(R.string.font_helvatica_lt);
        res = act.getResources();
        inflater = (LayoutInflater) act
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        app = (FashionTalksApp) act.getApplication();
    }

    public void addData(ArrayList<Country> data) {
        if (this.data == null) {
            this.data = data;
        } else {
            // this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        final Country country = data.get(position);
        if (vi == null) {
            vi = inflater.inflate(R.layout.country_row, parent,
                    false);
            holder = new ViewHolder();
            holder.nameTv = (TextView) vi.findViewById(R.id.textViewItem);
            holder.nameTv.setTypeface(FTUtils.loadFont(activity.getAssets(), font));
            vi.setTag(holder);
        } else {
            holder = (ViewHolder) vi.getTag();
        }
        holder.nameTv.setText(country.getName());
        return vi;
    }

    public static class ViewHolder {
        TextView nameTv;
    }
}