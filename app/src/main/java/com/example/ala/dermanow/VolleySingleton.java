package com.example.ala.dermanow;

/**
 * Created by Walaa Nogali on 05/02/2018.
 */

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    //----------------------------------------------------------------------------------------------
    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }

    //----------------------------------------------------------------------------------------------

    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }
        return mInstance;
    }

    //----------------------------------------------------------------------------------------------

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    //----------------------------------------------------------------------------------------------
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
