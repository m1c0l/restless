package com.example.eric.restless;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class PMActivity extends AppCompatActivity {
    ListView list;
    projectAdapter adapter;
    public PMActivity customListView = null;
    public ArrayList<projectPreview> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);
        customListView = this;

        Resources res = getResources();
        list = (ListView) findViewById(R.id.project_list);

        //do data pull of project

        CustomListViewValuesArr.add(new projectPreview("Project 1"));
        CustomListViewValuesArr.add(new projectPreview("Project 2"));
        CustomListViewValuesArr.add(new projectPreview("Project 3"));

        adapter = new projectAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
        //get list of projects for user
    }
    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        //goes to project view page with project id
    }


    public void createProjectMethod(View v){
        Intent transfer=new Intent(PMActivity.this, createProjectActivity.class);
        startActivity(transfer);
    }

}
