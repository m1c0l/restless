package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PMActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);
        Button createProject = (Button) findViewById(R.id.createProjectButton);
        Button selectProject = (Button) findViewById(R.id.selectProjectButton);
    }

    public void createProjectMethod(View v){
        Intent transfer=new Intent(PMActivity.this, createProjectActivity.class);
        startActivity(transfer);
    }
    public void selectProjectMethod(View v){
        Intent transfer=new Intent(PMActivity.this, selectProjectActivity.class);
        startActivity(transfer);
    }
}
