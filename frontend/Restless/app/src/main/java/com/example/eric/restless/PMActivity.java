package com.example.eric.restless;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PMActivity extends AppCompatActivity {
    ListView list;
    projectAdapter adapter;
    public PMActivity customListView = null;
    public ArrayList<projectUnit> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pm);
        //setting up listview
        customListView = this;
        Resources res = getResources();
        list = (ListView) findViewById(R.id.project_list);
        //getting user
        final httpInterface requester = new httpInterface();
        final ArrayList<Integer> projectIDs = new ArrayList<>();

        try {
            System.setProperty("http.keepAlive", "false");
            final String url = new String("http://159.203.243.194/api/get/user/"
                    + User.getUser().getId());
            //final JSONObject requestObj = new JSONObject();
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                    try {
                        if(b!=null){
                            JSONArray users = (JSONArray)b.get("results");
                            JSONObject user = users.getJSONObject(0);
                            JSONArray jProjectIDs = user.getJSONArray("projects_managing");
                            for (int i = 0; i < jProjectIDs.length(); i++){
                                projectIDs.add(jProjectIDs.getInt(i));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //for all projects in project list
        //do a pull to populate the project objects
        for (Integer projectId : projectIDs){
            projectUnit p = new projectUnit();
            p.setId(projectId);
            p.pullFromServer();
            //if project isn't deleted
            if (p.getState() != 2)
                CustomListViewValuesArr.add(p);
        }

        //display object preview
        adapter = new projectAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
        //get list of projects for user
    }
    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        Intent transfer=new Intent(PMActivity.this, viewProjectPM.class);
        projectUnit project = CustomListViewValuesArr.get(mPosition);
        transfer.putExtra("TEMP_PROJECT", project);
        startActivity(transfer);
    }


    public void createProjectMethod(View v){
        Intent transfer=new Intent(PMActivity.this, createProjectActivity.class);
        projectUnit p = new projectUnit();
        transfer.putExtra("TEMP_PROJECT", p);
        startActivity(transfer);
    }

}
