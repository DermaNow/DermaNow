package com.example.ala.dermanow;

/**
 * Created by Walaa Nogali on 20/02/2018.
 */

//SINGLETON CLASS
//--------------------------------------------------------------------------------------------------
//All class imports needed

import android.content.Context;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

//--------------------------------------------------------------------------------------------------
//Starting the Class :
//Overview : This Class is for storing the user data in android application that are gotten from database when user logged in
public class SharedPrefManager {

    private static final String SHARED_PREF_NAME = "mysharedpref12";
    //Common Attribute for doctor , patient , secretary
    private static final String KEY_USER_TYPE = "generalUser";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_USER_ID = "userid";
    private static final String KEY_USER_NAME = "userrealname";
    private static final String KEY_USER_EMAIL = "useremail";
    private static final String KEY_USER_PHONE = "userphone";
    //Unique for PATIENT
    private static final String KEY_USER_SEX = "usersex";
    private static final String KEY_USER_MARITAL_STATUS = "usermaritalstatus";
    //Unique for DOCTOR
    private static final String KEY_USER_CLINIC_NAME = "userclinicname";
    private static final String KEY_USER_MAJOR = "usermajor";
    private static final String KEY_USER_DEGREE = "userdegree";
    //doctor secretary information
    private static final String KEY_SECRETAERY_USERNAME = "sec_username";
    private static final String KEY_SECRETARY_USER_ID = "sec_userid";
    private static final String KEY_SECRETARY_USER_NAME = "sec_userrealname";
    private static final String KEY_SECRETARY_USER_EMAIL = "sec_useremail";
    private static final String KEY_SECRETARY_USER_PHONE = "sec_userphone";
    private static final String KEY_SECRETARY_DR_ID = "sec_drid";
    private static SharedPrefManager mInstance;
    private static Context mCtx;

    //----------------------------------------------------------------------------------------------

    //----------------------------------------------------------------------------------------------
    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    //----------------------------------------------------------------------------------------------
    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //----------------------------------------------------------------------------------------------
    public boolean doctorLogin(String id, String username, String name, String clinicName, String major, String degree, String email, String phone) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //These all data will be gittent from database and saved in the androids cash
        editor.putString(KEY_USER_TYPE, "doctor");
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_CLINIC_NAME, clinicName);
        editor.putString(KEY_USER_MAJOR, major);
        editor.putString(KEY_USER_DEGREE, degree);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);

        editor.apply();

        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean patientLogin(String id, String username, String name, String sex, String maritalStatus, String email, String phone) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //These all data will be gittent from database and saved in the androids cash
        editor.putString(KEY_USER_TYPE, "patient");
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_SEX, sex);
        editor.putString(KEY_USER_MARITAL_STATUS, maritalStatus);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);

        editor.apply();

        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean secretaryLogin(String id, String username, String name, String email, String phone, String dr_id) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //These all data will be gittent from database and saved in the androids cash
        editor.putString(KEY_USER_TYPE, "secretary");
        editor.putString(KEY_USER_ID, id);
        editor.putString(KEY_USERNAME, username);
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.putString(KEY_SECRETARY_DR_ID, dr_id);

        editor.apply();

        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean drHomeSecretaryProfile(String sec_id, String sec_username, String sec_name, String sec_email, String sec_phone) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //These all data will be gittent from database and saved in the androids cash
        editor.putString(KEY_SECRETARY_USER_ID, sec_id);
        editor.putString(KEY_SECRETAERY_USERNAME, sec_username);
        editor.putString(KEY_SECRETARY_USER_NAME, sec_name);
        editor.putString(KEY_SECRETARY_USER_EMAIL, sec_email);
        editor.putString(KEY_SECRETARY_USER_PHONE, sec_phone);
        editor.apply();
        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean updateProfileInformation(String name, String email, String phone) {

        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //These all data will be gittent from database and saved in the androids cash
        editor.putString(KEY_USER_NAME, name);
        editor.putString(KEY_USER_EMAIL, email);
        editor.putString(KEY_USER_PHONE, phone);
        editor.apply();
        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        if (sharedPreferences.getString(KEY_USERNAME, null) != null) {
            return true;
        }
        return false;
    }

    //----------------------------------------------------------------------------------------------
    //Logout Method -- let the user logout by clossing the session
    public boolean logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        return true;
    }

    //----------------------------------------------------------------------------------------------
    public String getUsertype() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_TYPE, null);
    }

    //----------------------------------------------------------------------------------------------
    //This list of methods are used in getting the current user profile
    public String getUsername() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERNAME, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_NAME, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getUserID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getUserEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_EMAIL, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getUserPhone() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USER_PHONE, null);
    }

    //----------------------------------------------------------------------------------------------
    //This list of method are used in getting the secretary profile to the doctor home page
    public String getSecretaryUsername() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SECRETAERY_USERNAME, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getSecretaryName() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SECRETARY_USER_NAME, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getSecretaryUserID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SECRETARY_USER_ID, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getSecretaryUserEmail() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SECRETARY_USER_EMAIL, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getSecretaryUserPhone() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SECRETARY_USER_PHONE, null);
    }

    //----------------------------------------------------------------------------------------------
    public String getSecretaryDrID() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_SECRETARY_DR_ID, null);
    }

    //----------------------------------------------------------------------------------------------
    public boolean saveState(String key, boolean checked) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, checked);
        editor.apply();
        return true;
    }

    //----------------------------------------------------------------------------------------------
    public boolean getState(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    //----------------------------------------------------------------------------------------------
    public boolean clearSchedule(String key) {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key).apply();
        return true;
    }
    //----------------------------------------------------------------------------------------------
}