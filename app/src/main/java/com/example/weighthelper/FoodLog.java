package com.example.weighthelper;


public class FoodLog { //entry for a food log
    private String food;
    private String cal;

    public FoodLog(String food) {
        this.food = food;
    }

    public String getFood() {
        return food;
    }

    public String getCal() {
        return cal;
    }

    public void setCal(String cal) {
        this.cal = cal;
    }
}
