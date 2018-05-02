package com.example.ala.dermanow;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class appointment extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {
    //doctor identity
    String identity;

    //to get the date
    String dateOf;
    //progress dialog for loading operation
    ProgressDialog progressDialog;
    // to get doctor names
    private ArrayAdapter doctorsNames;
    private Spinner doctorsList;
    //Rating..
    private RatingBar ratingBar;
    private TextView raters;
    //An ArrayList for Spinner Items
    private ArrayList<String> doctors;
    //JSON Array
    private JSONArray result;
    private JSONArray result2;
    private JSONArray workingHoursResult;
    private JSONArray availableHours;
    //reservation button
    private Button reservation;
    //editText of the appointement date
    private EditText app_date;
    //for clinic name
    private String clinic;
    //to get rate
    private String[] stars;
    //linearLayout for available time in the interface
    private LinearLayout linearLayout;
    //textVeiw to show if there no time available for selected doctor
    private TextView timeAvailability;

    //----------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        setTitle("Appointment");

        //initializing linearLayout
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout_time);
        //intializing progress dialog
        progressDialog = new ProgressDialog(this);
        //initializing arraylist
        doctors = new ArrayList<String>();
        //initializing spinner
        doctorsList = (Spinner) findViewById(R.id.appointment_doctor_spinner);

        //------------------------------------------------------------------------------------------
        //Get choosen clinic name
        clinic = getIntent().getStringExtra("Clinic");
        //------------------------------------------------------------------------------------------
        app_date = (EditText) findViewById(R.id.editText_app_date);
        showDatePickerDialog();

        reservation = (Button) findViewById(R.id.appointmernt_confirm_button);
        timeAvailability = (TextView) findViewById(R.id.textVeiw_availableTime);
        //Rating
        raters = (TextView) findViewById(R.id.count);
        ratingBar = (RatingBar) findViewById(R.id.appointment_ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.rgb(255, 215, 0), PorterDuff.Mode.SRC_ATOP);
        ratingBar.setStepSize(0.5f);
        ratingBar.setIsIndicator(true);

        getDoctorsData();
        reservation.setOnClickListener(this);

        //activate listener for spinner to retreive doctors name
        doctorsList.setOnItemSelectedListener(this);

    }

    //----------------------------------------------------------------------------------------------
    //Show calender dialog method

    private void showDatePickerDialog() {
        // Get open DatePickerDialog button.
        app_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
                        int selectedDay = calendar.get(Calendar.DAY_OF_WEEK);
                        //get the day by an integer
                        //              int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                        if (selectedDay != 6) {

                            app_date.setText(strBuf.toString());

                        } else {
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
                DatePickerDialog datePickerDialog = new DatePickerDialog(appointment.this, android.R.style.Theme_DeviceDefault_Light_Dialog_MinWidth, onDateSetListener, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(now.getTimeInMillis());
                // Popup the dialog.
                datePickerDialog.show();
            }
        });
    }

    //----------------------------------------------------------------------------------------------
    private void weekendAlertMessage() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(appointment.this);
        alertDialog.setMessage("Sorry it's a weekend, Please choose another day");
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }

                });
        alertDialog.show();

    }

    //---------------------------------------------------------------------------------------------------------
    //to get the choosen Doctor id
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        progressDialog.setMessage("Loading ..");
        progressDialog.show();
        // get id of the selected doctor
        identity = getDr_id(position);
        get_rate(identity);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        //if nothing selected get id of the first doctor in postion 0
        identity = getDr_id(0);
        get_rate(identity);
    }


    //---------------------------------------------------------------------------------------------
    //get all doctors from the database on selected clinic
    public void getDoctorsData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DR_APP_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("result");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);
                                doctors.add(json.getString("name"));

                            }

                            //Add doctors Arraylist to the adapter
                            doctorsNames = new ArrayAdapter<String>(appointment.this, android.R.layout.simple_spinner_dropdown_item, doctors);
                            //Set Adapter to the spinner
                            doctorsList.setAdapter(doctorsNames);
                            //Refresh
                            doctorsNames.notifyDataSetChanged();

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
                params.put("clinic_name", clinic);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    //----------------------------------------------------------------------------------------------------
    private String getDr_id(int position) {
        String dr_id = "";
        try {
            JSONObject json = result.getJSONObject(position);
            dr_id = json.getString("dr_id");

            getDoctorWorkingHours(dr_id);
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
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dr_id;
    }

    //--------------------------------------------------------------------------------------------------
    //this method will make an appiontment and store it
    public void confirmation() {
        //get the data that related with appointement
        final String selectedTime;
        dateOf = app_date.getText().toString().trim();
        final String pateintID = SharedPrefManager.getInstance(this).getUserID().toString();
        final String time = getAppointmentTime(linearLayout);

        if (dateOf.equals("")) {
            Toast.makeText(getApplicationContext(), "please, select a date", Toast.LENGTH_LONG).show();
            return;
        }
        if (time.equals("")) {
            Toast.makeText(getApplicationContext(), "please, select a time", Toast.LENGTH_LONG).show();
            return;
        } else {
            selectedTime = time + ":00";
        }
        if (time.equalsIgnoreCase(timeAvailability.getText().toString())) {
            Toast.makeText(getApplicationContext(), "No time available, select another doctor", Toast.LENGTH_LONG).show();
            return;
        }


        //create a request
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_APPOINTMENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.getString("message").equalsIgnoreCase("appointment is reserved successfully")) {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(appointment.this, patientHome.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dr_id", identity);
                params.put("p_id", pateintID);
                params.put("sch_date", dateOf);
                params.put("sch_time", selectedTime);
                return params;
            }

        };
        //send the request queue to server side
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    //---------------------------------------------------------------------------------------------

    public String getAppointmentTime(ViewGroup linearParent) {
        String message = "";
        View child = linearParent.getChildAt(0);
        if (!timeAvailability.getText().equals("")) {
            message = timeAvailability.getText().toString();
            return message;
        } else if (child instanceof RadioGroup) {
            ViewGroup radioGroup = (RadioGroup) child;
            for (int i = 0; i < radioGroup.getChildCount(); i++) {
                View radioChild = radioGroup.getChildAt(i);
                if (radioChild instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) radioChild;
                    boolean selectedRadio = radioButton.isChecked();
                    if (selectedRadio) {
                        message = radioButton.getText().toString().trim();
                        break;

                    }
                }
            }

        }
        return message;
    }

    //---------------------------------------------------------------------------------------------
    @Override
    public void onClick(View v) {
        if (v == reservation)
            confirmation();
    }

    //------------------------------------------------------------------------------------------------
    //to get rate from the database for selected doctor
    public void get_rate(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_RATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            result2 = jsonObject.getJSONArray("result");
                            stars = new String[result2.length()];
                            for (int i = 0; i < result2.length(); i++) {
                                JSONObject json = result2.getJSONObject(i);
                                String test = json.getString("rate_stars");
                                //Add number of stars to the Array
                                stars[i] = test;

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Rating Average
                        float result = 0;
                        //Array length to get number of raters to this doctor
                        int count = stars.length;
                        double totalRate = 0;
                        for (int i = 0; i < stars.length; i++) {
                            //number os stars from one rater
                            String starsNum = stars[i];
                            float Num = Float.parseFloat(starsNum);
                            //sum all
                            totalRate += Num;
                            //get average and round it to nearest half
                            result = Math.round((totalRate / stars.length) * 2) / 2.0f;
                        }
                        //set result rate on rating bar
                        ratingBar.setRating(result);
                        //number of raters
                        raters.setText("(" + count + ")");
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
                params.put("dr_id", id);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);


    }

    //--------------------------------------------------------------------------------------------

    public void getDoctorWorkingHours(final String dr_id) {
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

    //----------------------------------------------------------------------------------------------
    public void setAvailableHours() {

        //create radio groups and radio button
        //set massage if the array null (no available time)
        RadioGroup radioGroup = new RadioGroup(this);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_time);
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
}
