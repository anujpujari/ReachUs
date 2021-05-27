package com.example.reachus;
//this class stores these information to the Database
public class
UserDetail {
    public String Name,Password,Phone_number,s_email;
    public UserDetail() {
    }

    public UserDetail(String Name,String s_email, String Password, String Phone_number) {
        this.Name = Name;
        this.s_email=s_email;
        this.Password = Password;
        this.Phone_number = Phone_number;
    }
}
