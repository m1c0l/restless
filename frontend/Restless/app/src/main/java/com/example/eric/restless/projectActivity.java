package com.example.eric.restless;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;

public class projectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project);
        Button pendingMatch = (Button) findViewById(R.id.createProjectButton);
        Button editProject = (Button) findViewById(R.id.createProjectButton);
        Button selectDevelopers = (Button) findViewById(R.id.createProjectButton);

    }

    public void pendingMatchMethod(View v){
        Intent transfer=new Intent(projectActivity.this, pendingMatchActivity.class);
        startActivity(transfer);
    }
    public void editProjectMethod(View v){
        Intent transfer=new Intent(projectActivity.this, editProjectActivity.class);
        startActivity(transfer);
    }
    public void selectDevelopersMethod(View v){
        Intent transfer=new Intent(projectActivity.this, selectDevelopersActivity.class);
        startActivity(transfer);
    }
}
