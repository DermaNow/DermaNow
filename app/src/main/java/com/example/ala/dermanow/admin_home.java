package com.example.ala.dermanow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class admin_home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);
        setTitle("Home");
        //------------------------------------------------------------------------------------------

        ImageButton verified_doctors = (ImageButton) findViewById(R.id.AdminHome_verifiedButton);
        verified_doctors.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(admin_home.this, admin_verified_doctors.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        ImageButton unverified_doctors = (ImageButton) findViewById(R.id.AdminHome_unverifiedbutton);
        unverified_doctors.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(admin_home.this, admin_unverified_doctors.class);
                startActivity(myIntent);
            }
        });
        //------------------------------------------------------------------------------------------
        ImageView logout = (ImageView) findViewById(R.id.logout_admin_button);
        logout.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                finish();
                Intent myIntent = new Intent(admin_home.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
