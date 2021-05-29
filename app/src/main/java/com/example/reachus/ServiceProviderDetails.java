package com.example.reachus;
//this class stores these information to the Database
public class
ServiceProviderDetails {
    public String s_number,s_age,s_gender,s_name,s_about,s_service;
    public ServiceProviderDetails() {
    }

    public ServiceProviderDetails(String s_number, String s_age, String s_gender, String s_name, String s_about,String s_service) {
        this.s_number = s_number;
        this.s_about = s_about;
        this.s_name = s_name;
        this.s_gender = s_gender;
        this.s_age = s_age;
        this.s_service= s_service;
    }
}
