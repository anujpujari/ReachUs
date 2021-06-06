package com.example.reachus;

public class userBookingAttributes {

    String BookingDate;
    String BookingTime;
    String bookingId;
    String provideruserId;
    String Name;
    String StoreName;
    String mainJob;

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

    public userBookingAttributes(){}

    public userBookingAttributes(String BookingDate,String BookingTime,String bookingId,String provideruserId,String Name,String StoreName, String mainJob,String secondaryJob){
        this.BookingDate=BookingDate;
        this.BookingTime=BookingTime;
        this.bookingId=bookingId;
        this.provideruserId=provideruserId;
        this.Name=Name;
        this.StoreName=StoreName;
        this.mainJob=mainJob;
        this.secondaryJob=secondaryJob;
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

    public String getProvideruserId() {
        return provideruserId;
    }

    public void setProvideruserId(String provideruserId) {
        this.provideruserId = provideruserId;
    }


}
