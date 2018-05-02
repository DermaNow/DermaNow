package com.example.ala.dermanow;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class doctor_signup_info2 extends AppCompatActivity {
    private Button btnCancel, btnNext;
    private String name, email, username, password, major, degree, ssn, phone;
    private EditText editTextDrSSN, editTextDrPhone, editTextDrMajor, editTextDrDegree;
    private Toast toast;
    //------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup_info2);
        setTitle("Doctor registration request");

        //------------------------------------------------------------------------------------------

        //get data from previous intent
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");

        //------------------------------------------------------------------------------------------

        editTextDrSSN = (EditText) findViewById(R.id.editText_dr_ssn);
        editTextDrPhone = (EditText) findViewById(R.id.editText_dr_phone);
        editTextDrMajor = (EditText) findViewById(R.id.editText_dr_major);
        editTextDrDegree = (EditText) findViewById(R.id.editText_dr_degree);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnNext = (Button) findViewById(R.id.btn_next);
        //------------------------------------------------------------------------------------------
        //TOAST
        TextView toastView = new TextView(getApplicationContext());
        toastView.setTypeface(Typeface.SERIF);

        //Toat Design
        toast = new Toast(getApplicationContext());
        toast.setView(toastView);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        //------------------------------------------------------------------------------------------


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(doctor_signup_info2.this, MainActivity.class);
                startActivity(myIntent);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ssn = editTextDrSSN.getText().toString();
                phone = editTextDrPhone.getText().toString();
                major = editTextDrMajor.getText().toString();
                degree = editTextDrDegree.getText().toString();
                final boolean error = ErrorMessages();
                if (error == false) {
                    //send data to the next intent
                    Intent intent = new Intent(doctor_signup_info2.this, doctor_signup_location.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);

                    intent.putExtra("ssn", ssn);
                    intent.putExtra("phone", phone);
                    intent.putExtra("major", major);
                    intent.putExtra("degree", degree);

                    startActivity(intent);
                }
            }
        });

    }

    private boolean ErrorMessages() {
        String errorMsg = "";
        if (ssn.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", SSN";
            } else {
                errorMsg = "SSN";
            }
        }

        if (major.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Major";
            } else {
                errorMsg = "Major";
            }
        }

        if (degree.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Degree";
            } else {
                errorMsg = "Degree";
            }
        }

        if (phone.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Phone";
            } else {
                errorMsg = "Phone";
            }
        }

        if (errorMsg != "") {
            Toast.makeText(getApplicationContext(), "Please fill " + errorMsg, Toast.LENGTH_LONG).show();
            return true;
        }

        //------------------------------------------------------------------------------------------
        //Error Message for phone format
        if (phone.length() <= 9) {
            Toast.makeText(doctor_signup_info2.this, "phone must be 10 digits", Toast.LENGTH_LONG).show();
            return true;
        }
        //------------------------------------------------------------------------------------------
        //Error Message for phone format
        if (ssn.length() <= 9) {
            Toast.makeText(doctor_signup_info2.this, "SSN must be 10 digits", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

}
