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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class doctor_signup_info1 extends AppCompatActivity {


    public Toast toast;
    private Button btnCancel, btnNext;
    private String name, email, username, password;
    private EditText editTextDrName, editTextDrEmail, editTextDrUsername, editTextDrPassword;
    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup_info1);
        setTitle("Doctor registration request");
        //------------------------------------------------------------------------------------------

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnNext = (Button) findViewById(R.id.btn_next);

        editTextDrName = (EditText) findViewById(R.id.editText_dr_name);
        editTextDrEmail = (EditText) findViewById(R.id.editText_dr_email);
        editTextDrUsername = (EditText) findViewById(R.id.editText_dr_username);
        editTextDrPassword = (EditText) findViewById(R.id.editText_dr_password);

        //------------------------------------------------------------------------------------------
        //Toat Design
        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        //Textview
        TextView tv = new TextView(this);
        //TypeFace
        Typeface typeface = Typeface.create(Typeface.SERIF, Typeface.BOLD);
        tv.setTypeface(typeface);
        toast.setView(tv);
        //------------------------------------------------------------------------------------------

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(doctor_signup_info1.this, MainActivity.class);
                startActivity(myIntent);
            }
        });


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = editTextDrName.getText().toString();
                email = editTextDrEmail.getText().toString();
                username = editTextDrUsername.getText().toString();
                password = editTextDrPassword.getText().toString();
                final boolean error = ErrorMessage();
                if (error == false) {
                    //send data to the next intent
                    Intent intent = new Intent(doctor_signup_info1.this, doctor_signup_info2.class);
                    intent.putExtra("name", name);
                    intent.putExtra("email", email);
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    startActivity(intent);
                }
            }
        });

    }

    private boolean ErrorMessage() {
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
                errorMsg += " , User Name ";
            } else {
                errorMsg = "User Name";
            }
        }

        if (password.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Password";
            } else {
                errorMsg = "Password";
            }
        }

        if (errorMsg != "") {

            toast.makeText(getApplicationContext(), "Please fill " + errorMsg, Toast.LENGTH_LONG).show();
            return true;

        }
        //check the name if has alphanumeric or special characters
        if (!Pattern.matches("[a-zA-Z ]+", name)) {
            toast.makeText(doctor_signup_info1.this, "your name must contains alphabetic character only", Toast.LENGTH_LONG).show();
            return true;

        }
        //------------------------------------------------------------------------------------------
        //Error Message for email format
        String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            toast.makeText(doctor_signup_info1.this, "email has invalid format", Toast.LENGTH_LONG).show();
            return true;
        }
        //------------------------------------------------------------------------------------------
        //Error Message for password format
        if (password.length() <= 7) {
            toast.makeText(doctor_signup_info1.this, "password must be at least 8 charecter long", Toast.LENGTH_LONG).show();
            return true;

        }
        return false;

    }
}