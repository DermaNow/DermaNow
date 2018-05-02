//Package
package com.example.ala.dermanow;
/**
 * Created by Walaa Nogali on 20/02/2018.
 */
//Imports

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class profile extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------
    private TextView textViewUsername, textViewName, textViewUserEmail, textViewPhone, textViewUsertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //Setting Icon Code
        ImageView settings = (ImageView) findViewById(R.id.profile_settings_icon);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(profile.this, settings.class);
                startActivity(myIntent);
            }
        });
        //Getting All User Profile Information
        textViewUsertype = (TextView) findViewById(R.id.usertype_text);
        textViewUsername = (TextView) findViewById(R.id.profile_username_text);
        textViewName = (TextView) findViewById(R.id.TextView_profile_name);
        textViewUserEmail = (TextView) findViewById(R.id.TextView_profile_email);
        textViewPhone = (TextView) findViewById(R.id.TextView_profile_phone);
        //Getting User Type from sharedPrefManager Class Object
        textViewUsertype.setText(SharedPrefManager.getInstance(this).getUsertype().toUpperCase());
        //Getting Username from sharedPrefManager Class Object
        textViewName.setText(SharedPrefManager.getInstance(this).getName());
        //Getting Name from sharedPrefManager Class Object
        textViewUsername.setText(SharedPrefManager.getInstance(this).getUsername());
        //Getting Email from sharedPrefManager Class Object
        textViewUserEmail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        //Getting Phone from sharedPrefManager Class Object
        if (SharedPrefManager.getInstance(this).getUserPhone().startsWith("0")) {
            textViewPhone.setText(SharedPrefManager.getInstance(this).getUserPhone());
        } else {
            textViewPhone.setText("0" + SharedPrefManager.getInstance(this).getUserPhone());
        }

    }
    //----------------------------------------------------------------------------------------------
}