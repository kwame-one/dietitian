package com.kwame.dietitian.model;

public class DietitianModel {
    private String imageUrl;
    private String name;
    private String company;
    private String contact;
    private String address;
    private String website;

    public DietitianModel(String imageUrl, String name, String company, String contact, String address, String website) {
        this.imageUrl = imageUrl;
        this.name = name;
        this.company = company;
        this.contact = contact;
        this.address = address;
        this.website = website;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
