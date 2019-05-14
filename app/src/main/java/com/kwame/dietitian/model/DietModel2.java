package com.kwame.dietitian.model;

public class DietModel2 {

    private String image;
    private String title;
    private String day;
    private String content;

    public DietModel2(String image, String title, String day, String content) {
        this.image = image;
        this.title = title;
        this.day = day;
        this.content = content;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
