package com.kwame.dietitian.model;

public class PlansModel {

    private String id;
    private int image;
    private String plan;
    private int numberOfDays;

    public PlansModel(String id, int image, String plan, int numberOfDays) {
        this.id = id;
        this.image = image;
        this.plan = plan;
        this.numberOfDays = numberOfDays;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public int getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(int numberOfDays) {
        this.numberOfDays = numberOfDays;
    }
}
