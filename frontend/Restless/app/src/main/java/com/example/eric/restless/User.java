package com.example.eric.restless;

/**
 * Created by minh on 11/26/16.
 */

public class User {
    private int id;

    private static User u= new User();

    //accessor method
    public static User getUser(){
        return u;
    }
    //private constructor
    private User(){}

    public int getId(){
        return id;
    }
    public void setId(int i){
        id = i;
    }


}
