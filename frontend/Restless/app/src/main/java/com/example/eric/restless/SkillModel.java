package com.example.eric.restless;

/**
 * Created by minh on 11/13/16.
 */

public class SkillModel {
    private String skillString;
    private float skillRating;

    public SkillModel(String s, float r){
        skillString = s;
        skillRating = r;
    }
    public void setSkillString(String s){
        skillString = s;
    }
    public void setSkillRating(float i){
        skillRating = i;
    }

    public String getSkillString(){
        return skillString;
    }
    public float getSkillRating(){
        return skillRating;
    }
}
