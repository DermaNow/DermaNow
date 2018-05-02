package com.example.ala.dermanow;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class doctorHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        setTitle("Home");

        //------------------------------------------------------------------------------------------
        ImageButton skin_diagnosis = (ImageButton) findViewById(R.id.DoctorHome_CamButton);
        skin_diagnosis.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(doctorHome.this, skinDiagnosis.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        ImageButton profile = (ImageButton) findViewById(R.id.DoctorHome_profileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(doctorHome.this, profile.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        ImageButton app = (ImageButton) findViewById(R.id.DoctorHome_manageApp_button);
        app.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(doctorHome.this, manage_appointments.class);
                startActivity(myIntent);
            }
        });
        //------------------------------------------------------------------------------------------
        ImageButton secretaryProfile = (ImageButton) findViewById(R.id.DoctorHome_view_secretary);
        secretaryProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(doctorHome.this, dr_home_secretary_profile.class);
                startActivity(myIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                startActivity(new Intent(doctorHome.this, MainActivity.class));
                break;
        }
        return true;
    }
    //---------------------------------------------------------------------------------------------
}

