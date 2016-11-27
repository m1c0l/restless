package com.example.eric.restless;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by minh on 11/13/16.
 */

public abstract class CustomAdapter extends BaseAdapter implements View.OnClickListener {
    protected Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    //getters and setters
    public ArrayList getData(){
        return data;
    }
    public static LayoutInflater getInflater(){
        return inflater;
    }



    public CustomAdapter(Activity a, ArrayList d, Resources resLocal){
        activity = a;
        data = d;
        res = resLocal;
        /***********  Layout inflator to call external xml layout () ***********/
        inflater = ( LayoutInflater )activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }


    /****** Depends upon data size called for each row , Create each ListView row *****/
    public abstract View getView(int position, View convertView, ViewGroup parent);



}
