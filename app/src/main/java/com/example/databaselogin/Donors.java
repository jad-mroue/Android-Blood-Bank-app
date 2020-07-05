package com.example.databaselogin;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Donors {

    @SerializedName("username")
    @Expose
    private String name;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("phone_number")
    @Expose
    private int number;
    @SerializedName("blood_type")
    @Expose
    private String bldgroup;

    public void  setBld(String b){
        b=bldgroup;
    }

    public  String getBld(){
        return  bldgroup;
    }

    public void  setCity(String c){
        c=city;
    }

    public  String getCity(){
        return  city;
    }

    public void setNumber(int n){
        n=number;
    }

    public String getName(){
        return name;
    }

    public void setName(String n){
        n=name;
    }

    public int getNumber(){
        return number;
    }

}