package com.nng.onedollar.manager.Request;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Jin on 8/10/2015.
 */
public class OneDollarJsonObjectRequest extends JsonObjectRequest {

    public OneDollarJsonObjectRequest(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);
        setRetryPolicy(getMyRetryPolice());
    }

    public OneDollarJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        setRetryPolicy(getMyRetryPolice());
    }


    public RetryPolicy getMyRetryPolice() {
        return new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 5000;//5s
            }

            @Override
            public int getCurrentRetryCount() {
                return 2;
            }

            @Override
            public void retry(VolleyError error){

            }
        };
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
         params.put("Content-Type", "application/json");
        return params;
    }
}
