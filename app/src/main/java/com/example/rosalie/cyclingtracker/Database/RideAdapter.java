package com.example.rosalie.cyclingtracker.Database;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rosalie.cyclingtracker.R;

import java.util.ArrayList;

/**
 * Created by infer on 24/11/2017.
 */

public class RideAdapter extends ArrayAdapter<Ride> {

    public RideAdapter(Activity context, ArrayList<Ride> rides) {
        super(context, 0, rides);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
        }
        Ride currentRide = getItem(position);

        TextView idride = (TextView) listItemView.findViewById(R.id.idride);
        idride.setText(String.valueOf(currentRide.getId()));

        TextView date = (TextView) listItemView.findViewById(R.id.dateride);
        date.setText(currentRide.getDate());

        TextView time = (TextView) listItemView.findViewById(R.id.timeride);
        time.setText(String.valueOf(currentRide.getTime()));

        TextView average_speed = (TextView) listItemView.findViewById(R.id.averagespeed);
        average_speed.setText(String.valueOf( currentRide.getAverage_speed()));

        TextView distance = (TextView) listItemView.findViewById(R.id.distanceride);
        distance.setText(String.valueOf( currentRide.getDistance()));

        return listItemView;
    }
}
