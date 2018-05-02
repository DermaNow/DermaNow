package com.example.ala.dermanow;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class settings extends AppCompatActivity {


    private static final String TAG = "Settings";
    private SectionsPageAdapter mSectionsPageAdapter;
    private ViewPager mViewPager;

    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuItem menuItem = menu.add(Menu.NONE, 1000, Menu.NONE, "Done");
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String UserType = SharedPrefManager.getInstance(this).getUsertype();
        if (UserType.equalsIgnoreCase("patient")) {
            finish();
            startActivity(new Intent(this, patientHome.class));
            return true;
        }

        if (UserType.equalsIgnoreCase("doctor")) {
            finish();
            startActivity(new Intent(this, doctorHome.class));
            return true;
        }
        if (UserType.equalsIgnoreCase("secretary")) {
            finish();
            startActivity(new Intent(this, SecretaryHome.class));
            return true;
        }

        return false;
    }

    //----------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");
        Log.d(TAG, "onCreate: Starting.");

        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Settings");
        getSupportActionBar().setDisplayShowTitleEnabled(true);


    }

    //----------------------------------------------------------------------------------------------

    private void setupViewPager(ViewPager viewPager) {
        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        //set fragments to settings page
        adapter.addFragment(new settings_profile(), "Profile");
        adapter.addFragment(new settings_general(), "General");
        viewPager.setAdapter(adapter);
    }

}

