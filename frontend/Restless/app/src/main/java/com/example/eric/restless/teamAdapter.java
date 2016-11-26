package com.example.eric.restless;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by minh on 11/26/16.
 */

public class teamAdapter extends CustomAdapter {
    TeamModel tempTeam = null;
    public teamAdapter(Activity a, ArrayList d, Resources resLocal){
        super(a,d,resLocal);
    }

    public static class ViewHolder{
        public TextView teamName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        teamAdapter.ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = CustomAdapter.getInflater().inflate(R.layout.team_item, null);

            /****** View Holder Object to contain team_item.xml file elements ******/

            holder = new teamAdapter.ViewHolder();
            holder.teamName = (TextView) vi.findViewById(R.id.teamName);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(teamAdapter.ViewHolder)vi.getTag();

        if(super.getData().size()<=0)
        {
            holder.teamName.setText("No teammates");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempTeam=null;
            tempTeam = ( TeamModel ) super.getData().get( position );

            /************  Set Model values in Holder elements ***********/
            holder.teamName.setText( tempTeam.getName() );
            /******** Set Item Click Listner for LayoutInflater for each row *******/
            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }
}
