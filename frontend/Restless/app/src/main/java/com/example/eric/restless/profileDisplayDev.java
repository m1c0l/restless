package com.example.eric.restless;

import android.os.Bundle;
import android.view.View;

/**
 * Created by minh on 11/26/16.
 */

public class profileDisplayDev extends profileDisplay{
    /*
    public void onConfirm(View v){
        //push to pm
        //go back to original devs
    }
    public void onDelete(View v){
        //delete from list of matches
        //go back to list of devs
    }
    */

    public void setText(){
        Bundle b = getIntent().getExtras();
        developerUnit d = b.getParcelable("TEMP_USER");
        body1.setText(d.getBody1());
        body2.setText(d.getBody2());
        body3.setText(d.getBody3());
        body4.setText(d.getBody4());
    }
}