package com.example.rest_api_tutorial.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.rest_api_tutorial.R;
import com.example.rest_api_tutorial.model.WeatherReportModel;

import java.util.List;

public class WeatherReportAdapter extends RecyclerView.Adapter<WeatherReportAdapter.ViewHolder> {
    List<WeatherReportModel> weatherReportList;
    Context context;

    public WeatherReportAdapter(List<WeatherReportModel> weatherReportList) {
        this.weatherReportList = weatherReportList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView weather_status;
        TextView tv_date, tv_min_temp, tv_max_temp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO: get view id`s
            weather_status = itemView.findViewById(R.id.iv_state);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_max_temp = itemView.findViewById(R.id.tv_max_temp);
            tv_min_temp = itemView.findViewById(R.id.tv_min_temp);

        }
    }

    @NonNull
    @Override
    // Usually involves inflating a layout from XML and returning the holder
    public WeatherReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View reportView = inflater.inflate(R.layout.data_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(reportView);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    // Involves populating data into the item through holder
    public void onBindViewHolder(@NonNull WeatherReportAdapter.ViewHolder holder, int position) {
        WeatherReportModel dayReport = weatherReportList.get(position);
        // ToDo: set image
        String url = "https://www.metaweather.com/static/img/weather/png/64/" + dayReport.getWeather_state_abbr() + ".png";
        Glide.with(context).load(url).into(holder.weather_status);
        holder.tv_date.setText(dayReport.getApplicable_date());
        holder.tv_min_temp.setText("Minimum: " + dayReport.getMin_temp());
        holder.tv_max_temp.setText("Maximum: " + dayReport.getMax_temp());

    }

    @Override
    public int getItemCount() {
        return weatherReportList.size();
    }


}
