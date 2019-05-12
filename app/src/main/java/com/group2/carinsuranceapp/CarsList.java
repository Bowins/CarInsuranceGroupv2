package com.group2.carinsuranceapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.group2.databaseclasses.UserCar;

import java.util.List;

public class CarsList extends ArrayAdapter<UserCar> {

    private static final String TAG = "AddToDatabase";
    private Activity context;
    private List<UserCar> carlist;

    public CarsList(Activity context, List<UserCar> carlist) {
        super(context, R.layout.list_layout, carlist);
        this.context = context;
        this.carlist = carlist;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);


        TextView textViewRegistration = (TextView) listViewItem.findViewById(R.id.listHeaderReference);
        TextView textViewModel = (TextView) listViewItem.findViewById(R.id.sublistModel);
        TextView textViewMake = (TextView) listViewItem.findViewById(R.id.sublistMake);

        UserCar car = carlist.get(position);

        Log.e(TAG, "Inside adapter class: " + car.getRegistration());

        textViewRegistration.setText(car.getRegistration());
        textViewModel.setText(car.getModel());
        textViewMake.setText(car.getMake());

        return listViewItem;
    }
}
