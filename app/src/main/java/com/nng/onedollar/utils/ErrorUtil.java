package com.nng.onedollar.utils;

import org.apache.http.Header;
import org.apache.http.conn.ConnectTimeoutException;

import android.content.Context;

import com.nng.onedollar.R;

public class ErrorUtil {

	public static void showError(Context context, String message, int errorCode) {
		String title = context.getString(R.string.dialog_title_error);
		String btnTitle = context.getString(R.string.dialog_btn_close);
		if (errorCode != 0) {
			message += message + " (" + errorCode + ")";
		}
		UiUtil.showMessageDialog(context, title, message, btnTitle);
	}

	public static void showError(Context context, String message) {
		showError(context, message, 0);
	}

	public static void handleCommonError(Context context, int statusCode,
			Header[] headers, Throwable throwable) {
		if (throwable.getCause() instanceof ConnectTimeoutException) {
			showError(context, context.getString(R.string.msg_err_time_out));
		} else {
			showError(context, context.getString(R.string.msg_err_unknown));
		}
	}
}
