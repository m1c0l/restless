package com.example.eric.restless;

import android.app.Dialog;
import android.content.Intent;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class viewProjectPM extends AppCompatActivity {

    ListView list;
    teamAdapter adapter;
    public viewProjectPM customListView = null;
    public ArrayList<developerUnit> CustomListViewValuesArr = new ArrayList<>();
    private projectUnit project;
    private ArrayList<Integer> teamIds = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);
        customListView = this;
        TextView projectName = (TextView) findViewById(R.id.projectName);
        TextView projectDescription = (TextView) findViewById(R.id.projectDescription);
        ImageView projectImage = (ImageView) findViewById(R.id.projectImage);


        //getting data from previous activity
        Bundle b = getIntent().getExtras();
       project = b.getParcelable("TEMP_PROJECT");

        projectName.setText(project.getTitle());
        projectDescription.setText(project.getDescription());

        //pulling team ids of this project
        pullTeam();

        //getting team information
        list = (ListView) findViewById(R.id.teamList);
        Resources res = getResources();

        //setting up dynamic list adapter
        adapter = new teamAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);


        //setting up buttons
        Button swipeButton = (Button) findViewById(R.id.swipe);
        Button matchButton = (Button) findViewById(R.id.swipe);
        Button lockButton = (Button) findViewById(R.id.lock);
        Button deleteButton = (Button) findViewById(R.id.delete1);

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

    public void pullTeam(){
        final httpInterface requester = new httpInterface();

        try {
            System.setProperty("http.keepAlive", "false");
            final String url = new String("http://159.203.243.194/api/confirmed/"
                    + project.getId());
            //final JSONObject requestObj = new JSONObject();
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                    try {
                        if(b!=null){
                            JSONArray ids = (JSONArray)b.get("results");
                            for (int i = 0; i < ids.length(); i++){
                                teamIds.add(ids.getInt(i));
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
        //for every team member
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
        //TODO confirmation dialog(?)
        final httpInterface requester = new httpInterface();
        try {
            System.setProperty("http.keepAlive", "false");
            final String url = new String("http://159.203.243.194/api/update/project/" + project.getId());
            final JSONObject requestObj = new JSONObject();
            requestObj.put("current_state", 1);
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("POST", requestObj, url);
                }
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        //TODO wipe out pending confirmations

        //locking up swipe and manage swipe buttons

    }
    public void deleteProject(View v){
        //TODO confirmation dialog(?)
        final httpInterface requester = new httpInterface();
        try {
            System.setProperty("http.keepAlive", "false");
            final String url = new String("http://159.203.243.194/api/update/project/" + project.getId());
            final JSONObject requestObj = new JSONObject();
            requestObj.put("current_state", 2);
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("POST", requestObj, url);
                }
            });
            thread.start();
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        //transfering back to pm project page
        Intent transfer=new Intent(viewProjectPM.this, PMActivity.class);
        startActivity(transfer);
    }
}
