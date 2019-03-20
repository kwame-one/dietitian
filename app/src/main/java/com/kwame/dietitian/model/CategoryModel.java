package com.kwame.dietitian.model;

public class CategoryModel {
    private int icon;
    private String text;
    private boolean isChecked;

    public CategoryModel(int icon, String text, boolean isChecked) {
        this.icon = icon;
        this.text = text;
        this.isChecked = isChecked;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
