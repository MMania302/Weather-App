package com.example.weatherapp;

import org.json.JSONArray;
import org.json.JSONException;

public class Weather extends MainActivity {
    Integer id;
    String main, description;

    public Weather(){}

    public Weather(JSONArray tmp) throws JSONException {
        this.id = tmp.getJSONObject(0).getInt("id");
        this.main = tmp.getJSONObject(0).getString("main");
        this.description = tmp.getJSONObject(0).getString("description");
    }

    public Integer getId(){
        return this.id;
    }
    public String getMain(){
        return this.main;
    }
    public String getDescription(){
        return this.description;
    }

    public void setId(Integer id){ this.id = id; }
    public void setMain(String main){ this.main = main; }
    public void setDescription(String description){ this.description = description; }
}
