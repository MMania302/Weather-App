package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Coord extends MainActivity {
    float lon, lat;

    public Coord(){}

    public Coord(JSONObject tmpObj) throws JSONException {
        this.lon = tmpObj.getLong("lon");
        this.lat = tmpObj.getLong("lat");
    }

    public Float getLong(){
        return lon;
    }
    public Float getLat(){
        return lat;
    }

    public void setLon(Float lon){ this.lon = lon; }
    public void setLat(Float lat){ this.lat = lat; }
}
