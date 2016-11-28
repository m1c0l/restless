package com.example.eric.restless;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by minh on 11/26/16.
 */

public class projectUnit implements Parcelable {

    private int state;
    private String description;
    private int payRange;
    private int pm_id;
    private int id;
    private ArrayList<String> skills;
    private String title;

    //construct from parcel
    public projectUnit(Parcel in) {
        state = in.readInt();
        description = in.readString();
        payRange = in.readInt();
        pm_id = in.readInt();
        id = in.readInt();
        skills = (ArrayList<String>)in.readSerializable();
        title = in.readString();
    }
    //writing to parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(state);
        dest.writeString(description);
        dest.writeInt(payRange);
        dest.writeInt(pm_id);
        dest.writeInt(id);
        dest.writeSerializable(skills);
        dest.writeString(title);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<projectUnit> CREATOR = new Parcelable.Creator<projectUnit>() {
        public projectUnit createFromParcel(Parcel in) {
            return new projectUnit(in);
        }

        public projectUnit[] newArray(int size) {
            return new projectUnit[size];
        }
    };


    public projectUnit(){
        state = 0;
        description = "";
        payRange = -1;
        pm_id = User.getUser().getId();
        skills = new ArrayList<>();
        title = "";
    }

    public boolean newProjectToServer(){
        final httpInterface requester = new httpInterface();
        //creating new project first
        try{
            final String url = new String("http://159.203.243.194/api/new_project/");
            final JSONObject obj = new JSONObject();
            //populate obj
            obj.put("title", title);
            obj.put("pm_id", User.getUser().getId());
            obj.put("description", description);

            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("POST", obj, url);
                    try {
                        if(b!=null) {
                            //get id of project
                            id = b.getInt("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
            thread.join();

        }  catch (JSONException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

        //updating pay range and status
        try{
            final String url = new String("http://159.203.243.194/api/update/project/" + id);
            final JSONObject obj = new JSONObject();
            //populate obj
            obj.put("pay_range", payRange);
            obj.put("current_state", state);
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("POST", obj, url);
                    try {
                        if(b!=null) {
                            //get id of project
                            id = b.getInt("id");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            thread.start();
            thread.join();

        }  catch (JSONException e) {
            e.printStackTrace();
        }catch (InterruptedException e) {
            e.printStackTrace();
        }


        return true;

    }


    public boolean pullFromServer(){
        try{
            final String url = new String("http://159.203.243.194/api/get/project/" + id);
            //final JSONObject obj = new JSONObject();
            final httpInterface requester = new httpInterface();
            //populate obj
            Thread thread=new Thread(new Runnable() {
                public void run() {
                    JSONObject b=requester.request("GET", null, url);
                    try {

                        if(b!=null) {
                            JSONArray projects = (JSONArray)b.get("results");
                            JSONObject project = projects.getJSONObject(0);
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
        return true;
    }

    //setters
    public void setId(int i){
        id = i;
    }
    public void setTitle(String s){
         title = s;
    }
    public void setPm_id(int i){
        pm_id = i;
    }
    public void setPayRange(int i){
        payRange = i;
    }
    public void setDescription(String s){
        description = s;
    }
    public void setState(int i){
        state = i;
    }

    //getters
    public String getTitle(){
        return title;
    }
    public ArrayList<String> getSkills(){
        return skills;
    }
    public int getPm_id(){
        return pm_id;
    }
    public int getState(){
        return state;
    }
    public String getDescription(){
        return description;
    }
    public int getPayRange(){
        return payRange;
    }
    public int getId() {
        return id;
    }

}
