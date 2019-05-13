package com.group2.carinsuranceapp;

import android.Manifest;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.group2.databaseclasses.UserCar;
import com.group2.databaseclasses.UserIncident;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fragment_LogNewIncident extends Fragment implements OnMapReadyCallback {

    private Button submitButton;
    private EditText incidentDateField;
    private EditText incidentDescriptionField;
    private EditText incidentTimeField;
    private EditText incidentLocationAddress;
    private TextView incidentLocationAsCurrentLocation;
    private MapView mapView;
    private GoogleMap map;
    private Switch aSwitch;
    private LoggedInMainActivity loggedInMainActivity;
    private Location currentLocation;
    private LatLng currentLocationLatLang;
    private EditText incidentLocationPostCode;
    private EditText incidentLocationTownCity;
    private EditText incidentLocationCountry;
    private String currentAddress;
    private Button takePictureButton;
    private ListView carListView;
    private Button deletePicturesButton;
    private String manualAddress;
    private boolean addressFromMap = true;

    //taking photos fields
    private ImageView imView1;
    private ImageView imView2;
    private ImageView imView3;
    private ImageView imView4;

    /* ===========================================
    Global variables to be pushed into the database
     */
    String dateIn; //It's string but will be stored in x/x/x format because of the field so nw
    String timeIn;
    String descriptionIn;
    float latIn;
    float lonIn;


    //Database stuff
    DatabaseReference databaseCars;
    private FirebaseAuth mAuth;
    private String userID;
    FirebaseUser firebaseUser;
    private static final String TAG = "DEBUGGING - ";
    List<UserCar> listofcars;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_log_new_incident,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        submitButton = view.findViewById(R.id.b_submit);
        submitButton.setOnClickListener(submitListener);

        takePictureButton = view.findViewById(R.id.button_take_picture);
        takePictureButton.setOnClickListener(takePictureListener);

        deletePicturesButton = view.findViewById(R.id.button_delete_pictures);
        deletePicturesButton.setOnClickListener(deletePicturesListener);

        incidentDateField = view.findViewById(R.id.field_incident_date);
        incidentDescriptionField = view.findViewById(R.id.field_incident_description);
        incidentTimeField= view.findViewById(R.id.field_incident_time);

        incidentLocationAsCurrentLocation = view.findViewById(R.id.text_display_current_location);

        incidentLocationAddress = view.findViewById(R.id.field_input_current_position_address);
        incidentLocationCountry = view.findViewById(R.id.field_input_current_position_country);
        incidentLocationPostCode = view.findViewById(R.id.field_input_current_position_postcode);
        incidentLocationTownCity = view.findViewById(R.id.field_input_current_position_city);

        imView1 = view.findViewById(R.id.incident_picture_1);
        imView2 = view.findViewById(R.id.incident_picture_2);
        imView3 = view.findViewById(R.id.incident_picture_3);
        imView4 = view.findViewById(R.id.incident_picture_4);


        aSwitch = view.findViewById(R.id.switch_new_incident);
        aSwitch.setChecked(true);
        aSwitch.setOnClickListener(switchListener);

        loggedInMainActivity = (LoggedInMainActivity) getActivity();
        loggedInMainActivity.getLastLocation();

        carListView = view.findViewById(R.id.incident_car_list_view);
        //Initialise data
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = firebaseUser.getUid();
        databaseCars = FirebaseDatabase.getInstance().getReference("Car/" + userID);



        //Map
        mapView = view.findViewById(R.id.mapView_logIncident);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        currentLocation = loggedInMainActivity.lastKnownLocation;
        currentLocationLatLang = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());

        currentAddress = loggedInMainActivity.getCurrentAddress();
        incidentLocationAsCurrentLocation.setText(currentAddress);


        viewsSetup();

        if(Build.VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE},3);
        }

    }

    //makes some fields invisible
    private void viewsSetup() {
        mapView.setVisibility(View.VISIBLE);
        incidentLocationAsCurrentLocation.setVisibility(View.VISIBLE);
        incidentLocationAddress.setVisibility(View.INVISIBLE);
    }

