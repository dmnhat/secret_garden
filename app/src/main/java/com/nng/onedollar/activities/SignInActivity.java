package com.nng.onedollar.activities;

import org.apache.http.Header;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nng.onedollar.R;
import com.nng.onedollar.models.UserModel;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.CommonUtil;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.utils.SharedPrefUtil;
import com.nng.onedollar.utils.UiUtil;
import com.nng.onedollar.views.TMKProgressDialog;
import com.nng.onedollar.webservices.WebServicesManager;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class SignInActivity extends BaseActivity {

	private EditText mEtEmail, mEtPassword;
	private String mEmail, mPassword;
	private TMKProgressDialog mPDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

		mPDialog = new TMKProgressDialog(this);

		mEtEmail = (EditText) findViewById(R.id.et_email);
		mEtPassword = (EditText) findViewById(R.id.et_password);
		mEtPassword.setTypeface(mEtEmail.getTypeface());

		TextView tvSignUp = (TextView) findViewById(R.id.tv_sign_up);
		tvSignUp.setPaintFlags(tvSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		tvSignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
				startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
			}
		});

		TextView tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
		tvForgotPassword.setPaintFlags(tvSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		tvForgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
				startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
			}
		});

		findViewById(R.id.btn_sign_in).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				UiUtil.hideKeyboard(getApplicationContext(), mEtEmail);
				executeSignIn();
			}
		});
	}

	private boolean validate() {
		mEmail = mEtEmail.getText().toString().trim();
		mPassword = mEtPassword.getText().toString();

		if (TextUtils.isEmpty(mEmail) || TextUtils.isEmpty(mPassword)) {
			ErrorUtil.showError(this, getString(R.string.msg_err_not_enough_info));
			return false;
		} else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEmail).matches()) {
			ErrorUtil.showError(this, getString(R.string.msg_invalid_email));
			return false;
		}

		return true;
	}

	private void executeSignIn() {
		if (validate()) {
			if (ConnectionUtil.hasInternetConnection(this)) {
				mPDialog.show();

				User user = new User(mEmail, mPassword);
				String json = (new Gson().toJson(user));

				WebServicesManager.post(true, this, AppConstant.URL_SIGN_IN_EMAIL, CommonUtil.getEntity(json),
						new JsonHttpResponseHandler() {

							@Override
							public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
								super.onSuccess(statusCode, headers, response);
								handleSignInSuccess(statusCode, headers, response);
							}

							@Override
							public void onFailure(int statusCode, Header[] headers, Throwable throwable,
									JSONObject errorResponse) {
								super.onFailure(statusCode, headers, throwable, errorResponse);
								handleSignInFailure(statusCode, headers, throwable);
							}
						});
			} else {
				ErrorUtil.showError(this, getString(R.string.msg_err_no_internet));
			}
		}
	}

	private void handleSignInSuccess(int statusCode, Header[] headers, JSONObject response) {
		mPDialog.dismiss();
		Log.e("nhatdo","response login "+response.toString());
		UserModel user = (new Gson().fromJson(response.toString(), UserModel.class));
		SharedPrefUtil.saveString(this, AppConstant.USER_LOGIN,response.toString());
		SharedPrefUtil.saveBoolean(this, AppConstant.PREF_LOGIN_STATUS, true);
		SharedPrefUtil.saveInt(this, AppConstant.PREF_ID, user.id);
		SharedPrefUtil.saveString(this, AppConstant.PREF_TOKEN, user.token);

		SharedPrefUtil.saveString(this, AppConstant.PREF_USERNAME, user.getFullName());
		SharedPrefUtil.saveString(this, AppConstant.PREF_CITY, user.country_name);
		SharedPrefUtil.saveString(this, AppConstant.PREF_AVATAR, user.avatar);

		closeAllActivities();
		startActivity(new Intent(this, HomeActivity.class));
	}

	private void handleSignInFailure(int statusCode, Header[] headers, Throwable throwable) {
		mPDialog.dismiss();
		ErrorUtil.showError(this, getString(R.string.msg_err_sign_in_fail));
	}

	public class User {

		public User(String email, String password) {
			this.email = email;
			this.password = password;
		}

		public String email;
		public String password;
	}

}
