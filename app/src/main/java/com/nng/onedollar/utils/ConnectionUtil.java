package com.nng.onedollar.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionUtil {

	public static boolean hasInternetConnection(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnected();
	}

	public static boolean isGpsEnable(Context context) {
		LocationManager mgr = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return mgr.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

}
