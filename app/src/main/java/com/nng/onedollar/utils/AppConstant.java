package com.nng.onedollar.utils;

public class AppConstant {

    // date time format
    public static final String DATE_FORMAT = "dd-MM-yyyy";

    public static final int WS_TIME_OUT = 30000;

    // request code
    public static final int RC_CHOOSE_COUNTRY = 10000;
    public static final int RC_CAPTURE_PHOTO = 10001;
    public static final int RC_CHOOSE_PHOTO = 10002;

    // shared preferences
    public static final String PREF_LOGIN_STATUS = "PREF_LOGIN_STATUS";
    public static final String USER_LOGIN = "USER_LOGIN";
    public static final String PREF_ID = "PREF_ID";
    public static final String PREF_TOKEN = "PREF_TOKEN";
    public static final String PREF_USERNAME = "PREF_USERNAME";
    public static final String PREF_CITY = "PREF_CITY";
    public static final String PREF_AVATAR = "PREF_AVATAR";

    // api
    public static final String URL_BASE = "http://onedollar.projects.nng.bz/api/";
    public static final String URL_COUNTRIES = "countries/";
    public static final String URL_SIGN_UP = "users/signup/";
    public static final String URL_SIGN_IN_FB = "users/me/";
    public static final String URL_GET_MY_PRODUCT = "users/me/products/";
    public static final String URL_SIGN_IN_EMAIL = "users/me/";
    public static final String URL_EDIT_USER = "users/";
    public static final String URL_FORGOT_PASSWORD = "password/forget/";
    public static final String URL_PRODUCT_LIST = "products/";

    public static final int RSP_CODE_SUCCESSFUL_1 = 20010;
    public static final int RSP_CODE_SUCCESSFUL_2 = 20011;

}
