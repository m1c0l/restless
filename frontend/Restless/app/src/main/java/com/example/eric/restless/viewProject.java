package com.example.eric.restless;

import android.app.Dialog;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class viewProject extends AppCompatActivity {

    ListView list;
    teamAdapter adapter;
    public viewProject customListView = null;
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
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        //go to team member page corresponding to id?
    }

    



}
