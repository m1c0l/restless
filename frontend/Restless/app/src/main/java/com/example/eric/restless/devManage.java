package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class devManage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_manage);
    }
    public void onActive(View v){
        Intent transfer=new Intent(devManage.this,devActiveProjects.class);
        startActivity(transfer);

    }
    public void onMatches(View v){
        Intent transfer=new Intent(devManage.this,devPendingMatches.class);
        startActivity(transfer);
    }
    public void home(View v){
        Intent transfer = new Intent(devManage.this, DevPMSelectionActivity.class);
        startActivity(transfer);
    }
}
