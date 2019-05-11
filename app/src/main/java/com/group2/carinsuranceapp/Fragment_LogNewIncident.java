package com.group2.carinsuranceapp;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Fragment_LogNewIncident extends Fragment implements OnMapReadyCallback {

    private Button submitButton;
    private EditText incidentDateField;
    private EditText incidentDescriptionField;
    private EditText incidentTimeField;
    private EditText incidentLocationEditText;
    private TextView incidentLocationAsCurrentLocation;
    private MapView mapView;
    private GoogleMap map;
    private Switch aSwitch;
    private LoggedInMainActivity loggedInMainActivity;
    private Location currentLocation;
    private LatLng currentLocationLatLang;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_new_incident,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        submitButton = view.findViewById(R.id.b_submit);
        incidentDateField = view.findViewById(R.id.field_incident_date);
        incidentDescriptionField = view.findViewById(R.id.field_incident_description);
        incidentTimeField= view.findViewById(R.id.field_incident_time);
        incidentLocationAsCurrentLocation = view.findViewById(R.id.text_display_current_location);
        incidentLocationEditText = view.findViewById(R.id.field_input_current_position);
        aSwitch = view.findViewById(R.id.switch_new_incident);
        mapView = view.findViewById(R.id.mapView_logIncident);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }


        //location
            //set initial view
                aSwitch.setChecked(true);
                mapView.setVisibility(View.VISIBLE);
                incidentLocationAsCurrentLocation.setVisibility(View.VISIBLE);
                incidentLocationEditText.setVisibility(View.INVISIBLE);
        aSwitch.setOnClickListener(switchListener);

        loggedInMainActivity = (LoggedInMainActivity) getActivity();
        currentLocation = loggedInMainActivity.lastKnownLocation;
        currentLocationLatLang = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());



    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.addMarker(new MarkerOptions().position(currentLocationLatLang).title("You are here"));
        map.moveCamera(CameraUpdateFactory.zoomTo(12.0f));
        map.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLang));
    }

    View.OnClickListener switchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(aSwitch.isChecked()){
                //show map and current location textView
                mapView.setVisibility(View.VISIBLE);
                incidentLocationAsCurrentLocation.setVisibility(View.VISIBLE);
                //hide location edit text for address
                incidentLocationEditText.setVisibility(View.INVISIBLE);
            }else{

                //hide map and current location textView
                mapView.setVisibility(View.INVISIBLE);
                incidentLocationAsCurrentLocation.setVisibility(View.INVISIBLE);
                //show location edit text for address
                incidentLocationEditText.setVisibility(View.VISIBLE);

            }
        }
    };
}
