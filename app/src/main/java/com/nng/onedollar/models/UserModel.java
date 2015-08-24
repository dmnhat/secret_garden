package com.nng.onedollar.models;

import android.text.TextUtils;

public class UserModel {

    public int id;
    public String uri;
    public String email;
    public String first_name;
    public String last_name;
    public int gender;
    public String about;
    public String avatar;
    public int credits;
    public String dob;
    public String country;
    public String token;
    public String country_name;
    public String relationship_status;
    public int likes_count;
    public int dislikes_count;
    public String creation_date;
    public String fb_uid;
    public boolean is_email_verified;

    public String getFullName() {
        String firstName = TextUtils.isEmpty(first_name) ? "" : first_name;
        String lastName = TextUtils.isEmpty(last_name) ? "" : last_name;
        String fullName = (firstName + " " + lastName).trim();
        String userName = TextUtils.isEmpty(fullName) ? email : fullName;
        return userName;
    }
}
