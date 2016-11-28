package com.example.eric.restless;

import android.graphics.Bitmap;

/**
 * Created by minh on 11/26/16.
 */

public class User {
    private int id;
    private Bitmap image=null;
    private String name;
    private static User u= new User();
    //accessor method
    public static User getUser(){
        return u;
    }
    //private constructor
    private User(){}
    public Bitmap getImage(){return image;}
    public void setImage(Bitmap b){image=b;}
    public String getName(){return name;}
    public void setName(String s){name = s;}
    public int getId(){
        return id;
    }
    public void setId(int i){
        id = i;
    }


}
