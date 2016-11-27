package com.example.eric.restless;

import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class pendingMatchActivity extends AppCompatActivity {

    ListView list;
    teamAdapter adapter;
    public viewProjectPM customListView = null;
    public ArrayList<TeamModel> CustomListViewValuesArr = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_match);


        //pulling up members that project matched with
        list = (ListView) findViewById(R.id.teamList);
        Resources res = getResources();
        adapter = new teamAdapter(customListView, CustomListViewValuesArr, res);
        list.setAdapter(adapter);
    }
    /*****************  This function used by adapter ****************/
    public void onItemClick(int mPosition)
    {
        //go to profileDisplay that was clicked on
    }
}
