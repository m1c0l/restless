package com.example.eric.restless;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minh on 11/27/16.
 */

public class developerUnit {
    private String bio;
    private String city;
    private int desiredSalary;
    private String email;
    private String firstName;
    private String lastName;
    private String githubLink;
    private int id;
    //TODO more fields that we'll add as we go

    public developerUnit(){
        firstName = "";
        lastName = "";
    }


    //server side up
    //updates data from server with assumption
    //that id is updated
    public void pullFromServer(){
        try{
            final String url = new String("http://159.203.243.194/api/get/user/" + id);
            //final JSONObject obj = new JSONObject();
            final httpInterface requester = new httpInterface();
            //populate obj
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                    try {

                        if(b!=null) {
                            JSONArray projects = (JSONArray)b.get("results");
                            //JSONObject project = projects.getJSONObject(0);
                            /*
                            state = project.getInt("current_state");
                            description = project.getString("description");
                            payRange = project.getInt("pay_range");
                            pm_id = project.getInt("pm_id");
                            id = project.getInt("id");
                            title = project.getString("title");
                            JSONArray jskills = (JSONArray)project.get("skills_needed");
                            for (int i =0 ; i < jskills.length(); i++){
                                skills.add(jskills.getString(i));
                            }
                            */
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
    }


    //setters
    public void setId(int i){id = i;}

    //getters
    public int getId(){return id;}
    public String getName(){return firstName + " " + lastName;}

}
