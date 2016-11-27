package com.example.eric.restless;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class viewProjectPM extends AppCompatActivity {

    ListView list;
    teamAdapter adapter;
    public viewProjectPM customListView = null;
    public ArrayList<TeamModel> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);

        //pull project object
        TextView projectName = (TextView) findViewById(R.id.projectName);
        TextView projectDescription = (TextView) findViewById(R.id.projectDescription);
        ImageView projectImage = (ImageView) findViewById(R.id.projectImage);

        projectName.setText("Project Name");
        projectDescription.setText("Project Description");

        //pulling team
        list = (ListView) findViewById(R.id.teamList);
        Resources res = getResources();
        adapter = new teamAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);

        //setting up buttons
        Button swipeButton = (Button) findViewById(R.id.swipe);
        Button matchButton = (Button) findViewById(R.id.swipe);
        Button lockButton = (Button) findViewById(R.id.lock);
        Button deleteButton = (Button) findViewById(R.id.delete);

        //if locked, these 2 are not set and buttons are set unclickable
        swipeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                swipeProject(v);
            }
        });
        matchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                manageMatches(v);
            }
        });
        //

        lockButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                lockProject(v);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteProject(v);
            }
        });

    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        //pass member id and go to activity that you can view member profile
    }

    public void swipeProject(View v){
        //go to swipe page for pm
    }

    public void manageMatches(View v){
        //got to manage match page
    }
    public void lockProject(View v){
        //locks the project, aka team is locked
        //goes back to this proejct
    }
    public void deleteProject(View v){
        //confirmation pop up
        //go back to main pm page2
    }
}
