package com.example.ala.dermanow;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ScrollView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import dagger.android.support.AndroidSupportInjection;

/**
 * Created by Rahaf on 3/11/2018.
 */

public class Day extends Fragment implements View.OnClickListener {

    //Day shift
    final String shift = "day";
    LinearLayout linearLayout;
    Button confirm;
    Button reset;
    CheckBox checkBox;
    ArrayList<String> workHours;
    String massage = "";
    ProgressDialog progressDialog;
    boolean flag;
    String username = "";

    //----------------------------------------------------------------------------------------------------


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.day, container, false);

        linearLayout = (LinearLayout) root.findViewById(R.id.linearLayout_day);
        confirm = (Button) root.findViewById(R.id.confirm_button_day);
        confirm.setOnClickListener(this);
        reset = (Button) root.findViewById(R.id.reset_button_day);
        reset.setOnClickListener(this);
        progressDialog = new ProgressDialog(getContext());
        username = SharedPrefManager.getInstance(getContext()).getUsername();
//----------------------------------------------------------------------------------------------------
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String startTime = "08:00:00";
        String endTime = "12:00:00";
        Calendar cal = Calendar.getInstance();

        int minutes = 20;
        int count = 0;

        do {
            String curr = startTime + " - ";

            try {
                java.util.Date date = simpleDateFormat.parse(startTime);
                cal.setTime(date);
                cal.add(Calendar.MINUTE, minutes);
                startTime = simpleDateFormat.format(cal.getTime());
                curr += startTime;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            checkBox = new CheckBox(getContext());
            Typeface t = Typeface.create(Typeface.SERIF, Typeface.BOLD);
            checkBox.setTypeface(t);
            checkBox.setTextColor(Color.BLACK);
            checkBox.setText(curr);
            checkBox.setId(count);
            checkBox.setPadding(70, 40, 70, 0);
            String index = String.valueOf(count);
            String key = shift + index;
            checkBox.setChecked(SharedPrefManager.getInstance(getContext()).getState(key));
            linearLayout.addView(checkBox);
            count++;
        } while (!endTime.equalsIgnoreCase(startTime));


        return root;
    }

    //-----------------------------------------------------------------------------------------------

    @Override
    public void onClick(View view) {
        if (view == confirm) {
            progressDialog.setMessage("Creating Schedule...");
            progressDialog.show();
            delete();
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    boolean checkSchedule = store(linearLayout);
                    if (checkSchedule) {
                        checkToInsert();
                    } else {
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "please, fill your schedule or it will be empty", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            }, 6000);

        }

        if (view == reset) {
            resetAlert();
        }
    }
    //----------------------------------------------------------------------------------------------

    private boolean store(ViewGroup parent) {
        workHours = new ArrayList<String>();
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox cb = (CheckBox) child;
                boolean choice = cb.isChecked();
                String index = String.valueOf(i);
                String key = shift + index;
                SharedPrefManager.getInstance(getContext()).saveState(key, choice);
                String time = cb.getText().toString().substring(0, 9);
                if (choice)
                    workHours.add(time);
            }
        }
        if (workHours.size() != 0) {
            return true;
        }
        return false;

    }
    //---------------------------------------------------------------------------------------------

    public void checkToInsert() {
        flag = true;
        for (int count = 0; count < workHours.size(); count++) {
            insert(shift, workHours.get(count), username);
        }
    }
    //---------------------------------------------------------------------------------------------

    public void insert(final String shift, final String hour, final String dr_username) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_DOCTOR_WORKING_HOURS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            massage = jsonObject.getString("message");
                            if (flag) {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), massage, Toast.LENGTH_LONG).show();
                                flag = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", dr_username);
                params.put("hour", hour);
                params.put("shift", shift);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

//---------------------------------------------------------------------------------------------

    public boolean delete() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_CLEAR_SCHEDULE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("shift", shift);
                return params;
            }
        };
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        //Adding request to the queue
        requestQueue.add(stringRequest);

        return true;
    }

    //------------------------------------------------------------------------------------------

    public void resetSchedule(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);
            if (child instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) child;
                checkBox.setChecked(false);
                String index = String.valueOf(i);
                String key = shift + index;
                SharedPrefManager.getInstance(getContext()).clearSchedule(key);
            }
        }
    }

    //------------------------------------------------------------------------------------------

    public void resetAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage("your schedule will be reset");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                resetSchedule(linearLayout);
                delete();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialog.create();
        dialog.show();
    }
//-------------------------------------------------------------------------------------------------

}

