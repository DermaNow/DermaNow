package com.example.ala.dermanow;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.example.ala.dermanow.R.id.map_findClinic;

public class findNearestClinic extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleMap.OnInfoWindowClickListener {


    private static final String TAG = "CurrentLocation";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    protected LocationManager locationManager;
    LatLng currentLocation = null;
    Location nearest = null;
    Location current = null;
    ArrayList<Location> location;
    private GoogleMap mMap;
    private String name, lat, lon;

    //------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location = new ArrayList<>();
        setContentView(R.layout.activity_find_clinic);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map_findClinic);
        mapFragment.getMapAsync(this);


    }

    //------------------------------------------------------------------------------------------

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //call map to display my location when map is ready
        displayCurrentLocation(mMap);
        //get All clinics locations
        getLocations();
    }

    //----------------------------------------------------------------------------------------------

    private void displayCurrentLocation(GoogleMap mMap) {
        //check the permisions of location service
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }

        //------------------------------------------------------------------------------------------
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        //get location services
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);

        //get last known location
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            onLocationChanged(location);

        }

        locationManager.requestLocationUpdates(bestProvider, 20000, 0, this);

    }
    //------------------------------------------------------------------------------------------

    @Override
    public void onLocationChanged(Location location) {


        //get latitude & longitude
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        // set current latitude and longitude to LatLng object
        currentLocation = new LatLng(latitude, longitude);

        //Add blue marker
        mMap.addMarker(new MarkerOptions().position(currentLocation).title("You're here")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));


    }

    //------------------------------------------------------------------------------------------

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    //------------------------------------------------------------------------------------------

    @Override
    public void onProviderEnabled(String provider) {

    }

    //------------------------------------------------------------------------------------------

    @Override
    public void onProviderDisabled(String provider) {

    }

    //------------------------------------------------------------------------------------------
    //Method to get all clinics locations
    private void getLocations() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_DR_CLINIC_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);
                            //minmum distance
                            float min = 100000;
                            //traversing through all
                            for (int i = 0; i < array.length(); i++) {
                                //getting clinic from json array
                                JSONObject clinic = array.getJSONObject(i);
                                name = clinic.getString("clinic_name");
                                lat = clinic.getString("latitude");
                                lon = clinic.getString("longitude");
                                Location loc = new Location("Location");
                                loc.setLatitude(Double.parseDouble(lat));
                                loc.setLongitude(Double.parseDouble(lon));
                                Location current = new Location("current");
                                current.setLatitude(currentLocation.latitude);
                                current.setLongitude(currentLocation.longitude);
                                //compare each clinic distance with minumum
                                if (min > loc.distanceTo(current)) {
                                    min = loc.distanceTo(current);
                                    //set nearest with closet clinic to current
                                    nearest = loc;
                                }
                                //call add marker
                                addMarker();
                            }
                            //if nearest is not null
                            if (nearest != null) {
                                //get location to move camera to it
                                LatLng lat = new LatLng(nearest.getLatitude(), nearest.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lat, 18f));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
    //----------------------------------------------------------------------------------------------

    private void addMarker() {
        //create LatLng object
        LatLng L = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));

        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(L.latitude, L.longitude, 1); // Here 1 represent max location result to returned
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (addresses != null && addresses.size() > 0) {
            //get full address
            String address = addresses.get(0).getAddressLine(0);
            //get city
            String city = addresses.get(0).getLocality();
            //add a marker with info
            mMap.addMarker(new MarkerOptions().position(L).title(name.toUpperCase()).snippet("city" + city + "address" + address));
            //call info window method
            InfoWindow();
            //call on window click
            mMap.setOnInfoWindowClickListener(this);
        }
    }

    //----------------------------------------------------------------------------------------------
    private void InfoWindow() {
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {

                //return null view
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                //infowindow for current location
                if (marker.getTitle().equalsIgnoreCase("You're here")) {
                    return null;
                }
                //if its clinic
                View v = getLayoutInflater().inflate(R.layout.info_widow_layout, null);
                TextView Window_ClinicName = (TextView) v.findViewById(R.id.info_name);
                TextView Window_Address = (TextView) v.findViewById(R.id.info_address_text);
                TextView Window_city = (TextView) v.findViewById(R.id.info_city);
                // get index of address
                int index = marker.getSnippet().indexOf("address") + 7;
                //get city string
                String city = marker.getSnippet().substring(4, index - 7);
                //get address string
                String address = marker.getSnippet().substring(index);
                //set info to the textviews
                Window_ClinicName.setText(marker.getTitle());
                Window_Address.setText(address);
                Window_city.setText(city);

                //return the view
                return v;
            }
        });


    }

    //----------------------------------------------------------------------------------------------
    @Override
    public void onInfoWindowClick(Marker marker) {
        //check if the selected is the current location display a message
        if (marker.getTitle().equalsIgnoreCase("You're here")) {
            Toast.makeText(getApplicationContext(), "Please select a clinic", Toast.LENGTH_LONG).show();

        } else {
            //if clinic is selected
            Intent myIntent = new Intent(findNearestClinic.this, appointment.class);
            //add clinic name to the next intent
            myIntent.putExtra("Clinic", marker.getTitle());
            startActivity(myIntent);
        }
    }

}

