package com.nng.onedollar.manager.User;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.nng.onedollar.R;
import com.nng.onedollar.application.OneDollarApplication;
import com.nng.onedollar.manager.Request.OneDollarJsonObjectRequest;
import com.nng.onedollar.manager.Request.SignUpRequest;
import com.nng.onedollar.manager.Response.SignUpResponse;
import com.nng.onedollar.manager.SignUpResponseListener;
import com.nng.onedollar.manager.WebServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jin on 8/7/2015.
 */
public class UserManager {
    private static String TAG_USER = "/users";
    private static UserManager instance;

    public static UserManager shareInstance() {
        if (instance == null) {
            instance = new UserManager();
        }
        return instance;
    }

    public void signUpWithEmail(SignUpRequest signUpRequest, String tagRequest, final SignUpResponseListener signUpResponseListener) {
        String urlSignUp = OneDollarApplication.getInstance().getString(R.string.api_domain) + TAG_USER + "/signup/";

        try {
            //TODO : Try to use OneDollarJsonObjectRequest
            OneDollarJsonObjectRequest jsonObjectRequest = new OneDollarJsonObjectRequest(Request.Method.POST, urlSignUp,signUpRequest.initRequest(), new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("nhatdo", "signUpWithEmail onResponse " + response.toString());
                    if (response != null) {
                        Gson gson = new Gson();
                        SignUpResponse signUpResponse = gson.fromJson(response.toString(), SignUpResponse.class);
                        signUpResponse.setError(null);
                        signUpResponseListener.didSignUpResponse(signUpResponse);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("nhatdo", "signUpWithEmail onErrorResponse " + new String(error.networkResponse.data));
                    SignUpResponse signUpResponse = new SignUpResponse();
                    if(error.networkResponse.data!=null){
                        signUpResponse.setError(new String(error.networkResponse.data));
                    }else{
                        signUpResponse.setError(OneDollarApplication.getInstance().getString(R.string.msg_err_sign_up_fail));
                    }

                    signUpResponseListener.didSignUpResponse(signUpResponse);
                }
            }) {

            };
            jsonObjectRequest.setTag(tagRequest);
            WebServiceManager.shareInstance(OneDollarApplication.getInstance().getApplicationContext()).addRequest(jsonObjectRequest);
        }catch(JSONException e){
            Log.e("nhatdo", "signUpWithEmail error " + e.getLocalizedMessage()+" - "+e.getMessage());
        }



    }

}
