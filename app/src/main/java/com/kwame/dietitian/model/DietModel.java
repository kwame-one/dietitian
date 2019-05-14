package com.kwame.dietitian.model;

public class DietModel {
    private String time;
    private String diet;

    public DietModel(String time, String diet) {
        this.time = time;
        this.diet = diet;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }
}
