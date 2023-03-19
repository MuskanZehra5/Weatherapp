package com.example.weatherapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    Context context;
    ArrayList<Weathermodel> weathermodelArrayList;

    public WeatherAdapter(Context context, ArrayList<Weathermodel> weathermodelArrayList) {
        this.context = context;
        this.weathermodelArrayList = weathermodelArrayList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherAdapter.ViewHolder holder, int position) {
        Weathermodel wm = weathermodelArrayList.get(position);

        holder.deftemp.setText(wm.temp +"°C");
        holder.windspeed.setText(wm.windspeed + "Km/h");
        Picasso.get().load("http".concat(wm.getIcon())).into(holder.img);
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");

        try{
            Date t = input.parse(wm.getTime());
            holder.time.setText(output.format(t));

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return weathermodelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView time,deftemp,windspeed;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            deftemp = itemView.findViewById(R.id.defaulttemp);
            windspeed = itemView.findViewById(R.id.windspeed);
            img = itemView.findViewById(R.id.tempcond);
        }
    }
}
