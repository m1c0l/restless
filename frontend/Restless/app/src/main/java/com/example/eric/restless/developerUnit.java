package com.example.eric.restless;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by minh on 11/27/16.
 */

public class developerUnit implements Parcelable{
    private String bio;
    private String city;
    private int desiredSalary;
    private String email;
    private String firstName;
    private String lastName;
    private String githubLink;
    private int id;
    private String phone;
    private ArrayList<Integer> projectsDev;
    private ArrayList<Integer> projectsManage;
    private String userName;
    private ArrayList<String> skillSet;

    //TODO more fields that we'll add as we go

    public developerUnit(){
        bio = "";
        city = "";
        desiredSalary = -1;
        email = "";
        firstName = "";
        lastName = "";
        githubLink = "";
        id = -1;
        phone = "";
        projectsDev = new ArrayList<>();
        projectsManage = new ArrayList<>();
        userName = "";
        skillSet = new ArrayList<>();
    }

    //name bio
    public String getBody1(){
        return "NAME";
    }
    public String getBody2(){
        return "hello world";
    }
    public String getBody3(){
        return "hello world";
    }
    public String getBody4(){
        return "hello world";
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
                            JSONArray users = (JSONArray)b.get("results");
                            JSONObject user = users.getJSONObject(0);
                            bio = user.getString("bio");
                            city = user.getString("city");
                            desiredSalary = user.getInt("desired_salary");
                            email = user.getString("email");
                            firstName = user.getString("first_name");
                            lastName = user.getString("last_name");
                            githubLink = user.getString("github_link");
                            phone = user.getString("phone");

                            userName = user.getString("username");
                            JSONArray temp = user.getJSONArray("skill_sets");
                            for (int i = 0; i < temp.length(); i++){
                                skillSet.add(temp.getString(i));
                            }
                            temp = user.getJSONArray("projects_managing");
                            for (int i = 0; i < temp.length(); i++){
                                projectsDev.add(temp.getInt(i));
                            }
                            temp = user.getJSONArray("projects_developing");
                            for (int i = 0; i < temp.length(); i++){
                                projectsManage.add(temp.getInt(i));
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
    }


    //setters
    public void setId(int i){id = i;}

    //getters
    public int getId(){return id;}
    public String getName(){return firstName + " " + lastName;}


    //parceable
    public developerUnit(Parcel in) {
        bio = in.readString();
        city = in.readString();
        desiredSalary = in.readInt();
        email = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        githubLink = in.readString();
        id = in.readInt();
        phone = in.readString();
        projectsDev = (ArrayList<Integer>)in.readSerializable();
        projectsManage = (ArrayList<Integer>)in.readSerializable();
        userName = in.readString();
        skillSet = (ArrayList<String>)in.readSerializable();
    }
    //writing to parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bio);
        dest.writeString(city);
        dest.writeInt(desiredSalary);
        dest.writeString(email);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(githubLink);
        dest.writeInt(id);
        dest.writeString(phone);
        dest.writeSerializable(projectsDev);
        dest.writeSerializable(projectsManage);
        dest.writeString(userName);
        dest.writeSerializable(skillSet);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<developerUnit> CREATOR = new Parcelable.Creator<developerUnit>() {
        public developerUnit createFromParcel(Parcel in) {
            return new developerUnit(in);
        }

        public developerUnit[] newArray(int size) {
            return new developerUnit[size];
        }
    };
    public ArrayList<String> getSkillSet(){
        return skillSet;
    }
    public String getGithubLink(){
        return githubLink;
    }
}
