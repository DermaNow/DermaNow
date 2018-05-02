package com.example.ala.dermanow;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class doctor_signup_location extends AppCompatActivity implements OnMapReadyCallback {

    PlaceAutocompleteFragment placeAutoComplete;
    private GoogleMap mMap;
    private Button btnCancel, btnNext;
    private String name, email, username, password, major, degree, clinicName, ssn, phone, district, city, country, street;
    private String latitude;
    private String Longitude;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup_location);
        setTitle("Clinic Location");
        //------------------------------------------------------------------------------------------

        //get data from previous intent
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        ssn = getIntent().getStringExtra("ssn");
        major = getIntent().getStringExtra("major");
        degree = getIntent().getStringExtra("degree");
        phone = getIntent().getStringExtra("phone");


        //------------------------------------------------------------------------------------------
        //Auto complete search
        placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete2);
        placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.d("Maps", "Place selected: " + place.getName());
                //get selected place name
                clinicName = (String) place.getName();
                //call the method to add a marker on this place
                addMarker(place);

            }

            //--------------------------------------------------------------------------------------
            @Override
            public void onError(Status status) {

                Log.d("Maps", "An error occurred: " + status);
            }
        });
        //------------------------------------------------------------------------------------------

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_clinic_location);
        mapFragment.getMapAsync(this);

        //------------------------------------------------------------------------------------------
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnNext = (Button) findViewById(R.id.btn_next);


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(doctor_signup_location.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(doctor_signup_location.this, attachment.class);
                //send data to the next intent
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("username", username);
                intent.putExtra("password", password);
                intent.putExtra("ssn", ssn);
                intent.putExtra("phone", phone);
                intent.putExtra("major", major);
                intent.putExtra("degree", degree);
                intent.putExtra("clinicName", clinicName);


                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", Longitude);
                intent.putExtra("district", district);
                intent.putExtra("street", street);
                intent.putExtra("country", country);
                intent.putExtra("city", city);


                startActivity(intent);
            }
        });

    }

    //----------------------------------------------------------------------------------------------

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    //----------------------------------------------------------------------------------------------

    //Add Selected location marker
    public void addMarker(Place p) {

        MarkerOptions markerOptions = new MarkerOptions();
        //get latitude and longitude on LatLng object for the postion
        markerOptions.position(p.getLatLng());
        //set title with place name
        markerOptions.title(p.getName() + "");
        //colored red marker on place
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

        //add marker to the map
        mMap.addMarker(markerOptions);
        //move camera to this marker place
        mMap.moveCamera(CameraUpdateFactory.newLatLng(p.getLatLng()));
        //zoom on this place
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        //get latitude & longitude
        latitude = String.valueOf(p.getLatLng().latitude);
        Longitude = String.valueOf(p.getLatLng().longitude);
        //call method to get the address
        setDistrict(p.getLatLng().latitude, p.getLatLng().longitude);
    }

    public void setDistrict(double latitude, double longitude) {

        //create LatLng object
        LatLng L = new LatLng(latitude, longitude);

        //create Geocoder to get location address
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(getApplication(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(L.latitude, L.longitude, 1); // Here 1 represent max location result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get address from 1 address line
        String address = addresses.get(0).getAddressLine(0);
        //get index of comma to spreate address patrs
        int comma = address.indexOf(',');
        //if comma exist
        if (comma != -1) {
            // district is the text after the comma
            district = address.substring(comma + 1);
            // district is the text before the comma
            street = address.substring(0, comma - 1);
            //get city name
            city = addresses.get(0).getLocality();
            //get country name
            country = addresses.get(0).getCountryName();
        }
    }

}