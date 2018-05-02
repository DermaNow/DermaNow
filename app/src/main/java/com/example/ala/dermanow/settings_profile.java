package com.example.ala.dermanow;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class settings_profile extends Fragment {

    private static final int GALLERY = 1;
    String TextTags = "None";
    //Variables for doing updating user profile information in the database
    EditText update_user_name, update_user_email, update_user_phone;
    String username, id, name, email, phone, userType;
    private CircleImageView imageView;
    //---------------------------------------------------------------------------------------------------------------------

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_profile, container, false);
        imageView = (CircleImageView) view.findViewById(R.id.settings_profile_pic);
        //------------------------------------------------------------------------------------------
        //for modifiying the user profile information
        update_user_name = (EditText) view.findViewById(R.id.editText_user_name);
        update_user_email = (EditText) view.findViewById(R.id.editText_user_email);
        update_user_phone = (EditText) view.findViewById(R.id.editText_user_phone);
        //------------------------------------------------------------------------------------------
        //Getting Username from sharedPrefManager Class Object
        update_user_name.setText(SharedPrefManager.getInstance(view.getContext()).getName());
        //Getting Email from sharedPrefManager Class Object
        update_user_email.setText(SharedPrefManager.getInstance(view.getContext()).getUserEmail());
        //Getting Phone from sharedPrefManager Class Object
        update_user_phone.setText("0" + SharedPrefManager.getInstance(view.getContext()).getUserPhone());
        //------------------------------------------------------------------------------------------
        //Declaring the confirmation button
        Button settings = (Button) view.findViewById(R.id.confirm_button);
        settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                name = update_user_name.getText().toString().trim();
                email = update_user_email.getText().toString().trim();
                phone = update_user_phone.getText().toString().trim();
                //Getting the username from sharedmemory of the user
                username = SharedPrefManager.getInstance(view.getContext()).getUsername();
                //Getting the id from sharedmemory of the user
                id = SharedPrefManager.getInstance(view.getContext()).getUserID();
                //Getting the userType from sharedmemory of the user
                //These if methods to return to users homepage depending on hid usertype
                userType = SharedPrefManager.getInstance(view.getContext()).getUsertype();
                updateUserProfile(view);
            }
        });

        //------------------------------------------------------------------------------------------
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.parse("package:" + getContext().getPackageName()));
            getActivity().finish();
            startActivity(intent);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            //checking the permission
            //if the permission is not given we will open setting to add permission
            //else app will not open

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });
        return view;
    }

    //----------------------------------------------------------------------------------------------------------------------------
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            //getting the image Uri
            Uri imageUri = data.getData();
            try {
                //getting bitmap object from uri
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);

                //displaying selected image to imageview
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //---------------------------------------------------------------------------------------------------------------------------
    public void updateUserProfile(final View view) {
        final ProgressDialog progressDialog = new ProgressDialog(view.getContext());
        //Check for null value in the editText object
        String errorMsg = "";
        if (name.equals("")) {
            errorMsg = "Name";
        }
        if (email.equals("")) {
            if (errorMsg != "") {
                errorMsg += ", Email";
            } else {
                errorMsg = "Email";
            }
        }
        if (phone.equals("")) {
            if (errorMsg != "") {
                errorMsg += " , Phone ";
            } else {
                errorMsg = "Phone";
            }
        }
        if (errorMsg != "") {
            Toast.makeText(view.getContext(), "Please fill " + errorMsg, Toast.LENGTH_LONG).show();
        }
        //------------------------------------------------------------------------------------------
        //check the name if has alphanumeric or special characters
        if (!Pattern.matches("[a-zA-Z ]+", name)) {
            Toast.makeText(view.getContext(), "Name must contains alphabetic character only", Toast.LENGTH_LONG).show();
            return;
        }
        //------------------------------------------------------------------------------------------
        //Error Message for email format
        String regex = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(view.getContext(), "email has invalid format", Toast.LENGTH_LONG).show();
            return;
        }
        //------------------------------------------------------------------------------------------
        //Error massage for phone
        if (phone.length() <= 9) {
            Toast.makeText(view.getContext(), "phone must be 10 digits long", Toast.LENGTH_LONG).show();
            return;
        }

        //-----------------------------------------------------------------------------------------------------
        //start loading the progress bar
        progressDialog.setMessage("Updating your profile...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Constants.URL_UPDATE_USER_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            //check if the msg is successfully or not
                            if (jsonObject.getString("message").equalsIgnoreCase("Your Profile Updated Successfully")) {
                                Toast.makeText(view.getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                                SharedPrefManager.getInstance(view.getContext()).updateProfileInformation(name, email, phone);
                                if (userType.equalsIgnoreCase("patient")) {
                                    startActivity(new Intent(view.getContext(), patientHome.class));
                                }
                                if (userType.equalsIgnoreCase("doctor")) {
                                    startActivity(new Intent(view.getContext(), doctorHome.class));
                                }
                                if (userType.equalsIgnoreCase("secretary")) {
                                    startActivity(new Intent(view.getContext(), SecretaryHome.class));
                                }
                            } else {
                                //to veiw any error massages
                                Toast.makeText(view.getContext(), jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    //--------------------------------------------------------------------------------
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(view.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                //store the secretary data to be send to server(Database)
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("id", id);
                params.put("name", name);
                params.put("email", email);
                params.put("phone", phone);
                return params;
            }
        };
        //starting the server Resquest
        RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
        requestQueue.add(stringRequest);
    }
}
