package com.example.eric.restless;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RatingBar;

/**
 * Created by Eric on 11/3/2016.
 */

public class RowAdapter extends BaseAdapter{

    private Context mContext;
    @Override
    public int getCount() {
        return 0;
    }
    public RowAdapter(Context mContext, int a){
        this.mContext=mContext;
    }
    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v =View.inflate(mContext,R.layout.customrow, null);
        EditText skill_name= (EditText) v.findViewById(R.id.row_skill);
        RatingBar skill_rating = (RatingBar) v.findViewById(R.id.ratingBar);
        return v;
    }
}
