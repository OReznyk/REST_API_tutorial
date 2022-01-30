package com.example.rest_api_tutorial.service;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rest_api_tutorial.model.WeatherReportModel;
import com.example.rest_api_tutorial.service.volley.VolleyReportListResponseListener;
import com.example.rest_api_tutorial.service.volley.VolleySingletonRequest;
import com.example.rest_api_tutorial.service.volley.VolleyStringResponseListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WeatherDataService {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_REQUEST_BY_ID = "https://www.metaweather.com/api/location/";

    private Context context;

    public WeatherDataService(Context context) {
        this.context = context;
    }

    public void getCityID(String cityName, VolleyStringResponseListener volleyResponseListener){
        String url = QUERY_FOR_CITY_ID + cityName;
        // Request a string response from the provided URL.
        //ToD0: change to JSON
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            JSONObject data = response.getJSONObject(0);
                            String cityID = data.getString("woeid");
                            volleyResponseListener.onResponse(cityID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = createErrorMessage(volleyError);
                volleyResponseListener.onError(message);
            }
        });

        // Add the request to the RequestQueue.
        VolleySingletonRequest.getInstance(context).addToRequestQueue(request);
    }

    public void getReportByID(String cityID, VolleyReportListResponseListener volleyReportListResponseListener){
        List<WeatherReportModel> report = new ArrayList<>();

        String url = QUERY_FOR_REQUEST_BY_ID + cityID;
        // get JSON object
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,new Response.Listener<JSONObject>(){
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray json_report_list = response.getJSONArray("consolidated_weather");
                    for (int i = 0; i < json_report_list.length(); i++) {

                        JSONObject day_report = (JSONObject) json_report_list.get(i);
                        WeatherReportModel weatherReportModel = new WeatherReportModel();
                        weatherReportModel.setId(day_report.getInt("id"));
                        weatherReportModel.setWeather_state_name(day_report.getString("weather_state_name"));
                        weatherReportModel.setWeather_state_abbr(day_report.getString("weather_state_abbr"));
                        weatherReportModel.setWind_direction_compass(day_report.getString("wind_direction_compass"));
                        weatherReportModel.setCreated(day_report.getString("created"));
                        weatherReportModel.setApplicable_date(day_report.getString("applicable_date"));
                        weatherReportModel.setMin_temp(day_report.getLong("min_temp"));
                        weatherReportModel.setMax_temp(day_report.getLong("max_temp"));
                        weatherReportModel.setThe_temp(day_report.getLong("the_temp"));
                        weatherReportModel.setWind_speed(day_report.getLong("wind_speed"));
                        weatherReportModel.setWind_direction(day_report.getLong("wind_direction"));
                        weatherReportModel.setAir_pressure(day_report.getLong("air_pressure"));
                        weatherReportModel.setHumidity(day_report.getInt("humidity"));
                        weatherReportModel.setVisibility(day_report.getLong("visibility"));
                        weatherReportModel.setPredictability(day_report.getInt("predictability"));
                        report.add(weatherReportModel);
                    }

                    volleyReportListResponseListener.onResponse(report);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = createErrorMessage(volleyError);
                volleyReportListResponseListener.onError(message);
            }
        });
        VolleySingletonRequest.getInstance(context).addToRequestQueue(request);
    }

    public void getReportByName(String cityName, VolleyReportListResponseListener volleyReportListResponseListener){
        getCityID(cityName, new VolleyStringResponseListener() {
            @Override
            public void onError(String data) {

            }

            @Override
            public void onResponse(String data) {
                getReportByID(data, volleyReportListResponseListener);
            }
        });
    }

    private String createErrorMessage(VolleyError volleyError){
        String message = null;
        if (volleyError instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection! " + volleyError;
        } else if (volleyError instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!! " + volleyError;
        } else if (volleyError instanceof AuthFailureError) {
            message = "Cannot connect to Internet...Please check your connection! " + volleyError;
        } else if (volleyError instanceof ParseError) {
            message = "Parsing error! Please try again after some time!! " + volleyError;
        } else if (volleyError instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection. " + volleyError;
        }
        return  message;
    }

}
