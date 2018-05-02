package com.example.ala.dermanow;

/**
 * Created by Walaa Nogali on 20/02/2018.
 */

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class
Signin extends AppCompatActivity implements View.OnClickListener {

    static String UserType = "";//for setting user type
    CharSequence[] values = {" Doctor ", " Other"};
    AlertDialog alertDialog1;
    //defining editTxt and Button
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button signin;
    //for time loading operation
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Sign in");
        //------------------------------------------------------------------------------------------
        //Check if the user were logged in or not using sharedPrefrenceObject
        //If it were logged in before and he doesn't logged out
        //the application will open his home page depending on his user type : patient , doctor , secretary
        if (SharedPrefManager.getInstance(this).isLoggedIn()) {
            if (UserType.equalsIgnoreCase("patient")) {
                finish();
                startActivity(new Intent(this, patientHome.class));
                return;
            }
            if (UserType.equalsIgnoreCase("doctor")) {
                finish();
                startActivity(new Intent(this, doctorHome.class));
                return;
            }
            if (UserType.equalsIgnoreCase("secretary")) {
                finish();
                startActivity(new Intent(this, SecretaryHome.class));
                return;
            }
            if (UserType.equalsIgnoreCase("admin")) {
                finish();
                startActivity(new Intent(this, admin_home.class));
                return;
            }
        }
        //----------------------------------------------------------------------
        //EditText inializations
        editTextUsername = (EditText) findViewById(R.id.editText_signin_uername);
        editTextPassword = (EditText) findViewById(R.id.editText_signin_password);
        TextView Signup_text = (TextView) findViewById(R.id.signin_signupText);
        // Sign up text
        Signup_text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogWithRadioButtonGroup();
            }
        });
        //button initialization
        signin = (Button) findViewById(R.id.signin_button);
        progressDialog = new ProgressDialog(this);
        //set a listener for button when got clicked
        signin.setOnClickListener(this);
    }

    //----------------------------------------------------------------------------------------------
    public void sign_in() {

        //get String(text) from the editText
        final String enteredUsername = editTextUsername.getText().toString().trim();
        final String enteredPassword = editTextPassword.getText().toString().trim();

        //check if the username or password are empty or not
        if (enteredUsername.equals("") || enteredPassword.equals("")) {
            //show an error massage
            Toast.makeText(Signin.this, "Username or password must be filled", Toast.LENGTH_LONG).show();
            return;
        }

        //User State Message while signing in
        progressDialog.setMessage("sign in...");
        progressDialog.show();

        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_DR_PATIENT_SIGNIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (!jsonObject.getBoolean("error")) {
                                //----------------------------------------------------------------------
                                if (jsonObject.getString("TableName").equalsIgnoreCase("PATIENT")) {
                                    UserType = "patient";
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .patientLogin(
                                                    jsonObject.getString("p_id"),
                                                    jsonObject.getString("username"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("sex"),
                                                    jsonObject.getString("marital_status"),
                                                    jsonObject.getString("email"),
                                                    jsonObject.getString("phone")
                                            );
                                    startActivity(new Intent(getApplicationContext(), patientHome.class));
                                    finish();
                                }
                                if (jsonObject.getString("TableName").equalsIgnoreCase("DOCTOR")) {
                                    UserType = "doctor";
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .doctorLogin(
                                                    jsonObject.getString("dr_id"),
                                                    jsonObject.getString("username"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("clinic_name"),
                                                    jsonObject.getString("major"),
                                                    jsonObject.getString("degree"),
                                                    jsonObject.getString("email"),
                                                    jsonObject.getString("phone")
                                            );
                                    startActivity(new Intent(getApplicationContext(), doctorHome.class));
                                    finish();
                                }
                                if (jsonObject.getString("TableName").equalsIgnoreCase("SECRETARY")) {
                                    UserType = "secretary";
                                    SharedPrefManager.getInstance(getApplicationContext())
                                            .secretaryLogin(
                                                    jsonObject.getString("sec_id"),
                                                    jsonObject.getString("username"),
                                                    jsonObject.getString("name"),
                                                    jsonObject.getString("email"),
                                                    jsonObject.getString("phone"),
                                                    jsonObject.getString("dr_id")
                                            );
                                    startActivity(new Intent(getApplicationContext(), SecretaryHome.class));
                                    finish();
                                }
                                if (jsonObject.getString("TableName").equalsIgnoreCase("ADMIN")) {
                                    UserType = "admin";
                                    startActivity(new Intent(getApplicationContext(), admin_home.class));
                                    finish();
                                }
                            } else {
                                Toast.makeText(
                                        getApplicationContext(),
                                        jsonObject.getString("message"),
                                        Toast.LENGTH_LONG
                                ).show();
                            }
                            //----------------------------------------------------------------------
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();//---------------
                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage(),
                        Toast.LENGTH_LONG)
                        .show();

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", enteredUsername);
                params.put("password", enteredPassword);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //----------------------------------------------------------------------------------------------
    //listener method to be called
    @Override
    public void onClick(View view) {
        sign_in();
    }

    //----------------------------------------------------------------------------------------------
    //select job dialog
    public void DialogWithRadioButtonGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);

        builder.setTitle("Please Select Your Job:");

        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch (item) {
                    case 0:
                        Intent myIntent = new Intent(Signin.this, doctor_signup_info1.class);
                        startActivity(myIntent);
                        break;
                    case 1:
                        Intent myIntent2 = new Intent(Signin.this, patient_signup.class);
                        startActivity(myIntent2);
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }

}
