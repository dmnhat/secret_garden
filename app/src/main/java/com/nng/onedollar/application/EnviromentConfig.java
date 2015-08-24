package com.nng.onedollar.application;

import android.content.res.Resources;

import com.nng.onedollar.R;

/**
 * Created by Jin on 8/6/2015.
 */
public class EnviromentConfig {

    public static String getBaseURL() {
        Resources resources = OneDollarApplication.getInstance().getResources();
        return resources.getString(R.string.api_domain);
    }
}
