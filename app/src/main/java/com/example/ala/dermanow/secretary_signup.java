package com.example.ala.dermanow;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class secretary_signup extends AppCompatActivity implements View.OnClickListener {

    //defining editTxt and Button
    private EditText editTextName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPhone;
    private EditText editTextEmail;
    private Button secretarySignup;
    private String doctor_username;
    //for time loading
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_signup);

        doctor_username = getIntent().getStringExtra("username");
        //editText & button initialization
        editTextName = (EditText) findViewById(R.id.editText_secretary_name);
        editTextPassword = (EditText) findViewById(R.id.editText_secretary_password);
        editTextUsername = (EditText) findViewById(R.id.editText_secretary_username);
        editTextPhone = (EditText) findViewById(R.id.editText_secretary_phone);
        editTextEmail = (EditText) findViewById(R.id.editText_secretary_email);
        secretarySignup = (Button) findViewById(R.id.button_secretary_signup);

        //intilize progress dialog to current context
        progressDialog = new ProgressDialog(this);

        //set a listener to button when it clicked
        secretarySignup.setOnClickListener(this);

    }

    //-----------------------------------------------------------------------------------------------------
    public void create_secretary_account() {

        //get the text from EditText
        final String enteredSecretaryName = editTextName.getText().toString().trim();
        final String enteredUsername = editTextUsername.getText().toString().trim();
        final String enteredPassword = editTextPassword.getText().toString().trim();
        final String enteredPhone = editTextPhone.getText().toString().trim();
        final String enteredEmail = editTextEmail.getText().toString().trim();

        //to get the doctor id to be store as a foreign key for the secretary
        final String doc_user = SharedPrefManager.getInstance(getApplicationContext()).getUsername();
        //error massages for empty fields
        String errorMsg = "";
        if (enteredSecretaryName.equals("")) {
            errorMsg = "Secretary name";
        }

        if (enteredUsername.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", username";
            } else {
                errorMsg = "username";
            }
        }

        if (enteredPassword.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", password";
            } else {
                errorMsg = "password";
            }
        }

        if (enteredEmail.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", email";
            } else {
                errorMsg = "email";
            }
        }

        if (enteredPhone.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", phone";
            } else {
                errorMsg = "phone";
            }
        }
//-------------------------------------------------------------------------------------------------------------
        if (errorMsg != "") {
            Toast.makeText(getApplicationContext(), "Please fill " + errorMsg, Toast.LENGTH_LONG).show();
            return;
        }

        //check the name if has alphanumeric or special characters
        if (!Pattern.matches("[a-zA-Z ]+", enteredSecretaryName)) {
            Toast.makeText(secretary_signup.this, "Secretary name must contains alphabetic character only", Toast.LENGTH_LONG).show();
            return;
        }

        //Error Message for password format
        if (enteredPassword.length() <= 7) {
            Toast.makeText(secretary_signup.this, "password must be at least 8 charecter long", Toast.LENGTH_LONG).show();
            return;
        }

        //Error massage for phone
        if (enteredPhone.length() <= 9) {
            Toast.makeText(secretary_signup.this, "phone must be 10 digits long", Toast.LENGTH_LONG).show();
            return;
        }

        //Error Message for email format
        String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(enteredEmail);
        if (!matcher.matches()) {
            Toast.makeText(secretary_signup.this, "email has invalid format", Toast.LENGTH_LONG).show();
            return;
        }
//-----------------------------------------------------------------------------------------------------
        //start loading the progress bar
        progressDialog.setMessage("Registering Secretary...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_SECRETARY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //check if the msg is successfully or not
                            if (jsonObject.getString("message").equalsIgnoreCase("Secretary Account is created successfully")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                SharedPrefManager.getInstance(getApplicationContext()).logout();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //--------------------------------------------------------------------------------
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //store the secretary data to be send to server(Database)
                Map<String, String> params = new HashMap<>();
                params.put("name", enteredSecretaryName);
                params.put("username", enteredUsername);
                params.put("password", enteredPassword);
                params.put("dr_username", doc_user);
                params.put("phone", enteredPhone);
                params.put("email", enteredEmail);
                return params;
            }
        };
        //starting the server Resquest
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //---------------------------------------------------------------------------------------------------------
    public void onClick(View v) {
        if (v == secretarySignup) create_secretary_account();
    }

}


