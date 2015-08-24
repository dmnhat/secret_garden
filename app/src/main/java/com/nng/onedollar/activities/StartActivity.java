package com.nng.onedollar.activities;

import java.util.Arrays;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nng.onedollar.R;
import com.nng.onedollar.models.UserModel;
import com.nng.onedollar.utils.AppConstant;
import com.nng.onedollar.utils.CommonUtil;
import com.nng.onedollar.utils.ConnectionUtil;
import com.nng.onedollar.utils.ErrorUtil;
import com.nng.onedollar.utils.SharedPrefUtil;
import com.nng.onedollar.views.TMKProgressDialog;
import com.nng.onedollar.webservices.WebServicesManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class StartActivity extends BaseActivity {

	private CallbackManager callbackManager;
	private TMKProgressDialog mPDialog;
	private String mEmail, mUsername, mAvatarUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (SharedPrefUtil.loadBoolean(this, AppConstant.PREF_LOGIN_STATUS, false)) {
			finish();
			startActivity(new Intent(this, HomeActivity.class));
		} else {
			FacebookSdk.sdkInitialize(getApplicationContext());
			callbackManager = CallbackManager.Factory.create();

			setContentView(R.layout.activity_start);
			overridePendingTransition(R.anim.in_from_right, R.anim.no_change);

			mPDialog = new TMKProgressDialog(this);

			final LoginButton btnLoginFB = new LoginButton(this);
			btnLoginFB.setReadPermissions(Arrays.asList("email", "user_photos", "public_profile", "user_about_me"));

			// Callback registration
			btnLoginFB.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
				@Override
				public void onSuccess(LoginResult loginResult) {
					executeCheckFBToken();
				}

				@Override
				public void onCancel() {
				}

				@Override
				public void onError(FacebookException exception) {
				}
			});

			findViewById(R.id.btn_facebook).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (AccessToken.getCurrentAccessToken() != null) {
						executeCheckFBToken();
					} else {
						btnLoginFB.performClick();
					}
				}
			});

			findViewById(R.id.btn_email).setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(getApplicationContext(), SignInActivity.class));
				}
			});

			CommonUtil.showKeyHash(this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		AppEventsLogger.activateApp(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		AppEventsLogger.deactivateApp(this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}

	private void executeCheckFBToken() {
		if (ConnectionUtil.hasInternetConnection(this)) {
			mPDialog.show();

			AccessToken accessToken = AccessToken.getCurrentAccessToken();
			FBToken token = new FBToken(accessToken.getToken());
			String json = (new Gson().toJson(token));

			WebServicesManager.post(true, this, AppConstant.URL_SIGN_IN_FB, CommonUtil.getEntity(json),
					new JsonHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							handleCheckFBTokenSuccess(statusCode, headers, response);
						}

						@Override
						public void onFailure(int statusCode, Header[] headers, Throwable throwable,
								JSONObject errorResponse) {
							super.onFailure(statusCode, headers, throwable, errorResponse);
							handleCheckFBTokenFailure(statusCode, headers, throwable);
						}
					});
		} else {
			ErrorUtil.showError(this, getString(R.string.msg_err_no_internet));
		}
	}

	private void handleCheckFBTokenSuccess(int statusCode, Header[] headers, JSONObject response) {
		mPDialog.dismiss();

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

	private void handleCheckFBTokenFailure(int statusCode, Header[] headers, Throwable throwable) {
		GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
				new GraphRequest.GraphJSONObjectCallback() {
					@Override
					public void onCompleted(JSONObject object, GraphResponse response) {
						try {
							mUsername = object.getString("name");
							mEmail = object.getString("email");
							JSONObject picture = object.getJSONObject("picture");
							JSONObject data = picture.getJSONObject("data");
							mAvatarUrl = data.getString("url");
						} catch (JSONException e) {
							e.printStackTrace();
						}

						Intent intent = new Intent(StartActivity.this, SignUpDetailActivity.class);
						intent.putExtra("isFB", true);
						intent.putExtra("email", mEmail);
						intent.putExtra("username", mUsername);
						intent.putExtra("avatarUrl", mAvatarUrl);
						intent.putExtra("fbToken", AccessToken.getCurrentAccessToken().getToken());
						startActivity(intent);
					}
				});

		Bundle parameters = new Bundle();
		parameters.putString("fields", "id,name,email,picture");
		request.setParameters(parameters);
		request.executeAsync();
	}

	public class FBToken {

		public FBToken(String fbToken) {
			this.fb_token = fbToken;
		}

		public String fb_token;
	}

}
