package com.example.eric.restless;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minh on 11/27/16.
 */

public class skillUnit {


    private String name;
    private String type; // user or
    private int id;
    private float skillRating;

    public skillUnit(){
        name = null;
        skillRating = 0;
        type = null;
        id = -1;
    }
    public skillUnit(String s, float f){
        name = s;
        skillRating = f;
        type = null;
        id = -1;
    }


    //server side stuff
    public void pushToServer(){
        final httpInterface requester = new httpInterface();
        //creating new project first
        try{
            final String url = new String("http://159.203.243.194/api/skill/add/"
            + type + "/" + name + "/" + id);

            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);

                }
            });
            thread.start();
            thread.join();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //getters
    public String getName(){return name;}
    public float getSkillRating(){return skillRating;}

    //setters
    public void setName(String s){
        name = s;
    }

    public void setId(int i){
        id = i;
    }
    public void setSkillRating(float f){
        skillRating = f;
    }
    public void setUser(){
        type = "user";
    }
    public void setProject(){
        type = "project";
    }
}
