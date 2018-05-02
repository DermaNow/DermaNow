//Package
package com.example.ala.dermanow;
//imports

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//--------------------------------------------------------------------------------------------------
public class patient_signup extends AppCompatActivity implements View.OnClickListener {
    //----------------------------------------------------------------------------------------------
    //Spinner Patient Marital Status in String form
    static final String[] Marital_status = new String[]{"Single", "Married", "Devorce"};
    //defining editTxt and Button
    private EditText editTextFullname, editTextEmail, editTextUsername, editTextPassword, editTextPhone;
    private Button buttonPatientSignup;
    private TextView datePickerValueTextView;
    private RadioButton radioButtonMale, radioButtonFemale;
    private String gender = "";
    private String[] maritalStatus = {""};
    private int Year, Month, DayOfMonth;
    //----------------------------------------------------------------------------------------------
    //For network operation
    private ProgressDialog progressDialog;

    //----------------------------------------------------------------------------------------------
    //ON CREATE METHOD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_signup);
        setTitle("Sign up");
        //------------------------------------------------------------------------------------------
        //ProgressDialog
        progressDialog = new ProgressDialog(this);
        //------------------------------------------------------------------------------------------
        //editText & Radio Buttons Initialization
        editTextFullname = (EditText) findViewById(R.id.editText_patient_name);
        editTextEmail = (EditText) findViewById(R.id.editText_patient_email);
        editTextUsername = (EditText) findViewById(R.id.editText_patient_username);
        editTextPassword = (EditText) findViewById(R.id.editText_patient_password);
        editTextPhone = (EditText) findViewById(R.id.editText_patient_phone);
        radioButtonMale = (RadioButton) findViewById(R.id.radioButton_patient_male);
        radioButtonFemale = (RadioButton) findViewById(R.id.radioButton_patient_female);
        //Button OnclickListner
        buttonPatientSignup = (Button) findViewById(R.id.button_patient_signup);
        buttonPatientSignup.setOnClickListener(this);
//------------------------------------------------------------------------------------------
        //Spinner Patient Marital Status Initialization
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, Marital_status);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner status = (Spinner) findViewById(R.id.patient_signup_mStatus_spinner);
        status.setAdapter(adapter4);
        status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position).toString();
                String marital = selectedItem;
                maritalStatus[0] = marital;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        this.showDatePickerDialog();

    }
    //------------------------------------------------------------------------------------------

    private void patientSignup() {

        //---------------------------------------------------------------------------------------------
        final String name = editTextFullname.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();//foreign key
        final String phone = editTextPhone.getText().toString().trim();//foreign key
        final String username = editTextUsername.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        //------------------------------------------------------------------------------------------
        //Setting Gender
        if (radioButtonMale.isChecked()) {
            gender = "M";
        }
        if (radioButtonFemale.isChecked()) {
            gender = "F";
        }
        //------------------------------------------------------------------------------------------
        //Birthday Date Format to be entered in the database with correct form
        final String birthdate = Year + "-" + Month + "-" + DayOfMonth;
        //------------------------------------------------------------------------------------------
        //ERROR MESSAGES____________________________________________________________________________
        //USER ERROR MESSAGES FOR EMPTY FILED
        String errorMsg = "";
        if (name.equals("")) {
            errorMsg = "Name";
        }

        if (email.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Email";
            } else {
                errorMsg = "Email";
            }
        }

        if (username.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Username";
            } else {
                errorMsg = "Username";
            }
        }

        if (password.equals("")) {
            if (errorMsg != "") {
                errorMsg += " , Password";
            } else {
                errorMsg = "Password";
            }
        }

        if (phone.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Phone";
            } else {
                errorMsg = "Phone";
            }
        }
        //Show the error message in TOAST FORM
        if (errorMsg != "") {
            Toast.makeText(patient_signup.this, errorMsg + " must be filled", Toast.LENGTH_LONG).show();
            return;
        }
        //------------------------------------------------------------------------------------------
        //check the name if has alphanumeric or special characters
        if (!Pattern.matches("[a-zA-Z ]+", name)) {
            Toast.makeText(patient_signup.this, "your name must contains alphabetic character only", Toast.LENGTH_LONG).show();
            return;
        }
        //------------------------------------------------------------------------------------------
        //Error Message for email format
        String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(patient_signup.this, "email has invalid format", Toast.LENGTH_LONG).show();
            return;
        }
        //------------------------------------------------------------------------------------------
        //Error Message for password format
        if (password.length() <= 7) {
            Toast.makeText(patient_signup.this, "password must be at least 8 charecter long", Toast.LENGTH_LONG).show();
            return;
        }
        //------------------------------------------------------------------------------------------
        //Error Message for phone format
        if (phone.length() <= 9) {
            Toast.makeText(patient_signup.this, "phone must be 10 digits", Toast.LENGTH_LONG).show();
            return;
        }
        //------------------------------------------------------------------------------------------
        //User State Message while registering
        progressDialog.setMessage("Registering patient...");
        progressDialog.show();
        //------------------------------------------------------------------------------------------
        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_PATIENT_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("message").equalsIgnoreCase("User registered successfully")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                startActivity(new Intent(patient_signup.this, patientHome.class));
                            } else {
                                progressDialog.hide();
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("sex", gender);
                params.put("marital_status", maritalStatus[0]);
                params.put("b_day", birthdate);
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("phone", phone);
                return params;
            }
        };
        //------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        finish();
    }
//--------------------------------------------------------------------------------------

    private void showDatePickerDialog() {
        // Get open DatePickerDialog button.
        TextView pickDate = (TextView) findViewById(R.id.patient_signup_pickup_bdate);
        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Create a new OnDateSetListener instance.
                DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
                        if (year < thisYear) {
                            StringBuffer strBuf = new StringBuffer();
                            strBuf.append(year);
                            strBuf.append("-");
                            strBuf.append(month + 1);
                            strBuf.append("-");
                            strBuf.append(dayOfMonth);
                            datePickerValueTextView = (TextView) findViewById(R.id.patient_signup_pickup_bdate);
                            datePickerValueTextView.setText(strBuf.toString());
                            Year = year;
                            Month = month;
                            DayOfMonth = dayOfMonth;
                        } else {
                            BirthdayAlertMessage();
                        }
                    }
                };
                // Get current year, month and day.
                Calendar now = Calendar.getInstance();
                int year = now.get(java.util.Calendar.YEAR);
                int month = now.get(java.util.Calendar.MONTH);
                int day = now.get(java.util.Calendar.DAY_OF_MONTH);
                // Create the new DatePickerDialog instance.
                DatePickerDialog datePickerDialog = new DatePickerDialog(patient_signup.this, android.R.style.Theme_Holo_Light_Dialog, onDateSetListener, year, month, day);
                // Popup the dialog.
                datePickerDialog.show();
            }
        });
    }


    //--------------------------------------------------------------------------------------
    private void BirthdayAlertMessage() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(patient_signup.this);
        dlgAlert.setMessage("Please choose a date before the current date");
        dlgAlert.setTitle("Alert");
        // dlgAlert.setCancelable(false);
        dlgAlert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        dlgAlert.show();

    }

    //----------------------------------------------------------------------------------------------
    //call patientSignup Method On Click
    @Override
    public void onClick(View view) {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
        patientSignup();
    }
}
