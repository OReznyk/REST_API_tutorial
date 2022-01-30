package com.example.rest_api_tutorial.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rest_api_tutorial.R;
import com.example.rest_api_tutorial.service.WeatherDataService;
import com.example.rest_api_tutorial.adapter.WeatherReportAdapter;
import com.example.rest_api_tutorial.model.WeatherReportModel;
import com.example.rest_api_tutorial.service.volley.VolleyReportListResponseListener;
import com.example.rest_api_tutorial.service.volley.VolleyStringResponseListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    Button btn_id, btn_get_by_id, btn_get_by_name;
    EditText et_input;
    RecyclerView rv_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get id`s
        btn_id = findViewById(R.id.btn_get_id_by_country_name);
        btn_get_by_id = findViewById(R.id.btn_get_report_by_id);
        btn_get_by_name = findViewById(R.id.btn_get_report_by_name);
        et_input = findViewById(R.id.et_id_or_name);
        rv_report = findViewById(R.id.rv_data);

    }

    public void doOnClick(View view) {
        String input = et_input.getText().toString();
        WeatherDataService weatherDataService = new WeatherDataService(MainActivity.this);
        // TODO: change URL
        switch (view.getId()) {
            case R.id.btn_get_id_by_country_name:
                // TODO: get id by city name
                weatherDataService.getCityID(input, new VolleyStringResponseListener() {
                    @Override
                    public void onError(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                    }
                });
                break;
            case R.id.btn_get_report_by_id:
                weatherDataService.getReportByID(input, new VolleyReportListResponseListener() {
                    @Override
                    public void onError(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportList) {
                        // TODO: for each loop -> set view
                        updateRecyclerView(weatherReportList);
                    }
                });
                break;
            case R.id.btn_get_report_by_name:
                weatherDataService.getReportByName(input, new VolleyReportListResponseListener() {
                    @Override
                    public void onError(String data) {
                        Toast.makeText(MainActivity.this, data, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(List<WeatherReportModel> weatherReportList) {
                        updateRecyclerView(weatherReportList);
                    }
                });
                break;
            default:
                Toast.makeText(MainActivity.this, "No btn pressed", Toast.LENGTH_LONG).show();
        }

        hideSoftKeyboard(MainActivity.this);
    }

    private void updateRecyclerView(List<WeatherReportModel> weatherReportList){
        WeatherReportAdapter adapter = new WeatherReportAdapter(weatherReportList);
        rv_report.setAdapter(adapter);
        rv_report.setLayoutManager(new LinearLayoutManager(this));
        Toast.makeText(MainActivity.this, "updated", Toast.LENGTH_LONG).show();
    }
    private static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }
}