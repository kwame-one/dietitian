package com.kwame.dietitian.model;

public class PlansModel {

    private String nodeId;
    private String id;
    private int image;
    private String plan;
    private int numberOfDays;
    private String imageUrl;


    public PlansModel(String id, int image, String plan, int numberOfDays) {
        this.id = id;
        this.image = image;
        this.plan = plan;
        this.numberOfDays = numberOfDays;
    }

    public PlansModel(String nodeId, String id, int image, String plan, int numberOfDays) {
        this.nodeId = nodeId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
}
