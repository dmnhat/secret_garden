package com.nng.onedollar.manager;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Jin on 8/7/2015.
 */
public interface SignUpResponseListener{
    public void didSignUpResponse(BaseResponse baseResponse);

}
