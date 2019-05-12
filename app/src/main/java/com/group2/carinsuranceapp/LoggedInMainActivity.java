package com.group2.carinsuranceapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LoggedInMainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    public FusedLocationProviderClient fusedLocationClient;
    public Location lastKnownLocation;
    private final static int MY_PERMISSION_REQUEST_LOCATION = 1;
    Geocoder geocoder;
    List<Address> addresses;
    String currentAddress;

    public String getCurrentAddress() {
        return this.currentAddress;
    }

    File photoFile;
    protected String currentPathToFile;
    protected int photoCounter = 0;
    List<String> photoFilePathsList = new ArrayList<>(4);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loged_in_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        createLocationRequest();

        auth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        lastKnownLocation = new Location("");
        currentAddress = "";

        //Fab takes user to "Log new incident"
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New Incident", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                Fragment incident = new Fragment_LogNewIncident();

                FragmentManager incidentManager = getSupportFragmentManager();
                FragmentTransaction incidentTransaction = incidentManager.beginTransaction();

                incidentTransaction.replace(R.id.screen, incident);
                incidentTransaction.commit();

            }
        });

        //Menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        Fragment fragment = new Fragment_Dashboard();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.screen, fragment);
        fragmentTransaction.commit();


        //Location
        geocoder = new Geocoder(this, Locale.getDefault());
        getLastLocation();

        Log.d("ADRESS",currentAddress);


    }



//--------menu--------------------------------------------------------------------------------------
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //field to hold current fragment
        Fragment fragment = null;

        //either assigns fragment or logs out
        if (id == R.id.m_logIncident) {
            fragment = new Fragment_LogNewIncident();
        } else if (id == R.id.m_pastIncidents) {
            fragment = new Fragment_ViewAllPastIncidents();
        } else if (id == R.id.m_myInfo) {
            fragment = new Fragment_MyInformation();
        } else if (id == R.id.m_myIncuranceInfo) {
            fragment = new Fragment_MyInsuranceInfo();
        } else if (id == R.id.m_map) {
            fragment = new Fragment_Map();
        } else if (id == R.id.m_dashboard) {
            fragment = new Fragment_Dashboard();
        } else if (id == R.id.m_logOut) {
            auth.signOut();
            if (auth.getCurrentUser() == null) {
                startActivity(new Intent(LoggedInMainActivity.this, MainActivity.class));

            }
        }


        //if didn't sign out change the view fragment
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.screen, fragment);
            fragmentTransaction.commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Location---------------------------------------------------------------------------------------------------
    public void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(ActivityCompat.shouldShowRequestPermissionRationale(LoggedInMainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION )&&ActivityCompat.shouldShowRequestPermissionRationale(LoggedInMainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION )){
                Toast.makeText(this,"no permission",Toast.LENGTH_LONG);
            } else {
                ActivityCompat.requestPermissions(LoggedInMainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSION_REQUEST_LOCATION);
            }
        }else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        lastKnownLocation = location;
                        try {
                            updateAddress(lastKnownLocation);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    protected void updateAddress(LatLng latLng) throws IOException {
        addresses = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        String address = addresses.get(0).getAddressLine(0);
        currentAddress = address;

    }

    private void updateAddress(Location location) throws IOException {
        addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
        String address = addresses.get(0).getAddressLine(0);
        currentAddress = address;

    }

    protected void createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    //Picture handling------------------------------------------------------------------------------
    public void takePicture() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},1);
            }
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }else{
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},2);
            }
        }
        else{

            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if(takePictureIntent.resolveActivity(getPackageManager())!= null){
                photoFile = null;
                try {
                    photoFile = createFile();
                    Log.d("HEYYYYY", photoFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(photoFile != null){
                    Uri photoURI = FileProvider.getUriForFile(LoggedInMainActivity.this,
                            "com.group2.carinsuranceapp.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                    startActivityForResult(takePictureIntent,0);

                }
            }


        }


    }

    protected File createFile() throws IOException{
        String timestamp = new SimpleDateFormat("yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timestamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        currentPathToFile = image.getAbsolutePath();
        return image;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(photoCounter <=4) {
                photoFilePathsList.add(currentPathToFile);
                if (requestCode == 0) {
                    switch (photoCounter) {
                        case 0: {
                            loadImageFromFile(currentPathToFile, R.id.incident_picture_1);
                            photoCounter++;
                            return;
                        }
                        case 1: {
                            loadImageFromFile(currentPathToFile, R.id.incident_picture_2);
                            photoCounter++;
                            return;
                        }
                        case 2: {
                            loadImageFromFile(currentPathToFile, R.id.incident_picture_3);
                            photoCounter++;
                            return;
                        }
                        case 3: {
                            loadImageFromFile(currentPathToFile, R.id.incident_picture_4);
                            Button takepic = (Button)findViewById(R.id.button_take_picture);
                            takepic.setVisibility(View.INVISIBLE);
                            photoCounter++;
                            return;
                        }
                    }
                }
            }

        }
    }

    protected void loadImageFromFile(String filePath, int viewID) {
        ImageView im = findViewById(viewID);
        Log.d("IN LOAD FROM",String.valueOf(viewID));
        im.setVisibility(View.VISIBLE);

        int targetW = im.getWidth();
        int targetH = im.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);


        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(filePath,bmOptions);
        im.setClickable(true);
        im.setImageBitmap(bitmap);

    }

    public  void resetPhotoCounter(){
        photoCounter = 0;
    }


    public void deletePhotos() {
        ImageView im = findViewById(R.id.incident_picture_1);
        im.setImageBitmap(null);
        im = findViewById(R.id.incident_picture_2);
        im.setImageBitmap(null);
        im = findViewById(R.id.incident_picture_3);
        im.setImageBitmap(null);
        im = findViewById(R.id.incident_picture_4);
        im.setImageBitmap(null);

        photoFilePathsList.clear();
        resetPhotoCounter();
    }
    //-----------------------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 1:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{

                }
                return;
            }
            case 2:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

                }else{

                }
                return;
            }
        }
    }


}
