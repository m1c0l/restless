package com.example.eric.restless;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minh on 11/27/16.
 */

public class enterSkillsEdit extends enterSkills {
    private developerUnit d = new developerUnit();
    protected void onCreate(Bundle savedInstanceState) {
        //getting skills
        //doing query of user skills
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

    public void finishSkillsList(View v) {


        final httpInterface requester = new httpInterface();
        for (String skill : d.getSkillSet()) {
            //deleting all skill
            try {
                final String url = new String("http://159.203.243.194/api/skill/delete/user/"
                        + skill + "/" + User.getUser().getId());
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        JSONObject b = requester.request("GET", null, url);
                    }
                });
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (skillUnit skill1 : CustomListViewValuesArr) {
            String skill = skill1.getName();
            //deleting all skill
            try {
                final String url = new String("http://159.203.243.194/api/skill/add/user/"
                        + skill + "/" + User.getUser().getId());
                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        JSONObject b = requester.request("GET", null, url);
                    }
                });
                thread.start();
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Intent transfer = new Intent(enterSkillsEdit.this, DevPMSelectionActivity.class);
            startActivity(transfer);
        }
    }

}
