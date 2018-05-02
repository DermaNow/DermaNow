package com.example.ala.dermanow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class skinDiagnosis extends AppCompatActivity implements View.OnClickListener {
    private static final int RESULT_UPLOAD_IMAGE = 1;
    private static final int RESULT_REAL_IMAGE = 2;
    private static final String IMAGE_URL = "http://localhost/Android/diseasePictures/";
    private ImageView imageHolder;
    private ProgressBar spinner;
    private String usertype = "GENERALUSER";
    private String username;


    //------------------------------------------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skin_diagnosis);
        setTitle("Skin Diagnosis");
        //------------------------------------------------------------------------------------------------------------------
        imageHolder = (ImageView) findViewById(R.id.captured_photo);
        spinner = (ProgressBar) findViewById(R.id.skinDiagnosis_progressBar);
        Button capturedImageButton = (Button) findViewById(R.id.Takephoto_button);
        Button uploadImage = (Button) findViewById(R.id.uploadPhoto_button);
        //------------------------------------------------------------------------------------------------------------------
        spinner.getIndeterminateDrawable().setColorFilter(0xffcccccc, android.graphics.PorterDuff.Mode.MULTIPLY);
        spinner.setVisibility(View.GONE);
        usertype = SharedPrefManager.getInstance(this).getUsertype();
        username = SharedPrefManager.getInstance(this).getUsername();
        //Toast.makeText( getApplicationContext(), usertype ,Toast.LENGTH_LONG ).show();
        capturedImageButton.setOnClickListener(this);
        uploadImage.setOnClickListener(this);

        //------------------------------------------------------------------------------------------------------------------
    }

    //ON CLICK
    @Override
    public void onClick(View v) {
        //REAL TIME SHOT BUTTON CASE
        if (v.getId() == R.id.Takephoto_button) {
            //REAL TIME SHOT BUTTON CASE
            //to get the real time image captured
            Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(photoCaptureIntent, RESULT_REAL_IMAGE);
        }
        //UPLOADING BUTTON CASE
        if (v.getId() == R.id.uploadPhoto_button) {
            //To got the gallery of the phone
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, RESULT_UPLOAD_IMAGE);
        }
        //---------------------------------------------------------------------------------------------------------------------
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i ;
                if(usertype.equalsIgnoreCase( "doctor" )){
                    i = new Intent(skinDiagnosis.this, Diagnosis_doctor.class);}
                else if (usertype.equalsIgnoreCase( "patient")){
                    i = new Intent(skinDiagnosis.this, Diagnosis_patient.class);
                }else{
                    i = new Intent(skinDiagnosis.this, Diagnosis_general_user.class);
                }
                startActivity(i);
                finish();
            }
        }, TIME_OUT);*/
        spinner.setVisibility(View.VISIBLE);
    }

    //--------------------------------------------------------------------------------------------------------------------
    public void run() {
        Intent i;
        if (usertype.equalsIgnoreCase("doctor")) {
            i = new Intent(skinDiagnosis.this, Diagnosis_doctor.class);
        } else if (usertype.equalsIgnoreCase("patient")) {
            i = new Intent(skinDiagnosis.this, Diagnosis_patient.class);
        } else {
            i = new Intent(skinDiagnosis.this, Diagnosis_general_user.class);
        }
        startActivity(i);
        finish();
    }

    //----------------------------------------------------------------------------------------------------------------
    //upload button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ON THE EMULATER
        //make sure that we receive an image from gallary
        if (requestCode == RESULT_UPLOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            //showen the path of the image selected from gallary
            Uri selectedImage = data.getData();
            imageHolder.setImageURI(selectedImage);
            Bitmap image = ((BitmapDrawable) imageHolder.getDrawable()).getBitmap();
            new UploadImage(image, SharedPrefManager.getInstance(this).getUsername()).execute();
            uploadImageUrl();
            final Intent i;
            if (usertype.equalsIgnoreCase("doctor")) {
                i = new Intent(skinDiagnosis.this, Diagnosis_doctor.class);
            } else if (usertype.equalsIgnoreCase("patient")) {
                i = new Intent(skinDiagnosis.this, Diagnosis_patient.class);
            } else {
                i = new Intent(skinDiagnosis.this, Diagnosis_general_user.class);
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                            finish();

                        }
                    });

                }
            }, 20000);
//            startActivity(i);
//            finish();
            //----------------------------------------------------------------------------------
            //ON THE SAME MOM
        }
        if (requestCode == RESULT_REAL_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageHolder.setImageBitmap(bitmap);
            Bitmap image = ((BitmapDrawable) imageHolder.getDrawable()).getBitmap();
            new UploadImage(image, SharedPrefManager.getInstance(this).getUsername()).execute();
            uploadImageUrl();
            final Intent i;
            if (usertype.equalsIgnoreCase("doctor")) {
                i = new Intent(skinDiagnosis.this, Diagnosis_doctor.class);
            } else if (usertype.equalsIgnoreCase("patient")) {
                i = new Intent(skinDiagnosis.this, Diagnosis_patient.class);
            } else {
                i = new Intent(skinDiagnosis.this, Diagnosis_general_user.class);
            }

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(i);
                            finish();

                        }
                    });

                }
            }, 20000);
//            startActivity(i);
//            finish();
        }
    }

    //------------------------------------------------------------------------------------------
    public void uploadImageUrl() {
        //UPLOADING IMAGE URL TO DATABASE
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.UPLOUD_SKIN_IMAGE_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //check if the msg is successfully or not
                            if (jsonObject.getString("message").equalsIgnoreCase("Uploaded url successfully")) {
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            } else {
                                //to veiw any error massages
                                Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //--------------------------------------------------------------------------------
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //store the secretary data to be send to server(Database)
                Map<String, String> params = new HashMap<>();
                params.put("img_url", IMAGE_URL + username);
                return params;
            }
        };
        //starting the server Resquest
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private HttpParams getHttpRequestParams() {
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, 100 * 30);
        HttpConnectionParams.setSoTimeout(httpRequestParams, 1000 * 30);
        return httpRequestParams;
    }

    //-----------------------------------------------------------------------------------------------------------
    //NEW CLASS FOR UPLOADING
    private class UploadImage extends AsyncTask<Void, Void, Void> {
        Bitmap image;
        String imageName;

        public UploadImage(Bitmap image, String imageName) {
            this.image = image;
            this.imageName = imageName;
        }

        //------------------------------------------------------------------------------------------
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
        }

        //------------------------------------------------------------------------------------------
        @Override
        protected Void doInBackground(Void... voids) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

            ArrayList<NameValuePair> dataToSend = new ArrayList<>();
            dataToSend.add(new BasicNameValuePair("image", encodedImage));
            dataToSend.add(new BasicNameValuePair("name", imageName));

            HttpParams httpRequestParams = getHttpRequestParams();

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(Constants.UPLOUD_SKIN_IMAGE);

            try {
                post.setEntity(new UrlEncodedFormEntity(dataToSend));
                client.execute(post);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
        //------------------------------------------------------------------------------------------
    }//END OF UPLOAD CLASS
    //-----------------------------------------------------------------------------------------------
}