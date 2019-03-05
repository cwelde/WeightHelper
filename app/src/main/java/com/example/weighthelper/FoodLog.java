package com.example.weighthelper;


import java.io.Serializable;

public class FoodLog implements Serializable { //entry for a food log
    private String food;
    private String cal;
    private String carb;
    private String protein;
    private String fat;

    public FoodLog(String food) {
        this.food = food;
    }

    public String getFood() {
        return food;
    }

    public String getCal() {
        return cal;
    }

    public String getCarb() { return carb; }

    public String getProtein() { return protein; }

    public String getFat() { return fat; }

    public void setCal(String cal) {
        this.cal = cal;
    }

    public void setCarb(String carb) { this.carb = carb; }

    public void setProtein(String protein) { this.protein = protein; }

    public void setFat(String fat) { this.fat = fat; }
}
