package com.example.rest_api_tutorial.service.volley;

import com.example.rest_api_tutorial.model.WeatherReportModel;

import java.util.List;

public interface VolleyReportListResponseListener {
    void onError(String data);
    void onResponse(List<WeatherReportModel> weatherReportList);
}
