package com.nng.onedollar.webservices;

import org.apache.http.HttpEntity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nng.onedollar.utils.AppConstant;

import android.content.Context;

public class WebServicesManager {

	private static AsyncHttpClient mClient = new AsyncHttpClient();

	static {
		mClient.addHeader(AsyncHttpClient.HEADER_CONTENT_TYPE, "application/json");
		mClient.setTimeout(AppConstant.WS_TIME_OUT);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return AppConstant.URL_BASE + relativeUrl;
	}

	public static void get(boolean needAttachBaseUrl, String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		String fullUrl = needAttachBaseUrl ? getAbsoluteUrl(url) : url;
		mClient.removeAllHeaders();
		mClient.get(fullUrl, params, responseHandler);
	}
	public static void patch(boolean needAttachBaseUrl,Context context, String url,HttpEntity entity,AsyncHttpResponseHandler responseHandler,String token){
		String fullUrl = needAttachBaseUrl ? getAbsoluteUrl(url) : url;
		//mClient.addHeader("Authorization", "Token " + token);
		//mClient.addHeader("Content-Type", "application/json");
		mClient.put(context, fullUrl, entity, "multipart/form-data", responseHandler);
	}
	public static void patch(boolean needAttachBaseUrl,Context context, String url,RequestParams entity,AsyncHttpResponseHandler responseHandler,String token){
		String fullUrl = needAttachBaseUrl ? getAbsoluteUrl(url) : url;
		mClient.removeAllHeaders();
		mClient.addHeader("Authorization", "Token " + token);
		mClient.put(context, fullUrl, entity, responseHandler);
	}

	public static void post(boolean needAttachBaseUrl, Context context, String url, HttpEntity entity,
			AsyncHttpResponseHandler responseHandler) {
		String fullUrl = needAttachBaseUrl ? getAbsoluteUrl(url) : url;
		mClient.removeAllHeaders();
		mClient.addHeader("Content-Type", "application/json");
		mClient.post(context, fullUrl, entity, "application/json", responseHandler);
	}
	public static void postWithToken(boolean needAttachBaseUrl, Context context, String url, HttpEntity entity,
							AsyncHttpResponseHandler responseHandler,String token) {
		String fullUrl = needAttachBaseUrl ? getAbsoluteUrl(url) : url;
		mClient.addHeader("Authorization", "Token " + token);
		mClient.post(context, fullUrl, entity, "application/json", responseHandler);
	}
	public static void getWithToken(boolean needAttachBaseUrl, String url, RequestParams params,
						   AsyncHttpResponseHandler responseHandler,String token) {
		String fullUrl = needAttachBaseUrl ? getAbsoluteUrl(url) : url;
		mClient.removeAllHeaders();
		mClient.addHeader("Authorization", "Token " + token);
		mClient.get(fullUrl, params, responseHandler);
	}

}
