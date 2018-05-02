package com.example.ala.dermanow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.ala.dermanow.Constants.URL_ALL_UNVERIFIED_DOCTORS;

public class admin_unverified_doctors extends AppCompatActivity {

    private ListView doctorsList;
    private Doctor doctor;
    private ArrayList<Doctor> dr = new ArrayList<Doctor>();

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_list);
        doctorsList = (ListView) findViewById(R.id.list);
        setTitle("Unverified Doctors");
        //------------------------------------------------------------------------------------------

        //Call method to get all doctors information in the listview
        getAllDoctors();

        //on item selected in listview
        doctorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                Doctor selectrdDoctor = dr.get(position);

                //Send data to the next intent with doctor's info
                Intent intent = new Intent(admin_unverified_doctors.this, listview_item.class);
                intent.putExtra("dr_id", selectrdDoctor.getID());
                intent.putExtra("name", selectrdDoctor.getName());
                intent.putExtra("ssn", selectrdDoctor.getSSN());
                intent.putExtra("clinic", selectrdDoctor.getClinicName());
                intent.putExtra("major", selectrdDoctor.getMajor());
                intent.putExtra("degree", selectrdDoctor.getDegree());
                intent.putExtra("phone", selectrdDoctor.getPhone());
                intent.putExtra("email", selectrdDoctor.getEmail());
                intent.putExtra("job_url", selectrdDoctor.getJob_url());
                intent.putExtra("clinic_url", selectrdDoctor.getClinic_url());
                intent.putExtra("status", "0"); // 0 for Unverified

                startActivity(intent);
            }

        });
    }

    //----------------------------------------------------------------------------------------------

    private void getAllDoctors() {
        //create Arraylist to store doctors
        final ArrayList<String> doctors = new ArrayList<String>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, URL_ALL_UNVERIFIED_DOCTORS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject responds = jsonArray.getJSONObject(i);
                                doctor = new Doctor();
                                doctor.setID(responds.getString("dr_id"));
                                doctor.setSSN(responds.getString("dr_ssn"));
                                doctor.setName(responds.getString("name"));
                                doctor.setClinicName(responds.getString("clinic_name"));
                                doctor.setDegree(responds.getString("degree"));
                                doctor.setMajor(responds.getString("major"));
                                doctor.setPhone(responds.getString("phone"));
                                doctor.setEmail(responds.getString("email"));
                                doctor.setJob_url(responds.getString("job_url"));
                                doctor.setClinic_url(responds.getString("clinic_url"));

                                //Add doctor name to Arraylist to be viewed on listview
                                doctors.add(doctor.getName());

                                //Add doctor object to Arraylist
                                dr.add(doctor);
                            }
                            //create Adapter to take the Arraylist
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.listview_text, doctors);

                            //Set Adapter to the listview
                            doctorsList.setAdapter(adapter);
                            //if data changed refresh
                            adapter.notifyDataSetChanged();

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
                });

        // add it to the RequestQueue
        requestQueue.add(getRequest);
    }
}

