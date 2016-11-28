package com.example.eric.restless;

import android.os.Bundle;

/**
 * Created by minh on 11/27/16.
 */

public class profileDisplayProject extends profileDisplay{
    public void setText(){
        projectUnit d;
        Bundle b = getIntent().getExtras();
        d = b.getParcelable("TEMP_USER");
        if (d != null) {
            title.setText(d.getTitle());
            body1.setText(d.getBody1());
            body2.setText(d.getBody2());
            body3.setText(d.getBody3());
            body4.setText(d.getBody4());
        }
    }
}
