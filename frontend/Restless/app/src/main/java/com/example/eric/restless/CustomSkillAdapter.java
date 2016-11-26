package com.example.eric.restless;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by minh on 11/25/16.
 */

public class CustomSkillAdapter extends CustomAdapter{
    SkillModel tempSkill = null;
    public CustomSkillAdapter(Activity a, ArrayList d, Resources resLocal){
        super(a,d,resLocal);
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView skillText;
        public RatingBar skillRating;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = CustomAdapter.getInflater().inflate(R.layout.skillitem, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.skillText = (TextView) vi.findViewById(R.id.skillText);
            holder.skillRating = (RatingBar)vi.findViewById(R.id.skillRating);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(super.getData().size()<=0)
        {
            holder.skillText.setText("No skill");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempSkill=null;
            tempSkill = ( SkillModel ) super.getData().get( position );

            /************  Set Model values in Holder elements ***********/

            holder.skillText.setText( tempSkill.getSkillString() );
            holder.skillRating.setRating(tempSkill.getSkillRating());
            /******** Set Item Click Listner for LayoutInflater for each row *******/
            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }
}
