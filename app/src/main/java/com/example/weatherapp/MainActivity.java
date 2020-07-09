package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import android.widget.Toast;
import android.widget.TextView;

/* Not sure if these libraries will ever be required */
import android.widget.ImageView;
import java.lang.reflect.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.StringTokenizer;
import android.content.Intent;


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String OPENWEATHER_API_URL = "https://api.openweathermap.org/data/2.5/weather?";
    private static final String TEST_URL = "https://api.openweathermap.org/data/2.5/weather?q=London,UK&APPID=eed349e14a8ea71c5f68b00d1e2827ee&units=imperial";
    private static final String API_KEY = "&APPID=eed349e14a8ea71c5f68b00d1e2827ee";
    private static final String METRIC_UNITS = "&units=metric";
    private static final String IMPERIAL_UNITS = "&units=imperial";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button updateButton = findViewById(R.id.newZipButton);
        Button refreshButton = findViewById(R.id.refreshButton);

        /* Initializing data class to store data once it's received */
        final WeatherMain data = new WeatherMain();

        /* Preloads the weather for London until the user updates the location */
        try { openWeatherRequest(TEST_URL, data); }
        catch (JSONException e) { e.printStackTrace(); }

        /* Listen for if the user clicks the updateZipButton, then start update */
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Get zip code from editText and pass to openWeatherRequest to parse and update UI */
                EditText editText = (EditText) findViewById(R.id.zipEnter);
                String userZip = editText.getText().toString();
                data.setZip(userZip);
                try { openWeatherRequest(createURLFromZip(userZip), data); }
                catch (JSONException e) { e.printStackTrace(); }

                editText.setText("");
                /* Minimize keyboard */
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });

        /* Listen for the refresh button */
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Checking to see if there is a zipcode already stored to use in the HTTP request */
                if (data.getZip() == null) {
                    String tmpURL = createURLFromCityName(data.getLocName(), data.getCountry());
                    try { openWeatherRequest(tmpURL, data); }
                    catch (JSONException e) { e.printStackTrace(); }
                }
                else{
                    try { openWeatherRequest(createURLFromZip(data.getZip()), data); }
                    catch (JSONException e) { e.printStackTrace(); }
                }
            }
        });
    }

    public String createURLFromZip(String zip){
        String tmp = OPENWEATHER_API_URL.concat("zip=" + zip + API_KEY + IMPERIAL_UNITS);
        return tmp;
    }

    public String createURLFromCityName(String name, String country){
        String tmpURL = OPENWEATHER_API_URL + "q=" + name + "," + country + API_KEY + IMPERIAL_UNITS;
        return tmpURL;
    }

    /* Called to handle HTTP request to OpenWeather API */
    public void openWeatherRequest(String URL, final WeatherMain data) throws JSONException{
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                                JSONObject resp = new JSONObject(response);
                                updateUI(jsonParse(resp, data));
                        }
                        catch (Exception e) { e.printStackTrace(); }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    /* Called in openWeatherRequest() to parse the JSON response */
    public WeatherMain jsonParse(JSONObject tmpObj, WeatherMain data) throws JSONException {
        /* Displays the raw response in a text view

        tmpTxtView.setText(tmpObj.toString()); */

        /* Temporary JSON objects to split the response into smaller objects */
        JSONObject tmpWind = tmpObj.getJSONObject("wind");
        JSONObject tmpMain = tmpObj.getJSONObject("main");
        JSONObject tmpSys = tmpObj.getJSONObject("sys");
        JSONArray tmpWeather = tmpObj.getJSONArray("weather");
        JSONObject tmpCoord = tmpObj.getJSONObject("coord");

        /* Taking the temporary objects and extracting the actual data */
        data.setMain(tmpWeather.getJSONObject(0).getString("main"));
        data.setId(tmpWeather.getJSONObject(0).getInt("id"));
        data.setDescription(tmpWeather.getJSONObject(0).getString("description"));

        data.setLocName(tmpObj.getString("name"));

        data.setWindSpd(tmpWind.getDouble("speed"));
        data.setWindDeg(tmpWind.getDouble("speed"));

        String tmpTemp = String.format("%.0f",tmpMain.getDouble("temp"));
        data.setTemp(tmpTemp); // Cast Temp to an int to remove decimal places
        data.setTempMax(tmpMain.getDouble("temp_max"));
        data.setTempMin(tmpMain.getDouble("temp_min"));
        data.setHumidity(tmpMain.getDouble("humidity"));

        data.setCountry(tmpSys.getString("country"));
        data.setSunrise(tmpSys.getString("sunrise"));
        data.setSunset(tmpSys.getString("sunset"));

        data.setLon((float) tmpCoord.getLong("lon"));
        data.setLat((float) tmpCoord.getLong("lat"));

        /* Update time codes from unix to local time zones */
        data.setSunrise(formatTime(data.getSunrise()));
        data.setSunset(formatTime(data.getSunset()));

        return data; // Return the full data class
    }

    private void updateUI(WeatherMain data){
        TextView tmpName = (TextView) findViewById(R.id.currentLocationName);
        TextView tmpTemp = (TextView) findViewById(R.id.currentTempText);
        TextView tmpCond = (TextView) findViewById(R.id.currentWeatherText);
        TextView tmpSunset = (TextView) findViewById(R.id.sunsetValue);
        TextView tmpSunrise = (TextView) findViewById(R.id.sunriseValue);
        TextView tmpHumidity = (TextView) findViewById(R.id.humidityValue);
        TextView tmpDesc = (TextView) findViewById(R.id.currentWeatherDesc);
        TextView tmpLong = (TextView) findViewById(R.id.longValue);
        TextView tmpLat = (TextView) findViewById(R.id.latValue);
        TextView tmpWindSpd = (TextView) findViewById(R.id.windSpeedValue);
        TextView tempUnits = (TextView) findViewById(R.id.tempUnitsLabel);

        TextView tmpText = (TextView) findViewById(R.id.jsonTestView);
        char tmpChar = data.getId().toString().charAt(0);
        String tmpStr = data.getId() + " : " + tmpChar + " : " + data.getDescription() + " : ";

        tmpName.setText(data.getLocName());
        tmpTemp.setText(data.getTemp());
        tmpCond.setText(data.getMain());
        tmpHumidity.setText(Double.toString(data.getHumidity()));
        tmpSunrise.setText(data.getSunrise());
        tmpSunset.setText(data.getSunset());
        tmpDesc.setText(data.getDescription());
        tmpLong.setText(data.getLon().toString());
        tmpLat.setText(data.getLat().toString());
        tmpWindSpd.setText(data.getWindSpd().toString());
        tempUnits.setText("F");

        /* TODO: Figure out why image isn't showing */
        /* Use Picasso image loading library to fetch and load icons from HTTP */
        ImageView tmpIcon = (ImageView) findViewById(R.id.currentWeatherIcon);
        String iconURL = generateIconURL(data.getId().toString());
        Picasso.get().load(iconURL).into(tmpIcon);
        //tmpText.setText(tmpStr + iconURL);
    }

    private String generateIconURL(String desCode){
        String ICON_URL_PRE = "http://openweathermap.org/img/wn/";
        String ICON_URL_SUFF = "@2x.png";
        String tmpStr;

        /* Extract first two characters of description code */
        char firstChar = desCode.charAt(0);
        char secChar = desCode.charAt(1);

        switch (firstChar) {
            case '2':
                tmpStr = ICON_URL_PRE + "11d" + ICON_URL_SUFF;
                break;
            case '3':
                tmpStr = ICON_URL_PRE + "09d" + ICON_URL_SUFF;
                break;
            case '5':
                switch (secChar){
                    case '1':
                        tmpStr = ICON_URL_PRE + "13d" + ICON_URL_SUFF;
                        break;
                    case '2':
                        tmpStr = ICON_URL_PRE + "09d" + ICON_URL_SUFF;
                        break;
                    default:
                        tmpStr = ICON_URL_PRE + "10d" + ICON_URL_SUFF;
                        break;
                }
                break;
            case '6':
                tmpStr = ICON_URL_PRE + "13d" + ICON_URL_SUFF;
                break;
            case '7':
                tmpStr = ICON_URL_PRE + "50d" + ICON_URL_SUFF;
                break;
            case '8':
                if (desCode.equals("800")){
                    tmpStr = ICON_URL_PRE + "01d" + ICON_URL_SUFF;
                    break;
                }
                else if (desCode.equals("801")){
                    tmpStr = ICON_URL_PRE + "02d" + ICON_URL_SUFF;
                    break;
                }
                else if (desCode.equals("802")){
                    tmpStr = ICON_URL_PRE + "03d" + ICON_URL_SUFF;
                    break;
                }
                else if (desCode.equals("803")){
                    tmpStr = ICON_URL_PRE + "04d" + ICON_URL_SUFF;
                    break;
                }
                else if (desCode.equals("804")){
                    tmpStr = ICON_URL_PRE + "04d" + ICON_URL_SUFF;
                    break;
                }
            default:
                tmpStr = ICON_URL_PRE + "01d" + ICON_URL_SUFF;
                break;
        }
        return tmpStr;
    }

    private String formatTime(String time){
        Timestamp tmpTime = new Timestamp(Long.valueOf(time) * 1000);
        String[] seperatedTime1 = tmpTime.toString().split(" ");
        String[] seperatedTime2 = seperatedTime1[1].split("\\.");
        StringTokenizer items = new StringTokenizer(seperatedTime2[0], ":");
        return items.nextToken()  + ":" + items.nextToken();
    }
}








