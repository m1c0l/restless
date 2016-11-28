package com.example.eric.restless;

import android.app.Activity;
import android.content.res.Resources;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by minh on 11/25/16.
 */

public class projectAdapter extends CustomAdapter {
    projectUnit project = null;
    public projectAdapter(Activity a, ArrayList d, Resources resLocal){
        super(a,d,resLocal);
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public TextView projectName;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        projectAdapter.ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = CustomAdapter.getInflater().inflate(R.layout.project_preview_item, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new projectAdapter.ViewHolder();
            holder.projectName = (TextView) vi.findViewById(R.id.projectName);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(projectAdapter.ViewHolder)vi.getTag();

        if(super.getData().size()<=0)
        {
            holder.projectName.setText("No project");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            project=null;
            project = ( projectUnit ) super.getData().get( position );

            /************  Set Model values in Holder elements ***********/

            holder.projectName.setText( project.getTitle() );
            /******** Set Item Click Listner for LayoutInflater for each row *******/
            vi.setOnClickListener(new OnItemClickListener( position ));
        }
        return vi;
    }
    public void onClick(View v) {
        Log.v("CustomAdapter", "=====Row button clicked=====");
    }
    /********* Called when Item click in ListView ************/
    protected class OnItemClickListener  implements View.OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
                PMActivity sct = (PMActivity)activity;
            /****  Call  onItemClick Method inside CustomListViewAndroidExample Class ( See Below )****/
            sct.onItemClick(mPosition);
        }
    }
}
