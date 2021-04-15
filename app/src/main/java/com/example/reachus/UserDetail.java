package com.example.reachus;
//this class stores these information to the Database
public class UserDetail {
    public String Name,Password,Phone_number;
    public UserDetail() {
    }

    public UserDetail(String Name, String Password, String Phone_number) {
        this.Name = Name;
        this.Password = Password;
        this.Phone_number = Phone_number;
    }
}
