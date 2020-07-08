package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Sys extends MainActivity{
    String country;
    Integer sunrise, sunset;

    public Sys(){}

    public Sys(JSONObject tmpSys) throws JSONException {
        this.country = tmpSys.getString("country");
        this.sunrise = tmpSys.getInt("sunrise");
        this.sunset = tmpSys.getInt("sunset");
    }

    public String getCountry() { return this.country; }
    public Integer getSunrise() { return this.sunrise; }
    public Integer getSunset() { return this.sunset; }

    public void setSunrise(Integer sunrise){ this.sunrise = sunrise; }
    public void setSunset(Integer sunset){ this.sunset = sunset; }
    public void setCountry(String country){ this.country = country; }
}
