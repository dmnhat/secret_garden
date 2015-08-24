package com.nng.onedollar.manager.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Jin on 8/7/2015.
 */
public class SignUpRequest extends JSONObject {
    @SerializedName("username")
    public String username;
    @SerializedName("country")
    public String country;
    @SerializedName("email")
    public String email;
    @SerializedName("password")
    public String password;
    @SerializedName("avatar")
    public String avatar;
    @SerializedName("fb_token")
    public String fb_token;


    public SignUpRequest initRequest() throws JSONException {
       // SignUpRequest jsonObject = new SignUpRequest();
        this.remove("username");
        this.put("username", username);
        this.remove("country");
        this.put("country", country);
        this.remove("email");
        this.put("email", email);
        this.remove("avatar");
        this.put("avatar", avatar);

        this.remove("fb_token");
        this.remove("password");
        if (password != null) {
            this.put("password", password);
        } else {
            this.put("fb_token", fb_token);
        }
        return this;
    }

}