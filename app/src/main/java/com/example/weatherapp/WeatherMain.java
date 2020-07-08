package com.example.weatherapp;

import org.json.JSONException;
import org.json.JSONObject;

public class WeatherMain extends MainActivity {
    Double  tempMax, tempMin, humidity, windSpd, windDeg;
    Integer id;
    String main, description, locName, country, sunrise, sunset, zip, temp;
    Float lon, lat;

    public WeatherMain(){}

    public WeatherMain(JSONObject tmpMain) throws JSONException {
        /* this.temp = tmpMain.getDouble("temp");
        this.tempMax = tmpMain.getDouble("temp_min");
        this.tempMin = tmpMain.getDouble("temp_max");
        this.humidity = tmpMain.getDouble("humidity"); */
    }

    /* setter functions to set the values */
    public void setTemp(String temp){ this.temp = temp; }
    public void setTempMax(Double tempMax){ this.tempMax = tempMax; }
    public void setTempMin(Double tempMin){ this.tempMin = tempMin; }
    public void setHumidity(Double humidity){ this.humidity = humidity; }
    public void setWindSpd(Double windSpd){ this.windSpd = windSpd; }
    public void setWindDeg(Double windDeg){ this.windDeg = windDeg; }

    public void setId(Integer id){ this.id = id; }
    public void setZip(String zip){ this.zip = zip; }

    public void setSunrise(String sunrise){ this.sunrise = sunrise; }
    public void setSunset(String sunset){ this.sunset = sunset; }
    public void setMain(String main){ this.main = main; }
    public void setDescription(String description){ this.description = description; }
    public void setLocName(String locName){ this.locName = locName; }
    public void setCountry(String country){ this.country = country; }

    public void setLon(Float lon){ this.lon = lon; }
    public void setLat(Float lat){ this.lat = lat; }

    /* getter functions to return the stored values */
    public String getTemp(){
        return this.temp;
    }
    public Double getTempMax(){
        return this.tempMax;
    }
    public Double getTempMin(){
        return this.tempMin;
    }
    public Double getHumidity(){
        return this.humidity;
    }
    public Double getWindSpd(){return this.windSpd; }
    public Double getWindDeg(){ return this.windDeg; }

    public Integer getId(){ return this.id; }
    public String getZip(){ return this.zip; }

    public String getSunrise(){ return this.sunrise; }
    public String getSunset(){ return this.sunset; }
    public String getMain(){ return this.main; }
    public String getDescription(){return this.description; }
    public String getLocName(){ return this.locName; }
    public String getCountry(){ return this.country; }

    public Float getLon(){ return this.lon; }
    public Float getLat(){ return this.lat; }
}