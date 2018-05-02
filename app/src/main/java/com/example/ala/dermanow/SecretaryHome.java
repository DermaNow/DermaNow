package com.example.ala.dermanow;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class SecretaryHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secretary_home);
        setTitle("Home");

        //------------------------------------------------------------------------------------------

        ImageButton profile = (ImageButton) findViewById(R.id.secretaryHome_profileButton);
        profile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(SecretaryHome.this, profile.class);
                startActivity(myIntent);
            }
        });

        //------------------------------------------------------------------------------------------

        ImageButton app = (ImageButton) findViewById(R.id.secretaryHome_manageApp_button);
        app.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(SecretaryHome.this, secretary_manage_appointments.class);
                startActivity(myIntent);
            }
        });
        //------------------------------------------------------------------------------------------
        //imageView_Secretary_logout
//        ImageView logout = (ImageView) findViewById(R.id.logout_secretary_button);
//        logout.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View view) {
//                SharedPrefManager.getInstance(getApplicationContext()).logout();
//                finish();
//                Intent myIntent = new Intent(SecretaryHome.this, MainActivity.class);
//                startActivity(myIntent);
//            }
//        });
        //------------------------------------------------------------------------------------------

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
                startActivity(new Intent(SecretaryHome.this, MainActivity.class));
                break;
        }
        return true;
    }
}
