package com.example.eric.restless;

import android.os.Bundle;

/**
 * Created by minh on 11/27/16.
 */

public class profileDisplayManageDev extends profileDisplayManage {
    projectUnit project;
    developerUnit developer;
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Bundle b = getIntent().getExtras();
        project = b.getParcelable("TEMP_PROJECT");
        developer = b.getParcelable("TEMP_USER");

    }

        public void setText(){
            body1.setText(developer.getBody1());
            body2.setText(developer.getBody2());
            body3.setText(developer.getBody3());
            body4.setText(developer.getBody4());


        }
    public  void onDelete(){}
    public  void onConfirm(){}
}
