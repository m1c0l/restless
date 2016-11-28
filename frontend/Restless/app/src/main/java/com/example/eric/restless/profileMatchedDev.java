package com.example.eric.restless;

import android.content.Intent;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minh on 11/27/16.
 */

public class profileMatchedDev extends profileMatched{
    projectUnit p;
    developerUnit d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        d = b.getParcelable("TEMP_USER");
        p = b.getParcelable("TEMP_PROJECT");
    }

    public void setText(){
        body1.setText(d.getBody1());
        body2.setText(d.getBody2());
        body3.setText(d.getBody3());
        body4.setText(d.getBody4());
    }
    public void onConfirm(){
        //pushign confirmation
        final httpInterface requester = new httpInterface();
        //creating new project first
        try{
            final String url = new String("http://159.203.243.194/api/matches/accept"
                    + d.getId() + "/" + p.getId());

            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);

                }
            });
            thread.start();
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        //go back to managed matches
        Intent transfer=new Intent(profileMatchedDev.this, manageMatchesPM.class);
        //pass member id and go to activity that you can view member profile
        transfer.putExtra("TEMP_PROJECT", p);
        startActivity(transfer);


    }

    public void onDelete(){
        final httpInterface requester = new httpInterface();
        //creating new project first
        try{
            final String url = new String("http://159.203.243.194/api/matches/decline/"
                    + d.getId() + "/" + p.getId());

            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);

                }
            });
            thread.start();
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        //go back to managed matches
        Intent transfer=new Intent(profileMatchedDev.this, manageMatchesPM.class);
        //pass member id and go to activity that you can view member profile
        transfer.putExtra("TEMP_PROJECT", p);
        startActivity(transfer);
    }
}
