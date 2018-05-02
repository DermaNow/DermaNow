package com.example.ala.dermanow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class patientHome extends AppCompatActivity {

    private JSONArray result, result2;
    private String p_id, dr_id, sch_date, sch_time, dr_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);
        setTitle("Home");

        //------------------------------------------------------------------------------------------

        if (!SharedPrefManager.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        //------------------------------------------------------------------------------------------
        ImageButton skin_diagnosis = (ImageButton) findViewById(R.id.patientHome_CamButton);
        skin_diagnosis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(patientHome.this, skinDiagnosis.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        ImageButton profile = (ImageButton) findViewById(R.id.patientHome_profileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(patientHome.this, profile.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        ImageButton app = (ImageButton) findViewById(R.id.patientHome_search_button);
        app.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(patientHome.this, search.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        ImageButton clinic = (ImageButton) findViewById(R.id.patientHome_reserve_appButton);
        clinic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(patientHome.this, patient_view_appointments.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        Rating();
    }

    //------------------------------------------------------------------------------------------
    //Show rating dialog box
    public void RatingDialog(String DrName, final String DrID) {
        final AlertDialog.Builder popDialog = new AlertDialog.Builder(this);
        //create rating bar
        final RatingBar rating = new RatingBar(this);
        rating.setNumStars(5);
        // set stars to the dialog
        LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
        //set stars  color
        stars.getDrawable(2).setColorFilter(Color.rgb(255, 215, 0), PorterDuff.Mode.SRC_ATOP);
        rating.setStepSize(0.5f);
        //add layout to rating bar
        rating.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout parent = new LinearLayout(this);
        parent.setGravity(Gravity.CENTER);
        parent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        //add rating bar to the layout
        parent.addView(rating);
        //add layout to the dialog
        popDialog.setView(parent);
        popDialog.setTitle("Rating!");
        popDialog.setMessage("How was your appointment with Dr." + DrName + "?");
        popDialog.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //call set rating method
                setRating(rating.getRating(), DrID);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });

        popDialog.create().show();

    }
    //------------------------------------------------------------------------------------------

    // To store rate in database
    public void setRating(final float rate, final String id) {
        final String Rate = Float.toString(rate);
        final String p_id = SharedPrefManager.getInstance(this).getUserID();
        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SET_RATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            //--------------------------------------------------------------------------------------
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dr_id", id);
                params.put("p_id", p_id);
                params.put("rate_stars", Rate);
                return params;
            }
        };
        //------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                startActivity(new Intent(patientHome.this, MainActivity.class));
                break;
        }
        return true;
    }

    //------------------------------------------------------------------------------------------
    // Get patient last appoitments to view rating dialog
    public void Rating() {
        p_id = SharedPrefManager.getInstance(this).getUserID();
        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_PATIENT_APP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);
                                dr_id = json.getString("dr_id");
                                sch_date = json.getString("sch_date");
                                sch_time = json.getString("sch_time");
                                dr_name = json.getString("dr_name");
                                String DateTime = sch_date + " " + sch_time;
                                //format date
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                final Date strDate = sdf.parse(DateTime);
                                //check if current date is after appoitment date
                                if (new Date().after(strDate)) {
                                    //store dr id and name for that appoitment
                                    final String dr = dr_id;
                                    final String name = dr_name;
                                    //check if the patient rate this doctor before or not
                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CHECK_RATE,
                                            new Response.Listener<String>() {
                                                @Override
                                                public void onResponse(String response) {
                                                    try {
                                                        JSONObject jsonObject = new JSONObject(response);
                                                        result2 = jsonObject.getJSONArray("result");
                                                        for (int i = 0; i < result2.length(); i++) {
                                                            JSONObject json = result2.getJSONObject(i);
                                                            String rated = json.getString("checked");
                                                            if (rated.equalsIgnoreCase("true")) {
                                                                //rated
                                                                break;
                                                            } else if (rated.equalsIgnoreCase("false")) {
                                                                // not be rated yet
                                                                //call rating dialog
                                                                RatingDialog(name, dr);
                                                                break;
                                                            }
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
                                            params.put("dr_id", dr);
                                            params.put("p_id", p_id);

                                            return params;
                                        }
                                    };
                                    //Creating a request queue
                                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                                    //Adding request to the queue
                                    requestQueue.add(stringRequest);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            //--------------------------------------------------------------------------------------
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("p_id", p_id);
                return params;
            }
        };
        //------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //---------------------------------------------------------------------------------------------------

}
