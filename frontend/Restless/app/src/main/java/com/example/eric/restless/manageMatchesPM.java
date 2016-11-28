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

public class manageMatchesPM extends AppCompatActivity {
    ListView list;
    matchAdapter adapter;
    private projectUnit project;
    private ArrayList<Integer> matchIds = new ArrayList<>();
    public manageMatchesPM customListView = null;
    public ArrayList<developerUnit> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_matches_pm);
        //getting project that you are managing matches for
        Bundle b = getIntent().getExtras();
        project = b.getParcelable("TEMP_PROJECT");
        //getting members
        getPendingMatches();

        //custom list view stuff
        customListView = this;
        list = (ListView) findViewById(R.id.matchList);
        Resources res = getResources();
        //setting adapter
        adapter = new matchAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
    }

    private void getPendingMatches(){
        //querying match
        try{
            final httpInterface requester = new httpInterface();
            final String url = new String("http://159.203.243.194/api/matches/0/"
                    + project.getId() + "1");
        Thread thread=new Thread(new Runnable() {
            public void run() {
                JSONObject b=requester.request("GET", null, url);
                try {

                    if(b!=null) {
                        JSONArray a = b.getJSONArray("results");
                        for (int i = 0; i < a.length(); i++){
                            matchIds.add(a.getInt(i));
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

        //once ids are gotten, need to populate arraylist
        for (int id : matchIds){
            developerUnit d = new developerUnit();
            d.setId(id);
            d.pullFromServer();
            CustomListViewValuesArr.add(d);
        }
    }
    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        //go to profiledisplaydev to confirm or deny this person
        Intent transfer=new Intent(manageMatchesPM.this, profileDisplayManageDev.class);
        //pass member id and go to activity that you can view member profile
        transfer.putExtra("TEMP_PROJECT", project);
        transfer.putExtra("TEMP_USER", CustomListViewValuesArr.get(mPosition));
        startActivity(transfer);

    }




    }
