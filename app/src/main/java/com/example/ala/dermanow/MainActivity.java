package com.example.ala.dermanow;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    AlertDialog alertDialog1;
    CharSequence[] values = {" Doctor ", " Other"};

    //------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button skin_diagnosis = (Button) findViewById(R.id.main_skinDiagnosis_button);
        Button Sign_in = (Button) findViewById(R.id.main_signin_button);
        Button Sign_up = (Button) findViewById(R.id.main_signup_button);
        //--------------------------------------------------------------------------------------
        skin_diagnosis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, skinDiagnosis.class);
                startActivity(myIntent);
            }
        });

        Sign_in.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, Signin.class);
                startActivity(myIntent);
            }
        });

        Sign_up.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                DialogWithRadioButtonGroup();
            }
        });


    }

    //--------------------------------------------------------------------------------------

    //Create Job Dialog.
    public void DialogWithRadioButtonGroup() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Please Select Your Job:");
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0: // if user is doctor
                        Intent myIntent = new Intent(MainActivity.this, doctor_signup_info1.class);
                        startActivity(myIntent);
                        break;
                    case 1: // if user is patient
                        Intent myIntent2 = new Intent(MainActivity.this, patient_signup.class);
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
