package com.nng.onedollar.manager;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.nng.onedollar.application.OneDollarApplication;
import com.nng.onedollar.manager.Request.SignUpRequest;
import com.nng.onedollar.utils.AppConstant;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jin on 8/6/2015.
 */
public class WebServiceManager {
    public static final String kAccessTokenKey = "access_token";
    private static final int CONNECT_TIMEOUT = 30 * 1000;     // 30s
    private static final int READ_TIMEOUT = 30 * 1000;     // 30s
    private Context mActivity;
    private static WebServiceManager mInstance;
    private static RequestQueue requestQueue = null;

    private WebServiceManager() {
        initResAdapterRetrofit();
    }


    private void initResAdapterRetrofit() {
        if (requestQueue == null) {
            // Instantiate the cache
           //   Cache cache = new DiskBasedCache(OneDollarApplication.getInstance().getApplicationContext().getCacheDir(), 1024 * 1024); // 1MB cap
            //   Set up the network to use HttpURLConnection as the HTTP client.
            //   Network network = new BasicNetwork(new HurlStack());
            //  Instantiate the RequestQueue with the cache and network.
            //  requestQueue = new RequestQueue(cache, network);  requestQueue.start();
            requestQueue = Volley.newRequestQueue(OneDollarApplication.getInstance().getApplicationContext());
        }

    }

    public WebServiceManager(Context activity) {
        initResAdapterRetrofit();
        this.mActivity = activity;
    }

    public static WebServiceManager shareInstance(Context activity) {
        if (mInstance == null) {
            mInstance = new WebServiceManager();
        }
        mInstance.initResAdapterRetrofit();
        mInstance.setActivity(activity);
        return mInstance;
    }

    public void addRequest(JsonObjectRequest jsonObjectRequest) {
        if (jsonObjectRequest != null) {
            requestQueue.add(jsonObjectRequest);
        }
    }

    public void removeRequest(String tag) {
        requestQueue.cancelAll(tag);
    }

    public static WebServiceManager shareInstance() {
        if (mInstance == null) {
            mInstance = new WebServiceManager();
        }
        mInstance.initResAdapterRetrofit();
        mInstance.setActivity(null);
        return mInstance;
    }

    private void setActivity(Context activity) {
        this.mActivity = activity;
    }

    public void deAlloc() {

    }

}
