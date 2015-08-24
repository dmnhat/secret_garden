package com.nng.onedollar.activities;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nng.onedollar.R;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.CommonUtil;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.utils.UiUtil;
import com.nng.onedollar.views.TMKProgressDialog;
import com.nng.onedollar.webservices.WebServicesManager;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class ForgotPasswordActivity extends BaseActivity {

	private EditText mEtEmail;
	private String mEmail;
	private TMKProgressDialog mPDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

		mPDialog = new TMKProgressDialog(this);

		mEtEmail = (EditText) findViewById(R.id.et_email);

		findViewById(R.id.btn_send).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
				executeForgotPassword();
			}
		});
	}

	private boolean validate() {
		mEmail = mEtEmail.getText().toString().trim();

		if (TextUtils.isEmpty(mEmail)) {
			ErrorUtil.showError(this, getString(R.string.msg_err_not_enough_info));
			return false;
		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
			ErrorUtil.showError(this, getString(R.string.msg_invalid_email));
			return false;
		}

		return true;
	}

	private void executeForgotPassword() {
		if (validate()) {
			if (ConnectionUtil.hasInternetConnection(this)) {
				mPDialog.show();

				User user = new User(mEmail);
				String json = (new Gson().toJson(user));

				WebServicesManager.post(true, this, AppConstant.URL_FORGOT_PASSWORD, CommonUtil.getEntity(json),
						new JsonHttpResponseHandler() {
							@Override
							public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
								super.onSuccess(statusCode, headers, response);
								handleForgotPasswordSuccess(statusCode, headers, response);
							}

							@Override
							public void onFailure(int statusCode, Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								super.onFailure(statusCode, headers, throwable, errorResponse);
								handleForgotPasswordFailure(statusCode, headers, throwable);
							}
						});
			} else {
				ErrorUtil.showError(this, getString(R.string.msg_err_no_internet));
			}
		}
	}

	private void handleForgotPasswordSuccess(int statusCode, Header[] headers, JSONObject response) {
		mPDialog.dismiss();
		UiUtil.showToast(this, getString(R.string.msg_forgot_password_success));
	}

	private void handleForgotPasswordFailure(int statusCode, Header[] headers, Throwable throwable) {
		mPDialog.dismiss();
		ErrorUtil.showError(this, getString(R.string.msg_err_forgot_password_fail));
	}

	public class User {

		public User(String email) {
			this.email = email;
		}

		public String email;
	}

}
