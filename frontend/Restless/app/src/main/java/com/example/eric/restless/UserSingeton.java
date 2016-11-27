package com.example.eric.restless;

/**
 * Created by minh on 11/26/16.
 */

public class UserSingeton {
    private int id;
    private static UserSingeton u = new UserSingeton();

    //accessor method
    public static UserSingeton getUser(){
        return u;
    }
    //private constructor
    private UserSingeton(){}

    public int getId(){
        return id;
    }
    public void setId(int i){
        id = i;
    }


}
