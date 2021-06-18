package com.example.reachus;

public class Services {

    private String mainJob;private String secondaryJob;
    private String Price;
    private String Description;
    private String userID;
    private String pincode;
    private String LegalName;
    private String Address_1;
    private String StoreName;
    private String Address_2;
    private String City;
    private String District;
    private String Phone;
    private String Email;


    public Services(){}

    public Services(String mainJob, String secondaryJob,String Price,String Description,String StoreName, String userID,String pincode, String LegalName, String Address_1, String Address_2, String City, String District,boolean isIdVerified,String Phone,String Email){
        this.mainJob=mainJob;
        this.secondaryJob=secondaryJob;
        this.Price=Price;
        this.Description=Description;
        this.StoreName=StoreName;
        this.userID=userID;
        this.pincode=pincode;
        this.LegalName=LegalName;
        this.Address_1=Address_1;
        this.Address_2=Address_2;
        this.City=City;
        this.District=District;
        this.isIdVerified=isIdVerified;
        this.Phone=Phone;
        this.Email=Email;
    }
    public boolean getisIdVerified() {
        return isIdVerified;
    }

    public void setisIdVerified(boolean isIdVerified) {
        this.isIdVerified = isIdVerified;
    }

    private boolean isIdVerified;
    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLegalName() {
        return LegalName;
    }

    public void setLegalName(String legalName) {
        LegalName = legalName;
    }

    public String getAddress_1() {
        return Address_1;
    }

    public void setAddress_1(String address_1) {
        Address_1 = address_1;
    }

    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getAddress_2() {
        return Address_2;
    }

    public void setAddress_2(String address_2) {
        Address_2 = address_2;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getSecondaryJob() {
        return secondaryJob;
    }

    public void setSecondaryJob(String secondaryJob) {
        this.secondaryJob = secondaryJob;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
    public String getMainJob() {
        return mainJob;
    }

    public void setMainJob(String mainJob) {
        this.mainJob = mainJob;
    }
}
