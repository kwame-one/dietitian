package com.kwame.dietitian.model;

public class DietPlanModel {
    private int number;
    private String day;

    public DietPlanModel(int number, String day) {
        this.number = number;
        this.day = day;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
