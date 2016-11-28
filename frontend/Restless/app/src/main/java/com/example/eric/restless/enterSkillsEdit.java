package com.example.eric.restless;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minh on 11/27/16.
 */

public class enterSkillsEdit extends enterSkills {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //getting skills
        //doing query of user skills
        developerUnit d = new developerUnit();
        d.setId(User.getUser().getId());
        d.pullFromServer();

        for (String skill : d.getSkillSet()){
            skillUnit s = new skillUnit();
            s.setUser();
            s.setId(User.getUser().getId());
            s.setName(skill);
            CustomListViewValuesArr.add(s);
        }
        super.onCreate(savedInstanceState);
    }

    public void finishSkillsList(View v){
        final httpInterface requester = new httpInterface();
            //updating skill set
            try {
                JSONArray j = new JSONArray("skill_sets");
                for (skillUnit s: CustomListViewValuesArr)
                    j.put(s.getName());
                final JSONObject obj = new JSONObject();
                obj.put("skill_sets", j);
                try{
                final String url = new String("http://159.203.243.194/api/update/user/" + User.getUser().getId());
                    Thread thread=new Thread(new Runnable() {
                    public void run() {
                        JSONObject b=requester.request("POST", obj, url);

                    }
                });
                    thread.start();
                    thread.join();
                }catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch(JSONException e){
                e.printStackTrace();
            }
        Intent transfer = new Intent(enterSkillsEdit.this, DevPMSelectionActivity.class);
        startActivity(transfer);
    }

}
