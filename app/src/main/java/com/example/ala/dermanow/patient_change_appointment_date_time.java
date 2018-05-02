package com.example.ala.dermanow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class patient_change_appointment_date_time extends AppCompatActivity implements View.OnClickListener {
    private String doctorName, appointmentDate, appointmentTime, doctorID, appointmentID, patientID;
    private LinearLayout linearLayout;
    private EditText editTextDoctorName;
    private EditText editTextAppointmentDate;
    private Button buttonSaveChanges;

    private JSONArray workingHoursResult;
    private ProgressDialog progressDialog;
    private TextView timeAvailability;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_change_appointment_date_time);
        setTitle("change appointment time");

        //get appointment information from Intent
        appointmentID = getIntent().getStringExtra("app_id");
        doctorName = getIntent().getStringExtra("dr_name");
        appointmentDate = getIntent().getStringExtra("date");
        appointmentTime = getIntent().getStringExtra("time");
        doctorID = getIntent().getStringExtra("dr_id");
        patientID = getIntent().getStringExtra("p_id");

        //initialize the Views
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_change_time_patient);
        editTextDoctorName = (EditText) findViewById(R.id.doctor_name_editText);
        editTextDoctorName.setText(doctorName);
        editTextAppointmentDate = (EditText) findViewById(R.id.appointment_dateChange_editText_patient);
        editTextAppointmentDate.setText(appointmentDate);
        timeAvailability = (TextView) findViewById(R.id.textVeiw_change_availableTime_patient);
        buttonSaveChanges = (Button) findViewById(R.id.appointment_saveChanges_button_patient);
        buttonSaveChanges.setOnClickListener(this);
        editTextAppointmentDate.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);

        //show progress Dialog while loading
        progressDialog.setMessage("Loading..");
        progressDialog.show();

        getDoctorWorkingHours(doctorID);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setAvailableHours();

                    }
                });

            }
        }, 3000);

    }

    //---------------------------------------------------------------------------------------------
    private void showDatePickerDialog() {
        // Get open DatePickerDialog button.

        // Create a new OnDateSetListener instance.
        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                StringBuffer strBuf = new StringBuffer();
                strBuf.append(year);
                strBuf.append("-");
                strBuf.append(month + 1);
                strBuf.append("-");
                strBuf.append(dayOfMonth);
                Date scheduleDate = null;
                try {
                    scheduleDate = simpleDateFormat.parse(strBuf.toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(scheduleDate);
                //get the day by an integer
                int selectedDay = calendar.get(Calendar.DAY_OF_WEEK);
                //the selected day wasn't a weekend day
                if (selectedDay != 6) {
                    //display it on editText
                    editTextAppointmentDate.setText(strBuf.toString());

                } else {
                    //otherwise show an alert massage
                    weekendAlertMessage();
                }
            }
        };
        // Get current year, month and day.
        Calendar now = Calendar.getInstance();
        int year = now.get(java.util.Calendar.YEAR);
        int month = now.get(java.util.Calendar.MONTH);
        int day = now.get(java.util.Calendar.DAY_OF_MONTH);
        // Create the new DatePickerDialog instance.
        DatePickerDialog datePickerDialog = new DatePickerDialog(patient_change_appointment_date_time.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
        //disable all the past days on DatePickerDialog
        datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
        // Popup the dialog.
        datePickerDialog.show();
    }

    //----------------------------------------------------------------------------------------------
    //alert massage if a weekend day was selected
    private void weekendAlertMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(patient_change_appointment_date_time.this);
        alertDialog.setMessage("Sorry it's a weekend, Please choose another day");
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        alertDialog.show();

    }

    //-------------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {
        if (v == editTextAppointmentDate) {
            showDatePickerDialog();
        }
        if (v == buttonSaveChanges) {
            appointementSaveChanges();
        }
    }

    //--------------------------------------------------------------------------------------------
    private void getDoctorWorkingHours(final String dr_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_DR_WORKING_HOURS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject;
                            jsonObject = new JSONObject(response);
                            workingHoursResult = jsonObject.getJSONArray("response");


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
                params.put("dr_id", dr_id);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);


    }
    //--------------------------------------------------------------------------------------------

    private void setAvailableHours() {

        //create radio groups and radio button
        //set massage if the array null (no available time)
        RadioGroup radioGroup = new RadioGroup(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_change_time_patient);
        radioGroup.removeAllViews();
        linearLayout.removeAllViews();
        timeAvailability.setText("");
        //   radioGroup = new RadioGroup(this);
        try {
            for (int i = 0; i < workingHoursResult.length(); i++) {
                JSONObject jsonObject = workingHoursResult.getJSONObject(i);
                if (!jsonObject.getString("massage").equalsIgnoreCase("No time available for this doctor")) {
                    RadioButton radioButton = new RadioButton(this);
                    radioButton.setText(jsonObject.getString("hour").substring(0, 5));
                    //Design the radio button text
                    Typeface t = Typeface.create(Typeface.SERIF, Typeface.BOLD);
                    radioButton.setTypeface(t);
                    radioButton.setTextColor(Color.BLACK);

                    radioGroup.addView(radioButton);
                } else {
                    progressDialog.dismiss();
                    timeAvailability.setText(jsonObject.getString("massage"));
                    break;
                }
            }
            linearLayout.addView(radioGroup);
            progressDialog.dismiss();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------
    private String getSelectedTime() {
        String selectedTime = "";
        View child = linearLayout.getChildAt(0);
        if (child instanceof RadioGroup) {
            ViewGroup radioGroup = (RadioGroup) child;
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                View radioChild = radioGroup.getChildAt(i);
                if (radioChild instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) radioChild;
                    boolean selectedRadio = radioButton.isChecked();
                    if (selectedRadio) {
                        selectedTime = radioButton.getText().toString().trim();
                        break;

                    }
                }
            }

        }
        return selectedTime;
    }

    //------------------------------------------------------------------------------------------
    private void appointementSaveChanges() {

        final String selectedTime;
        final String changedDate = editTextAppointmentDate.getText().toString().trim();
        ;
        final String changedTime = getSelectedTime();

        if (changedTime.equals("")) {
            //if was empty, show error message
            Toast.makeText(getApplicationContext(), "please, select a time", Toast.LENGTH_LONG).show();
            return;
        } else {
            selectedTime = changedTime + ":00";
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_APPOINTMENT_CHANGE_DATE_TIME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("message").equalsIgnoreCase("Changes are saved successfully")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                //return to view appointment
                                startActivity(new Intent(patient_change_appointment_date_time.this, patient_view_appointments.class));
                                finish();
                            } else {
                                //show the error massage
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("app_id", appointmentID);
                params.put("sch_date", changedDate);
                params.put("sch_time", selectedTime);
                params.put("dr_id", doctorID);
                params.put("p_id", patientID);

                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    //--------------------------------------------------------------------------------------------

}

