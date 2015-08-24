package com.nng.onedollar.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.http.entity.StringEntity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;

public class CommonUtil {

    public static String formateDate(String format, long date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        return sdf.format(new Date(date));
    }

    public static String formatDate(String strDate) throws ParseException {
        //2015-08-16T14:39:31Z
        Date date = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss").parse(strDate);
        String newstring = new SimpleDateFormat("yyyy/MM/dd").format(date);
        return newstring;
    }

    public static StringEntity getEntity(String json) {
        StringEntity entity;
        try {
            entity = new StringEntity(json, "UTF-8");
            return entity;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    public static boolean isUrlValid(String source) {
        try {
            new URL(source);
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void showKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo("com.nng.onedollar",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

}