//------map------------------------------------------------
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        //put marker on current location
        if(loggedInMainActivity.getUseAddressFromMapFrament()){
            map.addMarker(new MarkerOptions().position(loggedInMainActivity.getLatlngFromMapFragment()).title("Position you chose"));
            map.moveCamera(CameraUpdateFactory.zoomTo(13.0f));
            map.moveCamera(CameraUpdateFactory.newLatLng(loggedInMainActivity.getLatlngFromMapFragment()));
            incidentLocationAsCurrentLocation.setText(loggedInMainActivity.getAddressFromMapFragment());
            currentLocationLatLang = loggedInMainActivity.getLatlngFromMapFragment();
        }else{
        map.addMarker(new MarkerOptions().position(currentLocationLatLang).title("You are here"));
            map.moveCamera(CameraUpdateFactory.zoomTo(13.0f));
            map.moveCamera(CameraUpdateFactory.newLatLng(currentLocationLatLang));

            currentAddress = loggedInMainActivity.getCurrentAddress();
            incidentLocationAsCurrentLocation.setText(currentAddress);
        }


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                map.clear();
                map.addMarker(new MarkerOptions().position(latLng).title("You are here"));
                currentLocationLatLang = latLng;
                try {
                    loggedInMainActivity.updateAddress(latLng);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                currentAddress = loggedInMainActivity.getCurrentAddress();
                incidentLocationAsCurrentLocation.setText(currentAddress);

            }
        });
    }


    public void chageCurrentLocationLatLngToManual(){
       String addressLine  = incidentLocationAddress.getText().toString();
       String country =incidentLocationCountry.getText().toString();
       String postCode =incidentLocationPostCode.getText().toString();
       String city =incidentLocationTownCity.getText().toString();

       StringBuffer buffer = new StringBuffer();
       buffer.append("" + addressLine +", " + city + " " + postCode.toUpperCase() + ", " + country);
       String fullAddress = buffer.toString();

        Geocoder geocoder = new Geocoder(getContext());
        List<Address> address;
        LatLng latl = null;

        try {
            address = geocoder.getFromLocationName(fullAddress,1);
            if (address != null){
                Address location = address.get(0);
                latl = new LatLng(location.getLatitude(),location.getLongitude());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        if(latl != null){
            currentLocationLatLang = latl;
        }


    }

    //on click listeners------------------------------------------------------------------------------
    View.OnClickListener switchListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(aSwitch.isChecked()){
                addressFromMap = true;
                addressFromMap();
            }else{
                addressFromMap = false;
                addressManual();
            }
        }
    };

    View.OnClickListener submitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(addressFromMap == false){
                chageCurrentLocationLatLngToManual();
                Log.d("Location", currentLocationLatLang.latitude + " " + currentLocationLatLang.longitude);
            }




            loggedInMainActivity.resetPhotoCounter();
        }
    };

    View.OnClickListener takePictureListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //saves photo file paths of all the pictures in LoggedInMainActivity
            //  photoFilePathsList
            loggedInMainActivity.takePicture();
        }
    };
    View.OnClickListener deletePicturesListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            loggedInMainActivity.deletePhotos();
        }
    };

    //-----------------------------------------------------------------------------------------------

    //methods called by the switch
    public void addressManual(){
        mapView.setVisibility(View.INVISIBLE);
        incidentLocationAsCurrentLocation.setVisibility(View.INVISIBLE);

        incidentLocationAddress.setVisibility(View.VISIBLE);
        incidentLocationCountry.setVisibility(View.VISIBLE);
        incidentLocationPostCode.setVisibility(View.VISIBLE);
        incidentLocationTownCity.setVisibility(View.VISIBLE);
    }

    public void addressFromMap(){
        mapView.setVisibility(View.VISIBLE);
        incidentLocationAsCurrentLocation.setVisibility(View.VISIBLE);


        incidentLocationAddress.setVisibility(View.INVISIBLE);
        incidentLocationCountry.setVisibility(View.INVISIBLE);
        incidentLocationPostCode.setVisibility(View.INVISIBLE);
        incidentLocationTownCity.setVisibility(View.INVISIBLE);
    }
    //---------------------------------------------------------------------------------------------
}
