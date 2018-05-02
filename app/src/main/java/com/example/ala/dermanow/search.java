package com.example.ala.dermanow;

import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.LEFT;


public class search extends AppCompatActivity {

    public Typeface t;
    LinearLayout scrollLayout;
    SearchView searchview;
    TextView searchResultsText;
    //JSON Arrays
    private JSONArray result;
    private JSONArray result2;
    private JSONArray result3;
    private JSONArray result4;
    //------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Search");
        //------------------------------------------------------------------------------------------

        searchview = (SearchView) findViewById(R.id.searchView);
        searchview.setQueryHint("Type your keyword here");
        searchResultsText = new TextView(search.this);
        final RadioButton search_doctor = (RadioButton) findViewById(R.id.radioButton_search_doctor);
        final RadioButton search_clinic = (RadioButton) findViewById(R.id.radioButton_search_clinic);
        final RadioButton search_rate = (RadioButton) findViewById(R.id.radioButton_search_rate);
        final RadioButton search_district = (RadioButton) findViewById(R.id.radioButton_search_district);
        scrollLayout = (LinearLayout) findViewById(R.id.scroll);
        Typeface t = Typeface.create(Typeface.SERIF, Typeface.BOLD);
        //------------------------------------------------------------------------------------------

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null) {
                    //check if user want to search by rate
                    if (search_rate.isChecked()) {
                        scrollLayout.removeAllViews();
                        searchByRate(query);
                    }
                    //check if user want to search by doctor name
                    if (search_doctor.isChecked()) {
                        scrollLayout.removeAllViews();
                        searchByName(query);
                    }
                    //check if user want to search by clinic name
                    if (search_clinic.isChecked()) {
                        scrollLayout.removeAllViews();
                        searchByClinic(query);
                    }
                    //check if user want to search by district
                    if (search_district.isChecked()) {
                        scrollLayout.removeAllViews();
                        searchByDistrict(query);
                    }
                }


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    //----------------------------------------------------------------------------------------------
    public void searchByRate(final String query) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEARCH_RATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");
                            String[] ResopnseResult = new String[result.length()];
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);
                                //store result
                                String ResultString = "Doctor Name :" + json.getString("name") + "\nClinic Name :" + json.getString("clinic_name") +
                                        "\nMajor :" + json.getString("major") + "\nDegree :" + json.getString("degree")
                                        + "\nPhone :" + json.getString("phone") +
                                        "\nRate :" + json.getString("rates") +
                                        " (" + json.getString("num_rates") + ")\n";
                                //store it in Array
                                ResopnseResult[i] = ResultString;
                            }
                            //text to be set in textview
                            String text = "";
                            //traverse on Array
                            for (int j = 0; j < ResopnseResult.length; j++) {
                                //new line between each result
                                text += "\n" + ResopnseResult[j];
                            }
                            // if no result
                            if (text == "") {
                                searchResultsText.setText("No Results");
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.GRAY);
                                searchResultsText.setGravity(CENTER);
                                searchResultsText.setTextAlignment(CENTER);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);

                                // if there is a result
                            } else {
                                searchResultsText.setText(text);
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.BLACK);
                                searchResultsText.setPadding(14, 0, 0, 0);
                                searchResultsText.setGravity(LEFT);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rates", query);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }

    //---------------------------------------------------------------------------------------------
    public void searchByName(final String query) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEARCH_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result2 = jsonObject.getJSONArray("result");
                            String[] ResopnseResult = new String[result2.length()];

                            for (int i = 0; i < result2.length(); i++) {
                                JSONObject json = result2.getJSONObject(i);
                                //store result
                                String ResultString = "Doctor Name :" + json.getString("name") + "\nClinic Name :" + json.getString("clinic_name") +
                                        "\nMajor :" + json.getString("major") + "\nDegree :" + json.getString("degree")
                                        + "\nPhone :" + json.getString("phone") +
                                        "\nRate :" + json.getString("rates") +
                                        " (" + json.getString("num_rates") + ")\n";
                                //store result in array
                                ResopnseResult[i] = ResultString;


                            }
                            //text to be set in textview
                            String text = "";
                            //traverse on Array
                            for (int j = 0; j < ResopnseResult.length; j++) {
                                //new line
                                text += "\n" + ResopnseResult[j];

                            }
                            //no result
                            if (text == "") {
                                searchResultsText.setText("No Results");
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.GRAY);
                                searchResultsText.setGravity(CENTER);
                                searchResultsText.setTextAlignment(CENTER);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);

                            } else {
                                searchResultsText.setText(text);
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.BLACK);
                                searchResultsText.setPadding(14, 0, 0, 0);
                                searchResultsText.setGravity(LEFT);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Name", query);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    //---------------------------------------------------------------------------------------------

    public void searchByClinic(final String query) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEARCH_CLINIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result3 = jsonObject.getJSONArray("result");
                            String[] ResopnseResult = new String[result3.length()];

                            for (int i = 0; i < result3.length(); i++) {
                                JSONObject json = result3.getJSONObject(i);
                                String ResultString = "Clinic Name :" + json.getString("clinic_name");


                                LatLng Location = new LatLng(Double.parseDouble(json.getString(
                                        "latitude")), Double.parseDouble(json.getString("longitude")));

                                Geocoder geocoder;
                                List<Address> addresses = null;
                                geocoder = new Geocoder(getApplication(), Locale.getDefault());

                                try {
                                    addresses = geocoder.getFromLocation(Location.latitude, Location.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                                String city = addresses.get(0).getLocality();
                                String country = addresses.get(0).getCountryName();
                                //store result
                                ResultString += "\nAddress :" + address + "\nCity :" + city + "\nCountry : " + country + "\n";
                                //store result in Array
                                ResopnseResult[i] = ResultString;

                            }
                            //text to be set in textview
                            String text = "";
                            //traverse on Array
                            for (int j = 0; j < ResopnseResult.length; j++) {
                                //new line
                                text += "\n" + ResopnseResult[j];

                            }
                            //no result
                            if (text == "") {
                                searchResultsText.setText("No Results");
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.GRAY);
                                searchResultsText.setGravity(CENTER);
                                searchResultsText.setTextAlignment(CENTER);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);

                            } else {
                                searchResultsText.setText(text);
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.BLACK);
                                searchResultsText.setPadding(14, 0, 0, 0);
                                searchResultsText.setGravity(LEFT);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clinic_name", query);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    //---------------------------------------------------------------------------------------------

    public void searchByDistrict(final String query) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_SEARCH_DISTRICT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result4 = jsonObject.getJSONArray("result");
                            String[] ResopnseResult = new String[result4.length()];

                            for (int i = 0; i < result4.length(); i++) {
                                JSONObject json = result4.getJSONObject(i);
                                //store result
                                String ResultString = "Clinic Name :" + json.getString("name");
                                ResultString += "\nDistrict :" + json.getString("district") + "\nStreet :" + json.getString("street") +
                                        "\nCity :" + json.getString("city")
                                        + "\nCountry :" + json.getString("country") + "\n";

                                //store result in array
                                ResopnseResult[i] = ResultString;

                            }
                            //text to be set in textview
                            String text = "";
                            //traverse on Array
                            for (int j = 0; j < ResopnseResult.length; j++) {
                                //new line
                                text += "\n" + ResopnseResult[j];

                            }
                            //no result
                            if (text == "") {
                                searchResultsText.setText("No Results");
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.GRAY);
                                searchResultsText.setGravity(CENTER);
                                searchResultsText.setTextAlignment(CENTER);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);

                            } else {
                                searchResultsText.setText(text);
                                searchResultsText.setTextSize(18);
                                searchResultsText.setTextColor(Color.BLACK);
                                searchResultsText.setPadding(14, 0, 0, 0);
                                searchResultsText.setGravity(LEFT);
                                searchResultsText.setTypeface(t);
                                scrollLayout.addView(searchResultsText);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("district", query);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
}
