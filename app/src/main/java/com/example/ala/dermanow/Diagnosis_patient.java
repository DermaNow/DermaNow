package com.example.ala.dermanow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Diagnosis_patient extends AppCompatActivity {

    TextView disease_name, description, symptoms, precautions;
    private JSONArray result;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_patient);
        setTitle("Diagnosis");
        //------------------------------------------------------------------------------------------

        Button clinic_search = (Button) findViewById(R.id.patientDiagnosis_clinic_search_button);
        clinic_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(Diagnosis_patient.this, findNearestClinic.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        disease_name = (TextView) findViewById(R.id.patientDiagnosis_diseaseName_text);
        description = (TextView) findViewById(R.id.patient_Descreption_fulltext);
        symptoms = (TextView) findViewById(R.id.patient_symptoms_fulltext);
        precautions = (TextView) findViewById(R.id.patient_precaution_fulltext);
        //------------------------------------------------------------------------------------------

        //call method to get Diagnosis result
        getpatientDisease();
    }

    //----------------------------------------------------------------------------------------------

    private void getpatientDisease() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_DISEASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");
                            //   for (int i = 0; i < result.length(); i++) {
                            JSONObject patient = result.getJSONObject(0);
                            //get results and set it to text views
                            disease_name.setText(patient.getString("disease_name"));
                            description.setText(patient.getString("description"));
                            symptoms.setText(patient.getString("symptoms"));
                            precautions.setText(patient.getString("precautions"));
                            //      }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getApplicationContext(),
                                error.getMessage(),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                }) {
        };
        //--------------------------------------------------------------------------------------
        //adding our stringrequest to queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
