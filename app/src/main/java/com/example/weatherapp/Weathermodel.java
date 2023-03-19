package com.example.weatherapp;

public class Weathermodel {
    String time,temp,windspeed,icon;

    public Weathermodel(String time, String temp, String windspeed, String icon) {
        this.time = time;
        this.temp = temp;
        this.windspeed = windspeed;
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
