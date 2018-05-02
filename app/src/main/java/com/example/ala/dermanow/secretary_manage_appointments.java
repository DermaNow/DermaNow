package com.example.ala.dermanow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

public class secretary_manage_appointments extends AppCompatActivity implements View.OnClickListener {

    private TableLayout tableLayout;
    private JSONArray result;
    private String dr_id;
    private TextView checkingMassage;
    private boolean flag = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_manage_appointments);
        setTitle("Manage Appointments");

        tableLayout = (TableLayout) findViewById(R.id.Schedule_Table_secretary);
        progressDialog = new ProgressDialog(this);

        loadingTheSchedule();


    }

    //-----------------------------------------------------------------------------------------
    private void loadingTheSchedule() {

        tableLayout.removeAllViews();
        dr_id = SharedPrefManager.getInstance(this).getSecretaryDrID().toString();
        checkingMassage = (TextView) findViewById(R.id.reservation_check_secretary);
        progressDialog.setMessage("Loading Schedule...");
        progressDialog.show();
        checkDoctorReservation(dr_id);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (flag) {
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            checkingMassage.setText("No reserved apppointments");
                            return;
                        }
                    });
                }

            }
        }, 3000);

//------------------------------------------------------------------------------------------

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!flag) {
                            createScheduleView();
                            progressDialog.dismiss();
                        }
                    }
                });

            }
        }, 3000);
    }

    //----------------------------------------------------------------------------------------------
    public void checkDoctorReservation(final String doctor_id) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_GET_DR_RESERVATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            String massage = "";
                            jsonObject = new JSONObject(response);
                            result = jsonObject.getJSONArray("response");
                            for (int i = 0; i < result.length(); i++) {
                                JSONObject json = result.getJSONObject(i);
                                massage = json.getString("massage");
                                if (massage.equalsIgnoreCase("No reserved appointments")) {
                                    flag = true;
                                    break;
                                }


                            }
                            //  checkingMassage.setText(massage);
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
                params.put("dr_id", dr_id);
                return params;
            }

        };
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);


    }

    //---------------------------------------------------------------------------------------------

    public void createScheduleView() {
        //check the incoming reservations to be displayed
        if (checkInComingReservations()) {
            //set margins for table row & table layout
            tableLayout.setGravity(Gravity.CENTER);
            TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
            TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
            tableRowParams.setMargins(15, 15, 15, 15);

            createTableHeader();

            //create the table programmatically
            try {
                //go through the JSONArray to get the information of the schedule
                for (int i = 0; i < result.length(); i++) {
                    //create a table row
                    TableRow tableRow = new TableRow(this);


                    //set a background color for the table row
                    tableRow.setBackgroundColor(Color.parseColor("#BBDEFB"));

                    JSONObject json = result.getJSONObject(i);

                    //create text veiw for each item in column
                    TextView textViewDay = new TextView(this);
                    TextView textViewDate = new TextView(this);
                    TextView textViewTime = new TextView(this);
                    TextView textViewName = new TextView(this);

                    //get the upcoming reservations
                    String sch_date = json.getString("sch_date");
                    String sch_time = json.getString("sch_time");
                    String DateTime = sch_date + " " + sch_time;
                    //use a specific format for date & time
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //get current date
                    Calendar currentCalendar = Calendar.getInstance();
                    //get appointment date & time in date format
                    Date appointmentDate = simpleDateFormat.parse(DateTime);
                    Calendar appointmentCalendar = Calendar.getInstance();
                    appointmentCalendar.setTime(appointmentDate);
                    //check if the appointment date & time after current date & time
                    if (appointmentCalendar.after(currentCalendar)) {

                        //add the schedule information to textVeiw
                        textViewDay.setText(dayOfWeek(json.getString("sch_date")));
                        textViewDate.setText(json.getString("sch_date"));
                        textViewTime.setText(json.getString("sch_time").substring(0, 5));
                        textViewName.setText(json.getString("name"));

                        //set style for the text Veiw (Bold)
                        textViewDay.setTypeface(textViewDay.getTypeface(), Typeface.BOLD);
                        textViewDate.setTypeface(textViewDate.getTypeface(), Typeface.BOLD);
                        textViewTime.setTypeface(textViewTime.getTypeface(), Typeface.BOLD);
                        textViewName.setTypeface(textViewName.getTypeface(), Typeface.BOLD);

                        //set text color for text Veiw
                        textViewDay.setTextColor(Color.WHITE);
                        textViewDate.setTextColor(Color.WHITE);
                        textViewTime.setTextColor(Color.WHITE);
                        textViewName.setTextColor(Color.WHITE);


                        ImageView imageButtonEdit = new ImageView(this);
                        imageButtonEdit.setId(i);
                        imageButtonEdit.setTag("change");
                        imageButtonEdit.setImageResource(R.drawable.ic_create_black_18dp);
                        imageButtonEdit.setOnClickListener(this);
                        //imageButtonEdit.setAdjustViewBounds(true);

                        ImageView imageButtonCancel = new ImageView(this);
                        imageButtonCancel.setId(i);
                        imageButtonCancel.setTag("cancel");
                        imageButtonCancel.setImageResource(R.drawable.ic_action_close);
                        imageButtonCancel.setOnClickListener(this);
                        //imageButtonCancel.setAdjustViewBounds(true);

                        //then, add text veiw inside the table row
                        tableRow.addView(textViewDay, tableRowParams);
                        tableRow.addView(textViewDate, tableRowParams);
                        tableRow.addView(textViewTime, tableRowParams);
                        tableRow.addView(textViewName, tableRowParams);
                        tableRow.addView(imageButtonEdit);
                        tableRow.addView(imageButtonCancel);

                        //add the whole row to the table layout
                        tableLayout.addView(tableRow, tableLayoutParams);
                        //      }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            checkingMassage.setText("No upcoming reserved apppointments");
            return;
        }
    }

    //---------------------------------------------------------------------------------------
    public void createTableHeader() {
        //tableLayout = (TableLayout) findViewById(R.id.Schedule_Table_secretary);
        //set margins for table row & table layout
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams();
        tableRowParams.setMargins(15, 15, 15, 15);

        //create the table header
        TableRow tableRowHeader = new TableRow(this);
        tableRowHeader.setBackgroundColor(Color.parseColor("#64B5F6"));

        TextView day = new TextView(this);
        TextView date = new TextView(this);
        TextView time = new TextView(this);
        TextView pateintName = new TextView(this);

        day.setText("Day");
        date.setText("Date");
        time.setText("Time");
        pateintName.setText("Patient's Name");

        //set style for the text view (Bold)
        day.setTypeface(day.getTypeface(), Typeface.BOLD);
        date.setTypeface(date.getTypeface(), Typeface.BOLD);
        time.setTypeface(time.getTypeface(), Typeface.BOLD);
        pateintName.setTypeface(pateintName.getTypeface(), Typeface.BOLD);

        //set text color for text view
        day.setTextColor(Color.WHITE);
        date.setTextColor(Color.WHITE);
        time.setTextColor(Color.WHITE);
        pateintName.setTextColor(Color.WHITE);

        //then, add text view inside the table row
        tableRowHeader.addView(day, tableRowParams);
        tableRowHeader.addView(date, tableRowParams);
        tableRowHeader.addView(time, tableRowParams);
        tableRowHeader.addView(pateintName, tableRowParams);

        //add header to tableLayout
        tableLayout.addView(tableRowHeader, tableLayoutParams);

    }

    //---------------------------------------------------------------------------------------
    //get the specefic day by passing the date
    public String dayOfWeek(String date) {
        String day = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date scheduleDate = simpleDateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(scheduleDate);
            //get the day by an integer
            int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
            //get specific day by string
            switch (dayOfWeek) {
                case 1:
                    day = "Sunday";
                    break;
                case 2:
                    day = "Monday";
                    break;
                case 3:
                    day = "Tuesday";
                    break;
                case 4:
                    day = "Wednesday";
                    break;
                case 5:
                    day = "Thursday";
                    break;
                case 6:
                    day = "Friday";
                    break;
                case 7:
                    day = "Saturday";
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return day;
    }

    //----------------------------------------------------------------------------------------------
    private boolean checkInComingReservations() {
        JSONObject jsonObject = null;
        boolean check = false;
        for (int i = 0; i < result.length(); i++) {
            try {
                jsonObject = result.getJSONObject(i);
                //get the upcoming reservations
                String sch_date = jsonObject.getString("sch_date");
                String sch_time = jsonObject.getString("sch_time");
                String DateTime = sch_date + " " + sch_time;
                //use a specific format for date & time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                //get current date
                Calendar currentCalendar = Calendar.getInstance();
                //get appointment date & time in date format
                Date appointmentDate = simpleDateFormat.parse(DateTime);
                Calendar appointmentCalendar = Calendar.getInstance();
                appointmentCalendar.setTime(appointmentDate);
                //check if the appointment date & time after current date & time
                if (appointmentCalendar.after(currentCalendar)) {
                    check = true;
                    break;

                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return check;
    }

    //--------------------------------------------------------------------------------------------

    @Override
    public void onClick(View v) {

        JSONObject jsonObject = null;
        try {
            jsonObject = result.getJSONObject(v.getId());
            String selectedIcon = (String) v.getTag().toString();
            Intent intent;
            switch (selectedIcon) {

                case "change":
                    intent = new Intent(secretary_manage_appointments.this, change_appointment_date_time.class);
                    //appointment id
                    intent.putExtra("app_id", jsonObject.getString("app_id"));
                    //patient's ID
                    intent.putExtra("p_id", jsonObject.getString("p_id"));
                    //patient name
                    intent.putExtra("name", jsonObject.getString("name"));
                    //patient's appointment date
                    intent.putExtra("date", jsonObject.getString("sch_date"));
                    //patient's appointment time
                    intent.putExtra("time", jsonObject.getString("sch_time"));
                    //doctor id
                    intent.putExtra("dr_id", jsonObject.getString("dr_id"));
                    startActivity(intent);
                    finish();
                    break;

                case "cancel":
                    //appointment id
                    cancelDialogBox(jsonObject.getString("app_id"),
                            //patient name
                            jsonObject.getString("name"),
                            //patient's appointment date
                            jsonObject.getString("sch_date"),
                            //patient's appointment time
                            jsonObject.getString("sch_time"),
                            //patient's email
                            jsonObject.getString("email"));
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //---------------------------------------------------------------------------------------

    private void cancelDialogBox(final String app_id, final String name, final String date, final String time, final String email) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(secretary_manage_appointments.this);
        alertDialog.setTitle("Are sure to cancel this patient's appointment: ");
        alertDialog.setMessage("Patient's Name: " + name + "\n" + "Appointment's Date: " + date +
                "\n" + "Appointment's Time: " + time.substring(0, 5));
        alertDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        cancelAppointement(app_id);
                        sendEmail_CancelApp(email, name, date, time);
                    }

                });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    //----------------------------------------------------------------------------------
    private void cancelAppointement(final String app_id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_APPOINTMENT_CANCEL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("message").equalsIgnoreCase("Appointment is canceled successfully")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                loadingTheSchedule();
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
                params.put("app_id", app_id);
                return params;
            }
        };

        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //Adding request to the queue
        requestQueue.add(stringRequest);

    }
    //---------------------------------------------------------------------------------------

    public void sendEmail_CancelApp(String patientEmail, String patientName, String Date, String time) {
        // String patientEmail = "rahaf.fatouh@gmail.com";
        String subject = "Appointment Canceled";
        String message = "We're sorry to inform you that your appointment on " + Date + " at " + time + " has been canceled";
        //Creating SendMail object
        SendMail sendMail = new SendMail(this, "a95baubaid@gmail.com", subject, patientName, message);
        //Executing sendmail to send email
        sendMail.execute();
    }


}

