package com.example.eric.restless;

/**
 * Created by minh on 11/25/16.
 */

public class projectPreview {
    private String projectName;
    //project icon?
    //private String image;

    public projectPreview(String s){
        projectName = s;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectName(){
        return projectName;
    }
    /*
    public void setImage(String s){
        image = s;
    }
    public String getImage() {
        return image;
    }
    */
}
