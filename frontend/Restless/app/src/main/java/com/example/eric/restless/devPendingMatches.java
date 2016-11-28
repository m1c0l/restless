package com.example.eric.restless;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class devPendingMatches extends AppCompatActivity {
    ListView list;
    pendingProjectAdapter adapter;
    public devPendingMatches customListView = null;
    public ArrayList<projectUnit> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_pending_matches);
        //getting pending matches
        customListView = this;
        list = (ListView) findViewById(R.id.project_list);
        Resources res = getResources();

        //getting matched projects
        final ArrayList<Integer> pid = new ArrayList<>();
        final httpInterface requester = new httpInterface();
        try {
            System.setProperty("http.keepAlive", "false");
            final String url = new String("http://159.203.243.194/api/matches/1/" + User.getUser().getId() + "/1");
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                    try {
                        if(b!=null){
                            JSONArray ids = (JSONArray)b.get("results");
                            for (int i = 0; i < ids.length(); i++){
                                pid.add(ids.getInt(i));
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
        //for all project ids
        for (int id : pid){
            projectUnit p = new projectUnit();
            p.setId(id);
            p.pullFromServer();
            CustomListViewValuesArr.add(p);
        }

        adapter = new pendingProjectAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
    }
    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        Intent transfer=new Intent(devPendingMatches.this, profileMatchedProject.class);
        projectUnit project = CustomListViewValuesArr.get(mPosition);
        transfer.putExtra("TEMP_PROJECT", project);
        startActivity(transfer);
    }
}
