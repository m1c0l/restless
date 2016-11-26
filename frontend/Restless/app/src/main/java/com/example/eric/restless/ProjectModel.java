package com.example.eric.restless;

import java.util.ArrayList;

public class ProjectModel {
    private String projectName;
    private String description;
    private float minIncome;
    private float maxIncome;
    private String imageString;
    private ArrayList<SkillModel> skillsList;

    //settters
    public void setProjectName(String s){
        projectName = s;
    }
    public void setDescription(String d){
        description = d;
    }
    public void setMinIncome(float f){
        minIncome = f;
    }
    public void setMaxIncome(float f){
        maxIncome = f;
    }
    public void setImageString(String s){
        imageString = s;
    }
    public void setSkillsList(ArrayList<SkillModel> s){
        skillsList = s;
    }
    //getters
    public String getProjectName(){
        return projectName;
    }
    public String getDescription(){
        return description;
    }
    public float getMinIncome(){
        return minIncome;
    }
    public float getMaxIncome(){ return maxIncome;}
    public String getImageString(){
        return imageString;
    }
    public ArrayList<SkillModel> skillModels(){
        return skillsList;
    }
}
