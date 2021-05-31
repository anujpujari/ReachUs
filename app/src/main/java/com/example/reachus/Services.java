package com.example.reachus;

public class Services {

    private String mainJob;private String secondaryJob;
    private String Price;
    private String Description;

    public Services(){}

    public Services(String mainJob, String secondaryJob,String Price,String Description){
        this.mainJob=mainJob;
        this.secondaryJob=secondaryJob;
        this.Price=Price;
        this.Description=Description;
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
