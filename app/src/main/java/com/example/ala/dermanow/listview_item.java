package com.example.ala.dermanow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

public class listview_item extends AppCompatActivity {

    TextView Name, SSN, clinicName, Email, Phone, Major, Degree, Job_url, Clinic_url;
    String ID, clinic_url, job_url;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_item);
        setTitle("Doctor Info");
        //------------------------------------------------------------------------------------------

        Name = (TextView) findViewById(R.id.listitem_name);
        SSN = (TextView) findViewById(R.id.listitem_ssn);
        clinicName = (TextView) findViewById(R.id.listitem_clinic);
        Email = (TextView) findViewById(R.id.listitem_email);
        Phone = (TextView) findViewById(R.id.listitem_phone);
        Major = (TextView) findViewById(R.id.listitem_major);
        Degree = (TextView) findViewById(R.id.listitem_degree);
        Job_url = (TextView) findViewById(R.id.job_license_view);
        Clinic_url = (TextView) findViewById(R.id.clinic_license_view);
        Button accept = (Button) findViewById(R.id.admin_acceptbtn);
        Button reject = (Button) findViewById(R.id.admin_rejectbtn);
        Button deleteAccount = (Button) findViewById(R.id.admin_deleteaccount_btn);
        //------------------------------------------------------------------------------------------

        //get data from previous intent
        Intent i = getIntent();
        ID = i.getStringExtra("dr_id");
        clinic_url = i.getStringExtra("clinic_url");
        job_url = i.getStringExtra("job_url");
        Name.setText("Name: " + i.getStringExtra("name"));
        SSN.setText("SSN: " + i.getStringExtra("ssn"));
        clinicName.setText("Clinic: " + i.getStringExtra("clinic"));
        Email.setText("Email: " + i.getStringExtra("email"));
        Phone.setText("Phone: " + i.getStringExtra("phone"));
        Major.setText("Major: " + i.getStringExtra("major"));
        Degree.setText("Degree: " + i.getStringExtra("degree"));

        //------------------------------------------------------------------------------------------

        //view job license
        Job_url.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String url = job_url.replace("localhost", "192.168.8.111");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //open browser
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                //set URL
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //------------------------------------------------------------------------------------------

        //view clinic license
        Clinic_url.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String url = clinic_url.replace("localhost", "192.168.8.111");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                //open browser
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                //set URL
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        //------------------------------------------------------------------------------------------

        //get status from intent
        String status = i.getStringExtra("status");
        //if its verified
        if (status.equals("1")) {
            //make button visible
            deleteAccount.setVisibility(View.VISIBLE);
        } else { //if it is unverified
            //make these buttons visible
            reject.setVisibility(View.VISIBLE);
            accept.setVisibility(View.VISIBLE);
        }
        //------------------------------------------------------------------------------------------

        //if accept request
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //call verify method
                verifyDoctor();
                Intent intent = new Intent(listview_item.this, admin_home.class);
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------

        reject.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //call reject and delete method with message
                rejectAndDeleteDoctor("Doctor request is regected successfully");
            }
        });
        //------------------------------------------------------------------------------------------

        deleteAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //call delete dialog box
                DeleteDialogBox();
            }
        });

    }

    //---------------------------------------------------------------------------------------------
    //verify doctor on database
    public void verifyDoctor() {
        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_VERIFY_DOCTORS,
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
                params.put("dr_id", ID);
                return params;
            }
        };
        //------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //----------------------------------------------------------------------------------------------
    // reject & delete doctor from database
    public void rejectAndDeleteDoctor(final String message) {
        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_REJECT_DOCTORS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(listview_item.this, admin_home.class);
                            startActivity(intent);
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
                params.put("dr_id", ID);
                return params;
            }
        };
        //------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //---------------------------------------------------------------------------------------------
    //Dialog box when delete account
    private void DeleteDialogBox() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(listview_item.this);
        alertDialog.setTitle("Alert!");
        alertDialog.setMessage("Are you sure you want to delete this account?");
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        rejectAndDeleteDoctor("Doctor account is deleted successfully");
                    }

                });

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

}
