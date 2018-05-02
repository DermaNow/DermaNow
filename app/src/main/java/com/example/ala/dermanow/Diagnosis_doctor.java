package com.example.ala.dermanow;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Diagnosis_doctor extends AppCompatActivity {

    TextView disease_name, description, symptoms, medication;
    private JSONArray result;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis_doctor);
        setTitle("Diagnosis");
        //------------------------------------------------------------------------------------------

        disease_name = (TextView) findViewById(R.id.dorctorDiagnosis_diseaseName_text);
        description = (TextView) findViewById(R.id.dr_descreption_fulltext);
        symptoms = (TextView) findViewById(R.id.dr_symptoms_fulltext);
        medication = (TextView) findViewById(R.id.dr_midication_fulltext);
        //------------------------------------------------------------------------------------------

        //call method to get Diagnosis result
        getDoctorDisease();
    }

    //----------------------------------------------------------------------------------------------
    public void getDoctorDisease() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constants.URL_GET_DISEASE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONObject jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject doctor = result.getJSONObject(i);
                                //get results and set it to text views
                                disease_name.setText(doctor.getString("disease_name"));
                                description.setText(doctor.getString("description"));
                                symptoms.setText(doctor.getString("symptoms"));
                                medication.setText(doctor.getString("medication"));
                            }
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
