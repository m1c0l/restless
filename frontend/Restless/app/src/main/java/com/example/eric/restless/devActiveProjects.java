package com.example.eric.restless;

import android.content.Intent;
import android.content.res.Resources;
import android.media.projection.MediaProjection;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class devActiveProjects extends AppCompatActivity {
    ListView list;
    projectAdapter adapter;
    public devActiveProjects customListView = null;
    public ArrayList<projectUnit> CustomListViewValuesArr = new ArrayList<>();

    //adding data



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_active_projects);

        //setting up adapter
        customListView = this;
        Resources res = getResources();
        list = (ListView) findViewById(R.id.project_list);
        //getting data
        developerUnit d = new developerUnit();
        d.setId(User.getUser().getId());
        d.pullFromServer();

        for (int pid : d.getProjectsDev()){
            projectUnit p = new projectUnit();
            p.setId(pid);
            p.pullFromServer();
            //inactive state
            if (p.getState() != 2){
                CustomListViewValuesArr.add(p);
            }
        }
        adapter = new projectAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        Intent transfer=new Intent(devActiveProjects.this, viewProjectDev.class);
        projectUnit project = CustomListViewValuesArr.get(mPosition);
        transfer.putExtra("TEMP_PROJECT", project);
        startActivity(transfer);
    }


}
