package com.example.eric.restless;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class viewProjectDev extends AppCompatActivity {

    ListView list;
    teamAdapterDev adapter;
    public viewProjectDev customListView = null;
    public ArrayList<developerUnit> CustomListViewValuesArr = new ArrayList<>();
    private projectUnit project;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_project);
        customListView = this;
        TextView projectName = (TextView) findViewById(R.id.projectName);
        TextView projectDescription = (TextView) findViewById(R.id.projectDescription);
        final ImageView projectImage = (ImageView) findViewById(R.id.projectImage);



        //getting data from previous activity
        Bundle b = getIntent().getExtras();
        project = b.getParcelable("TEMP_PROJECT");

        projectName.setText(project.getTitle());
        projectDescription.setText(project.getDescription());
        Thread thread =new Thread(new Runnable(){
            public void run(){
                final Bitmap bmp[] = new Bitmap[1];
                bmp[0] = null;
                URL url = null;
                try {
                    url = new URL("http://159.203.243.194/api/img/get/project/"+project.getId());

                    bmp[0] = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (bmp[0] != null)
                                projectImage.setImageBitmap(bmp[0]);
                        }
                    });
                }catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //pulling team ids of this project
        pullTeam();

        //getting team information
        list = (ListView) findViewById(R.id.teamList);
        Resources res = getResources();

        //setting up dynamic list adapter
        adapter = new teamAdapterDev(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);

    }

    public void pullTeam(){
        final ArrayList<Integer> teamIds = new ArrayList<>();
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
        for (int id : teamIds){
            developerUnit d = new developerUnit();
            d.setId(id);
            d.pullFromServer();
            CustomListViewValuesArr.add(d);
        }
    }

    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {

        Intent transfer=new Intent(viewProjectDev.this, profileDisplayDev.class);
        //pass member id and go to activity that you can view member profile
        transfer.putExtra("TEMP_USER", CustomListViewValuesArr.get(mPosition));
        startActivity(transfer);


    }

}
