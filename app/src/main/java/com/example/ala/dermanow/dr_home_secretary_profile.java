package com.example.ala.dermanow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class dr_home_secretary_profile extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    private TextView textViewUsername, textViewName, textViewUserEmail, textViewPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dr_home_secretary_profile);
        setTitle("My Secretary Profile");
        //Getting All User Profile Information
        textViewUsername = (TextView) findViewById(R.id.secretary_profile_username_text);
        textViewName = (TextView) findViewById(R.id.TextView_profile_secretary_name);
        textViewUserEmail = (TextView) findViewById(R.id.TextView_profile_secretary_email);
        textViewPhone = (TextView) findViewById(R.id.TextView_profile_secretary_phone);
        //------------------------------------------------------------------------------------------
        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_DR_SECRETARY_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                //----------------------------------------------------------------------
                                SharedPrefManager.getInstance(getApplicationContext())
                                        .drHomeSecretaryProfile(
                                                jsonObject.getString("sec_id"),
                                                jsonObject.getString("username"),
                                                jsonObject.getString("name"),
                                                jsonObject.getString("email"),
                                                jsonObject.getString("phone")
                                        );
                                //Getting Username from sharedPrefManager Class Object
                                textViewName.setText(SharedPrefManager.getInstance(getApplicationContext()).getSecretaryName());
                                //Getting Name from sharedPrefManager Class Object
                                textViewUsername.setText(SharedPrefManager.getInstance(getApplicationContext()).getSecretaryUsername());
                                //Getting Email from sharedPrefManager Class Object
                                textViewUserEmail.setText(SharedPrefManager.getInstance(getApplicationContext()).getSecretaryUserEmail());
                                //Getting Phone from sharedPrefManager Class Object
                                textViewPhone.setText("0" + SharedPrefManager.getInstance(getApplicationContext()).getSecretaryUserPhone());
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                                startActivity(new Intent(getApplicationContext(), secretary_signup.class));
                                finish();
                            }
                            //----------------------------------------------------------------------
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dr_id", SharedPrefManager.getInstance(getApplicationContext()).getUserID());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //----------------------------------------------------------------------------------------------
}