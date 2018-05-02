package com.example.ala.dermanow;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
//Imports


public class settings_general extends Fragment {

    private static final String TAG = "General";

    //Setting Icon Code


    //---------------------------------------------------------------------------------------------------------------------
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_settings_general, container, false);
        //set buttons and view actions
        Button aboutUs = (Button) view.findViewById(R.id.Button_aboutUs);
        Button generalSittings = (Button) view.findViewById(R.id.Button_general_sittings);

        //---------------------------------------------------------------------------------------------------------------------
        //Set about us button action
        aboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), about_us.class);
                startActivity(myIntent);
            }
        });
        //---------------------------------------------------------------------------------------------------------------------
        //imageView_dr_logout
        Button logout = (Button) view.findViewById(R.id.signout_button);
        logout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                SharedPrefManager.getInstance(getActivity()).logout();
                Intent myIntent = new Intent(getActivity(), MainActivity.class);
                startActivity(myIntent);
            }
        });
        //---------------------------------------------------------------------------------------------------------------------
        //Set General Information App button action
        generalSittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + getContext().getPackageName()));
                getActivity().finish();
                startActivity(intent);
            }
        });
        //------------------------------------------------------------------------------------------
        return view;
    }


}
