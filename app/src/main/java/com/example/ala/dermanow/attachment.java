package com.example.ala.dermanow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class attachment extends AppCompatActivity implements View.OnClickListener {

//attachment class

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;
    public int i = 0;
    Uri[] files = new Uri[2];
    String[] uploadURL = new String[2];
    private String name, email, username, password, major, degree, clinicName, ssn, phone, latitude, Longitude, district, city, street, country;
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;
    //Uri to store the file uri
    private Uri filePath;
    private ProgressDialog progressDialog;
    private Button buttonBrowse1, buttonBrowse2, btnCancel, btnNext;
    private EditText clinic;
    private EditText job;
    private String doctor_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_signup_attachment);
        setTitle("Attachment");

        //get All information from the previous intents
        name = getIntent().getStringExtra("name");
        email = getIntent().getStringExtra("email");
        username = getIntent().getStringExtra("username");
        password = getIntent().getStringExtra("password");
        ssn = getIntent().getStringExtra("ssn");
        major = getIntent().getStringExtra("major");
        degree = getIntent().getStringExtra("degree");
        clinicName = getIntent().getStringExtra("clinicName");
        phone = getIntent().getStringExtra("phone");

        latitude = getIntent().getStringExtra("latitude");
        Longitude = getIntent().getStringExtra("longitude");

        district = getIntent().getStringExtra("district");
        city = getIntent().getStringExtra("city");
        street = getIntent().getStringExtra("street");
        country = getIntent().getStringExtra("country");


        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnNext = (Button) findViewById(R.id.btn_next);

        uploadURL[0] = Constants.UPLOAD_URL1;
        uploadURL[1] = Constants.UPLOAD_URL2;


        //permission to access the user files
        requestStoragePermission();
        //------------------------------------------------------------------------------------------
        //ProgressDialog
        progressDialog = new ProgressDialog(this);
        clinic = (EditText) findViewById(R.id.editText_clinicLicense);
        job = (EditText) findViewById(R.id.editText_jobLicense);

        //------------------------------------------------------------------------------------------
        //Button OnclickListner

        buttonBrowse1 = (Button) findViewById(R.id.button_browse_clinicLicense);
        buttonBrowse1.setOnClickListener(this);
        buttonBrowse2 = (Button) findViewById(R.id.button_browse_jobLicense);
        buttonBrowse2.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCancel.setOnClickListener(this);


    }

    private void drSignup() {
        SharedPrefManager.getInstance(getApplicationContext()).logout();
        doctor_username = username;
        //User State Message while registering
        progressDialog.setMessage("Registering doctor...");
        progressDialog.show();
        //------------------------------------------------------------------------------------------
        //Create Request Using Volly Library
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_DR_SIGNUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            startActivity(new Intent(attachment.this, MainActivity.class));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            //--------------------------------------------------------------------------------------
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dr_ssn", ssn);
                params.put("name", name);
                params.put("clinic_name", clinicName);
                params.put("latitude", latitude);
                params.put("longitude", Longitude);
                params.put("major", major);
                params.put("degree", degree);
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("phone", phone);
                return params;
            }
        };
        //------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //----------------------------------------------------------------------------------------------
    //OnClick Button Method
    @Override
    public void onClick(View view) {
        if (view == btnNext) {
            drSignup();
            uploadMultipart();
            // call method to save clinic address in database
            clinicAddress();
        }
        if (view == buttonBrowse1) {
            showFileChooser();
        }
        if (view == buttonBrowse2) {
            i++;
            showFileChooser();
        }
        if (view == btnCancel) {
            Intent myIntent = new Intent(attachment.this, MainActivity.class);
            startActivity(myIntent);
        }

    }

    //----------------------------------------------------------------------------------------------
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PICK_PDF_REQUEST);


    }

    //----------------------------------------------------------------------------------------------
    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            files[i] = filePath;
            if (i == 0) {
                String path = FilePath.getPath(this, files[i]);
                clinic.setText(path);
            } else {
                String path = FilePath.getPath(this, files[i]);
                job.setText(path);
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    public void uploadMultipart() {
        //getting name for the image
        for (int x = 0; x < files.length; x++) {
            //getting the actual path of the image
            String path = FilePath.getPath(this, files[x]);

            if (path == null) {

                Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
            } else {
                //Uploading pdf files
                try {
                    String uploadId = UUID.randomUUID().toString();

                    //Creating a multi part request
                    new MultipartUploadRequest(this, uploadId, uploadURL[x])
                            .addFileToUpload(path, "pdf") //Adding file
                            .addParameter("name", doctor_username) //Adding text parameter to the request
                            .setNotificationConfig(new UploadNotificationConfig())
                            .setMaxRetries(4)
                            .startUpload(); //Starting the upload

                } catch (Exception exc) {
                    Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    //----------------------------------------------------------------------------------------------
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //----------------------------------------------------------------------------------------------
    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    //----------------------------------------------------------------------------------------------
    public void clinicAddress() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_ADD_CLINIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            //--------------------------------------------------------------------------------------
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("clinic", clinicName);
                params.put("dist", district);
                params.put("street", street);
                params.put("city", city);
                params.put("country", country);
                return params;
            }
        };
        //------------------------------------------------------------------------------------------
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

