package com.example.eric.restless;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
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

public class CustomAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList data;
    private static LayoutInflater inflater = null;
    public Resources res;
    SkillModel tempSkill = null;
    int i = 0;

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

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView skillText;
        public RatingBar skillRating;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.tabitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.skillText = (TextView) vi.findViewById(R.id.skillText);
            holder.skillRating = (RatingBar)vi.findViewById(R.id.skillRating);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.skillText.setText("No skill");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempSkill=null;
            tempSkill = ( SkillModel ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.skillText.setText( tempSkill.getSkillString() );
            holder.skillRating.setRating(tempSkill.getSkillRating());
            /******** Set Item Click Listner for LayoutInflater for each row *******/

            //vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }

}
