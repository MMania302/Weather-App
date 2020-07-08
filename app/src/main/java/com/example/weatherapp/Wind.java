package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Wind{
    Double speed, deg;

    public Wind(){}

    public Wind(JSONObject tmpWind) throws JSONException {
        this.speed = tmpWind.getDouble("speed");
        this.deg = tmpWind.getDouble("deg");
    }

    public Double getSpeed(){
        return this.speed;
    }
    public Double getDeg(){
        return this.deg;
    }
    public void setWindSpd(Double windSpd){ this.speed = windSpd; }
    public void setWindDeg(Double windDeg){ this.deg = windDeg; }
}
