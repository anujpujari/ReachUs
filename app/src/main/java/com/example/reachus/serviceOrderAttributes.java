package com.example.reachus;

public class serviceOrderAttributes {

    String BookingDate;
    String BookingTime;
    String bookingId;
    String providerUserId;
    String Name;
    String StoreName;
    String BookingUserId;
    String mainJob;

    public String getBookingUserId() {
        return BookingUserId;
    }

    public void setBookingUserId(String bookingUserId) {
        BookingUserId = bookingUserId;
    }
    public String getStoreName() {
        return StoreName;
    }

    public void setStoreName(String storeName) {
        StoreName = storeName;
    }

    public String getMainJob() {
        return mainJob;
    }

    public void setMainJob(String mainJob) {
        this.mainJob = mainJob;
    }

    public String getSecondaryJob() {
        return secondaryJob;
    }

    public void setSecondaryJob(String secondaryJob) {
        this.secondaryJob = secondaryJob;
    }

    String secondaryJob;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public serviceOrderAttributes(){}

    public serviceOrderAttributes(String BookingUserId,String BookingDate,String BookingTime,String bookingId,String providerUserId,String Name,String StoreName, String mainJob,String secondaryJob){
        this.BookingDate=BookingDate;
        this.BookingTime=BookingTime;
        this.bookingId=bookingId;
        this.providerUserId=providerUserId;
        this.Name=Name;
        this.StoreName=StoreName;
        this.mainJob=mainJob;
        this.secondaryJob=secondaryJob;
        this.BookingUserId=BookingUserId;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookingTime() {
        return BookingTime;
    }

    public void setBookingTime(String bookingTime) {
        BookingTime = bookingTime;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }

}
